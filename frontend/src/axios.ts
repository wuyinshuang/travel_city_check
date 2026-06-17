import axios from 'axios'

const instance = axios.create({
  baseURL: '/travel-city-checkin',
  withCredentials: true
})

export default instance