<template>
  <div class="dashboard-page">
    <section class="hero">
      <div>
        <h1>欢迎回来，{{ userName }}</h1>
        <p>{{ summary.todayDate }} · 你的 AI 成长中枢已就绪</p>
      </div>
      <el-card class="ai-advice" shadow="never">
        <template #header>🤖 AI 今日建议</template>
        <p>{{ summary.aiAdvice }}</p>
      </el-card>
    </section>

    <section class="metrics-grid">
      <el-card shadow="hover">
        <div class="metric">
          <span>任务完成率</span>
          <strong>{{ summary.taskCompletionRate }}%</strong>
        </div>
        <el-progress :percentage="summary.taskCompletionRate" />
      </el-card>

      <el-card shadow="hover">
        <div class="metric">
          <span>专注时长</span>
          <strong>{{ summary.focusHours }}h</strong>
        </div>
      </el-card>

      <el-card shadow="hover">
        <div class="metric">
          <span>习惯达成</span>
          <strong>{{ summary.habitCompleted }}/{{ summary.habitTotal }}</strong>
        </div>
      </el-card>

      <el-card shadow="hover">
        <div class="metric">
          <span>目标推进</span>
          <strong>{{ summary.goalProgress }}%</strong>
        </div>
        <el-progress :percentage="summary.goalProgress" status="success" />
      </el-card>
    </section>

    <section class="insight-grid">
      <el-card shadow="never">
        <template #header>📈 本周成长趋势</template>
        <ul>
          <li v-for="(item, index) in summary.weeklyTrends" :key="index">{{ item }}</li>
        </ul>
      </el-card>

      <el-card shadow="never">
        <template #header>⚠️ AI 风险提醒</template>
        <ul>
          <li v-for="(item, index) in summary.risks" :key="index">{{ item }}</li>
        </ul>
      </el-card>

      <el-card shadow="never">
        <template #header>✅ 今日关键动作</template>
        <ul>
          <li v-for="(item, index) in summary.todayActions" :key="index">{{ item }}</li>
        </ul>
      </el-card>
    </section>
  </div>
</template>

<script setup>
import { onMounted, reactive } from 'vue'
import { ElMessage } from 'element-plus'
import { getDashboardSummary } from '@/api/dashboard'

const userName = localStorage.getItem('username') || '成长者'

const summary = reactive({
  todayDate: '-',
  aiAdvice: '',
  taskCompletionRate: 0,
  focusHours: 0,
  habitCompleted: 0,
  habitTotal: 0,
  goalProgress: 0,
  weeklyTrends: [],
  risks: [],
  todayActions: []
})

const loadSummary = async () => {
  try {
    const res = await getDashboardSummary()
    Object.assign(summary, res.data)
  } catch (e) {
    ElMessage.error('成长中枢加载失败')
  }
}

onMounted(loadSummary)
</script>

<style scoped>
.dashboard-page {
  padding: 20px;
  color: #1f2937;
  background: #ffffff;
  min-height: 100%;
}

.hero {
  display: grid;
  grid-template-columns: 1.4fr 1fr;
  gap: 16px;
  margin-bottom: 16px;
}

.metrics-grid {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 12px;
  margin-bottom: 16px;
}

.insight-grid {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 12px;
}

.metric {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 8px;
}

.metric strong {
  color: #2563eb;
  font-size: 22px;
}

:deep(.el-card) {
  background: #ffffff;
  border: 1px solid #e5e7eb;
  color: #111827;
}

ul {
  margin: 0;
  padding-left: 18px;
}

li {
  margin: 8px 0;
  color: #4b5563;
}

@media (max-width: 1100px) {
  .hero,
  .metrics-grid,
  .insight-grid {
    grid-template-columns: 1fr;
  }
}
</style>