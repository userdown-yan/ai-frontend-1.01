import axios from 'axios'

const request = axios.create({
    baseURL: 'http://localhost:8080'
})

request.interceptors.request.use(config => {
    const token = localStorage.getItem('token')
    if (token) {
        config.headers['Authorization'] = token
    }
    return config
})

export const login = (data) => request.post('/user/login', data)
export const register = (data) => request.post('/user/register', data)