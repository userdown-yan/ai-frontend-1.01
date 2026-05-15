<template>
  <div class="input-area">
    <div class="container">
      <!-- Toolbar -->
      <div class="toolbar">
        <el-button link class="tool-btn">
          <Paperclip :size="18" />
        </el-button>
        <el-button link class="tool-btn">
          <ImageIcon :size="18" />
        </el-button>
        <el-button link class="tool-btn">
          <Smile :size="18" />
        </el-button>
        <div class="spacer"></div>
        <el-button link class="tool-btn">
          <Mic :size="18" />
        </el-button>
      </div>

      <!-- Input Box -->
      <div class="input-wrapper" :class="{ 'is-focused': isFocused }">
        <textarea
            ref="textareaRef"
            v-model="text"
            placeholder="输入消息，Enter 发送..."
            class="chat-textarea"
            @focus="isFocused = true"
            @blur="isFocused = false"
            @keydown.enter.exact.prevent="handleSend"
            @input="adjustHeight"
        ></textarea>

        <el-button
            type="primary"
            class="send-btn"
            :disabled="!text.trim()"
            @click="handleSend"
        >
          <Send :size="16" :class="{ 'icon-sending': text.trim() }" />
        </el-button>
      </div>

      <div class="hint">
        按下 Shift + Enter 换行
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, defineEmits } from 'vue';
import { Send, Paperclip, Smile, Image as ImageIcon, Mic } from 'lucide-vue-next';

const emit = defineEmits(['send']);
const text = ref('');
const isFocused = ref(false);
const textareaRef = ref(null);

const handleSend = () => {
  if (text.value.trim()) {
    emit('send', text.value);
    text.value = '';
    if (textareaRef.value) {
      textareaRef.value.style.height = 'auto';
    }
  }
};

const adjustHeight = (e) => {
  const target = e.target;
  target.style.height = 'auto';
  target.style.height = `${target.scrollHeight}px`;
};
</script>

<style scoped>
.input-area {
  padding: 16px;
  background-color: #ffffff;
  border-top: 1px solid #e2e8f0;
}

.container {
  max-width: 896px;
  margin: 0 auto;
}

.toolbar {
  display: flex;
  align-items: center;
  gap: 8px;
  margin-bottom: 12px;
  padding: 0 4px;
}

.tool-btn {
  padding: 8px;
  color: #94a3b8;
  border-radius: 8px;
  transition: all 0.2s;
}

.tool-btn:hover {
  color: #4f46e5;
  background-color: #eef2ff;
}

.spacer {
  flex: 1;
}

.input-wrapper {
  display: flex;
  align-items: flex-end;
  gap: 8px;
  background-color: #f8fafc;
  border: 1px solid #f1f5f9;
  border-radius: 16px;
  padding: 8px;
  transition: all 0.3s;
}

.input-wrapper.is-focused {
  border-color: #c7d2fe;
  background-color: #ffffff;
  box-shadow: 0 0 0 4px rgba(79, 70, 229, 0.05);
}

.chat-textarea {
  flex: 1;
  min-height: 44px;
  max-height: 128px;
  padding: 12px;
  border: none;
  background: transparent;
  resize: none;
  outline: none;
  font-size: 14px;
  color: #334155;
  line-height: 1.5;
}

.chat-textarea::placeholder {
  color: #94a3b8;
}

.send-btn {
  width: 40px;
  height: 40px;
  border-radius: 12px;
  background-color: #4f46e5;
  border: none;
  display: flex;
  align-items: center;
  justify-content: center;
  transition: all 0.3s;
}

.send-btn.is-disabled {
  background-color: #e2e8f0;
  color: #94a3b8;
  opacity: 0.6;
}

.icon-sending {
  transform: translate(1px, -1px);
}

.hint {
  margin-top: 8px;
  text-align: center;
  font-size: 10px;
  color: #94a3b8;
}
</style>