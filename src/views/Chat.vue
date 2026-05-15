<template>
  <div class="chat-page">
    <div class="chat-sidebar">
      <div class="new-chat-wrap">
        <el-button type="primary" plain block @click="createNewChat">
          + 新对话
        </el-button>
      </div>

      <div class="conv-list">
        <div
            v-for="item in conversations"
            :key="item.id"
            class="conv-item"
            :class="{ active: currentConversationId === item.id }"
            @click="loadConversation(item.id)"
        >
          {{ item.title }}
        </div>
      </div>
    </div>

    <div class="chat-content">
      <div class="message-box" ref="messageBox">
        <div v-for="(msg, idx) in messages" :key="idx" class="message" :class="msg.role">
          <div class="bubble">{{ msg.content }}</div>
        </div>
      </div>

      <div class="chat-input">
        <el-input
            v-model="input"
            placeholder="输入消息..."
            @keyup.enter="sendMessage"
        />
        <el-button type="primary" @click="sendMessage">发送</el-button>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, nextTick } from 'vue'
import { ElMessage } from 'element-plus'
import { chatStream } from '@/api/ai.js'

const input = ref('')
const messages = ref([])
const conversations = ref([{ id: 1, title: '默认对话', messages: [] }])
const currentConversationId = ref(1)
const messageBox = ref(null)

let eventSource = null

const createNewChat = () => {
  const id = Date.now()
  conversations.value.unshift({ id, title: '新对话', messages: [] })
  loadConversation(id)
}

const loadConversation = (id) => {
  currentConversationId.value = id
  const chat = conversations.value.find(c => c.id === id)
  if (chat) messages.value = chat.messages
}

// ====================== ✅ 完全修复！======================
const sendMessage = async () => {
  if (!input.value) return
  const text = input.value.trim()

  // 推入用户消息
  messages.value.push({ role: 'user', content: text })

  // 准备AI消息
  const aiMsg = { role: 'ai', content: '' }
  messages.value.push(aiMsg)
  input.value=''
  saveChat()

  try {
    // 关闭旧连接
    if (eventSource) eventSource.close()

    // 建立 SSE 连接
    const { url } = chatStream(text)
    eventSource = new EventSource(url)

    eventSource.onmessage = (e) => {
      aiMsg.content += e.data
      scrollBottom()
    }

    eventSource.onerror = () => {
      eventSource.close()
    }

  } catch (err) {
    ElMessage.error('连接失败')
    console.error(err)
  }
  scrollBottom()
}

const saveChat = () => {
  const chat = conversations.value.find(c => c.id === currentConversationId.value)
  if (chat) chat.messages = messages.value
}

const scrollBottom = () => {
  nextTick(() => {
    if (messageBox.value) messageBox.value.scrollTop = messageBox.value.scrollHeight
  })
}

onMounted(() => loadConversation(1))
</script>

<style scoped>
.chat-page { display: flex; height: 100%; background: #f8fafc; }
.chat-sidebar { width: 260px; background: #fff; border-right: 1px solid #e2e8f0; padding: 20px; }
.conv-item { padding: 12px; border-radius: 10px; cursor: pointer; }
.conv-item.active { background: #eff6ff; color: #1d4ed8; }
.chat-content { flex: 1; display: flex; flex-direction: column; padding: 24px; gap: 20px; }
.message-box { flex: 1; overflow-y: auto; display: flex; flex-direction: column; gap: 16px; }
.message { display: flex; }
.message.user { justify-content: flex-end; }
.message.ai { justify-content: flex-start; }
.bubble { max-width: 70%; padding: 14px 18px; border-radius: 16px; }
.message.user .bubble { background: #0077ff; color: #fff; }
.message.ai .bubble { background: #fff; border: 1px solid #e2e8f0; }
.chat-input { display: flex; gap: 12px; }
</style>