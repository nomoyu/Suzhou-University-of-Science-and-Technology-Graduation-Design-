<template>
    <div class="app-container">

      <el-row :gutter="50">
        <!--部门数据-->
        <el-col :span="4" :xs="24">
          <div class="head-container">
            <el-input
              v-model="deptName"
              placeholder="请输入算法名称"
              clearable
              size="small"
              prefix-icon="el-icon-search"
              style="margin-bottom: 20px"
            />
          </div>
          <div class="head-container">
            <el-tree
              :data="deptOptions"
              :props="defaultProps"
              :expand-on-click-node="false"
              :filter-node-method="filterNode"
              ref="tree"
              node-key="id"
              default-expand-all
              highlight-current
              @node-click="handleNodeClick"
            />
          </div>

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

        <!--用户数据-->
        <el-col :span="20" :xs="24">

        </el-col>

      </el-row>


    </div>
  </template>

  <script>
  import { listUser, getUser, delUser, addUser, updateUser, resetUserPwd, changeUserStatus, deptTreeSelect } from "@/api/system/user";
  import { getToken } from "@/utils/auth";
  import Treeselect from "@riophae/vue-treeselect";
  import "@riophae/vue-treeselect/dist/vue-treeselect.css";
  import axios from 'axios'
  export default {
    name: "User",
    dicts: ['sys_normal_disable', 'sys_user_sex'],
    components: { Treeselect},
    data() {
      return {
        downloadFlag:false,
        file: null,
        // 遮罩层
        loading: true,
        // 选中数组
        ids: [],
        // 非单个禁用
        single: true,
        // 非多个禁用
        multiple: true,
        // 显示搜索条件
        showSearch: true,
        // 总条数
        total: 0,
        // 用户表格数据
        userList: null,
        // 弹出层标题
        title: "",
        // 部门树选项
        deptOptions: undefined,
        // 是否显示弹出层
        open: false,
        // 部门名称
        deptName: undefined,
        // 默认密码
        initPassword: undefined,
        // 日期范围
        dateRange: [],
        // 岗位选项
        postOptions: [],
        // 角色选项
        roleOptions: [],
        // 表单参数
        form: {},
        defaultProps: {
          children: "children",
          label: "label"
        },
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
        // 列信息
        columns: [
          { key: 0, label: `用户编号`, visible: true },
          { key: 1, label: `用户名称`, visible: true },
          { key: 2, label: `用户昵称`, visible: true },
          { key: 3, label: `部门`, visible: true },
          { key: 4, label: `手机号码`, visible: true },
          { key: 5, label: `状态`, visible: true },
          { key: 6, label: `创建时间`, visible: true }
        ],
        // 表单校验
        rules: {
          userName: [
            { required: true, message: "用户名称不能为空", trigger: "blur" },
            { min: 2, max: 20, message: '用户名称长度必须介于 2 和 20 之间', trigger: 'blur' }
          ],
          nickName: [
            { required: true, message: "用户昵称不能为空", trigger: "blur" }
          ],
          password: [
            { required: true, message: "用户密码不能为空", trigger: "blur" },
            { min: 5, max: 20, message: '用户密码长度必须介于 5 和 20 之间', trigger: 'blur' }
          ],
          email: [
            {
              type: "email",
              message: "请输入正确的邮箱地址",
              trigger: ["blur", "change"]
            }
          ],
          phonenumber: [
            {
              pattern: /^1[3|4|5|6|7|8|9][0-9]\d{8}$/,
              message: "请输入正确的手机号码",
              trigger: "blur"
            }
          ]
        }
      };
    },
    watch: {
      // 根据名称筛选部门树
      deptName(val) {
        this.$refs.tree.filter(val);
      }
    },
    created() {
      this.getList();
      this.getDeptTree();
      this.getConfigKey("sys.user.initPassword").then(response => {
        this.initPassword = response.msg;
      });
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
          const response = await axios.post('http://192.168.12.122:8000/process_file', formData, {
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

      /** 查询用户列表 */
      getList() {
        this.loading = true;
        listUser(this.addDateRange(this.queryParams, this.dateRange)).then(response => {
            this.userList = response.rows;
            this.total = response.total;
            this.loading = false;
          }
        );
      },
      /** 查询部门下拉树结构 */
      getDeptTree() {
        deptTreeSelect().then(response => {
          this.deptOptions = response.data;
        });
      },
      // 筛选节点
      filterNode(value, data) {
        if (!value) return true;
        return data.label.indexOf(value) !== -1;
      },
      // 节点单击事件
      handleNodeClick(data) {
        this.queryParams.deptId = data.id;
        this.handleQuery();
      },
      // 用户状态修改
      handleStatusChange(row) {
        let text = row.status === "0" ? "启用" : "停用";
        this.$modal.confirm('确认要"' + text + '""' + row.userName + '"用户吗？').then(function() {
          return changeUserStatus(row.userId, row.status);
        }).then(() => {
          this.$modal.msgSuccess(text + "成功");
        }).catch(function() {
          row.status = row.status === "0" ? "1" : "0";
        });
      },
      // 取消按钮
      cancel() {
        this.open = false;
        this.reset();
      },
      // 表单重置
      reset() {
        this.form = {
          userId: undefined,
          deptId: undefined,
          userName: undefined,
          nickName: undefined,
          password: undefined,
          phonenumber: undefined,
          email: undefined,
          sex: undefined,
          status: "0",
          remark: undefined,
          postIds: [],
          roleIds: []
        };
        this.resetForm("form");
      },
      /** 搜索按钮操作 */
      handleQuery() {
        this.queryParams.pageNum = 1;
        this.getList();
      },
      /** 重置按钮操作 */
      resetQuery() {
        this.dateRange = [];
        this.resetForm("queryForm");
        this.queryParams.deptId = undefined;
        this.$refs.tree.setCurrentKey(null);
        this.handleQuery();
      },
      // 多选框选中数据
      handleSelectionChange(selection) {
        this.ids = selection.map(item => item.userId);
        this.single = selection.length != 1;
        this.multiple = !selection.length;
      },
      // 更多操作触发
      handleCommand(command, row) {
        switch (command) {
          case "handleResetPwd":
            this.handleResetPwd(row);
            break;
          case "handleAuthRole":
            this.handleAuthRole(row);
            break;
          default:
            break;
        }
      },
      /** 新增按钮操作 */
      handleAdd() {
        this.reset();
        getUser().then(response => {
          this.postOptions = response.posts;
          this.roleOptions = response.roles;
          this.open = true;
          this.title = "添加用户";
          this.form.password = this.initPassword;
        });
      },
      /** 修改按钮操作 */
      handleUpdate(row) {
        this.reset();
        const userId = row.userId || this.ids;
        getUser(userId).then(response => {
          this.form = response.data;
          this.postOptions = response.posts;
          this.roleOptions = response.roles;
          this.$set(this.form, "postIds", response.postIds);
          this.$set(this.form, "roleIds", response.roleIds);
          this.open = true;
          this.title = "修改用户";
          this.form.password = "";
        });
      },
      /** 重置密码按钮操作 */
      handleResetPwd(row) {
        this.$prompt('请输入"' + row.userName + '"的新密码', "提示", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          closeOnClickModal: false,
          inputPattern: /^.{5,20}$/,
          inputErrorMessage: "用户密码长度必须介于 5 和 20 之间"
        }).then(({ value }) => {
            resetUserPwd(row.userId, value).then(response => {
              this.$modal.msgSuccess("修改成功，新密码是：" + value);
            });
          }).catch(() => {});
      },
      /** 分配角色操作 */
      handleAuthRole: function(row) {
        const userId = row.userId;
        this.$router.push("/system/user-auth/role/" + userId);
      },
      /** 提交按钮 */
      submitForm: function() {
        this.$refs["form"].validate(valid => {
          if (valid) {
            if (this.form.userId != undefined) {
              updateUser(this.form).then(response => {
                this.$modal.msgSuccess("修改成功");
                this.open = false;
                this.getList();
              });
            } else {
              addUser(this.form).then(response => {
                this.$modal.msgSuccess("新增成功");
                this.open = false;
                this.getList();
              });
            }
          }
        });
      },
      /** 删除按钮操作 */
      handleDelete(row) {
        const userIds = row.userId || this.ids;
        this.$modal.confirm('是否确认删除用户编号为"' + userIds + '"的数据项？').then(function() {
          return delUser(userIds);
        }).then(() => {
          this.getList();
          this.$modal.msgSuccess("删除成功");
        }).catch(() => {});
      },
      /** 导出按钮操作 */
      handleExport() {
        this.download('system/user/export', {
          ...this.queryParams
        }, `user_${new Date().getTime()}.xlsx`)
      },
      /** 导入按钮操作 */
      handleImport() {
        this.upload.title = "用户导入";
        this.upload.open = true;
      },
      /** 下载模板操作 */
      importTemplate() {
        this.download('system/user/importTemplate', {
        }, `user_template_${new Date().getTime()}.xlsx`)
      },
      // 文件上传中处理
      handleFileUploadProgress(event, file, fileList) {
        this.upload.isUploading = true;
      },
      // 文件上传成功处理
      handleFileSuccess(response, file, fileList) {
        this.upload.open = false;
        this.upload.isUploading = false;
        this.$refs.upload.clearFiles();
        this.$alert("<div style='overflow: auto;overflow-x: hidden;max-height: 70vh;padding: 10px 20px 0;'>" + response.msg + "</div>", "导入结果", { dangerouslyUseHTMLString: true });
        this.getList();
      },
      // 提交上传文件
      submitFileForm() {
        this.$refs.upload.submit();
      }
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
  </style>