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
        <h1>{{formData.title}}</h1>
        <div class="content"  v-html="formData.content"></div>
        <div class="content"  v-html="formData.format"></div>
      </el-col>


    </el-row>



  </div>
</template>

<script>

import { getToken } from "@/utils/auth";
import "@riophae/vue-treeselect/dist/vue-treeselect.css";
import axios from 'axios'
export default {
  name: "pca",
  data() {
    return {
      formData: {
        title:'数据降维',
        format:`
        <h2>代码</h2>
        <br>

        <code>import pandas as pd
        import numpy as np
        from sklearn.decomposition import PCA
        from openpyxl import Workbook
        from sklearn.preprocessing import StandardScaler

        # 读取数据
        df = pd.read_csv('data.csv', header=None, dtype=None,encoding='gbk')

        # 将字符串类型的数据转换为数值类型
        df = df.apply(pd.to_numeric, errors='coerce')

        # 删除包含NaN值的行
        df = df.dropna()
        # 获取数值类型的列，并进行标准化
        num_cols = df.select_dtypes(include='number').columns.tolist()
        # # 取出数值类型
        # print(num_cols)
        # print(df[1])
        # scaler = StandardScaler()
        # print(df[num_cols])
        # data = scaler.fit_transform(df[num_cols])
        # # 标准化数据
        data = df[num_cols].values
        mean = np.mean(data, axis=0)
        std = np.std(data, axis=0)
        data = (data - mean) / std

        # 使用PCA降维
        pca = PCA(n_components='mle')
        result = pca.fit_transform(data)

        # 将结果转换为DataFrame
        result_df = pd.DataFrame(result)

        non_num_cols = df.select_dtypes(exclude='number').columns.tolist()
        # 合并数值类型的列和非数值类型的列，并保存为新的 Excel 文件

        new_df = pd.concat([result_df, df[non_num_cols]], axis=1)
        new_df.to_excel('new_data.xlsx', index=False)
        </code>

        `,
        content:`
          <h2>理论</h2>
          <p>数据降维是指用过一定的方法将高维数据映射到底为空间中，从而减少数据维度的过程。数据降维可以减少数据的存储空间和计算成本，同时可以去除噪声和冗余信息，提高数据的表达能力和可解释性。</p>
          <p>数据降维的常见方法有四种：主成分分析（PCA）、线性判别分析（LDA）、t-SNE和自编码器（Autoencoder）。需要注意的是，不同的数据降维方法适用于不同的数据类型和应用场景，选择合适的方法可以提高数据处理的效率和精度。同时，数据降维也需要考虑数据质量、降维的目的和影响等方面，以确保降维后的数据仍然具有足够的信息量和可靠性。</p>
          <p>其中本文主要使用到主成分分析，也着重介绍主成分分析法。主成分分析（Principal Component Analysis，PCA）是一种线性数据降维方法，它通过将原始数据映射到一个新的坐标系中，从而找到数据中的主要特征或主成分。在新的坐标系中，第一个主成分是数据方差最大的方向，第二个主成分是与第一个主成分不相关的方向，依次类推。</p>
          <p>主成分分析可以用于数据预处理、特征提取、降维等领域，它可以减少数据维度、去除噪声和冗余信息，同时保留原始数据的主要特征，提高数据的表达能力和可解释性。</p>
          <p>假设有 n 个样本，每个样本有 p 个特征，表示为 x_1,x_2,...,x_p，可以将这些样本组成一个 n×p 的矩阵 X，其中每一行代表一个样本，每一列代表一个特征。</p>
          <p>主成分分析的公式如下：</p>
          <p>对原始数据进行中心化处理，即将每个特征的均值都减去相应的平均值，得到新的数据矩阵 X':</p>
          <p>X' = X -  ̅X </P>
          <p>其中 X ̅ 表示 X 的每一列的平均值。</p>
          <p>计算数据的协方差矩阵 S：</p>
          <p>S =  1/(n-1) X^'T X'</p>
          <p>对协方差矩阵进行特征值分解，得到特征值 λ_1,λ_2,...,λ_p 和对应的特征向量 V_1,V_2,...,V_p，其中特征向量 V_i 表示协方差矩阵 S 的第 i 个特征向量。</p>
          <p>将特征向量按照对应的特征值大小从大到小排序，选取前 k 个特征向量组成新的矩阵 V_k，其中 k 表示降维后的维度。如果不指定 k，则可以根据特征值的大小确定保留多少个主成分。</p>
          <p>将原始数据 X 通过线性变换 Z = XV_k 映射到新的低维空间中，其中 V_k 是由前 k 个特征向量组成的矩阵，Z 是降维后的数据矩阵。</p>
          <p>PCA 的主要优点是简单易懂、计算效率高、可以用于数据预处理和特征提取等领域。需要注意的是，PCA 的应用需要考虑数据的线性性、数据质量和维度选择等因素，以确保得到合理的降维结果。</p>
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
