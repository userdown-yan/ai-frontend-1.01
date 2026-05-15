<template>
  <div class="dashboard">

    <!-- 顶部标题 -->
    <div class="header">
      <h2>📊 Dashboard</h2>
      <p>你的今日成长状态</p>
    </div>

    <!-- 卡片区 -->
    <div class="grid">

      <!-- 1. 今日任务完成率 -->
      <el-card class="card">
        <div class="card-title">📌 今日任务完成率</div>

        <el-progress
            :percentage="72"
            status="success"
        />

        <div class="desc">
          比昨日提升 +12%
        </div>
      </el-card>

      <!-- 2. AI建议 -->
      <el-card class="card">
        <div class="card-title">🤖 AI建议</div>

        <div class="ai-box">
          今天适合专注完成高优先级任务，而不是增加新任务。
        </div>

        <el-button type="primary" size="small">
          查看详细分析
        </el-button>
      </el-card>

      <!-- 3. 行为统计 -->
      <el-card class="card">
        <div class="card-title">📈 行为统计</div>

        <div class="stats">
          <div>专注时长：3.2h</div>
          <div>完成任务：6个</div>
          <div>效率评分：85</div>
        </div>
      </el-card>

      <!-- 4. 快捷入口 -->
      <el-card class="card">
        <div class="card-title">⚡ 快捷操作</div>

        <div class="actions">
          <el-button type="primary">➕ 新建任务</el-button>
          <el-button>🤖 询问AI</el-button>
        </div>
      </el-card>

    </div>

  </div>
</template>

<style scoped>
.dashboard {
  padding: 10px;
}

/* 顶部 */
.header {
  margin-bottom: 20px;
}

.header h2 {
  margin: 0;
}

/* 卡片布局 */
.grid {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: 20px;
}

/* 卡片 */
.card {
  border-radius: 14px;
}

/* 标题 */
.card-title {
  font-weight: bold;
  margin-bottom: 12px;
}

/* AI建议框 */
.ai-box {
  background: #f5f7fb;
  padding: 12px;
  border-radius: 10px;
  margin-bottom: 10px;
  font-size: 14px;
  color: #333;
}

/* 行为统计 */
.stats div {
  margin: 6px 0;
}

/* 按钮区 */
.actions {
  display: flex;
  gap: 10px;
}
</style>