<template>
  <div class="app-container">
    <el-row :gutter="15">
      <el-form ref="elForm" :model="formData" :rules="rules" size="mini" label-width="100px"
               label-position="top">
        <el-col :span="24">
          <el-form-item label="请输入问题" prop="question">
            <el-input v-model="formData.question" type="textarea" placeholder="请输入问题"
                      :autosize="{minRows: 12, maxRows: 20}" :style="{width: '100%'}"></el-input>
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item size="large">
            <el-button type="primary" @click="submitForm" size="small">提交</el-button>
            <el-button @click="resetForm" size="small">重置</el-button>
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item label="答案" prop="model">
            <el-input v-model="answer" type="textarea" placeholder="等待答案生成" readonly
                      :autosize="{minRows: 16, maxRows: 20}" :style="{width: '100%'}"></el-input>
          </el-form-item>
        </el-col>

      </el-form>
    </el-row>
  </div>
</template>
<script>
import axios from "axios";
import {white} from "chalk";

export default {
  name:'chatgpt',
  components: {},
  props: [],
  data() {
    return {
      formData: {
        question: '',
        OpenAIKey:'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'
      },
      answer:'',
      areaContent:'',
      rules: {
        question: [{
          required: true,
          message: '请输入问题',
          trigger: 'blur'
        }],
      },
    }
  },
  computed: {},
  watch: {},
  created() {
  },
  mounted() {},
  methods: {
    submitForm() {

      this.$refs['elForm'].validate(valid => {
        if (valid) {
          this.uploadQuestion()

        }
      })
    },
    resetForm() {
      this.formData.question = ""
      //this.$refs['elForm'].resetFields()
    },
    clean(){
      this.formData.question = ""
    },

    async uploadQuestion(){
      this.answer = ""
      // 模型后台提交地址
      let data = new FormData();
      data.append('question', this.formData.question);
      data.append('OpenAIKey', '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');
      //const response = await axios.post('http://8.222.152.157/ask',data = data)
      axios.post('http://8.222.152.157/ask',data = data).then(res =>{
        const textList = JSON.parse(res.data.answer)
        console.log(textList)
        let index = 0
        let timer = setInterval(() => {
          // 将文字插入到textarea中
          this.answer  += textList['choices'][0]['message']['content'][index]
          index++
          // 如果文字列表已经遍历完，则清除定时器
          if (index === textList['choices'][0]['message']['content'].length) {
            clearInterval(timer)
          }
        }, 100)
      })

    },
  }
}

</script>
<style>
</style>
