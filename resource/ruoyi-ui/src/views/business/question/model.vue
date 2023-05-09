<template>
  <div class="app-container">
    <el-row :gutter="15">
      <el-form ref="elForm" :model="formData" :rules="rules" size="mini" label-width="100px"
        label-position="top">
        <el-col :span="24">
          <el-form-item label="请输入题目内容" prop="content">
            <el-input v-model="formData.content" type="textarea" placeholder="输入请请输入论文内容"
              :autosize="{minRows: 12, maxRows: 1}" :style="{width: '100%'}"></el-input>
          </el-form-item>
        </el-col>
<!--        <el-col :span="8">-->
<!--          <el-form-item label="" prop="upload">-->
<!--            <el-button type="primary" icon="el-icon-upload" size="medium"> 上传 </el-button>-->
<!--          </el-form-item>-->
<!--        </el-col>-->
<!--        <el-col :span="8">-->
<!--          <el-form-item label="" prop="clean">-->
<!--            <el-button type="warning" icon="el-icon-clean" size="medium" @click="clean()"> 清空内容 </el-button>-->
<!--          </el-form-item>-->
<!--        </el-col>-->
        <el-col :span="24">
          <el-form-item size="large">
            <el-button type="primary" @click="submitForm" size="small">提交</el-button>
            <el-button @click="resetForm" size="small">重置</el-button>
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item label="模型推荐" prop="model">
            <el-input v-model="formData.model" type="textarea" placeholder="等待模型生成" readonly
              :autosize="{minRows: 4, maxRows: 4}" :style="{width: '100%'}"></el-input>
          </el-form-item>
        </el-col>

      </el-form>
    </el-row>
  </div>
</template>
<script>
import axios from "axios";

export default {
  components: {},
  props: [],
  data() {
    return {
      formData: {
        content: '',
        upload: '',
        clean: undefined,
        model: undefined,
      },
      rules: {
        content: [{
          required: true,
          message: '输入请请输入论文内容',
          trigger: 'blur'
        }],

      },
    }
  },
  computed: {},
  watch: {},
  created() {},
  mounted() {},
  methods: {
    submitForm() {
      this.$refs['elForm'].validate(valid => {
        if (valid) {
          this.uploadQuestion()
          //this.formData.model = "功能开发中，请等待！"
        }
      })
    },
    resetForm() {
      this.formData.content = ""
      //this.$refs['elForm'].resetFields()
    },
    clean(){
      this.formData.content = ""
    },
    async uploadQuestion(){
      const response = await axios.get('http://127.0.0.1:8000/model/'+this.formData.content)
      this.formData.model = response.data.message

      const textList = '这是第一段文字。这是第二段文字。这是第三段文字。这是第四段文字。'

      let index = 0
      let timer = setInterval(() => {
        // 将文字插入到textarea中
        this.formData.model  += textList[index]
        index++
        // 如果文字列表已经遍历完，则清除定时器
        if (index === textList.length) {
          clearInterval(timer)
        }
      }, 100)
    },
  }
}

</script>
<style>
</style>
