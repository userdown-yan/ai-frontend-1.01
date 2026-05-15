import { defineStore } from "pinia"

export const useUserStore = defineStore("user", {
    state: () => ({
        token: localStorage.getItem("token") || "",
        userInfo: {}
    }),

    actions: {
        setToken(token) {
            this.token = token
            localStorage.setItem("token", token)
        },

        logout() {
            this.token = ""
            this.userInfo = {}
            localStorage.removeItem("token")
        }
    }
})