<template>

  <a-form
      :model="formState"
      name="basic"
      :label-col="{ span: 8 }"
      :wrapper-col="{ span: 16 }"
      autocomplete="off"
      @finish="onFinish"
      @finishFailed="onFinishFailed"
      class="login"
  >
    <a-form-item
        label="用户名"
        name="userName"
        :rules="[{ required: true, message: '请输入用户名!' }]"
    >
      <a-input v-model:value="formState.userName"/>
    </a-form-item>
    <a-form-item
        label="密&nbsp;&nbsp;&nbsp;码"
        name="password"
        :rules="[{ required: true, message: '请输入密码!' }]"
    >
      <a-input-password v-model:value="formState.password"/>
    </a-form-item>
    <a-form-item name="remember" :wrapper-col="{ offset: 8, span: 16 }">
      <a-checkbox v-model:checked="formState.remember">记住我</a-checkbox>
      <a-button type="link" :size="20" @click="toReg">还没有账号？去注册</a-button>
    </a-form-item>
    <a-form-item :wrapper-col="{ offset: 8, span: 16 }">
      <a-button type="primary" html-type="submit">登录</a-button>
    </a-form-item>
    <!--加载效果-->
    <a-spin tip="Logining..." :spinning="spinning"></a-spin>
  </a-form>
</template>

<script setup lang="ts">
import {reactive, ref, toRaw} from 'vue';
import {useRouter} from "vue-router";
import {getMapping} from "../utils/request";
import { renderAsync } from "docx-preview";

const router = useRouter()

interface FormState {
  userName: string;
  password: string;
  remember: boolean;
}

const spinning = ref(false)
const formState = reactive<FormState>({
  userName: '',
  password: '',
  remember: true,
});
const onFinish = (values: any) => {
  console.log('Success:', values);
  console.log('Success:', toRaw(values));
//get请求
  spinning.value = !spinning.value
  getMapping("/sys/sysUser/login", values).then((res) => {
    const {code, result} = res.data
    console.log("code", code)
    if (200 == code) {
      spinning.value = !spinning.value
      router.push({path: '/home'}).catch(() => {
        console.log('登录跳转首页成功')
      })
    } else {
      console.log("登录失败")
    }
  })
};
const onFinishFailed = (errorInfo: any) => {
  console.log('Failed:', errorInfo);
};
const toReg = () => {
  router.push({path: '/userReg'}).catch(() => {
  })
}
</script>
<style>
.login {
  position: absolute;
  top: 50%;
  left: 40%;
  transform: translate(-30%, -30%);
  width: 400px;
  height: 300px;
}
</style>
