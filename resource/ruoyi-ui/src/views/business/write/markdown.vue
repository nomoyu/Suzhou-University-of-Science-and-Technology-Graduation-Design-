<template>
  <div class="app-container">
    <el-form :inline="true" :model="formInline" class="demo-form-inline">
      <el-form-item label="审批人">
        <el-input v-model="formInline.user" placeholder="审批人"></el-input>
      </el-form-item>
      <el-form-item label="活动区域">
        <el-select v-model="formInline.region" placeholder="活动区域">
          <el-option label="区域一" value="shanghai"></el-option>
          <el-option label="区域二" value="beijing"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="onSubmit">查询</el-button>
      </el-form-item>
    </el-form>
    <mavon-editor :value="value" @change="valueChange" >
      <!-- 左工具栏前加入自定义按钮 -->
      <template slot="left-toolbar-before">
        <button
          type="button"
          @click="downloadZIP"
          class="op-icon fa fa-mavon-align-left"
          aria-hidden="true"
          title="Tex"
        ></button>
      </template>
    </mavon-editor>

    <div v-html="htmlText" class="markdown-body" />
  </div>


</template>

<script>
import { PRIORITY } from 'echarts/lib/echarts'
// import {download} from '@/api/test'
// import {resolveBlob} from '@/utils/zipdownload'

export default {
  name: "MarkDown",
  data() {
    return {
      formInline: {
        user: '',
        region: ''
      },
      value: "# ddd",
      htmlText:"",
      requestParam:{
        options: {
          packages: ["amsmath", "amssymb"],
          compiler: "pdflatex",
          outputDirectory: "output"
        },
        data: "# This is a markdown document\n\n ### Here is some math: $E=mc^2$"
      },

    };
  },
  methods: {
    onSubmit() {
      console.log('submit!');
    },
    valueChange(value, render) {
      this.htmlText =render

      let len = this.htmlText.split('\n').length

      console.log(len) // 4
      //value为输入的内容，render是markdown渲染之后的html代码
      console.log(value, render);
    },
    downloadZIP(){
      // download(this.requestParam).then(res =>{
      //   this.htmlText =res.data
        // resolveBlob(res,'application/zip')
      // });
    }


  },
}
</script>

<style scoped>

</style>
