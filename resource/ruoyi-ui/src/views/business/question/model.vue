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
        <el-col :span="24">
          <el-form-item size="large">
            <el-button type="primary" @click="submitForm" size="small">提交</el-button>
            <el-button @click="resetForm" size="small">重置</el-button>
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item label="模型推荐" prop="model">
            <el-input v-model="formData.model" type="textarea" placeholder="等待模型生成"
              :autosize="{minRows: 4, minRows: 6}" :style="{width: '100%'}"></el-input>
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
        content:
        `  (搜索案例)
    某电子产品制造企业面临以下问题：在多品种小批量的物料生产中，事先无法知道物料的实际需求量。企业希望运用数学方法，分析已有的历史数据，建立数学模型，帮助企业合理地安排物料生产。
    问题 1 请对附件中的历史数据进行分析，选择 6 种应当重点关注的物料（可从物料需求出现的频数、数量、趋势和销售单价等方面考虑），建立物料需求的周预测模型（即以周为基本时间单位，预测物料的周需求量，见附录(1)），并利用历史数据对预测模型进行评价。
    问题 2 如果按照物料需求量的预测值来安排生产，可能会产生较大的库存，或者出现较多的缺货，给企业带来经济和信誉方面的损失。企业希望从需求量的预测值、需求特征、库存量和缺货量等方面综合考虑，以便更合理地安排生产。
        `,
        upload: '',
        clean: undefined,
        model: '',
      },
      areaContent:'',
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
  created() {
  },
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
      this.formData.model = ""
      // 模型后台提交地址
      const response = await axios.get('http://127.0.0.1:8000/model/'+this.formData.content)
     // this.formData.model = response.data.message


      const textList = `本次检索题目库为2001年至2022年所有题目，检索出最相似文章为：《${response.data.message.name}》\n
        关键词如下：
        ${response.data.message.keywords1},${response.data.message.keywords2},${response.data.message.keywords3},${response.data.message.keywords4}
        `

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
  .el-textarea__inner{
    background-color: rgba(220, 211, 241, 0.1);
    color: black;
    font-size: 0.9rem;
    font-weight: bolder;
    line-height: 1.5;
  }
</style>
