<template>
  <div class="chat-app">
    <!-- 左侧会话栏 -->
    <div class="chat-sidebar" :class="{ closed: !sidebarOpen }">
      <div class="sidebar-header">
        <div class="title">
          <svg-icon name="message-square" />
          <span>消息</span>
        </div>
        <el-button circle type="primary" @click="createNewChat">+</el-button>
      </div>

      <div class="search-box">
        <svg-icon name="search" />
        <input
            v-model="searchKeyword"
            placeholder="搜索会话..."
            type="text"
        />
      </div>

      <div class="conv-list">
        <div
            v-for="item in filteredConversations"
            :key="item.id"
            class="conv-item"
            :class="{ active: currentConversationId === item.id }"
            @click="loadConversation(item.id)"
        >
          <div class="avatar-wrap">
            <div class="avatar">AI</div>
            <div class="online-dot"></div>
          </div>
          <div class="conv-info">
            <div class="conv-top">
              <span class="name">{{ item.title }}</span>
              <span class="time">12:30</span>
            </div>
            <div class="last-msg">{{ item.lastMsg || '暂无消息' }}</div>
          </div>
          <el-badge v-if="item.unread" :value="item.unread" type="danger" />
        </div>
      </div>

      <div class="sidebar-user">
        <div class="avatar small">我</div>
        <div class="user-info">
          <div class="username">当前用户</div>
          <div class="status">在线</div>
        </div>
        <el-button circle text size="small">···</el-button>
      </div>
    </div>

    <!-- 右侧聊天区域 -->
    <div class="chat-main">
      <div class="chat-header">
        <el-button
            text
            circle
            @click="sidebarOpen = !sidebarOpen"
            class="toggle-btn"
        >
          <svg-icon :name="sidebarOpen ? 'panel-left-close' : 'panel-left-open'" />
        </el-button>

        <div class="chat-title">
          <div class="avatar">AI</div>
          <div class="chat-name">智能助手</div>
          <div class="status-dot"></div>
        </div>

        <div class="chat-actions">
          <el-button text><svg-icon name="phone" /></el-button>
          <el-button text><svg-icon name="video" /></el-button>
          <el-divider direction="vertical" />
          <el-button text><svg-icon name="more-horizontal" /></el-button>
        </div>
      </div>

      <div class="message-box" ref="messageBox">
        <div v-if="messages.length === 0" class="empty-state">
          <svg-icon name="message-circle" size="48" />
          <p>暂无消息，开始聊天吧</p>
        </div>

        <div
            v-for="(msg, idx) in messages"
            :key="idx"
            class="message"
            :class="msg.role"
        >
          <div class="avatar small" v-if="msg.role === 'ai'">AI</div>
          <div class="bubble">{{ msg.content }}</div>
          <div class="avatar small" v-if="msg.role === 'user'">我</div>
        </div>

        <!-- 打字中动画 -->
        <div v-if="isLoading" class="typing">
          <span></span>
          <span></span>
          <span></span>
        </div>
      </div>

      <div class="input-bar">
        <div class="input-toolbar">
          <div class="left">
            <button class="tool-btn"><svg-icon name="paperclip" /></button>
            <button class="tool-btn"><svg-icon name="image" /></button>
            <button class="tool-btn"><svg-icon name="smile" /></button>
          </div>
          <div class="right">
            <button class="tool-btn"><svg-icon name="mic" /></button>
          </div>
        </div>

        <div class="input-wrap">
          <textarea
              v-model="input"
              placeholder="输入消息..."
              @keydown.enter.exact="sendMessage"
              @keydown.shift.enter="input += '\n'"
          ></textarea>
          <el-button
              type="primary"
              circle
              :disabled="!input.trim()"
              @click="sendMessage"
          >
            <svg-icon name="send" />
          </el-button>
        </div>

        <div class="input-hint">按下 Shift + Enter 换行</div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, nextTick, onMounted, h } from 'vue'
import { ElMessage, ElDivider, ElBadge, ElButton } from 'element-plus'

const input = ref('')
const messages = ref([])
const isLoading = ref(false)
const sidebarOpen = ref(true)
const searchKeyword = ref('')

const conversations = ref([
  { id: 1, title: '智能助手', lastMsg: '你好呀！', unread: 0, messages: [] }
])
const currentConversationId = ref(1)
const messageBox = ref(null)

const filteredConversations = computed(() => {
  if (!searchKeyword.value) return conversations.value
  return conversations.value.filter(i =>
      i.title.includes(searchKeyword.value)
  )
})

const createNewChat = () => {
  const id = Date.now()
  conversations.value.unshift({
    id,
    title: '新对话',
    lastMsg: '',
    unread: 0,
    messages: []
  })
  loadConversation(id)
}

const loadConversation = (id) => {
  currentConversationId.value = id
  const c = conversations.value.find(x => x.id === id)
  if (c) messages.value = c.messages
}

const saveChat = () => {
  const c = conversations.value.find(x => x.id === currentConversationId.value)
  if (c) c.messages = messages.value
}

const scrollBottom = () => {
  nextTick(() => {
    if (messageBox.value) {
      messageBox.value.scrollTop = messageBox.value.scrollHeight
    }
  })
}

const sendMessage = async () => {
  if (!input.value) return
  const text = input.value.trim()
  messages.value.push({ role: 'user', content: text })
  const ai = { role: 'ai', content: '' }
  messages.value.push(ai)
  input.value = ''
  isLoading.value = true
  saveChat()
  scrollBottom()

  try {
    const res = await fetch('http://localhost:8080/ai/chat/stream', {
      method: 'POST',
      headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
      body: 'message=' + encodeURIComponent(text)
    })

    const reader = res.body.getReader()
    const dec = new TextDecoder()

    while (true) {
      const { done, value } = await reader.read()
      if (done) break
      let txt = dec.decode(value)
      txt = txt.replace(/data:/g, '').trim()
      ai.content += txt
      scrollBottom()
    }
  } catch (e) {
    ElMessage.error('发送失败')
  } finally {
    isLoading.value = false
  }
}

// SVG 图标组件
const svgIcon = (props) => {
  const icons = {
    'message-square': 'M21 15a2 2 0 0 1-2 2H7l-4 4V5a2 2 0 0 1 2-2h14a2 2 0 0 1 2 2z',
    search: 'M21 21l-6-6m2-5a7 7 0 1 1-14 0 7 7 0 0 1 14 0z',
    'panel-left-close': 'M20 7H9v10h11V7zM4 5h1.5v14H4',
    'panel-left-open': 'M4 5h1.5v14H4zm12.5 0H20v14h-5.5V5z',
    phone: 'M22 16.92v3a2 2 0 0 1-2.18 2 19.79 19.79 0 0 1-8.63-3.07 19.5 19.5 0 0 1-6-6 19.79 19.79 0 0 1-3.07-8.67A2 2 0 0 1 4.11 2h3a2 2 0 0 1 2 1.72c.127.96.362 1.904.7 2.8a2 2 0 0 1-.45 2.11L8.09 9.91a16 16 0 0 0 6 6l1.27-1.27a2 2 0 0 1 2.11-.45c.896.338 1.84.571 2.8.7A2 2 0 0 1 22 16.92z',
    video: 'M23 7l-7 5 7 5V7z',
    'more-horizontal': 'M12 13a1 1 0 1 0 0-2 1 1 0 0 0 0 2zm-5 0a1 1 0 1 0 0-2 1 1 0 0 0 0 2zm10 0a1 1 0 1 0 0-2 1 1 0 0 0 0 2z',
    paperclip: 'M21.44 11.05l-9.19 9.19a6 6 0 0 1-8.49-8.49l8.57-8.57A4 4 0 1 1 18 8.84l-8.59 8.57a2 2 0 0 1-2.83-2.83l7.89-7.89',
    image: 'M15 2H5a2 2 0 0 0-2 2v14a2 2 0 0 0 2 2h14a2 2 0 0 0 2-2V8l-6-6z',
    smile: 'M12 22c5.523 0 10-4.477 10-10S17.523 2 12 2 2 6.477 2 12s4.477 10 10 10zm-4-9a4 4 0 1 0 8 0H8z',
    mic: 'M12 2a3 3 0 0 0-3 3v7a3 3 0 0 0 6 0V5a3 3 0 0 0-3-3z',
    send: 'm22 2-7 4-7 10 4 7 10-7-4-7z',
    'message-circle': 'M21 11.5a8.38 8.38 0 0 1-.9 3.8 8.5 8.5 0 0 1-7.6 4.7 8.38 8.38 0 0 1-3.8-.9L2 21l1.9-5.7a8.38 8.38 0 0 1-.9-3.8 8.5 8.5 0 0 1 4.7-7.6 8.38 8.38 0 0 1 3.8-.9h.5a8.48 8.48 0 0 1 8 8v.5z'
  }
  return h('svg', {
    xmlns: 'http://www.w3.org/2000/svg',
    width: props.size || 20,
    height: props.size || 20,
    viewBox: '0 0 24 24',
    fill: 'none',
    stroke: 'currentColor',
    strokeWidth: '2',
    strokeLinecap: 'round',
    strokeLinejoin: 'round'
  }, h('path', { d: icons[props.name] }))
}

onMounted(() => loadConversation(1))
</script>

<style scoped>
* {
  margin: 0;
  padding: 0;
  box-sizing: border-box;
  font-family: -apple-system, BlinkMacSystemFont, "Segoe UI", Roboto, sans-serif;
}

.chat-app {
  display: flex;
  width: 100%;
  height: 100%;
  min-height: calc(100vh - 60px);
  background: #f9fafb;
  overflow: hidden;
}

/* 侧边栏 */
.chat-sidebar {
  width: 260px;
  height: 100%;
  background: #fff;
  border-right: 1px solid #e2e8f0;
  display: flex;
  flex-direction: column;
  transition: width 0.2s ease;
}
.chat-sidebar.closed {
  width: 0;
  overflow: hidden;
}

.sidebar-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 20px 16px;
}
.title {
  display: flex;
  align-items: center;
  gap: 8px;
  font-size: 18px;
  font-weight: 600;
  color: #1e293b;
}

.search-box {
  display: flex;
  align-items: center;
  gap: 10px;
  background: #f8fafc;
  border-radius: 8px;
  padding: 10px 14px;
  margin: 0 16px 16px;
  color: #64748b;
}
.search-box input {
  flex: 1;
  border: none;
  outline: none;
  background: transparent;
  font-size: 14px;
  color: #1e293b;
}

.conv-list {
  flex: 1;
  overflow-y: auto;
  padding: 0 12px;
}
.conv-item {
  display: flex;
  align-items: center;
  gap: 12px;
  padding: 12px;
  border-radius: 12px;
  cursor: pointer;
  transition: 0.2s;
  margin-bottom: 4px;
}
.conv-item:hover {
  background: #f8fafc;
}
.conv-item.active {
  background: #eef2ff;
}

.avatar-wrap {
  position: relative;
}
.avatar {
  width: 48px;
  height: 48px;
  border-radius: 12px;
  background: #4f46e5;
  color: white;
  display: flex;
  align-items: center;
  justify-content: center;
  font-weight: bold;
}
.avatar.small {
  width: 36px;
  height: 36px;
}
.online-dot {
  position: absolute;
  bottom: 2px;
  right: 2px;
  width: 12px;
  height: 12px;
  background: #10b981;
  border-radius: 50%;
  border: 2px solid #fff;
}

.conv-info {
  flex: 1;
  min-width: 0;
}
.conv-top {
  display: flex;
  justify-content: space-between;
  font-size: 14px;
  font-weight: 600;
  color: #1e293b;
}
.time {
  font-size: 12px;
  color: #64748b;
  font-weight: 400;
}
.last-msg {
  font-size: 13px;
  color: #64748b;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}

.sidebar-user {
  display: flex;
  align-items: center;
  gap: 10px;
  padding: 16px;
  border-top: 1px solid #e2e8f0;
}
.user-info {
  flex: 1;
}
.username {
  font-size: 14px;
  font-weight: 600;
  color: #1e293b;
}
.status {
  font-size: 12px;
  color: #10b981;
}

/* 聊天主区域 */
.chat-main {
  flex: 1;
  display: flex;
  flex-direction: column;
  overflow: hidden;
}

.chat-header {
  height: 64px;
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 0 16px;
  background: rgba(255, 255, 255, 0.8);
  backdrop-filter: blur(12px);
  border-bottom: 1px solid #e2e8f0;
  z-index: 10;
}
.toggle-btn {
  font-size: 20px;
  color: #64748b;
}
.toggle-btn:hover {
  color: #4f46e5;
}

.chat-title {
  display: flex;
  align-items: center;
  gap: 10px;
}
.chat-name {
  font-size: 16px;
  font-weight: 600;
  color: #1e293b;
}
.status-dot {
  width: 6px;
  height: 6px;
  background: #10b981;
  border-radius: 50%;
  box-shadow: 0 0 6px #10b981;
}

.chat-actions {
  display: flex;
  align-items: center;
  gap: 8px;
}

/* 消息区 */
.message-box {
  flex: 1;
  padding: 20px;
  overflow-y: auto;
  background: rgba(248, 250, 252, 0.3);
}
.empty-state {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  height: 100%;
  color: #64748b;
  gap: 12px;
}

.message {
  display: flex;
  gap: 10px;
  margin-bottom: 16px;
  align-items: flex-end;
}
.message.user {
  justify-content: flex-end;
}
.message.ai {
  justify-content: flex-start;
}

.bubble {
  max-width: 65%;
  padding: 14px 18px;
  border-radius: 16px;
  font-size: 14px;
  line-height: 1.5;
}
.message.user .bubble {
  background: #4f46e5;
  color: #fff;
  border-bottom-right-radius: 4px;
}
.message.ai .bubble {
  background: #374151;
  color: #f1f5f9;
  border-bottom-left-radius: 4px;
}

/* 打字动画 */
.typing {
  display: flex;
  gap: 6px;
  padding: 10px 16px;
}
.typing span {
  width: 8px;
  height: 8px;
  background: #94a3b8;
  border-radius: 50%;
  animation: bounce 1.2s infinite;
}
.typing span:nth-child(2) { animation-delay: 0.2s; }
.typing span:nth-child(3) { animation-delay: 0.4s; }
@keyframes bounce {
  0%,80%,100% { transform: translateY(0); }
  40% { transform: translateY(-6px); }
}

/* 输入栏 */
.input-bar {
  background: #fff;
  border-top: 1px solid #e2e8f0;
  padding: 16px;
}
.input-toolbar {
  display: flex;
  justify-content: space-between;
  margin-bottom: 12px;
}
.left, .right {
  display: flex;
  gap: 8px;
}
.tool-btn {
  background: transparent;
  border: none;
  padding: 8px;
  border-radius: 8px;
  color: #64748b;
  cursor: pointer;
  transition: 0.2s;
}
.tool-btn:hover {
  background: #eef2ff;
  color: #4f46e5;
}

.input-wrap {
  display: flex;
  gap: 12px;
  align-items: flex-end;
}
textarea {
  flex: 1;
  min-height: 44px;
  max-height: 128px;
  resize: none;
  border-radius: 16px;
  padding: 12px 16px;
  border: 1px solid #f1f5f9;
  background: #f8fafc;
  outline: none;
  transition: 0.2s;
  font-size: 14px;
}
textarea:focus {
  background: #fff;
  border-color: #c7d2fe;
  box-shadow: 0 0 0 4px rgba(79, 70, 229, 0.1);
}

.input-hint {
  text-align: center;
  font-size: 12px;
  color: #94a3b8;
  margin-top: 10px;
}

/* 滚动条 */
::-webkit-scrollbar {
  width: 4px;
}
::-webkit-scrollbar-thumb {
  background: #cbd5e1;
  border-radius: 2px;
}
::-webkit-scrollbar-thumb:hover {
  background: #94a3b8;
}
</style>