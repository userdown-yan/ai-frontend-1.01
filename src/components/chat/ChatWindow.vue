<template>
  <div class="chat-window">
    <!-- Header -->
    <header class="window-header">
      <div class="header-left">
        <el-button link class="toggle-btn" @click="$emit('toggle-sidebar')">
          <PanelLeftClose v-if="isSidebarOpen" :size="20" />
          <PanelLeftOpen v-else :size="20" />
        </el-button>

        <div class="session-display">
          <el-avatar :size="40" class="header-avatar" src="/agent-py/static/source/images/placeholder.svg?text=Avatar">
            {{ sessionName.slice(0, 1) }}
          </el-avatar>
          <div class="header-info">
            <h2 class="session-name">{{ sessionName }}</h2>
            <div class="status-box">
              <span class="online-dot"></span>
              <span class="status-text">在线</span>
            </div>
          </div>
        </div>
      </div>

      <div class="header-right">
        <el-button link class="action-btn"><Phone :size="18" /></el-button>
        <el-button link class="action-btn"><Video :size="18" /></el-button>
        <div class="divider"></div>
        <el-button link class="action-btn"><MoreHorizontal :size="18" /></el-button>
      </div>
    </header>

    <!-- Chat Content -->
    <div ref="scrollRef" class="chat-content custom-scrollbar">
      <div v-if="messages.length === 0" class="empty-state">
        <div class="empty-icon-wrapper">
          <MessageCircle :size="32" />
        </div>
        <p>暂无消息，开始聊天吧</p>
      </div>

      <div v-else class="message-list">
        <div
            v-for="(msg, index) in messages"
            :key="msg.id"
            class="message-wrapper"
            :class="[msg.sender === 'user' ? 'user-msg' : 'ai-msg']"
        >
          <div class="message-container">
            <el-avatar
                :size="36"
                class="msg-avatar"
                :src="msg.sender === 'user' ? '/agent-py/static/source/images/placeholder.svg?text=Me' : '/agent-py/static/source/images/placeholder.svg?text=AI'"
            >
              {{ msg.sender === 'user' ? 'U' : 'AI' }}
            </el-avatar>

            <div class="msg-content-wrapper">
              <div class="bubble">
                <template v-if="msg.content === ''">
                  <div class="typing-dots">
                    <span class="dot"></span>
                    <span class="dot"></span>
                    <span class="dot"></span>
                  </div>
                </template>
                <template v-else>
                  {{ msg.content }}
                </template>
              </div>
              <span class="msg-timestamp">{{ msg.timestamp }}</span>
            </div>
          </div>
        </div>
      </div>
    </div>

    <!-- Input Area -->
    <ChatInput @send="$emit('send-message', $event)" />
  </div>
</template>

<script setup>
import { ref, onMounted, watch, nextTick } from 'vue';
import {
  Phone,
  Video,
  MoreHorizontal,
  PanelLeftClose,
  PanelLeftOpen,
  MessageCircle
} from 'lucide-vue-next';
import ChatInput from './ChatInput.vue';

const props = defineProps({
  sessionName: {
    type: String,
    default: 'AI助手'
  },
  messages: {
    type: Array,
    default: () => []
  },
  isSidebarOpen: {
    type: Boolean,
    default: true
  }
});

defineEmits(['send-message', 'toggle-sidebar']);

const scrollRef = ref(null);

const scrollToBottom = () => {
  nextTick(() => {
    if (scrollRef.value) {
      scrollRef.value.scrollTop = scrollRef.value.scrollHeight;
    }
  });
};

watch(() => props.messages, scrollToBottom, { deep: true });

onMounted(scrollToBottom);
</script>

<style scoped>
.chat-window {
  display: flex;
  flex-direction: column;
  height: 100%;
  background-color: rgba(248, 250, 252, 0.3);
}

.window-header {
  height: 64px;
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 0 24px;
  background-color: rgba(255, 255, 255, 0.8);
  backdrop-filter: blur(12px);
  border-bottom: 1px solid #e2e8f0;
  z-index: 10;
}

.header-left {
  display: flex;
  align-items: center;
  gap: 16px;
}

.toggle-btn {
  color: #64748b;
  transition: color 0.2s;
}

.toggle-btn:hover {
  color: #4f46e5;
}

.session-display {
  display: flex;
  align-items: center;
  gap: 12px;
}

.header-avatar {
  border: 1px solid #f1f5f9;
}

.header-info {
  display: flex;
  flex-direction: column;
}

.session-name {
  font-size: 16px;
  font-weight: 600;
  color: #1e293b;
  line-height: 1.2;
}

.status-box {
  display: flex;
  align-items: center;
  gap: 6px;
}

.online-dot {
  width: 6px;
  height: 6px;
  background-color: #10b981;
  border-radius: 50%;
  box-shadow: 0 0 0 2px rgba(16, 185, 129, 0.2);
}

.status-text {
  font-size: 11px;
  color: #94a3b8;
  font-weight: 500;
}

.header-right {
  display: flex;
  align-items: center;
  gap: 4px;
}

.action-btn {
  color: #94a3b8;
}

.action-btn:hover {
  color: #4f46e5;
}

.divider {
  width: 1px;
  height: 16px;
  background-color: #e2e8f0;
  margin: 0 8px;
}

.chat-content {
  flex: 1;
  overflow-y: auto;
  padding: 32px 24px;
}

.empty-state {
  height: 100%;
  display: flex;
  flex-direction: column;
  items-center: center;
  justify-content: center;
  opacity: 0.4;
  text-align: center;
}

.empty-icon-wrapper {
  width: 64px;
  height: 64px;
  background-color: #f1f5f9;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  margin: 0 auto 16px;
  color: #94a3b8;
}

.empty-state p {
  font-size: 14px;
  color: #64748b;
}

.message-list {
  display: flex;
  flex-direction: column;
  gap: 32px;
}

.message-wrapper {
  display: flex;
}

.user-msg {
  justify-content: flex-end;
}

.ai-msg {
  justify-content: flex-start;
}

.message-container {
  display: flex;
  gap: 12px;
  max-width: 80%;
}

.user-msg .message-container {
  flex-direction: row-reverse;
}

.msg-avatar {
  margin-top: 4px;
  flex-shrink: 0;
  border: 1px solid #ffffff;
  box-shadow: 0 1px 2px rgba(0, 0, 0, 0.05);
}

.msg-content-wrapper {
  display: flex;
  flex-direction: column;
}

.user-msg .msg-content-wrapper {
  align-items: flex-end;
}

.ai-msg .msg-content-wrapper {
  align-items: flex-start;
}

.bubble {
  padding: 10px 16px;
  border-radius: 16px;
  font-size: 14px;
  line-height: 1.6;
  position: relative;
  box-shadow: 0 1px 2px rgba(0, 0, 0, 0.05);
}

.user-msg .bubble {
  background-color: #4f46e5;
  color: #ffffff;
  border-top-right-radius: 0;
}

.ai-msg .bubble {
  background-color: #ffffff;
  color: #1e293b;
  border-top-left-radius: 0;
  border: 1px solid #f1f5f9;
}

.msg-timestamp {
  font-size: 10px;
  color: #94a3b8;
  margin-top: 6px;
  padding: 0 4px;
  opacity: 0;
  transition: opacity 0.2s;
}

.message-wrapper:hover .msg-timestamp {
  opacity: 1;
}

.typing-dots {
  display: flex;
  gap: 4px;
  padding: 4px 0;
}

.dot {
  width: 6px;
  height: 6px;
  background-color: #cbd5e1;
  border-radius: 50%;
  animation: bounce 1.4s infinite ease-in-out both;
}

.dot:nth-child(1) { animation-delay: -0.32s; }
.dot:nth-child(2) { animation-delay: -0.16s; }

@keyframes bounce {
  0%, 80%, 100% { transform: scale(0); }
  40% { transform: scale(1.0); }
}
</style>