<template>
  <div class="app-container">

    <el-row :gutter="50">
      <el-col :span="3" :xs="24">
        <div style="margin-top: 50px;">
          <label for="upload" class="top">上传文件</label>
          <input
            type="file"
            multiple id="upload"
            style="display: none;"
            ref="fileInput"
            @change="handleFileInputChange" />
          <p
            style="color: rgb(230, 0, 18);
              font-size: x-small;
              font-family: 'Franklin Gothic Medium', 'Arial Narrow', Arial, sans-serif;
              margin-left: 20px;"
              >
            文件格式为xls或xlsx</p>
          <!-- <input type="file" ref="fileInput" @change="handleFileInputChange"> -->

          <button @click="uploadFile" class="btn top">开始分析</button>
          <button @click="handleDownload" v-if="downloadFlag" class="btn top">下载</button>
        </div>
        <div class="tips">
            <b>使用说明：</b>
            <br>
            <small>
              1. 点击"上传文件"，确保文件格式准确后，点击"开始分析"等待分析完成后，下载文件即可。
              <br>
              2. 注意此页面格式所有字段都应为数值类型，如有异常值，空值等情况，请现在数据清洗界面进行数据处理。
            </small>
        </div>
      </el-col>

      <!--算法介绍部分加数据-->
      <el-col :span="20" :xs="24" >
        <h1 class="title">{{formData.title}}</h1>
        <div class="content" v-html="formData.content"></div>
        <div class="content" v-html="formData.format"></div>
      </el-col>


    </el-row>



  </div>
</template>

<script>

import { getToken } from "@/utils/auth";
import "@riophae/vue-treeselect/dist/vue-treeselect.css";
import axios from 'axios'
export default {
  name: "linear",
  data() {
    return {
      formData: {
        title:'线性回归',
        content:`
        <h2>理论</h2>
        <p>在统计学中，线性回归（Linear Regression）是利用称为线性回归方程的最小平方函数对一个或多个自变量和因变量之间关系进行建模的一种回归分析。这种函数是一个或多个称为回归系数的模型参数的线性组合。只有一个自变量的情况称为简单回归，大于一个自变量情况的叫做多元回归。（这反过来又应当由多个相关的因变量预测的多元线性回归区别，而不是一个单一的标量变量。）</p>
        <p>在线性回归中，数据使用线性预测函数来建模，并且未知的模型参数也是通过数据来估计。这些模型被叫做线性模型。最常用的线性回归建模是给定X值的y的条件均值是X的仿射函数。不太一般的情况，线性回归模型可以是一个中位数或一些其他的给定X的条件下y的条件分布的分位数作为X的线性函数表示。像所有形式的回归分析一样，线性回归也把焦点放在给定X值的y的条件概率分布，而不是X和y的联合概率分布（多元分析领域）。</p>
        <p>线性回归是回归分析中第一种经过严格研究并在实际应用中广泛使用的类型。这是因为线性依赖于其未知参数的模型比非线性依赖于其未知参数的模型更容易拟合，而且产生的估计的统计特性也更容易确定。</p>
        <p>线性回归模型经常用最小二乘逼近来拟合，但他们也可能用别的方法来拟合，比如用最小化“拟合缺陷”在一些其他规范里（比如最小绝对误差回归），或者在桥回归中最小化最小二乘损失函数的惩罚.相反,最小二乘逼近可以用来拟合那些非线性的模型.因此，尽管“最小二乘法”和“线性模型”是紧密相连的，但他们是不能划等号的。</p>
        `,
        format: `
        <h2>代码</h2>
        <br>
        <code>
        import numpy as np
        from sklearn.linear_model import LinearRegression

        # 生成训练数据
        X = np.array([[1], [2], [3], [4], [5]])
        y = np.array([2, 4, 6, 8, 10])

        # 实例化线性回归模型
        model = LinearRegression()

        # 训练模型
        model.fit(X, y)

        # 预测
        y_pred = model.predict(np.array([[6]]))

        # 输出预测结果
        print("预测结果：", y_pred[0])
        </code>
        `
      },
      downloadFlag:false,
      file: null,
      // 遮罩层
      loading: true,

      // 用户导入参数
      upload: {
        // 是否显示弹出层（用户导入）
        open: false,
        // 弹出层标题（用户导入）
        title: "",
        // 是否禁用上传
        isUploading: false,
        // 是否更新已经存在的用户数据
        updateSupport: 0,
        // 设置上传的请求头部
        headers: { Authorization: "Bearer " + getToken() },
        // 上传的地址
        url: process.env.VUE_APP_BASE_API + "/system/user/importData"
      },
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        userName: undefined,
        phonenumber: undefined,
        status: undefined,
        deptId: undefined
      },


    };
  },
  watch: {
    // 根据名称筛选部门树

  },
  created() {

  },
  methods: {
    handleFileInputChange(event) {
      this.file = event.target.files[0]
      this.$message.success('上传成功！')
    },
    async uploadFile() {
      const formData = new FormData()
      if (this.file) formData.append('file', this.file)
      else return this.$message.warning("请先上传文件！")
      try {
        const response = await axios.post('http://127.0.0.1:8000/process_file', formData, {
          headers: {
            'Content-Type': 'multipart/form-data'
          }
        })
        if (await response.data.code == 200) this.downloadFlag =true;
      } catch (error) {
        console.error(error)
        this.$message.warning(error)
      }
    },
    handleDownload(){
      // //根据文件路径参数，按斜杠进行分割，取得文件名，这是download函数需要的第三个参数
      var file_name = "data.xlsx";  //测试写法，实际中与应与上传文件名保持一样
      var url = "http://127.0.0.1:8000/download_file?file_name="  // 下载文件地址实际开发写入外部
      const a = document.createElement('a')
      a.setAttribute('download', name)
      a.setAttribute('target', '_blank')
      a.setAttribute('href', url+file_name)
      a.click()
      this.downloadFlag = false;
    },


  },

};
</script>

<style>
  .top {
      height: 30px;
      width: 80px;
      background-color: rgba(230, 0, 18, 0.8);
      font-size: 14px;
      line-height: 30px;
      cursor: pointer;
      display: inline-block;
      text-align: center;
      color: #fff;
      border-radius: 3px;
      margin-left: 20px;
      margin-top: 20px;
  }
  .btn{
    border: none;
  }
  h1 {
    text-align: center;
  }
  h2{
    display: inline-block;
    background-color: rgb(0, 188, 0);
    padding: 6px 20px;
    border-radius: 10px;
    margin-top: 10px;
    font-size: 1rem;
  }
  h3{
    font-weight: bolder;
  }
  .title{
    color: black;
  }
  .content{
    margin-bottom: 20px;
    padding: 2em;
    font-size: 1rem;
    line-height: 1.8;
    box-shadow: 0 0 5px  rgba(0, 0, 0, 0.5);
    border-radius: 10px;
    min-height: 450px;
  }
  .tips{
    margin-top: 50px;
    margin-left: 8px;
    font-family:'Gill Sans', 'Gill Sans MT', Calibri, 'Trebuchet MS', sans-serif ;
    border-radius: 5px;
    padding: 10px;
    background-color: rgba(193, 189, 189, 0.2);
    box-shadow: inset 0 0 5px rgba(60, 59, 59, 0.5);
    width: 8em;
    line-height: 1.5;
  }
</style>

