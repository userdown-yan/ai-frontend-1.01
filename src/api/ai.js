import axios from 'axios'

const request = axios.create({
    baseURL: 'http://localhost:8080'
})

request.interceptors.request.use(config => {
    const token = localStorage.getItem('token')
    if (token) config.headers['Authorization'] = token
    return config
})

/**
 * SSE 连接地址（Chat.vue 里用 EventSource）
 */
export const chatStream = (message) => ({
    url: `http://localhost:8080/ai/chat/stream?message=${encodeURIComponent(message)}`
})

/**
 * 会话管理
 */
export const createConversation = () => request.post('/conversation/create')
export const getConversationList = () => request.get('/conversation/list')
export const renameConversation = (id, title) =>
    request.post('/conversation/rename', new URLSearchParams({ id, title }))
export const deleteConversation = (id) => request.delete(`/conversation/${id}`)