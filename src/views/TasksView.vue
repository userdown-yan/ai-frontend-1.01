<template>
  <el-card>
    <template #header>
      <b>任务管理</b>
    </template>

    <div class="task-input">
      <el-input v-model="text" placeholder="输入任务" @keyup.enter="addTask" />
      <el-button type="primary" @click="addTask">新增</el-button>
    </div>

    <el-empty v-if="!tasks.length" description="暂无任务" />

    <div v-for="t in tasks" :key="t.id" class="task-item">
      <el-checkbox v-model="t.done" @change="save">{{ t.text }}</el-checkbox>
      <el-button text type="danger" @click="removeTask(t.id)">删除</el-button>
    </div>
  </el-card>
</template>

<script setup>
import { ref } from 'vue'

const text = ref('')
const tasks = ref([])

try {
  tasks.value = JSON.parse(localStorage.getItem('growth_tasks') || '[]')
} catch {
  tasks.value = []
}

const save = () => {
  localStorage.setItem('growth_tasks', JSON.stringify(tasks.value))
}

const addTask = () => {
  const value = text.value.trim()
  if (!value) return

  tasks.value.unshift({
    id: Date.now(),
    text: value,
    done: false
  })
  text.value = ''
  save()
}

const removeTask = (id) => {
  tasks.value = tasks.value.filter(t => t.id !== id)
  save()
}
</script>

<style scoped>
.task-input {
  display: flex;
  gap: 8px;
  margin-bottom: 12px;
}

.task-item {
  display: flex;
  justify-content: space-between;
  padding: 8px 0;
  border-bottom: 1px solid #f1f5f9;
}
</style>