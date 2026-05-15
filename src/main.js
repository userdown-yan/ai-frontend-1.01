import { createApp } from "vue"
import App from "./App.vue"

// 路由
import router from "./router"

// 状态管理
import { createPinia } from "pinia"

// UI库
import ElementPlus from "element-plus"
import "element-plus/dist/index.css"

// 全局样式
import "./style.css"

// 创建应用实例
const app = createApp(App)

// 插件注册（按顺序更清晰）
app.use(createPinia())
app.use(router)
app.use(ElementPlus)

// 挂载
app.mount("#app")