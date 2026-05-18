<script setup>
import { useRouter } from "vue-router"
import { useUserStore } from "../stores/user"

const router = useRouter()
const userStore = useUserStore()

const logout = () => {
  userStore.logout()
  router.push("/login")
}
</script>

<template>
  <el-container class="layout">
    <el-aside width="220px" class="sidebar">
      <div class="logo">Growth OS</div>

      <el-menu router default-active="/chat" class="menu">
        <el-menu-item index="/chat">🤖 AI Chat</el-menu-item>
        <el-menu-item index="/dashboard">📊 Dashboard</el-menu-item>
        <el-menu-item index="/tasks">📌 Tasks</el-menu-item>
      </el-menu>
    </el-aside>

    <el-container>
      <el-header class="header">
        <div>AI Growth System</div>
        <el-button type="danger" size="small" @click="logout">退出</el-button>
      </el-header>

      <el-main class="main">
        <router-view />
      </el-main>
    </el-container>
  </el-container>
</template>

<style scoped>
.layout {
  height: 100vh;
  background: #ffffff;
}

.sidebar {
  background: #ffffff;
  color: #111827;
  border-right: 1px solid #e5e7eb;
}

.logo {
  padding: 20px;
  font-size: 18px;
  font-weight: 700;
  color: #111827;
}

.menu {
  border-right: none;
  background: #ffffff;
}

.header {
  background: #ffffff;
  border-bottom: 1px solid #e5e7eb;
  display: flex;
  align-items: center;
  padding: 0 20px;
  justify-content: space-between;
  color: #111827;
}

.main {
  background: #ffffff;
  padding: 20px;
}
</style>