<template>
  <a-layout style="min-height: 100vh">
    <a-layout-sider v-model:collapsed="collapsed" collapsible>
      <div class="logo"/>
      <a-menu v-model:selectedKeys="selectedKeys" theme="dark" mode="inline">
        <a-sub-menu key="sub1">
          <template #title>
<span>
<user-outlined/>
<span>系统管理</span>
</span>
          </template>
          <router-link to="/userList">
            <a-menu-item key="3">用户管理</a-menu-item>
          </router-link>
          <router-link to="/userList">
            <a-menu-item key="4">dd</a-menu-item>
          </router-link>
          <a-menu-item key="5">Alex</a-menu-item>
        </a-sub-menu>
        <a-sub-menu key="sub2">
          <template #title>
<span>
<team-outlined/>
<span>Team</span>
</span>
          </template>
          <a-menu-item key="6">Team 1</a-menu-item>
          <a-menu-item key="8">Team 2</a-menu-item>
        </a-sub-menu>
        <a-menu-item key="9">
          <file-outlined/>
          <span>File</span>
        </a-menu-item>
      </a-menu>
    </a-layout-sider>
    <a-layout>
      2. 路由(vue-router)配置
      在src目录下新建 router\index.ts ，内容如下：
      <a-layout-header style="background: #fff; padding: 0">
        <div style="float:right;margin-right: 15px">
          <router-link to="/">
            <a-button type="link">退出</a-button>
          </router-link>
        </div>
      </a-layout-header>
      <a-layout-content style="margin: 0 16px">
        <a-breadcrumb style="margin: 16px 0">
          <a-breadcrumb-item>User</a-breadcrumb-item>
          <a-breadcrumb-item>Bill</a-breadcrumb-item>
        </a-breadcrumb>
        <!--Home.vue 子路由跳转-->
        <router-view></router-view>
      </a-layout-content>
      <a-layout-footer style="text-align: center">
        小袁全栈开发技术分享
      </a-layout-footer>
    </a-layout>
  </a-layout>
</template>
<script setup lang="ts">
import {FileOutlined, TeamOutlined, UserOutlined,} from '@ant-design/icons-vue';
import {ref} from 'vue';

const collapsed = ref<boolean>(false)
const selectedKeys = ref<string[]>(['1'])
</script>
<style>
#components-layout-demo-side .logo {
  height: 32px;
  margin: 16px;
  background: rgba(255, 255, 255, 0.3);
}

.site-layout .site-layout-background {
  background: #fff;
}

[data-theme='dark'] .site-layout .site-layout-background {
  background: #141414;
}
</style>
