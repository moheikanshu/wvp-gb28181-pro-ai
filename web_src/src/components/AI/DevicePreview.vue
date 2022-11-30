<template>
  <div id="devicePosition" style="width:100vw; height: 91vh">
    <el-container v-loading="loading" style="height: 91vh;" element-loading-text="拼命加载中">
      <el-aside width="300px" style="background-color: #ffffff">
        <DeviceTree :clickEvent="clickEvent" :contextMenuEvent="contextMenuEvent"></DeviceTree>
        <div class="button-box">
          <el-button class="pezhi-button" type="primary" @click="showVisble = true">配置播放地址</el-button>
        </div>
      </el-aside>
      <el-container>
        <el-header height="5vh" style="text-align: left;font-size: 17px;line-height:5vh">
          分屏:
          <i class="el-icon-full-screen btn" :class="{active:spilt==1}" @click="spilt=1"/>
          <i class="el-icon-menu btn" :class="{active:spilt==4}" @click="spilt=4"/>
          <i class="el-icon-s-grid btn" :class="{active:spilt==9}" @click="spilt=9"/>
        </el-header>
        <el-main style="padding: 0;">
          <div style="width: 99%;height: 85vh;display: flex;flex-wrap: wrap;background-color: #000;">
            <div v-for="i in spilt" :key="i" class="play-box"
                 :style="liveStyle" :class="{redborder:playerIdx == (i-1)}"
                 @click="playerIdx = (i-1)">
              <div v-if="!videoUrl[i-1]" style="color: #ffffff;font-size: 30px;font-weight: bold;">{{ i }}</div>
              <player ref="player" v-else :videoUrl="videoUrl[i-1]" fluent autoplay @screenshot="shot"
                      @destroy="destroy"/>
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
  },
  methods: {
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
          that.getUser(JSON.stringify(res.data.data.rtsp))
          // let videoUrl;
          // if (location.protocol === "https:") {
          //   videoUrl = res.data.data.wss_flv;
          // } else {
          //   videoUrl = res.data.data.ws_flv;
          // }
          // console.log(111,videoUrl)
          // itemData.playUrl = videoUrl;
          // that.setPlayUrl(videoUrl, idxTmp);
        } else {
          that.$message.error(res.data.msg);
        }
      }).catch(function (e) {
      }).finally(() => {
        that.loading = false
      });
    },
    getUser(rtspUrl){
      let that = this;
      this.$axios({
        method: 'post',
        // url: `http://192.168.200.8:1936/users?name=[1,${rtspUrl}]`,
        url: `${location.protocol}//192.168.200.8:1936/users?name=[1,${rtspUrl}]`,
        headers: {
          'Content-Type': 'application/json'
        },
      }).then(function (res) {
        console.log('请求成功',res)
      }).catch(function (e) {
        console.log('请求失败',e)
      }).finally(() => {
        that.loading = false
      });
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
</style>
