import axios from 'axios'

const request = axios.create({
    baseURL: 'http://localhost:8080'
})

// 请求拦截器
request.interceptors.request.use(config => {
    const token = localStorage.getItem('token')
    if (token) {
        config.headers['Authorization'] = token
    }
    return config
})

// ✅ 关键修复：地址 + 参数格式 完全对齐后端
export const chat = (message) => {
    return request.post(
        '/ai/chat/stream',
        new URLSearchParams({ message: message }),
        { headers: { 'Content-Type': 'application/x-www-form-urlencoded' } }
    )
}

export const createConversation = () => request.post('/conversation/create')
export const getConversationList = () => request.get('/conversation/list')