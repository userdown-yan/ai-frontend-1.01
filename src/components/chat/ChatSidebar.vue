<template>
  <div class="sidebar">
    <!-- Sidebar Header -->
    <div class="header">
      <h2 class="title">
        <MessageSquare class="icon-primary" />
        消息
      </h2>
      <el-button circle variant="ghost" class="plus-btn">
        <Plus :size="20" />
      </el-button>
    </div>

    <!-- Search Bar -->
    <div class="search-container">
      <el-input
          placeholder="搜索会话..."
          class="search-input"
          :prefix-icon="SearchIcon"
      />
    </div>

    <!-- Session List -->
    <div class="session-list custom-scrollbar">
      <div
          v-for="session in sessions"
          :key="session.id"
          class="session-item"
          :class="{ active: activeId === session.id }"
          @click="$emit('select', session.id)"
      >
        <div class="avatar-wrapper">
          <el-avatar :size="48" :src="session.avatar" class="session-avatar">
            {{ session.title.slice(0, 1) }}
          </el-avatar>
          <div class="status-indicator"></div>
        </div>

        <div class="session-info">
          <div class="info-top">
            <h3 class="session-title">{{ session.title }}</h3>
            <span class="timestamp">12:30</span>
          </div>
          <p class="last-message">{{ session.lastMessage }}</p>
        </div>

        <el-badge
            v-if="session.unreadCount > 0"
            :value="session.unreadCount"
            class="unread-badge"
        />

        <div class="more-options">
          <el-button link class="more-btn">
            <MoreVertical :size="16" />
          </el-button>
        </div>
      </div>
    </div>

    <!-- Profile Footer -->
    <div class="footer">
      <div class="profile-info">
        <el-avatar :size="40" src="/agent-py/static/source/images/placeholder.svg?text=Me" />
        <div class="user-details">
          <p class="user-id">用户 ID: 8848</p>
          <p class="user-status">在线</p>
        </div>
        <el-button link class="more-btn">
          <MoreVertical :size="16" />
        </el-button>
      </div>
    </div>
  </div>
</template>

<script setup>
import { defineProps, defineEmits, h } from 'vue';
import { MessageSquare, Plus, Search as SearchIconLucide, MoreVertical } from 'lucide-vue-next';

const SearchIcon = () => h(SearchIconLucide, { size: 16 });

defineProps({
  sessions: {
    type: Array,
    default: () => []
  },
  activeId: {
    type: String,
    default: ''
  }
});

defineEmits(['select']);
</script>

<style scoped>
.sidebar {
  display: flex;
  flex-direction: column;
  height: 100%;
  background-color: #ffffff;
}

.header {
  padding: 16px;
  display: flex;
  align-items: center;
  justify-content: space-between;
}

.title {
  font-size: 20px;
  font-weight: 700;
  color: #1e293b;
  display: flex;
  align-items: center;
  gap: 8px;
}

.icon-primary {
  color: #4f46e5;
  width: 24px;
  height: 24px;
}

.plus-btn {
  border: none;
  background: transparent;
  color: #64748b;
}

.plus-btn:hover {
  background-color: #f1f5f9;
  color: #4f46e5;
}

.search-container {
  padding: 0 16px 16px;
}

.search-input :deep(.el-input__wrapper) {
  background-color: #f8fafc;
  box-shadow: none;
  border: none;
  border-radius: 8px;
}

.session-list {
  flex: 1;
  overflow-y: auto;
  padding: 0 8px;
  display: flex;
  flex-direction: column;
  gap: 4px;
}

.session-item {
  position: relative;
  display: flex;
  align-items: center;
  gap: 12px;
  padding: 12px;
  border-radius: 12px;
  cursor: pointer;
  transition: all 0.2s;
}

.session-item:hover {
  background-color: #f8fafc;
}

.session-item.active {
  background-color: #eef2ff;
}

.avatar-wrapper {
  position: relative;
}

.session-avatar {
  border: 2px solid #ffffff;
  box-shadow: 0 1px 2px rgba(0, 0, 0, 0.05);
}

.status-indicator {
  position: absolute;
  bottom: 0;
  right: 0;
  width: 12px;
  height: 12px;
  background-color: #10b981;
  border: 2px solid #ffffff;
  border-radius: 50%;
}

.session-info {
  flex: 1;
  min-width: 0;
}

.info-top {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 4px;
}

.session-title {
  font-size: 14px;
  font-weight: 600;
  color: #1e293b;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.active .session-title {
  color: #312e81;
}

.timestamp {
  font-size: 10px;
  color: #94a3b8;
}

.last-message {
  font-size: 12px;
  color: #64748b;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.active .last-message {
  color: #4f46e5;
  opacity: 0.7;
}

.unread-badge {
  margin-left: 4px;
}

.unread-badge :deep(.el-badge__content) {
  background-color: #4f46e5;
  border: none;
}

.more-options {
  opacity: 0;
  position: absolute;
  right: 8px;
  transition: opacity 0.2s;
}

.session-item:hover .more-options {
  opacity: 1;
}

.footer {
  padding: 16px;
  border-top: 1px solid #f1f5f9;
  background-color: rgba(248, 250, 252, 0.5);
}

.profile-info {
  display: flex;
  align-items: center;
  gap: 12px;
}

.user-details {
  flex: 1;
  min-width: 0;
}

.user-id {
  font-size: 14px;
  font-weight: 600;
  color: #1e293b;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.user-status {
  font-size: 12px;
  color: #64748b;
}

.more-btn {
  color: #94a3b8;
}
</style>