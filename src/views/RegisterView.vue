<template>
  <div class="register-container">
    <div class="register-box">
      <h1>注册账号</h1>

      <input
          v-model="username"
          placeholder="请输入用户名"
      />

      <input
          v-model="password"
          type="password"
          placeholder="请输入密码"
      />

      <button @click="handleRegister">
        注册
      </button>

      <p class="tip" @click="goBack">
        已有账号？返回登录
      </p>
    </div>
  </div>
</template>

<script setup>
import { ref } from "vue";
import { useRouter } from "vue-router";
import { register } from "../api/user";

const router = useRouter();
const username = ref("");
const password = ref("");

// 注册
const handleRegister = async () => {
  if (!username.value || !password.value) {
    alert("请输入账号密码");
    return;
  }

  try {
    const res = await register({
      username: username.value,
      password: password.value,
    });

    console.log("注册返回:", res.data);

    if (res.data.code === 200) {
      alert("注册成功！请登录");
      router.push("/login"); // 注册成功 → 自动返回登录页
    } else {
      alert(res.data.message);
    }
  } catch (e) {
    console.log(e);
    alert("注册失败，请检查后端是否启动");
  }
};

// 返回登录
const goBack = () => {
  router.push("/login");
};
</script>

<style scoped>
.register-container {
  width: 100vw;
  height: 100vh;
  display: flex;
  justify-content: center;
  align-items: center;
  background: #f5f5f5;
}

.register-box {
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

.tip {
  text-align: center;
  color: #666;
  font-size: 14px;
  cursor: pointer;
}
</style>