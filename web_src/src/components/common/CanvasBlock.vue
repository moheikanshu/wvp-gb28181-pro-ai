<template>
  <canvas
    class="myCanvas"
    id="myCanvas"
    ref="myCanvas"
    width="910"
    height="511"
    @mousedown="mousedown"
    @mouseup="mouseup"
    @mousemove="mousemove"
  >
  </canvas>
</template>

<script>

export default {
    name: 'canvasblock',
    data() {
        return {
          flag: false,
          x: 0,
          y: 0,
          canvas: null,
          ctx: null,
        }
    },
    methods: {
      mousedown(e){
        this.flag = true;
        this.x = e.offsetX; // 鼠标落下时的X
        this.y = e.offsetY; // 鼠标落下时的Y
      },
      mouseup(){
        this.flag = false
      },
      mousemove(e){
        this.drawRect(e)
      },
      drawRect(e){
        if(this.flag){
          let { canvas, ctx } = this
          let x = this.x;
          let y = this.y;
          ctx.clearRect(0,0,canvas.width,canvas.height);
          ctx.beginPath();
          //设置线条颜色，必须放在绘制之前
          ctx.strokeStyle = '#fdea34';
          // 线宽设置，必须放在绘制之前
          ctx.lineWidth = 1;
          ctx.strokeRect(x,y,e.offsetX-x,e.offsetY-y);
        }
      },
      clear(){
        let { canvas, ctx } = this
        ctx.clearRect(0,0,canvas.width,canvas.height);
      }
    },
    mounted(){
      this.canvas = this.$refs.myCanvas
      this.ctx = this.canvas.getContext("2d")
    }
}
</script>

<style>
</style>
