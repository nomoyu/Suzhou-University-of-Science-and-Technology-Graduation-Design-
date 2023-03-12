<template>
  <div class="app-container">

    <el-form :model="queryParams" ref="queryForm" :inline="true" v-show="showSearch" >
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
          value-format="YYYY-MM-DD"
          placeholder="请选择论文年限">
        </el-date-picker>
      </el-form-item>
      <el-form-item label="比赛类型" prop="type">
        <el-select v-model="queryParams.type" placeholder="请选择比赛类型" clearable>
          <el-option
            v-for="dict in bus_match_type"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="论文分类" prop="level">
        <el-select v-model="queryParams.level" placeholder="请选择论文分类" clearable>
          <el-option
            v-for="dict in bus_papers_type"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="Search"  @click="handleQuery">搜索</el-button>
        <el-button icon="Refresh"  @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button
          type="primary"
          plain
          icon="Plus"
          @click="handleAdd"
          v-hasPermi="['business:papers:add']"
        >新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="success"
          plain
          icon="Edit"
          :disabled="single"
          @click="handleUpdate"
          v-hasPermi="['business:papers:edit']"
        >修改</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="danger"
          plain
          icon="Delete"
          :disabled="multiple"
          @click="handleDelete"
          v-hasPermi="['business:papers:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
            type="info"
            plain
            icon="Upload"
            @click="handleImport"
            v-hasPermi="['system:user:import']"
        >导入</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="Download"
          @click="handleExport"
          v-hasPermi="['business:papers:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar v-model:showSearch="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="papersList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="论文名称" align="center" prop="name" />
      <el-table-column label="论文内容" align="center" prop="content" :show-overflow-tooltip="true" />
      <el-table-column label="论文年限" align="center" prop="year">
        <template #default="scope">
          <span>{{ parseTime(scope.row.year, '{y}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="比赛类型" align="center" prop="type" >
        <template #default="scope">
          <dict-tag :options="bus_match_type" :value="scope.row.type"/>
        </template>
      </el-table-column>
      <el-table-column label="论文分类" align="center" prop="level" >
        <template #default="scope">
          <dict-tag :options="bus_papers_type" :value="scope.row.level"/>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template #default="scope">
          <el-button link type="primary" icon="Edit" @click="handleUpdate(scope.row)" v-hasPermi="['business:papers:edit']">修改</el-button>
          <el-button link type="primary" icon="Delete" @click="handleDelete(scope.row)" v-hasPermi="['business:papers:remove']" >删除</el-button>
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
    <el-dialog :title="title" v-model="open" width="780px" append-to-body>
      <el-form ref="papersRef" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="论文名称" prop="name">
          <el-input v-model="form.name" placeholder="请输入论文名称" />
        </el-form-item>
        <el-form-item label="论文内容">
          <el-input
              :rows="6"
              type="textarea"
              placeholder="请输入内容"
              v-model="form.content"
          />
        </el-form-item>
        <el-form-item label="论文年限" prop="year">
          <el-date-picker clearable
            v-model="form.year"
            type="date"
            value-format="YYYY-MM-DD"
            placeholder="请选择论文年限">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="比赛类型" prop="type">
          <el-select v-model="form.type" placeholder="请选择比赛类型">
            <el-option
              v-for="dict in bus_match_type"
              :key="dict.value"
              :label="dict.label"
              :value="dict.value"
            ></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="论文分类" prop="level">
          <el-select v-model="form.level" placeholder="请选择论文分类">
            <el-option
              v-for="dict in bus_papers_type"
              :key="dict.value"
              :label="dict.label"
              :value="dict.value"
            ></el-option>
          </el-select>
        </el-form-item>
      </el-form>
      <template #footer>
        <div class="dialog-footer">
          <el-button type="primary" @click="submitForm">确 定</el-button>
          <el-button @click="cancel">取 消</el-button>
        </div>
      </template>
    </el-dialog>

    <!-- 论文导入对话框 -->
    <el-dialog :title="upload.title" v-model="upload.open" width="400px" append-to-body>
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

<script setup name="Papers">
import { listPapers, getPapers, delPapers, addPapers, updatePapers } from "@/api/business/papers";
import {getToken} from "@/utils/auth";

const { proxy } = getCurrentInstance();
const { bus_papers_type,bus_match_type } = proxy.useDict('bus_papers_type', 'bus_match_type');

const loading = ref(true);
const ids = ref([]);
const single = ref(true);
const multiple = ref(true);
const showSearch = ref(true);
const total = ref(0);
const papersList = ref([]);
const title = ref("");
const open = ref(false);
const dateRange = ref([]);
/*** 用户导入参数 */
const upload = reactive({
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
  url: import.meta.env.VITE_APP_BASE_API + "/business/papers/importData"
});

const data = reactive({
  form: {},
  queryParams: {
    id:undefined,
    pageNum: 1,
    pageSize: 10,
    year: undefined,
    content: undefined,
    name: undefined,
    type:undefined,
    level:undefined
  },
  rules: {
    name: [
      {required: true, message: "论文名称不能为空", trigger: "blur"}
    ],
    content: [
      {required: true, message: "论文内容不能为空", trigger: "blur"}
    ],
    year: [
      {required: true, message: "论文年限不能为空", trigger: "blur"}
    ],
    type: [
      {required: true, message: "比赛类型不能为空", trigger: "change"}
    ],
    level: [
      {required: true, message: "论文分类不能为空", trigger: "change"}
    ]
  },
});

const { queryParams, form, rules } = toRefs(data);

function getList(){
  loading.value = true;
  listPapers(queryParams.value).then(response => {
    papersList.value = response.rows;
    total.value = response.total;
    loading.value = false;
  });
}
function cancel() {
  open.value = false;
  reset();
}

/** 表单重置 **/
function reset() {
  form.value = {
    id: undefined,
    name: undefined,
    content: undefined,
    year: undefined,
    type: undefined,
    level: undefined
  };
  proxy.resetForm("papersRef");
}

/** 搜索按钮操作 */
function handleQuery() {
  queryParams.value.pageNum = 1;
  getList();
}

/** 重置按钮操作 */
function resetQuery() {
  dateRange.value = [];
  proxy.resetForm("queryForm");
  handleQuery();
}
// 多选框选中数据
function handleSelectionChange(selection) {
  this.ids = selection.map(item => item.id)
  this.single = selection.length!==1
  this.multiple = !selection.length
}

/** 新增按钮操作 */
function handleAdd() {
  reset();
  open.value = true;
  title.value = "添加论文详细信息";
}

/** 修改按钮操作 */
function handleUpdate(row) {
  reset();
  const id = row.id || ids.value;
  getPapers(id).then(response => {
    form.value = response.data;
    open.value = true;
    title.value = "修改论文详细信息";
  });
}
/** 提交按钮 */
function submitForm() {
  proxy.$refs["papersRef"].validate(valid => {
    if (valid) {
      if (form.value.id != undefined) {
        updatePapers(form.value).then(response => {
          proxy.$modal.msgSuccess("修改成功");
          open.value = false;
          getList();
        });
      } else {
        addPapers(form.value).then(response => {
          proxy.$modal.msgSuccess("新增成功");
          open.value = false;
          getList();
        });
      }
    }
  });
}

/** 删除按钮操作 */
function handleDelete(row) {
  const ids = row.id || ids.value;
  proxy.$modal.confirm('是否确认删除论文详细信息编号为"' + ids + '"的数据项？').then(function() {
    return delPapers(ids);
  }).then(() => {
    getList();
    proxy.$modal.msgSuccess("删除成功");
  }).catch(() => {});
}

/** 导入按钮操作 */
function handleImport() {
  upload.title = "用户导入";
  upload.open = true;
};
/** 下载模板操作 */
function importTemplate() {
  proxy.download("business/papers/importTemplate", {
  }, `papers_template_${new Date().getTime()}.xlsx`);
};
/**文件上传中处理 */
const handleFileUploadProgress = (event, file, fileList) => {
  upload.isUploading = true;
};
/** 文件上传成功处理 */
const handleFileSuccess = (response, file, fileList) => {
  upload.open = false;
  upload.isUploading = false;
  proxy.$refs["uploadRef"].handleRemove(file);
  proxy.$alert("<div style='overflow: auto;overflow-x: hidden;max-height: 70vh;padding: 10px 20px 0;'>" + response.msg + "</div>", "导入结果", { dangerouslyUseHTMLString: true });
  getList();
};
/** 提交上传文件 */
function submitFileForm() {
  proxy.$refs["uploadRef"].submit();
};
/** 导出按钮操作 */
function handleExport() {
  proxy.download('business/papers/export', {
    ...queryParams.value
  }, `papers_${new Date().getTime()}.xlsx`)
}

getList();
</script>
