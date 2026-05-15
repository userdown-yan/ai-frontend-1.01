<script setup>
import { useRouter } from "vue-router"
import { useUserStore } from "../stores/user"

const router = useRouter()
const userStore = useUserStore()

// 退出登录
const logout = () => {
  userStore.logout()
  router.push("/login")
}
</script>

<template>
  <el-container class="layout">

    <!-- 左侧 -->
    <el-aside width="220px" class="sidebar">

      <div class="logo">Growth OS</div>

      <el-menu
          router
          default-active="/chat"
      >
        <el-menu-item index="/chat">🤖 AI Chat</el-menu-item>
        <el-menu-item index="/dashboard">📊 Dashboard</el-menu-item>
        <el-menu-item index="/tasks">📌 Tasks</el-menu-item>
      </el-menu>

    </el-aside>

    <!-- 右侧 -->
    <el-container>

      <!-- 顶部：右边加退出按钮 -->
      <el-header class="header">
        <div>AI Growth System</div>

        <!-- 退出登录按钮 -->
        <el-button type="danger" size="small" @click="logout">
          退出
        </el-button>
      </el-header>

      <!-- 内容 -->
      <el-main class="main">
        <router-view />
      </el-main>

    </el-container>

  </el-container>
</template>

<style scoped>
.layout {
  height: 100vh;
}

.sidebar {
  background: #0f172a;
  color: white;
}

.logo {
  padding: 20px;
  font-size: 18px;
  font-weight: bold;
  color: white;
}

.header {
  background: white;
  border-bottom: 1px solid #eee;
  display: flex;
  align-items: center;
  padding: 0 20px;
  /* 让标题靠左，退出按钮靠右 */
  justify-content: space-between;
}

.main {
  background: #f5f7fb;
  padding: 20px;
}
</style>