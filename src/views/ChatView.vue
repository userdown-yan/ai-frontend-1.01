<template>
  <div class="chat-page" :class="themeClass">
    <aside class="sidebar glass-panel">
      <div class="sidebar-header">
        <h2>会话</h2>
        <div class="sidebar-actions">
          <el-switch
              v-model="isDark"
              inline-prompt
              active-text="暗"
              inactive-text="亮"
          />
          <el-button type="primary" @click="createNewChat">+ 新对话</el-button>
        </div>
      </div>

      <el-input
          v-model="searchKeyword"
          placeholder="搜索会话..."
          class="search-input"
          clearable
      />

      <div class="conv-list">
        <div
            v-for="item in filteredConversations"
            :key="item.id"
            class="conv-item"
            :class="{ active: currentConversationId === item.id }"
            @click="loadConversation(item.id)"
        >
          <div class="conv-top">
            <span class="title">{{ item.title }}</span>
            <span class="time">{{ item.updatedAt }}</span>
          </div>
          <div class="preview">{{ item.preview || '暂无消息' }}</div>
          <el-badge v-if="item.unread" :value="item.unread" type="danger" class="badge" />
        </div>
      </div>
    </aside>

    <section class="chat-content glass-panel">
      <header class="toolbar">
        <el-select v-model="selectedModel" placeholder="模型" style="width: 160px;">
          <el-option v-for="item in models" :key="item" :label="item" :value="item" />
        </el-select>

        <el-select v-model="selectedTemplate" placeholder="对话模板" style="width: 220px;" clearable>
          <el-option v-for="item in templates" :key="item.name" :label="item.name" :value="item.prompt" />
        </el-select>

        <el-button @click="applyTemplate">应用模板</el-button>

        <el-upload :auto-upload="false" :show-file-list="false" :on-change="onFileSelect">
          <el-button>多模态上传</el-button>
        </el-upload>
      </header>

      <div class="message-box" ref="messageBox">
        <div v-if="messages.length === 0" class="empty-state">开始你的第一条对话吧 👋</div>

        <div
            v-for="(msg, idx) in messages"
            :key="idx"
            class="message"
            :class="msg.role"
        >
          <div class="bubble markdown" v-html="renderMarkdown(msg.content)"></div>
          <div class="ops">
            <el-button link size="small" @click="copyText(msg.content)">复制</el-button>
            <el-button link size="small" @click="quoteText(msg.content)">引用</el-button>
          </div>
        </div>
      </div>

      <footer class="chat-input">
        <el-input
            v-model="input"
            type="textarea"
            :rows="3"
            placeholder="输入消息（Enter 发送，Shift+Enter 换行）"
            @keyup.enter.exact.prevent="sendMessage"
        />
        <el-button type="primary" :disabled="!input.trim()" @click="sendMessage">发送</el-button>
      </footer>
    </section>
  </div>
</template>

<script setup>
import { computed, nextTick, onMounted, ref, watch } from 'vue'
import { ElMessage } from 'element-plus'
import { getModels, getTemplates } from '@/api/product'

const isDark = ref(localStorage.getItem('theme') !== 'light')
const input = ref('')
const searchKeyword = ref('')
const selectedModel = ref('gpt-4o-mini')
const selectedTemplate = ref('')
const messageBox = ref(null)

const models = ref(['gpt-4o-mini'])
const templates = ref([])
const messages = ref([])
const conversations = ref([{ id: 1, title: '默认对话', preview: '', unread: 0, updatedAt: '刚刚', messages: [] }])
const currentConversationId = ref(1)

const themeClass = computed(() => (isDark.value ? 'theme-dark' : 'theme-light'))
const filteredConversations = computed(() => conversations.value.filter(c => c.title.includes(searchKeyword.value)))

watch(isDark, (value) => localStorage.setItem('theme', value ? 'dark' : 'light'))

onMounted(async () => {
  await loadProductOptions()
  loadConversation(1)
})

const loadProductOptions = async () => {
  try {
    const [modelRes, templateRes] = await Promise.all([getModels(), getTemplates()])
    models.value = modelRes.data.data || []
    templates.value = templateRes.data.data || []
  } catch (e) {
    ElMessage.warning('模型或模板加载失败，已使用默认值')
  }
}

const createNewChat = () => {
  const id = Date.now()
  conversations.value.unshift({
    id,
    title: `新对话-${String(id).slice(-4)}`,
    preview: '',
    unread: 0,
    updatedAt: formatTime(),
    messages: []
  })
  loadConversation(id)
}

const loadConversation = (id) => {
  currentConversationId.value = id
  const target = conversations.value.find(item => item.id === id)
  if (target) {
    messages.value = target.messages
    target.unread = 0
    scrollBottom()
  }
}

const saveConversation = () => {
  const target = conversations.value.find(item => item.id === currentConversationId.value)
  if (!target) return
  target.messages = messages.value
  target.preview = messages.value.at(-1)?.content?.slice(0, 40) || ''
  target.updatedAt = formatTime()
}

const sendMessage = async () => {
  const text = input.value.trim()
  if (!text) return

  messages.value.push({ role: 'user', content: text })
  input.value = ''

  const aiReply = [
    '### AI 回复',
    '',
    `当前模型：**${selectedModel.value}**`,
    '',
    '```text',
    text,
    '```',
    '',
    '| 功能 | 状态 |',
    '| --- | --- |',
    '| Markdown | ✅ |',
    '| 模板能力 | ✅ |'
  ].join('\n')

  messages.value.push({ role: 'ai', content: aiReply })
  saveConversation()
  scrollBottom()
}

const onFileSelect = (file) => {
  messages.value.push({ role: 'user', content: `已上传文件：${file.name}` })
  saveConversation()
  scrollBottom()
}

const applyTemplate = () => {
  if (!selectedTemplate.value) return
  input.value = `${selectedTemplate.value}\n`
}

const copyText = async (content) => {
  await navigator.clipboard.writeText(content)
  ElMessage.success('已复制')
}

const quoteText = (content) => {
  input.value = `> ${content}\n`
}

const renderMarkdown = (raw = '') => {
  const safe = raw
      .replace(/&/g, '&amp;')
      .replace(/</g, '&lt;')
      .replace(/>/g, '&gt;')

  return safe
      .replace(/^###\s(.+)$/gm, '<h3>$1</h3>')
      .replace(/\*\*(.+?)\*\*/g, '<strong>$1</strong>')
      .replace(/```([\s\S]*?)```/g, '<pre><code>$1</code></pre>')
      .replace(/\|(.+)\|/g, '<div class="md-row">|$1|</div>')
      .replace(/\n/g, '<br/>')
}

const scrollBottom = () => nextTick(() => {
  if (messageBox.value) messageBox.value.scrollTop = messageBox.value.scrollHeight
})

const formatTime = () => new Date().toLocaleTimeString([], { hour: '2-digit', minute: '2-digit' })
</script>

<style scoped>
.chat-page { display: grid; grid-template-columns: 320px 1fr; gap: 16px; height: 100%; }
.glass-panel { border-radius: 16px; padding: 16px; backdrop-filter: blur(10px); border: 1px solid rgba(255,255,255,.16); }
.theme-dark { --bg: #0f172a; --panel: rgba(30,41,59,.72); --text: #e2e8f0; --user: #2563eb; --ai: #1e293b; }
.theme-light { --bg: #f3f4f6; --panel: rgba(255,255,255,.85); --text: #0f172a; --user: #2563eb; --ai: #ffffff; }
.chat-page { background: var(--bg); color: var(--text); }
.sidebar,.chat-content { background: var(--panel); }
.sidebar-header,.toolbar { display: flex; align-items: center; justify-content: space-between; gap: 8px; flex-wrap: wrap; }
.sidebar-actions { display: flex; gap: 8px; }
.search-input { margin: 12px 0; }
.conv-list { max-height: calc(100vh - 220px); overflow: auto; }
.conv-item { padding: 10px; border-radius: 12px; margin-bottom: 8px; cursor: pointer; transition: .2s; position: relative; }
.conv-item:hover { transform: translateY(-1px); box-shadow: 0 6px 16px rgba(0,0,0,.2); }
.conv-item.active { outline: 1px solid #6366f1; }
.conv-top { display: flex; justify-content: space-between; font-size: 13px; }
.title { font-weight: 600; }
.time,.preview { opacity: .75; font-size: 12px; }
.badge { position: absolute; right: 10px; bottom: 8px; }
.message-box { flex: 1; min-height: 56vh; max-height: 56vh; overflow: auto; margin: 14px 0; display: flex; flex-direction: column; gap: 12px; }
.message { max-width: 80%; display: flex; flex-direction: column; }
.message.user { margin-left: auto; align-items: flex-end; }
.bubble { padding: 12px 14px; border-radius: 14px; box-shadow: 0 8px 20px rgba(0,0,0,.12); transition: .2s; }
.bubble:hover { transform: translateY(-1px); }
.user .bubble { background: var(--user); color: #fff; }
.ai .bubble { background: var(--ai); color: var(--text); }
.ops { opacity: .75; }
.chat-input { display: flex; gap: 10px; align-items: flex-end; }
pre { background: #020617; color: #e2e8f0; border-radius: 8px; padding: 8px; overflow: auto; }
.empty-state { opacity: .7; text-align: center; margin-top: 20px; }
</style>