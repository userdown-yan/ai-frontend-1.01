import { createRouter, createWebHistory } from "vue-router"

import LoginView from "../views/LoginView.vue"
import RegisterView from "../views/RegisterView.vue"
import MainLayout from "../layouts/MainLayout.vue"
import ChatView from "../views/ChatView.vue"

// 页面
const DashboardView = { template: "<div>Dashboard</div>" }
const TasksView = { template: "<div>Tasks</div>" }

const routes = [
    // 登录系统
    {
        path: "/login",
        component: LoginView
    },
    {
        path: "/register",
        component: RegisterView
    },

    // 主系统（Layout）
    {
        path: "/",
        component: MainLayout,
        children: [
            {
                path: "",
                redirect: "/chat"
            },
            {
                path: "chat",
                component: ChatView
            },
            {
                path: "dashboard",
                component: DashboardView
            },
            {
                path: "tasks",
                component: TasksView
            }
        ]
    }
]

const router = createRouter({
    history: createWebHistory(),
    routes
})

// ======================
// 🔥 路由守卫（关键）
// ======================
router.beforeEach((to, from, next) => {
    const token = localStorage.getItem("token")

    const publicPages = ["/login", "/register"]

    if (!publicPages.includes(to.path) && !token) {
        next("/login")
    } else {
        next()
    }
})

export default router