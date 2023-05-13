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
  name: "logistics",
  data() {
    return {
      formData: {
        title:'逻辑回归',
        content:`
        <h2>理论</h2>

        <h3>概念</h3>
        <p>logistic回归是一种广义线性回归（generalized linear model），因此与多重线性回归分析有很多相同之处。它们的模型形式基本上相同，都具有 w‘x+b，其中w和b是待求参数，其区别在于他们的因变量不同，多重线性回归直接将w‘x+b作为因变量，即y =w‘x+b，而logistic回归则通过函数L将w‘x+b对应一个隐状态p，p =L(w‘x+b),然后根据p 与1-p的大小决定因变量的值。如果L是logistic函数，就是logistic回归，如果L是多项式函数就是多项式回归。</p>

        <p>logistic回归的因变量可以是二分类的，也可以是多分类的，但是二分类的更为常用，也更加容易解释，多类可以使用softmax方法进行处理。实际中最为常用的就是二分类的logistic回归。</p>

        <h3>Logistic回归模型的适用条件</h3>
        <p>1 因变量为二分类的分类变量或某事件的发生率，并且是数值型变量。但是需要注意，重复计数现象指标不适用于Logistic回归。<br>
        2 残差和因变量都要服从二项分布。二项分布对应的是分类变量，所以不是正态分布，进而不是用最小二乘法，而是最大似然法来解决方程估计和检验问题。<br>
        3 自变量和Logistic概率是线性关系。<br>
        4 各观测对象间相互独立。</p>

        <p>原理：如果直接将线性回归的模型扣到Logistic回归中，会造成方程二边取值区间不同和普遍的非直线关系。因为Logistic中因变量为二分类变量，某个概率作为方程的因变量估计值取值范围为0-1，但是，方程右边取值范围是无穷大或者无穷小。所以，才引入Logistic回归。</p>
        <p>Logistic回归实质：发生概率除以没有发生概率再取对数。就是这个不太繁琐的变换改变了取值区间的矛盾和因变量自变量间的曲线关系。究其原因，是发生和未发生的概率成为了比值 ，这个比值就是一个缓冲，将取值范围扩大，再进行对数变换，整个因变量改变。不仅如此，这种变换往往使得因变量和自变量之间呈线性关系，这是根据大量实践而总结。所以，Logistic回归从根本上解决因变量要不是连续变量怎么办的问题。还有，Logistic应用广泛的原因是许多现实问题跟它的模型吻合。例如一件事情是否发生跟其他数值型自变量的关系。 </p>

        <p>注意：如果自变量为字符型，就需要进行重新编码。一般如果自变量有三个水平就非常难对付，所以，如果自变量有更多水平就太复杂。这里只讨论自变量只有三个水平。非常麻烦，需要再设二个新变量。共有三个变量，第一个变量编码1为高水平，其他水平为0。第二个变量编码1为中间水平，0为其他水平。第三个变量，所有水平都为0。实在是麻烦，而且不容易理解。最好不要这样做，也就是，最好自变量都为连续变量。 </p>

        <p>spss操作：进入Logistic回归主对话框，通用操作不赘述。</p>
        <p>发现没有自变量这个说法，只有协变量，其实协变量就是自变量。旁边的块就是可以设置很多模型。</p>
        <p>“方法”栏：这个根据词语理解不容易明白，需要说明。</p>
        <p>共有7种方法。但是都是有规律可寻的。<br>
        “向前”和“向后”：向前是事先用一步一步的方法筛选自变量，也就是先设立门槛。称作“前”。而向后，是先把所有的自变量都进来，然后再筛选自变量。也就是先不设置门槛，等进来了再一个一个淘汰。<br>
        “LR”和“Wald”,LR指的是极大偏似然估计的似然比统计量概率值，有一点长。但是其中重要的词语就是似然。<br>
        Wald指Wald统计量概率值。<br>
        “条件”指条件参数似然比统计量概率值。<br>
        “进入”就是所有自变量都进来，不进行任何筛选。<br>
        将所有的关键词组合在一起就是7种方法，分别是“进入”“向前LR”“向前Wald”"向后LR"“向后Wald”“向后条件”“向前条件”。<br>
        下一步：一旦选定协变量，也就是自变量，“分类”按钮就会被激活。其中，当选择完分类协变量以后，“更改对比”选项组就会被激活。一共有7种更改对比的方法。<br>
        “指示符”和“偏差”，都是选择最后一个和第一个个案作为对比标准，也就是这二种方法能够激活“参考类别”栏。“指示符”是默认选项。“偏差”表示分类变量每个水平和总平均值进行对比，总平均值的上下界就是"最后一个"和"第一个"在“参考类别”的设置。<br>
        “简单”也能激活“参考类别”设置。表示对分类变量各个水平和第一个水平或者最后一个水平的均值进行比较。<br>
        “差值”对分类变量各个水平都和前面的水平进行作差比较。第一个水平除外，因为不能作差。<br>
        “Helmert”跟“差值”正好相反。是每一个水平和后面水平进行作差比较。最后一个水平除外。仍然是因为不能做差。<br>
        “重复”表示对分类变量各个水平进行重复对比。<br>
        “多项式”对每一个水平按分类变量顺序进行趋势分析，常用的趋势分析方法有线性，二次式。</p>

        <h3>主要用途<h3>
        <h4>寻找危险因素</h4>
        正如上面所说的寻找某一疾病的危险因素等。
        <h4>预测</h4>
        如果已经建立了logistic回归模型，则可以根据模型，预测在不同的自变量情况下，发生某病或某种情况的概率有多大。
        <h4>判别</h4>
        实际上跟预测有些类似，也是根据logistic模型，判断某人属于某病或属于某种情况的概率有多大，也就是看一下这个人有多大的可能性是属于某病。
        <p>这是logistic回归最常用的三个用途，实际中的logistic回归用途是极为广泛的，logistic回归几乎已经成了流行病学和医学中最常用的分析方法，因为它与多重线性回归相比有很多的优势，以后会对该方法进行详细的阐述。实际上有很多其他分类方法，只不过Logistic回归是最成功也是应用最广的。</p>
        `,
        format: `
        <h2>代码</h2>
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
    /* text-indent: 2em; */
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
    width: 130px;
    line-height: 1.5;
  }
</style>
