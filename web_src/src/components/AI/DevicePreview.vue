<template>
  <div id="devicePosition" style="width:100vw; height: 91vh">
    <el-container v-loading="loading" style="height: 91vh;" element-loading-text="拼命加载中">
      <el-aside width="300px" style="background-color: #ffffff">
        <DeviceTree :clickEvent="clickEvent" :contextMenuEvent="contextMenuEvent"></DeviceTree>
        <!-- <div class="button-box">
          <el-button class="pezhi-button" type="primary" @click="showVisble = true">配置播放地址</el-button>
        </div> -->
      </el-aside>
      <el-container>
        <el-header height="5vh" style="text-align: left;font-size: 17px;line-height:5vh">
          分屏:
          <i class="el-icon-full-screen btn" :class="{active:spilt==1}" @click="spilt=1"/>
          <i class="el-icon-menu btn" :class="{active:spilt==4}" @click="spilt=4"/>
          <i class="el-icon-s-grid btn" :class="{active:spilt==9}" @click="spilt=9"/>
        </el-header>
        <el-main style="padding: 0;">
          <div class="flex" style="width: 99%;height: 85vh;background-color: #000;">
            <div class="flex-item ht100 flex" style="flex-wrap: wrap;">
              <div v-for="i in spilt" :key="i" class="play-box"
                   :style="liveStyle" :class="{redborder:playerIdx == (i-1)}"
                   @click="playerIdx = (i-1)">
                <div v-if="!videoUrl[i-1]" style="color: #ffffff;font-size: 30px;font-weight: bold;">{{ i }}</div>
                <video class="video" :src="videoUrl[i-1]" autoplay controls playsinline v-else></video>
                <!-- <player ref="player" v-else :videoUrl="videoUrl[i-1]" fluent autoplay @screenshot="shot" @destroy="destroy"/> -->
              </div>
            </div>
            <div class="video-text ht100" v-if="queryData.length">
              <div class="item" v-for="(item, index) in queryData" :key="index">
                <p class="ptit">{{item.ip}}:{{item.port}}</p>
                <p class="ptxt">{{item.data}}</p>
              </div>
            </div>
          </div>
          <el-dialog title="配置播放地址" :visible.sync="showVisble">
          	<div class="box"><el-input v-model="inputUrl"></el-input></div>
          	<span slot="footer">
          		<!-- 确定 -->
          		<!-- <a :href="focusMediaData.url">{{$t('download')}}</a> -->
          		<el-button type="primary" @click="saveUrl">保存</el-button>
          	</span>
          </el-dialog>
          <el-dialog title="列表" :visible.sync="listVisble" :close-on-click-modal="false" width="400px">
          	<div class="tree-box" v-if="listVisble">
              <el-tree class="list-tree" ref="gdTree" v-loading="deviceLoading" :props="defaultProps" :data="deviceList" show-checkbox :check-on-click-node="true" :default-expand-all="true" node-key="id" @check="handleNodeClick">
              </el-tree>
            </div>
          	<span slot="footer">
          		<!-- 确定 -->
          		<!-- <a :href="focusMediaData.url">{{$t('download')}}</a> -->
          		<el-button type="primary" @click="urlSubmit">确定</el-button>
          	</span>
          </el-dialog>
        </el-main>
      </el-container>
    </el-container>
  </div>
</template>

<script>
import uiHeader from "@/layout/UiHeader.vue";
import player from '@/components/common/jessibuca.vue'
import DeviceTree from '@/components/common/DeviceTree.vue'

export default {
  name: "live",
  components: {
    uiHeader, player, DeviceTree
  },
  data() {
    return {
      videoUrl: [''],
      spilt: 1,//分屏
      playerIdx: 0,//激活播放器

      updateLooper: 0, //数据刷新轮训标志
      count: 15,
      total: 0,

      //channel
      loading: false,
      showVisble: false,
      inputUrl: '',
      itemDatas: {},
      listVisble: false, //显示设备列表弹窗
      getdata: '',
      treeList: [
        {
          children: null,
          id: 1,
          label: '测试1'
        },
        {
          children: null,
          id: 2,
          label: '测试2'
        },
        {
          children: null,
          id: 3,
          label: '测试3'
        },
      ],
      defaultProps: {
        children: 'children',
        label: 'name'
      },
      deviceList: [],
      deviceLoading: false,
      checkedNode: {},
      personTimer: [],
      queryData: [],
    };
  },
  mounted() {

  },
  created() {
    this.checkPlayByParam()
  },

  computed: {
    liveStyle() {
      let style = {width: '100%', height: '100%'}
      switch (this.spilt) {
        case 4:
          style = {width: '49%', height: '49%'}
          break
        case 9:
          style = {width: '32%', height: '32%'}
          break
      }
      this.$nextTick(() => {
        for (let i = 0; i < this.spilt; i++) {
          const player = this.$refs.player
          player && player[i] && player[i].updatePlayerDomSize()
        }
      })
      return style
    }
  },
  watch: {
    listVisble(val){
      if(!val){
        this.checkedNode = {}
      }
    },
    spilt(newValue) {
      console.log("切换画幅;" + newValue)
      let that = this
      for (let i = 1; i <= newValue; i++) {
        if (!that.$refs['player' + i]) {
          continue
        }
        this.$nextTick(() => {
          if (that.$refs['player' + i] instanceof Array) {
            that.$refs['player' + i][0].resize()
          } else {
            that.$refs['player' + i].resize()
          }
        })

      }
      window.localStorage.setItem('split', newValue)
    },
    '$route.fullPath': 'checkPlayByParam'
  },
  destroyed() {
    clearTimeout(this.updateLooper);
    if(this.personTimer.length){
      this.personTimer.forEach((v) => {
        clearInterval(v)
      })
      this.personTimer = []
    }
  },
  methods: {
    handleNodeClick(data){
      const { id } = data
      this.checkedNode = data
      this.$refs.gdTree.setCheckedKeys([id])
    },
    saveUrl(){
      if(!this.inputUrl){
        return this.$message.error('请输入地址')
      }
      let idxTmp = this.playerIdx
      this.itemDatas.playUrl = this.inputUrl;
      this.setPlayUrl(this.inputUrl, idxTmp)
      this.showVisble = false
    },
    destroy(idx) {
      console.log(idx);
      this.clear(idx.substring(idx.length - 1))
    },
    clickEvent: function (device, data, isCatalog) {
      if (data.channelId && !isCatalog) {
        if (device.online === 0) {
          this.$message.error('设备离线!不允许点播');
        }else {
          this.sendDevicePush(data)
        }
      }
    },
    contextMenuEvent: function (device, event, data, isCatalog) {

    },
    //通知设备上传媒体流
    sendDevicePush: function (itemData) {
      // if (itemData.status === 0) {
      //   this.$message.error('设备离线!');
      //   return
      // }
      this.save(itemData)
      this.itemDatas = itemData
      let deviceId = itemData.deviceId;
      // this.isLoging = true;
      let channelId = itemData.channelId;
      console.log("通知设备推流1：" + deviceId + " : " + channelId);
      let idxTmp = this.playerIdx
      let that = this;
      this.loading = true
      this.$axios({
        method: 'get',
        url: '/api/play/start/' + deviceId + '/' + channelId
      }).then(function (res) {
        if (res.data.code === 0 && res.data.data) {
          that.listVisble = true
          that.getDeviceList()
          that.getdata = res.data.data.rtsp
          // that.getUser(JSON.stringify(res.data.data.rtsp))
          // let videoUrl;
          // if (location.protocol === "https:") {
          //   videoUrl = res.data.data.wss_flv;
          // } else {
          //   videoUrl = res.data.data.ws_flv;
          // }
          // console.log(111,videoUrl)
          // itemData.playUrl = videoUrl;
          // that.setPlayUrl(videoUrl, idxTmp);deviceList
        } else {
          that.$message.error(res.data.msg);
        }
      }).catch(function (e) {
      }).finally(() => {
        that.loading = false
      });
    },
    getDeviceList(){
      this.deviceLoading = true
      this.$axios({
        method: 'get',
        url: `/api/ai/device/all`,
      }).then( (res)=> {
        const { code, data, msg } = res.data
        if (code === 0) {
          this.deviceList = data;
        }else{
          this.$message.error(msg)
        }
        this.deviceLoading = false;
      }).catch( (error)=> {
        console.error(error);
        this.deviceLoading = false;
      });
    },
    urlSubmit(){
      this.loading = true
      this.getUser(this.getdata)
      this.listVisble = false
    },
    getUser(rtspUrl){
      let that = this;
      const { ip, port } = this.checkedNode
      this.listVisble = false
      // let params = { name: [1,rtspUrl] }
      // params = JSON.stringify(params)
      // console.log(333,params)
      this.$axios({
        method: 'post',
        // url: `http://192.168.200.8:1936/users?name=[1,${rtspUrl}]`,
        url: `${location.protocol}//${ip}:${port}/users`,
        data: {
          name: [1,rtspUrl]
        },
        headers: {
          'Content-Type': 'application/json'
        },
      }).then(function (res) {
        console.log('请求成功',res.data.Url,res)
        const data = JSON.parse(res.data)
        console.log('json转换', data)
        if(data.Url){
          let url = data.url
          url = url.replace(/0.0.0.0/, ip)
          that.setPlayUrl(url, that.playerIdx)
          let personTimer = setInterval(() => {
            that.getPersons(ip, port)
          }, 2000)
          that.$set(that.personTimer, that.playerIdx, personTimer)
        }
      }).catch(function (e) {
        console.log('请求失败',e)
        // that.setPlayUrl('rtmp://175.178.213.69:1935/rtp/34020000002000000013_34020000002000000013', that.playerIdx)
        that.loading = false
      }).finally(() => {
        that.loading = false
      });
    },
    getPersons(ip, port){
      let that = this
      that.$axios({
        method: 'post',
        // url: `http://192.168.200.8:1936/users?name=[1,${rtspUrl}]`,
        url: `${location.protocol}//${ip}:${port}/persons`,
        headers: {
          'Content-Type': 'application/json'
        },
      }).then(function (res) {
        console.log('请求成功',res.data.Url,res)
        if(res.data){
          let item = {
            ip: ip,
            port: port,
            data: data
          }
          that.queryData = that.queryData.concat(item)
        }
      }).catch(function (e) {
        console.log('请求失败',e)
      })
    },
    setPlayUrl(url, idx) {
      this.$set(this.videoUrl, idx, url)
      let _this = this
      setTimeout(() => {
        window.localStorage.setItem('videoUrl', JSON.stringify(_this.videoUrl))
      }, 100)

    },
    checkPlayByParam() {
      let {deviceId, channelId} = this.$route.query
      if (deviceId && channelId) {
        this.sendDevicePush({deviceId, channelId})
      }
    },
    shot(e) {
      // console.log(e)
      // send({code:'image',data:e})
      var base64ToBlob = function (code) {
        let parts = code.split(';base64,');
        let contentType = parts[0].split(':')[1];
        let raw = window.atob(parts[1]);
        let rawLength = raw.length;
        let uInt8Array = new Uint8Array(rawLength);
        for (let i = 0; i < rawLength; ++i) {
          uInt8Array[i] = raw.charCodeAt(i);
        }
        return new Blob([uInt8Array], {
          type: contentType
        });
      };
      let aLink = document.createElement('a');
      let blob = base64ToBlob(e); //new Blob([content]);
      let evt = document.createEvent("HTMLEvents");
      evt.initEvent("click", true, true); //initEvent 不加后两个参数在FF下会报错  事件类型，是否冒泡，是否阻止浏览器的默认行为
      aLink.download = '截图';
      aLink.href = URL.createObjectURL(blob);
      aLink.click();
    },
    save(item) {
      let dataStr = window.localStorage.getItem('playData') || '[]'
      let data = JSON.parse(dataStr);
      data[this.playerIdx] = item
      window.localStorage.setItem('playData', JSON.stringify(data))
    },
    clear(idx) {
      let dataStr = window.localStorage.getItem('playData') || '[]'
      let data = JSON.parse(dataStr);
      data[idx - 1] = null;
      console.log(data);
      window.localStorage.setItem('playData', JSON.stringify(data))
    },
  }
};
</script>
<style>
.el-main{
  padding-right: 3px;
  padding-bottom: 0;
}
.btn {
  margin: 0 10px;

}

.btn:hover {
  color: #409EFF;
}

.btn.active {
  color: #409EFF;

}

.redborder {
  border: 2px solid red !important;
}

.play-box {
  background-color: #000000;
  border: 2px solid #505050;
  display: flex;
  align-items: center;
  justify-content: center;
}
</style>
<style>
.videoList {
  display: flex;
  flex-wrap: wrap;
  align-content: flex-start;
}

.video-item {
  position: relative;
  width: 15rem;
  height: 10rem;
  margin-right: 1rem;
  background-color: #000000;
}

.video-item-img {
  position: absolute;
  top: 0;
  bottom: 0;
  left: 0;
  right: 0;
  margin: auto;
  width: 100%;
  height: 100%;
}

.video-item-img:after {
  content: "";
  display: inline-block;
  position: absolute;
  z-index: 2;
  top: 0;
  bottom: 0;
  left: 0;
  right: 0;
  margin: auto;
  width: 3rem;
  height: 3rem;
  background-image: url("../../assets/loading.png");
  background-size: cover;
  background-color: #000000;
}

.video-item-title {
  position: absolute;
  bottom: 0;
  color: #000000;
  background-color: #ffffff;
  line-height: 1.5rem;
  padding: 0.3rem;
  width: 14.4rem;
}

.baidumap {
  width: 100%;
  height: 100%;
  border: none;
  position: absolute;
  left: 0;
  top: 0;
  right: 0;
  bottom: 0;
  margin: auto;
}

/* 去除百度地图版权那行字 和 百度logo */
.baidumap > .BMap_cpyCtrl {
  display: none !important;
}

.baidumap > .anchorBL {
  display: none !important;
}
.button-box{
  position: absolute;
  left: 0;
  right: 0;
  bottom: 20px;
}
.el-aside{
  position: relative;
}
.video{
  width: 100%;
  height: 100%;
}
.list-tree /deep/ .el-tree-node__label{
  font-size: 16px;
}
.tree-box{
  max-height: 400px;
  overflow: auto;
}
.flex {
  display: flex;
}
.flex-item{
	flex: 1;
}
.video-text{
  width: 250px;
  color: #fff;
  height: 100%;
  overflow: auto;
  font-size: 14px;
  text-align: left;
}
.play-boxs{
  width: 100%;
  height: 100%;
}
.video-box{
  height: 100%;
}
.video-text .item{
  padding: 0 10px;
  word-break: break-all;
}
.video-text .item + .item{
  border-top: 1px solid #fff;
}
.ht100{
  height: 100%;
}
</style>
