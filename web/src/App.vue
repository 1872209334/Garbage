<template>
    <div id="app">
        <router-view v-if="isRouterAlive"></router-view>
    </div>
</template>
<script>
    export default {
        provide(){
            return{
                reload:this.reload
            }
        },
        data(){
            return{
                isRouterAlive:true
            }
        },
        methods:{
            reload(){
                this.isRouterAlive = false;
                this.$nextTick(function () {
                    this.isRouterAlive = true;
                })
            }
        }
    }
</script>
<style>
    @import "./resource/css/main.css";
    @import "./resource/css/color-dark.css";     /*深色主题*/
    /*@import "./resource/css/theme-hixent/color-green.css";   浅绿色主题*/
</style>
