<template>
  <div>
    <mavon-editor :value="value" @change="valueChange" >
      <!-- 左工具栏前加入自定义按钮 -->
      <template slot="left-toolbar-before">
        <button
          type="button"
          @click="download()"
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
import request from '@/utils/request'

export default {
  name: "MarkDown",
  data() {
    return {
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
    valueChange(value, render) {
      //value为输入的内容，render是markdown渲染之后的html代码
      this.htmlText = render+"hello"
      console.log(value, render+"hello");
    },

    download(){
      return request({
        url: 'http://127.0.0.1:3220/dep',
        method: 'post',
        data: this.requestParam,
        withCredentials:false,
      })
    }
  },
}
</script>

<style scoped>

</style>
