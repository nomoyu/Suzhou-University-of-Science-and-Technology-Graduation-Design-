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
      <el-table-column label="论文内容" align="center" prop="content" />
      <el-table-column label="论文年限" align="center" prop="year" width="180">
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
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['business:papers:edit']"
          >修改</el-button>
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
  </div>
</template>

<script>
import { listPapers, getPapers, delPapers, addPapers, updatePapers } from "@/api/business/papers";

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
    /** 导出按钮操作 */
    handleExport() {
      this.download('business/papers/export', {
        ...this.queryParams
      }, `papers_${new Date().getTime()}.xlsx`)
    }
  }
};
</script>
<style>

</style>