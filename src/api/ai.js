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

// 正确 POST 请求
export const chatStream = (message) => {
    return {
        url: `http://localhost:8080/ai/chat/stream?message=${encodeURIComponent(message)}`,
        method: 'POST'  // 这里改成 POST！！！
    }
}

export const createConversation = () => request.post('/conversation/create')
export const getConversationList = () => request.get('/conversation/list')