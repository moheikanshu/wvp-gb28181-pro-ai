<template>
  <div id="app" style="width: 100%">
    <div class="page-header">
      <div class="page-title">设备列表</div>
      <div class="page-header-btn">
        <el-button icon="el-icon-plus" size="mini" style="margin-right: 1rem;" type="primary" @click="add">添加设备</el-button>
        <el-button icon="el-icon-refresh-right" circle size="mini" :loading="getDeviceListLoading" @click="getDeviceList()"></el-button>
      </div>
    </div>
    <!--设备列表-->
    <el-table v-loading="getDeviceListLoading" :data="deviceList" style="width: 100%;font-size: 12px;" :height="winHeight" header-row-class-name="table-header">
      <el-table-column prop="id" label="设备编号">
      </el-table-column>
      <el-table-column prop="name" label="设备名称">
      </el-table-column>
      <el-table-column label="设备地址">
        <template slot-scope="scope">
          {{scope.row.ip}}:{{scope.row.port}}
        </template>
      </el-table-column>

      <el-table-column label="操作" fixed="right">
        <template slot-scope="scope">
          <el-button size="medium" icon="el-icon-edit" type="text" @click="edit(scope.row)">编辑</el-button>
          <el-divider direction="vertical"></el-divider>
          <el-button size="medium" icon="el-icon-delete" type="text" @click="deleteDevice(scope.row)" style="color: #f56c6c">删除</el-button>
        </template>
      </el-table-column>
    </el-table>
    <el-pagination
      style="float: right"
      @size-change="handleSizeChange"
      @current-change="currentChange"
      :current-page="currentPage"
      :page-size="count"
      :page-sizes="[15, 25, 35, 50]"
      layout="total, sizes, prev, pager, next"
      :total="total">
    </el-pagination>
    <el-dialog :title="dialogText" :visible.sync="infoVisble" :close-on-click-modal="false">
    	<el-form ref="deviceFrom" :model="aiDeviceForm" :rules="deviceRules" label-width="auto" v-if="infoVisble">
        <el-form-item label="设备名称" prop="name">
          <el-input v-model="aiDeviceForm.name"></el-input>
        </el-form-item>
        <el-form-item label="设备ip" prop="ip">
          <el-input v-model="aiDeviceForm.ip"></el-input>
        </el-form-item>
        <el-form-item label="设备端口" prop="port">
          <el-input v-model="aiDeviceForm.port"></el-input>
        </el-form-item>
      </el-form>
    	<span slot="footer">
    		<!-- 确定 -->
    		<!-- <a :href="focusMediaData.url">{{$t('download')}}</a> -->
    		<el-button type="info" @click="infoVisble = false">取消</el-button>
    		<el-button type="primary" @click="saveDevice">确定</el-button>
    	</span>
    </el-dialog>
  </div>
</template>

<script>
import uiHeader from '@/layout/UiHeader.vue'

const formOrign = {
  name: '',
  ip: '',
  port: '',
}
export default {
  name: 'app',
  components: {
    uiHeader,
  },
  data() {
    return {
      aiDeviceForm: {...formOrign},
      deviceList: [], //设备列表
      currentDevice: {}, //当前操作设备对象

      videoComponentList: [],
      updateLooper: 0, //数据刷新轮训标志
      currentDeviceChannelsLenth: 0,
      winHeight: window.innerHeight - 200,
      currentPage: 1,
      count: 15,
      total: 0,
      getDeviceListLoading: false,
      dialogText: '',
      infoVisble: false,
      deviceRules: {
        name: [{ required: true, message: "请输入设备名称", trigger: ["blur", "change"] }],
        ip: [{ required: true, message: "请输入设备ip", trigger: ["blur", "change"] }],
        port: [{ required: true, message: "请输入设备端口", trigger: ["blur", "change"] }],
      },
    };
  },
  watch: {
    infoVisble(val){
      if(!val){
        this.aiDeviceForm = {...formOrign}
      }
    }
  },
  mounted() {
    console.log(11,this.$cookies)
    this.initData();
    // this.updateLooper = setInterval(this.initData, 10000);
  },
  destroyed() {
    clearTimeout(this.updateLooper);
  },
  methods: {
    // 保存设备deviceDialog
    saveDevice(){
      this.$refs.deviceFrom.validate(valid => {
        if(!valid) return false
        this.$axios({
          method: 'post',
          url: `/api/ai/device/save`,
          data: this.aiDeviceForm
        }).then((res) => {
          const { code, msg } = res.data
          this.infoVisble = false
          if(code == 0){
            this.$message.success(msg)
            this.initData()
          }else{
            this.$message.error(msg)
          }
        }).catch((error) => {
          console.log(error);
        });
      })
    },
    initData: function () {
      this.getDeviceList();
    },
    currentChange: function (val) {
      this.currentPage = val;
      this.getDeviceList();
    },
    handleSizeChange: function (val) {
      this.count = val;
      this.getDeviceList();
    },
    getDeviceList: function () {
      this.getDeviceListLoading = true;
      this.$axios({
        method: 'get',
        url: `/api/ai/device/list`,
        params: {
          page: this.currentPage,
          count: this.count
        }
      }).then( (res)=> {
        if (res.data.code === 0) {
          this.total = res.data.data.total;
          this.deviceList = res.data.data.list;
        }
        this.getDeviceListLoading = false;
      }).catch( (error)=> {
        console.error(error);
        this.getDeviceListLoading = false;
      });
    },
    deleteDevice: function (row) {
      let msg = "确定删除此设备？"
      this.$confirm(msg, '提示', {
        dangerouslyUseHTMLString: true,
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        center: true,
        type: 'warning'
      }).then(() => {
        this.$axios({
          method: 'delete',
          url: `/api/ai/device/delete?id=${row.id}`,
        }).then((res) => {
          this.$message.success(res.data.msg)
          this.getDeviceList();
        }).catch((error) => {
          console.error(error);
        });
      }).catch(() => {

      });


    },
    edit: function (row) {
      const item = {
        id: row.id,
        name: row.name,
        ip: row.ip,
        port: row.port,
      }
      this.aiDeviceForm = {...item}
      this.dialogText = '编辑设备'
      this.infoVisble = true
    },
    add: function () {
      this.dialogText = '添加设备'
      this.infoVisble = true
    }
  }
};
</script>

<style>

</style>
