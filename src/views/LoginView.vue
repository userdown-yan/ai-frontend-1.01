<template>
  <div class="login-container">
    <div class="login-box">
      <h1>Growth OS</h1>

      <input
          v-model="username"
          placeholder="用户名"
      />

      <input
          v-model="password"
          type="password"
          placeholder="密码"
      />

      <button @click="handleLogin">
        登录
      </button>

      <p class="register-tip" @click="goToRegister">
        没有账号？立即注册
      </p>
    </div>
  </div>
</template>

<script setup>
import { ref } from "vue";
import { login } from "../api/user";
import { useRouter } from "vue-router";
import { useUserStore } from "../stores/user";

const router = useRouter();
const userStore = useUserStore();

const username = ref("");
const password = ref("");

// 登录（优化版：只存 store，不再双写 localStorage）
const handleLogin = async () => {
  if (!username.value || !password.value) {
    alert("请输入账号和密码！");
    return;
  }

  try {
    const res = await login({
      username: username.value,
      password: password.value,
    });

    console.log("登录返回:", res.data);

    if (res.data.code === 200) {
      // ✅ 只存 store，store 内部会自动存 localStorage
      userStore.setToken(res.data.data);

      alert("登录成功！");
      router.push("/chat");
    } else {
      alert("登录失败：" + res.data.message);
    }

  } catch (err) {
    console.error(err);
    alert("登录失败：服务器异常");
  }
};

// 去注册
const goToRegister = () => {
  router.push("/register");
};
</script>

<style scoped>
.login-container {
  width: 100vw;
  height: 100vh;
  display: flex;
  justify-content: center;
  align-items: center;
  background: #f5f5f5;
}

.login-box {
  width: 350px;
  background: white;
  padding: 40px;
  border-radius: 12px;
  box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
  display: flex;
  flex-direction: column;
  gap: 20px;
}

input {
  height: 45px;
  padding: 0 10px;
  border: 1px solid #ddd;
  border-radius: 8px;
}

button {
  height: 45px;
  border: none;
  background: black;
  color: white;
  border-radius: 8px;
  cursor: pointer;
}

.register-tip {
  text-align: center;
  color: #666;
  font-size: 14px;
  cursor: pointer;
}
</style>