import { defineStore } from 'pinia'
import { ref, computed } from 'vue'
import axios from '../axios'

export interface User {
  userId: number
  username: string
  createdAt: string
  lastLogin: string | null
}

export const useUserStore = defineStore('user', () => {
  const user = ref<User | null>(null)
  const isLoggedIn = computed(() => !!user.value)

  const login = async (username: string, password: string) => {
    const response = await axios.post('/api/v1/auth/login', { username, password })
    user.value = response.data.data
    return response.data
  }

  const register = async (username: string, password: string) => {
    const response = await axios.post('/api/v1/auth/register', { username, password })
    return response.data
  }

  const logout = async () => {
    await axios.post('/api/v1/auth/logout')
    user.value = null
  }

  const checkAuth = async () => {
    try {
      const response = await axios.get('/api/v1/auth/user')
      user.value = response.data.data
    } catch {
      user.value = null
    }
  }

  return { user, isLoggedIn, login, register, logout, checkAuth }
})