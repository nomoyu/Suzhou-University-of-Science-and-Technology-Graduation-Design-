<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="论文名称" prop="name">
        <el-input
          v-model="queryParams.name"
          placeholder="请输入论文名称"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="论文年限" prop="year">
        <el-date-picker clearable
          v-model="queryParams.year"
          type="date"
          value-format="yyyy-MM-dd"
          placeholder="请选择论文年限">
        </el-date-picker>
      </el-form-item>
      <el-form-item label="比赛类型" prop="type">
        <el-select v-model="queryParams.type" placeholder="请选择比赛类型" clearable>
          <el-option
            v-for="dict in dict.type.bus_match_type"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="论文分类" prop="level">
        <el-select v-model="queryParams.level" placeholder="请选择论文分类" clearable>
          <el-option
            v-for="dict in dict.type.bus_papers_type"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button
          type="primary"
          plain
          icon="el-icon-plus"
          size="mini"
          @click="handleAdd"
          v-hasPermi="['business:papers:add']"
        >新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="success"
          plain
          icon="el-icon-edit"
          size="mini"
          :disabled="single"
          @click="handleUpdate"
          v-hasPermi="['business:papers:edit']"
        >修改</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="danger"
          plain
          icon="el-icon-delete"
          size="mini"
          :disabled="multiple"
          @click="handleDelete"
          v-hasPermi="['business:papers:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
            type="info"
            plain
            icon="el-icon-upload"
            size="mini"
            @click="handleImport"
            v-hasPermi="['system:user:import']"
        >导入</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['business:papers:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="papersList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="论文id" align="center" prop="id" />
      <el-table-column label="论文名称" align="center" prop="name" />
      <el-table-column label="论文内容" align="center" prop="content" :show-overflow-tooltip="true" />
      <el-table-column label="论文年限" align="center" prop="year" width="180" >
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.year, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="比赛类型" align="center" prop="type">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.bus_match_type" :value="scope.row.type"/>
        </template>
      </el-table-column>
      <el-table-column label="论文分类" align="center" prop="level">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.bus_papers_type" :value="scope.row.level"/>
        </template>
      </el-table-column>
      <el-table-column label="附件" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-view"
            @click="handlePreview(scope.row)"
            v-hasPermi="['business:papers:edit']"
          >预览</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-download"
            @click="handleDownload(scope.row)"
            v-hasPermi="['business:papers:edit']"
          >下载</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['business:papers:remove']"
          >删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination
      v-show="total>0"
      :total="total"
      :page.sync="queryParams.pageNum"
      :limit.sync="queryParams.pageSize"
      @pagination="getList"
    />

    <!-- 添加或修改论文详细信息对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="论文名称" prop="name">
          <el-input v-model="form.name" placeholder="请输入论文名称" />
        </el-form-item>
        <el-form-item label="论文内容">
          <editor v-model="form.content" :min-height="192"/>
        </el-form-item>
        <el-form-item label="论文年限" prop="year">
          <el-date-picker clearable
            v-model="form.year"
            type="date"
            value-format="yyyy-MM-dd"
            placeholder="请选择论文年限">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="比赛类型" prop="type">
          <el-select v-model="form.type" placeholder="请选择比赛类型">
            <el-option
              v-for="dict in dict.type.bus_match_type"
              :key="dict.value"
              :label="dict.label"
              :value="dict.value"
            ></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="论文分类" prop="level">
          <el-select v-model="form.level" placeholder="请选择论文分类">
            <el-option
              v-for="dict in dict.type.bus_papers_type"
              :key="dict.value"
              :label="dict.label"
              :value="dict.value"
            ></el-option>
          </el-select>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>

    <!-- 论文导入对话框 -->
    <el-dialog :title="upload.title" :visible.sync="upload.open" width="400px" append-to-body>
      <el-upload
          ref="uploadRef"
          :limit="1"
          accept=".xlsx, .xls"
          :headers="upload.headers"
          :action="upload.url + '?updateSupport=' + upload.updateSupport"
          :disabled="upload.isUploading"
          :on-progress="handleFileUploadProgress"
          :on-success="handleFileSuccess"
          :auto-upload="false"
          drag
      >
        <el-icon class="el-icon--upload"><upload-filled /></el-icon>
        <div class="el-upload__text">将文件拖到此处，或<em>点击上传</em></div>
        <template #tip>
          <div class="el-upload__tip text-center">
            <div class="el-upload__tip">
              <el-checkbox v-model="upload.updateSupport" />是否更新已经存在的用户数据
            </div>
            <span>仅允许导入xls、xlsx格式文件。</span>
            <el-link type="primary" :underline="false" style="font-size:12px;vertical-align: baseline;" @click="importTemplate">下载模板</el-link>
          </div>
        </template>
      </el-upload>
      <template #footer>
        <div class="dialog-footer">
          <el-button type="primary" @click="submitFileForm">确 定</el-button>
          <el-button @click="upload.open = false">取 消</el-button>
        </div>
      </template>
    </el-dialog>
  </div>
</template>

<script>
import { listPapers, getPapers, delPapers, addPapers, updatePapers } from "@/api/business/papers";
import {getToken} from "@/utils/auth";

export default {
  name: "Papers",
  dicts: ['bus_papers_type', 'bus_match_type'],
  data() {
    return {
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
      // 论文详细信息表格数据
      papersList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        name: null,
        content: null,
        year: null,
        type: null,
        level: null,
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        name: [
          { required: true, message: "论文名称不能为空", trigger: "blur" }
        ],
        content: [
          { required: true, message: "论文内容不能为空", trigger: "blur" }
        ],
        year: [
          { required: true, message: "论文年限不能为空", trigger: "blur" }
        ],
        type: [
          { required: true, message: "比赛类型不能为空", trigger: "change" }
        ],
        level: [
          { required: true, message: "论文分类不能为空", trigger: "change" }
        ]
      },
      upload:{
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
        url: process.env.VUE_APP_BASE_API + "/business/papers/importData"
      }
    };
  },
  created() {
    this.getList();
  },
  methods: {
    /** 查询论文详细信息列表 */
    getList() {
      this.loading = true;
      listPapers(this.queryParams).then(response => {
        this.papersList = response.rows;
        this.total = response.total;
        this.loading = false;
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
        id: null,
        name: null,
        content: null,
        year: null,
        type: null,
        level: null
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
      this.resetForm("queryForm");
      this.handleQuery();
    },
    // 多选框选中数据
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.id)
      this.single = selection.length!==1
      this.multiple = !selection.length
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset();
      this.open = true;
      this.title = "添加论文详细信息";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const id = row.id || this.ids
      getPapers(id).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改论文详细信息";
      });
    },
    /**预览按钮操作*/
    handlePreview(row){
      window.open(row.url);
    },
    handleDownload(row){
      this.download(row.url, {
      }, `papers_${new Date().getTime()}.xlsx`);

    },
     /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.id != null) {
            updatePapers(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addPapers(this.form).then(response => {
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
      const ids = row.id || this.ids;
      this.$modal.confirm('是否确认删除论文详细信息编号为"' + ids + '"的数据项？').then(function() {
        return delPapers(ids);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {});
    },
    /** 导入按钮操作 */
    handleImport() {
      this.upload.title = "用户导入";
      this.upload.open = true;
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('business/papers/export', {
        ...this.queryParams
      }, `papers_${new Date().getTime()}.xlsx`)
    },
    /** 文件上传成功处理 */
    handleFileSuccess(response, file, fileList){
      this.upload.open = false;
      this.upload.isUploading = false;
      this.$refs["uploadRef"].handleRemove(file);
      this.$alert("<div style='overflow: auto;overflow-x: hidden;max-height: 70vh;padding: 10px 20px 0;'>" + response.msg + "</div>", "导入结果", { dangerouslyUseHTMLString: true });
      this.getList();
    },
     /**文件上传中处理 */
    handleFileUploadProgress(event, file, fileList){
      this.upload.isUploading = true;
    },
    /** 提交上传文件 */
    submitFileForm(){
      this.$refs["uploadRef"].submit();
    },
    /** 下载模板操作 */
    importTemplate() {
      this.download("business/papers/importTemplate", {
      }, `papers_template_${new Date().getTime()}.xlsx`);
    },
  },

};
</script>
<style>
  .el-table-column{
    background-color: aqua;
  }
</style>
