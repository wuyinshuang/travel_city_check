<script setup lang="ts">
import { ref, onMounted, onUnmounted, watch } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import * as echarts from 'echarts';
import axios from '../axios';

const route = useRoute();
const router = useRouter();

const provinceId = ref<number>(0);
const provinceName = ref('');
const cities = ref<any[]>([]);
const checkedCities = ref<number[]>([]);
const mapChart = ref<any>(null);
const loading = ref(true);
const selectedCity = ref<any>(null);
const showNoteModal = ref(false);
const noteContent = ref('');
const noteImages = ref<any[]>([]);
const noteId = ref<number | null>(null);
const uploadingImage = ref(false);
const pendingImages = ref<File[]>([]);

const getProvinceId = () => {
  const id = Number(route.params.provinceId);
  if (!isNaN(id)) {
    provinceId.value = id;
  }
};

const loadProvinceData = async () => {
  try {
    const [geoJsonRes, provinceRes, checkinsRes] = await Promise.all([
      axios.get(`/api/v1/provinces/${provinceId.value}/map`),
      axios.get(`/api/v1/provinces/${provinceId.value}`),
      axios.get('/api/v1/checkins')
    ]);

    const provinceData = provinceRes.data.data;
    provinceName.value = provinceData.province.name;
    cities.value = provinceData.cities;

    const checkins = checkinsRes.data.data;
    const cityIds = checkins
      .filter((c: any) => c.provinceId === provinceId.value)
      .map((c: any) => c.cityId);
    checkedCities.value = cityIds;

    const provinceGeoJson = typeof geoJsonRes.data === 'string' ? JSON.parse(geoJsonRes.data) : geoJsonRes.data;
    echarts.registerMap('province', provinceGeoJson);

    initMap();
    loading.value = false;
  } catch (error) {
    console.error('Failed to load province data:', error);
    loading.value = false;
  }
};

const initMap = () => {
  const chartDom = document.getElementById('province-map');
  if (!chartDom) return;

  mapChart.value = echarts.init(chartDom);

  const option: echarts.EChartsOption = {
    tooltip: {
      trigger: 'item',
      formatter: (params: any) => {
        const city = cities.value.find(c => c.name === params.name);
        if (!city) return `<strong>${params.name}</strong>`;
        const isChecked = checkedCities.value.includes(city.id);
        return `<strong>${params.name}</strong><br/>${isChecked ? '✓ 已打卡' : '点击打卡'}`;
      }
    },
    series: [
      {
        name: '城市',
        type: 'map',
        map: 'province',
        zoom: 1.2,
        roam: true,
        label: {
          show: true,
          color: '#333',
          fontSize: 10
        },
        itemStyle: {
          borderColor: '#000',
          borderWidth: 2,
          areaColor: '#e0e0e0'
        },
        emphasis: {
          label: {
            show: true,
            color: '#000',
            fontSize: 12,
            fontWeight: 'bold'
          },
          itemStyle: {
            areaColor: '#87CEEB',
            borderColor: '#000',
            borderWidth: 2
          }
        },
        data: cities.value
          .filter(c => {
            // 过滤掉省份名称（包括各种可能的格式）
            const provinceNames = [
              provinceName.value,
              provinceName.value.replace('省', ''),
              provinceName.value.replace('自治区', ''),
              provinceName.value.replace('特别行政区', ''),
              provinceName.value.replace('维吾尔', ''),
              provinceName.value.replace('壮族', ''),
              provinceName.value.replace('回族', '')
            ];
            return !provinceNames.some(name => c.name === name || c.name.includes(name));
          })
          .map(c => ({
            name: c.name,
            value: checkedCities.value.includes(c.id) ? 1 : 0,
            itemStyle: {
              areaColor: checkedCities.value.includes(c.id) ? '#87CEEB' : '#ffffff'
            },
            label: {
              show: true,
              color: '#333',
              fontSize: 10
            }
          }))
      }
    ]
  };

  mapChart.value.setOption(option);

  mapChart.value.on('click', (params: any) => {
    const city = cities.value.find(c => c.name === params.name);
    if (city) {
      handleCityClick(city);
    }
  });
};

const handleCityClick = (city: any) => {
  selectedCity.value = city;
  showNoteModal.value = true;
  loadCityNote(city.id);
  // 弹窗打开后重新调整地图大小
  setTimeout(() => {
    mapChart.value?.resize();
  }, 350);
};

// 监听弹窗关闭，重新调整地图大小
watch(showNoteModal, (newVal) => {
  if (!newVal) {
    setTimeout(() => {
      mapChart.value?.resize();
    }, 350);
  }
});

const loadCityNote = async (cityId: number) => {
  try {
    const res = await axios.get(`/api/v1/notes/city/${cityId}`);
    if (res.data.data) {
      noteContent.value = res.data.data.content;
      noteImages.value = res.data.data.images || [];
      noteId.value = res.data.data.id;
    } else {
      noteContent.value = '';
      noteImages.value = [];
      noteId.value = null;
    }
  } catch {
    noteContent.value = '';
    noteImages.value = [];
    noteId.value = null;
  }
};

const handleCheckin = async () => {
  if (!selectedCity.value) return;
  try {
    await axios.post('/api/v1/checkins', { cityId: selectedCity.value.id });
    checkedCities.value.push(selectedCity.value.id);
    initMap();
  } catch (error: any) {
    alert(error.response?.data?.message || '打卡失败');
  }
};

const handleUncheckin = async () => {
  if (!selectedCity.value) return;
  try {
    const checkinsRes = await axios.get('/api/v1/checkins');
    const checkin = checkinsRes.data.data.find((c: any) => c.cityId === selectedCity.value.id);
    if (checkin) {
      await axios.delete(`/api/v1/checkins/${checkin.id}`);
      checkedCities.value = checkedCities.value.filter(id => id !== selectedCity.value.id);
      initMap();
    }
  } catch (error: any) {
    alert(error.response?.data?.message || '取消打卡失败');
  }
};

const saveNote = async () => {
  if (!selectedCity.value) return;
  try {
    if (noteId.value) {
      // Update existing note
      await axios.put(`/api/v1/notes/${noteId.value}`, { content: noteContent.value });
    } else {
      // Create new note
      const res = await axios.post('/api/v1/notes', { cityId: selectedCity.value.id, content: noteContent.value });
      if (res.data.data && res.data.data.id) {
        noteId.value = res.data.data.id;
      }
    }
    // Recharge notes data
    await loadCityNote(selectedCity.value.id);
    alert('保存成功');
  } catch (error: any) {
    // If the note already exists, try to get the note ID and update
    if (!noteId.value && error.response?.data?.message?.includes('Note already exists')) {
      try {
        const checkRes = await axios.get(`/api/v1/notes/city/${selectedCity.value.id}`);
        if (checkRes.data.data) {
          noteId.value = checkRes.data.data.id;
          await axios.put(`/api/v1/notes/${noteId.value}`, { content: noteContent.value });
          await loadCityNote(selectedCity.value.id);
          alert('保存成功');
          return;
        }
      } catch {}
    }
    alert(error.response?.data?.message || '保存失败');
  }
};

const uploadImage = async (event: any) => {
  const files = event.target.files;
  if (!files) return;

  // Add to pending upload list
  for (let i = 0; i < files.length; i++) {
    pendingImages.value.push(files[i]);
  }

  // Clear input to allow re-selecting same files
  event.target.value = '';
};

const confirmUploadImages = async () => {
  if (pendingImages.value.length === 0) return;
  uploadingImage.value = true;

  try {
    // If no noteId yet, save note first (even if content is empty)
    if (!noteId.value && selectedCity.value) {
      try {
        // Try to create a note first
        const res = await axios.post('/api/v1/notes', {
          cityId: selectedCity.value.id,
          content: noteContent.value || ''
        });
        if (res.data.data && res.data.data.id) {
          noteId.value = res.data.data.id;
        }
      } catch (noteErr: any) {
        // If note already exists, get its ID
        if (noteErr.response?.data?.message?.includes('Note already exists')) {
          const checkRes = await axios.get(`/api/v1/notes/city/${selectedCity.value.id}`);
          if (checkRes.data.data) {
            noteId.value = checkRes.data.data.id;
          }
        } else {
          throw noteErr;
        }
      }
    }

    if (!noteId.value) {
      alert('请先保存备注再上传图片');
      uploadingImage.value = false;
      return;
    }

    for (const file of pendingImages.value) {
      const formData = new FormData();
      formData.append('file', file);
      const res = await axios.post(`/api/v1/images/note/${noteId.value}`, formData, {
        headers: { 'Content-Type': 'multipart/form-data' }
      });
      noteImages.value.push(res.data.data);
    }

    // Clear pending list
    pendingImages.value = [];
    alert('图片上传成功');
  } catch (error: any) {
    alert(error.response?.data?.message || '上传失败');
  } finally {
    uploadingImage.value = false;
  }
};

const deleteImage = async (imageId: number) => {
  try {
    await axios.delete(`/api/v1/images/${imageId}`);
    noteImages.value = noteImages.value.filter(img => img.id !== imageId);
  } catch (error: any) {
    alert(error.response?.data?.message || '删除失败');
  }
};

const goBack = () => {
  router.push('/');
};

const handleResize = () => {
  mapChart.value?.resize();
};

watch(() => route.params.provinceId, () => {
  getProvinceId();
  loadProvinceData();
});

onMounted(() => {
  getProvinceId();
  loadProvinceData();
  window.addEventListener('resize', handleResize);
});

onUnmounted(() => {
  window.removeEventListener('resize', handleResize);
  mapChart.value?.dispose();
});
</script>

<template>
  <div class="province-container">
    <header class="header">
      <div class="header-content">
        <button @click="goBack" class="btn-back">返回</button>
        <h1>{{ provinceName }} - 城市打卡</h1>
        <div class="legend">
          <span class="legend-item">
            <span class="legend-color checked"></span> 已打卡
          </span>
          <span class="legend-item">
            <span class="legend-color unchecked"></span> 未打卡
          </span>
        </div>
      </div>
    </header>

    <div class="map-wrapper" :class="{ 'with-modal': showNoteModal }">
      <div v-if="loading" class="loading">加载中...</div>
      <div id="province-map"></div>
    </div>

    <div v-if="showNoteModal" class="modal-overlay" @click.self="showNoteModal = false">
      <div class="modal-content">
        <div class="modal-header">
          <h2>{{ selectedCity?.name }}</h2>
          <button @click="showNoteModal = false" class="btn-close">×</button>
        </div>

        <div class="modal-body">
          <!-- 打卡状态信息 -->
          <div class="checkin-info-card">
            <div class="checkin-status">
              <span class="status-icon">{{ checkedCities.includes(selectedCity?.id) ? '✓' : '○' }}</span>
              <span class="status-text">{{ checkedCities.includes(selectedCity?.id) ? '已打卡' : '未打卡' }}</span>
            </div>
            <div class="checkin-actions">
              <button v-if="!checkedCities.includes(selectedCity?.id)" @click="handleCheckin" class="btn-checkin">
                打卡
              </button>
              <button v-else @click="handleUncheckin" class="btn-uncheckin">
                取消打卡
              </button>
            </div>
          </div>

          <!-- 备注信息 -->
          <div class="note-section">
            <h3>📝 旅行备注</h3>
            <textarea
              v-model="noteContent"
              placeholder="记录您在这座城市的旅行故事..."
              rows="4"
            ></textarea>
            <button @click="saveNote" class="btn-save-note">保存备注</button>
          </div>

          <!-- 图片展示 -->
          <div class="images-section">
            <h3>📷 打卡照片</h3>
            <div v-if="noteImages.length > 0" class="images-grid">
              <div v-for="image in noteImages" :key="image.id" class="image-item">
                <img :src="`/travel-city-checkin${image.filePath}`" :alt="image.fileName" />
                <button @click="deleteImage(image.id)" class="btn-delete-image">删除</button>
              </div>
            </div>
            <div v-else class="no-images">
              <p>暂无照片，上传您的旅行照片吧！</p>
            </div>
          </div>

          <!-- 上传区域 -->
          <div class="upload-section">
            <input
              type="file"
              accept="image/*"
              multiple
              @change="uploadImage"
              :disabled="uploadingImage"
              class="file-input"
            />
            <div v-if="pendingImages.length > 0" class="pending-images">
              <p>已选择 {{ pendingImages.length }} 张图片</p>
              <button @click="confirmUploadImages" :disabled="uploadingImage" class="btn-confirm-upload">
                {{ uploadingImage ? '上传中...' : '确认上传' }}
              </button>
            </div>
            <span class="hint">选择多个图片，点击"确认上传"按钮统一上传</span>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped>
.province-container {
  min-height: 100vh;
  background: #f5f5f5;
}

.header {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  padding: 16px 20px;
  color: white;
}

.header-content {
  max-width: 1400px;
  margin: 0 auto;
  display: flex;
  align-items: center;
  gap: 20px;
}

.btn-back {
  padding: 8px 16px;
  background: rgba(255, 255, 255, 0.2);
  border: 1px solid rgba(255, 255, 255, 0.3);
  border-radius: 6px;
  color: white;
  cursor: pointer;
  font-size: 14px;
}

.btn-back:hover {
  background: rgba(255, 255, 255, 0.3);
}

.header h1 {
  font-size: 20px;
  margin: 0;
  flex: 1;
}

.legend {
  display: flex;
  gap: 20px;
}

.legend-item {
  display: flex;
  align-items: center;
  gap: 8px;
  font-size: 14px;
}

.legend-color {
  width: 20px;
  height: 20px;
  border-radius: 4px;
}

.legend-color.checked {
  background: #87CEEB;
}

.legend-color.unchecked {
  background: #ffffff;
  border: 1px solid #ddd;
}

.map-wrapper {
  max-width: 1400px;
  margin: 20px auto;
  position: relative;
  transition: all 0.3s ease;
}

.map-wrapper.with-modal {
  max-width: 75%;
  margin: 20px 0 20px 20px;
}

#province-map {
  height: 600px;
  border-radius: 12px;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.1);
}

.loading {
  position: absolute;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  font-size: 18px;
  color: #666;
}

.modal-overlay {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: rgba(0, 0, 0, 0.3);
  display: flex;
  justify-content: flex-end;
  align-items: flex-start;
  padding: 80px 20px 20px 20px;
  z-index: 1000;
}

.modal-content {
  background: white;
  border-radius: 12px;
  width: 28%;
  min-width: 320px;
  max-width: 400px;
  max-height: calc(100vh - 100px);
  overflow-y: auto;
  box-shadow: -4px 0 20px rgba(0, 0, 0, 0.15);
}

.modal-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 20px;
  border-bottom: 1px solid #eee;
}

.modal-header h2 {
  margin: 0;
  font-size: 20px;
}

.btn-close {
  font-size: 28px;
  background: none;
  border: none;
  cursor: pointer;
  color: #999;
}

.modal-body {
  padding: 20px;
}

/* 打卡信息卡片 */
.checkin-info-card {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 16px;
  background: linear-gradient(135deg, #f8f9fa 0%, #e9ecef 100%);
  border-radius: 12px;
  margin-bottom: 20px;
  border: 1px solid #dee2e6;
}

.checkin-status {
  display: flex;
  align-items: center;
  gap: 12px;
}

.status-icon {
  font-size: 32px;
  color: #667eea;
}

.status-text {
  font-size: 18px;
  font-weight: 600;
  color: #333;
}

.checkin-actions {
  display: flex;
  align-items: center;
  gap: 12px;
}

.btn-checkin, .btn-uncheckin {
  padding: 10px 24px;
  border: none;
  border-radius: 8px;
  font-size: 16px;
  font-weight: 600;
  cursor: pointer;
}

.btn-checkin {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: white;
}

.btn-uncheckin {
  background: #e0e0e0;
  color: #333;
}

.note-section {
  margin-bottom: 20px;
}

.note-section h3 {
  margin-bottom: 8px;
  font-size: 16px;
}

.note-section textarea {
  width: 100%;
  padding: 12px;
  border: 2px solid #e0e0e0;
  border-radius: 8px;
  font-size: 14px;
  resize: vertical;
}

.btn-save-note {
  margin-top: 12px;
  padding: 10px 20px;
  background: #667eea;
  color: white;
  border: none;
  border-radius: 8px;
  cursor: pointer;
}

.images-section {
  margin-bottom: 20px;
  padding: 16px;
  background: #f8f9fa;
  border-radius: 12px;
}

.images-section h3 {
  margin-bottom: 12px;
  font-size: 16px;
  color: #333;
}

.images-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(150px, 1fr));
  gap: 16px;
}

.image-item {
  position: relative;
  background: #fff;
  border-radius: 12px;
  overflow: hidden;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
}

.image-item img {
  width: 100%;
  height: auto;
  min-height: 120px;
  max-height: 200px;
  object-fit: contain;
  display: block;
  background: #f8f9fa;
}

.btn-delete-image {
  position: absolute;
  top: 4px;
  right: 4px;
  padding: 4px 8px;
  background: rgba(0, 0, 0, 0.6);
  color: white;
  border: none;
  border-radius: 4px;
  font-size: 12px;
  cursor: pointer;
}

.no-images {
  text-align: center;
  padding: 20px;
  color: #999;
}

.no-images p {
  margin: 0;
  font-size: 14px;
}

.upload-section {
  margin-top: 16px;
}

.pending-images {
  margin-top: 12px;
  padding: 12px;
  background: #f5f5f5;
  border-radius: 8px;
  display: flex;
  align-items: center;
  gap: 12px;
}

.pending-images p {
  margin: 0;
  color: #666;
  font-size: 14px;
}

.btn-confirm-upload {
  padding: 8px 16px;
  background: #667eea;
  color: white;
  border: none;
  border-radius: 6px;
  cursor: pointer;
  font-size: 14px;
}

.btn-confirm-upload:hover {
  opacity: 0.9;
}

.btn-confirm-upload:disabled {
  background: #ccc;
  cursor: not-allowed;
}

.file-input {
  padding: 8px;
  border: 2px dashed #ddd;
  border-radius: 8px;
  cursor: pointer;
}

.hint {
  display: block;
  margin-top: 8px;
  color: #999;
  font-size: 12px;
}
</style>
