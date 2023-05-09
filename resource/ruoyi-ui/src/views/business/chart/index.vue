<template>
    <div class="app-container">

      <el-row :gutter="50">
        <!--部门数据-->
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

        </el-col>

        <!--算法介绍部分加数据-->
        <el-col :span="20" :xs="24" >
          <h1>{{formData.title}}</h1>
          <div class="content" style="background: #b5daec;font-size: 20px;line-height: 2"  v-html="formData.content">
          </div>
        </el-col>


      </el-row>



    </div>
  </template>

  <script>

  import { getToken } from "@/utils/auth";
  import "@riophae/vue-treeselect/dist/vue-treeselect.css";
  import axios from 'axios'
  export default {
    name: "User",
    data() {
      return {
        formData: {
          title:'数据清洗',
          content:'在数学建模中，面对数据类题型时，我们都有大致解决问题的步骤。第一要确定问题，要了解问题的背景和目标，明确需要解决的问题，包括了解问题的数据来源和可用数据，对所用到的数据进行收集与爬取。第二要对数据进行预处理，也就是对“脏数据”进行清洗，包括处理缺失值、异常值等。第三需要确定模型与建立模型，即根据问题的特点，选择适当的模型，可以是统计模型、数学模型、机器学习模型等，如主成分分析、多元线性回归、神经网络等机器学习算法，建立模型并进行计算，得到模型输出。第四需要对模型进行验证，看模型是否符合实际情况，是否能够解决问题。第五对模型进行优化，并将总结研究结果得出结论，并将研究报告按规定格式写出及排版。\n' +
            '在数学建模中，确定模型与模型的求解往往会消耗大量的时间，本文也是察觉到这点，致力于打造一个用于对数学建模数据题进行分析并提供有用算法的可视化平台，并提供大量往年优秀论文与往届真题。使得在处理数学建模数据题时可以及其方便的得到数据体的相关算法模型。\n',
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
  </style>
