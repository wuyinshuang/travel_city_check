import { createRouter, createWebHistory } from 'vue-router'
import { useUserStore } from '../stores/user'
import Home from '../views/Home.vue'
import Login from '../views/Login.vue'
import Register from '../views/Register.vue'
import ProvinceMap from '../views/ProvinceMap.vue'

const router = createRouter({
  history: createWebHistory('/travel-city-checkin'),
  routes: [
    {
      path: '/',
      name: 'home',
      component: Home,
      meta: { requiresAuth: true }
    },
    {
      path: '/login',
      name: 'login',
      component: Login
    },
    {
      path: '/register',
      name: 'register',
      component: Register
    },
    {
      path: '/province/:provinceId',
      name: 'province',
      component: ProvinceMap,
      meta: { requiresAuth: true }
    }
  ]
})

let authChecked = false

router.beforeEach(async (to, _from, next) => {
  const userStore = useUserStore()
  
  // 只在首次导航时检查认证状态
  if (!authChecked) {
    await userStore.checkAuth()
    authChecked = true
  }
  
  const requiresAuth = to.matched.some(record => record.meta.requiresAuth)
  
  if (requiresAuth && !userStore.isLoggedIn) {
    next('/login')
  } else if ((to.path === '/login' || to.path === '/register') && userStore.isLoggedIn) {
    next('/')
  } else {
    next()
  }
})

export default router