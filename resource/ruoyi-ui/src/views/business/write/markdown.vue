<template>
  <div class="app-container">
    <el-form :inline="true" :model="formInline" class="demo-form-inline">
      <el-form-item label="版本信息">
        <el-select v-model="index" placeholder="选择写作记录">
          <el-option label="记录1" value="1"></el-option>
          <el-option label="记录2" value="2"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="onSubmit">查询</el-button>
      </el-form-item>
    </el-form>
    <mavon-editor :value="value" @change="valueChange" @save="save(form)">
      <!-- 左工具栏前加入自定义按钮 -->
      <template slot="left-toolbar-before">
<!--        <button-->
<!--          type="button"-->
<!--          @click="downloadZIP"-->
<!--          class="op-icon fa fa-mavon-align-left"-->
<!--          aria-hidden="true"-->
<!--          title="Tex"-->
<!--        ></button>-->
      </template>
    </mavon-editor>

<!--    <div v-html="htmlText" class="markdown-body" />-->
  </div>


</template>

<script>
import {add,list} from "@/api/business/write"

export default {
  name: "MarkDown",
  data() {
    return {
      dataList:[],
      form:{
        teamNumber:'001',
        version:0,
        note:"",
      },
      index:'最新记录',
      // 初始值
      value: "",
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
  created() {
    this.getList()
  },
  methods: {
    getList(){
      list(this.form).then(res =>{
          this.dataList = res.data
            // 后端已经排序，始终取值第一条
          this.value = res.data[0].note
          // res.data.forEach(item=>{
          //   this.value = item.note;
          // })
      })
    },
    // 版本查询记录
    onSubmit() {
      console.log(this.index)
      this.value = this.dataList[this.index].note
    },
    save(form){
      add(form).then(res=>{
        console.log(res.data)
      });
      this.$message.success("save!")
    },
    valueChange(value, render) {
      this.form.note = value;
      this.htmlText =render

      let len = this.htmlText.split('\n').length

    },


  },
}
</script>

<style scoped>

</style>
