<template>
  <el-container style="height: 100%">
    <el-aside class="ai-aside" width="200px" style="background-color: rgb(238, 241, 246)">
      <el-menu router :default-active="defaultActive">
        <el-submenu index="1">
          <template slot="title"><i class="el-icon-setting"></i>设备配置</template>
          <el-menu-item index="/ai/devicepreview">设备预览</el-menu-item>
          <el-menu-item index="/ai/devicemanagement">设备管理</el-menu-item>
          <el-menu-item index="/ai/streamProxyList">拉流代理</el-menu-item>
        </el-submenu>
        <el-submenu index="2">
          <template slot="title"><i class="el-icon-user-solid"></i>用户</template>
          <el-menu-item index="/user/usermanagement">用户管理</el-menu-item>
        </el-submenu>
      </el-menu>
    </el-aside>
    <el-main>
      <el-container>
        <transition name="fade">
          <router-view></router-view>
        </transition>
      </el-container>
    </el-main>
  </el-container>
</template>

<script>
  export default {
    name: "com",
    components: {},
    data() {
      return {
        defaultActive: this.$route.path,
      }
    }
  }
</script>

<style scoped>
  .ai-aside{
    background-color: #fff!important;
  }
  .ai-aside /deep/ .el-submenu__title{
    text-align: left;
  }
  .ai-aside /deep/ .el-submenu [class^=el-icon-]{
    margin-top: -3px;
  }
</style>
