<script setup lang="ts">
import { ref, onMounted, onUnmounted } from 'vue'
import { useRouter } from 'vue-router'
import { useUserStore } from '../stores/user'
import * as echarts from 'echarts'
import axios from '../axios'

const router = useRouter()
const userStore = useUserStore()

const mapContainer = ref<HTMLElement | null>(null)
const mapChart = ref<any>(null)
const provinces = ref<any[]>([])
const checkedProvinces = ref<number[]>([])
const loading = ref(true)
const error = ref<string | null>(null)
const stats = ref({
  totalCheckins: 0,
  provincesVisited: 0
})

const initMap = async () => {
  if (!mapContainer.value) {
    error.value = '地图容器未找到'
    loading.value = false
    return
  }

  mapChart.value = echarts.init(mapContainer.value)

  try {
    console.log('开始加载地图数据...')
    
    const [geoJsonRes, provincesRes, checkinsRes] = await Promise.all([
      axios.get('/api/v1/provinces/geojson'),
      axios.get('/api/v1/provinces'),
      axios.get('/api/v1/checkins')
    ])

    console.log('GeoJSON响应:', geoJsonRes.data)
    console.log('省份数据:', provincesRes.data)
    console.log('打卡数据:', checkinsRes.data)

    provinces.value = provincesRes.data.data
    const checkins = checkinsRes.data.data

    const provinceIds = new Set<number>(checkins.map((c: any) => c.provinceId))
    checkedProvinces.value = Array.from(provinceIds)

    stats.value = {
      totalCheckins: checkins.length,
      provincesVisited: provinceIds.size
    }

    let chinaGeoJson = geoJsonRes.data
    if (typeof geoJsonRes.data === 'string') {
      chinaGeoJson = JSON.parse(geoJsonRes.data)
    }
    console.log('解析后的GeoJSON:', chinaGeoJson)
    
    echarts.registerMap('china', chinaGeoJson)

    const option: echarts.EChartsOption = {
      tooltip: {
        trigger: 'item',
        formatter: (params: any) => {
          const province = provinces.value.find(p => p.name === params.name)
          const isChecked = province ? checkedProvinces.value.includes(province.id) : false
          return `<strong>${params.name}</strong><br/>${isChecked ? '✓ 已打卡' : '未打卡'}`
        }
      },
      series: [
        {
          name: '省份',
          type: 'map',
          map: 'china',
          roam: true,
          zoom: 1.3,
          top: '5%',
          selectedMode: false,
          label: {
            show: true,
            color: '#000',
            fontSize: 10,
            fontWeight: 'normal'
          },
          itemStyle: {
            borderColor: '#000',
            borderWidth: 2,
            areaColor: '#f0f0f0'
          },
          emphasis: {
            disabled: false,
            label: {
              show: true,
              color: '#000000',
              fontSize: 12
            },
            itemStyle: {
              areaColor: '#87CEEB',
              borderColor: '#000000',
              borderWidth: 2
            }
          },
          data: provinces.value.map(p => ({
            name: p.name,
            value: checkedProvinces.value.includes(p.id) ? 1 : 0,
            itemStyle: {
              areaColor: checkedProvinces.value.includes(p.id) ? '#87CEEB' : '#f0f0f0',
              borderColor: '#000',
              borderWidth: 2
            },
            label: {
              show: true,
              color: '#000'
            }
          }))
        }
      ]
    }

    mapChart.value.setOption(option)

    mapChart.value.on('click', (params: any) => {
      const province = provinces.value.find(p => p.name === params.name)
      if (province) {
        router.push(`/province/${province.id}`)
      }
    })

    loading.value = false
  } catch (err: any) {
    console.error('加载地图失败:', err)
    console.error('错误详情:', {
      message: err.message,
      response: err.response,
      config: err.config
    })
    
    const status = err.response?.status
    const detail = err.response?.data?.message || err.response?.data || err.message || '未知错误'
    
    let errorMsg = ''
    if (status === 401) {
      errorMsg = '登录已过期，请重新登录'
    } else if (status === 403) {
      errorMsg = '访问被拒绝，请检查权限'
    } else if (status === 404) {
      errorMsg = `请求的资源不存在 (${err.config?.url})`
    } else if (status === 500) {
      errorMsg = '服务器内部错误，请稍后重试'
    } else if (err.message?.includes('Network Error')) {
      errorMsg = '网络连接失败，请检查网络'
    } else {
      errorMsg = status ? `请求失败 (${status}): ${detail}` : `连接失败: ${detail}`
    }
    
    error.value = errorMsg
    loading.value = false
  }
}

const handleLogout = async () => {
  await userStore.logout()
  router.push('/login')
}

const handleResize = () => {
  mapChart.value?.resize()
}

const retry = () => {
  error.value = null
  loading.value = true
  initMap()
}

onMounted(() => {
  initMap()
  window.addEventListener('resize', handleResize)
})

onUnmounted(() => {
  window.removeEventListener('resize', handleResize)
  mapChart.value?.dispose()
})
</script>

<template>
  <div class="home-container">
    <header class="header">
      <div class="header-content">
        <h1>中国城市旅游打卡</h1>
        <div class="user-info">
          <span>{{ userStore.user?.username }}</span>
          <button @click="handleLogout" class="btn-logout">退出登录</button>
        </div>
      </div>
    </header>

    <div class="stats-bar">
      <div class="stat-item">
        <span class="stat-value">{{ stats.totalCheckins }}</span>
        <span class="stat-label">已打卡城市</span>
      </div>
      <div class="stat-item">
        <span class="stat-value">{{ stats.provincesVisited }}</span>
        <span class="stat-label">已访问省份</span>
      </div>
      <div class="stat-item">
        <span class="stat-value">{{ 34 - stats.provincesVisited }}</span>
        <span class="stat-label">剩余省份</span>
      </div>
    </div>

    <div class="map-wrapper">
      <div id="map-container" ref="mapContainer"></div>
      <div v-if="loading" class="loading-overlay">加载中...</div>
      <div v-else-if="error" class="map-error-overlay">
        <p>{{ error }}</p>
        <button @click="retry" class="btn-retry">重新加载</button>
      </div>
      <div class="map-hint">点击省份进入查看详情</div>
    </div>
  </div>
</template>

<style scoped>
.home-container {
  min-height: 100vh;
  background: #f5f5f5;
}

.header {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  padding: 14px 20px;
  color: white;
}

.header-content {
  max-width: 1400px;
  margin: 0 auto;
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.header h1 {
  font-size: 24px;
  margin: 0;
}

.user-info {
  display: flex;
  align-items: center;
  gap: 16px;
}

.btn-logout {
  padding: 8px 16px;
  background: rgba(255, 255, 255, 0.2);
  border: 1px solid rgba(255, 255, 255, 0.3);
  border-radius: 6px;
  color: white;
  cursor: pointer;
  font-size: 14px;
}

.btn-logout:hover {
  background: rgba(255, 255, 255, 0.3);
}

.stats-bar {
  display: flex;
  justify-content: center;
  gap: 60px;
  padding: 12px 20px;
  background: white;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.06);
}

.stat-item {
  text-align: center;
}

.stat-value {
  display: block;
  font-size: 32px;
  font-weight: 700;
  color: #667eea;
}

.stat-label {
  font-size: 14px;
  color: #666;
}

.map-wrapper {
  max-width: 1000px;
  width: 66.67%;
  margin: 0 auto;
  padding: 0 20px;
  position: relative;
}

#map-container {
  height: 520px;
  border-radius: 12px;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.1);
}

.loading-overlay,
.map-error-overlay {
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  background: rgba(245, 245, 245, 0.95);
  border-radius: 12px;
  z-index: 10;
}

.loading-overlay {
  font-size: 18px;
  color: #666;
}

.map-error-overlay {
  text-align: center;
  padding: 80px 20px;
  color: #666;
}

.map-error-overlay p {
  font-size: 16px;
  margin-bottom: 16px;
}

.btn-retry {
  padding: 10px 24px;
  background: #667eea;
  color: white;
  border: none;
  border-radius: 8px;
  font-size: 14px;
  cursor: pointer;
}

.btn-retry:hover {
  opacity: 0.9;
}

.map-hint {
  text-align: center;
  margin-top: 4px;
  color: #999;
  font-size: 13px;
  padding-bottom: 20px;
}
</style>
