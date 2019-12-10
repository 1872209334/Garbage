<template>
    <div class="error-page">
        <div class="error-title">
            <img style="position: absolute;left: calc((100% - 500px)/2)" src="../../resource/img/staticDetail.jpg"/>
        </div>
        <div class="error-handle">
            <el-button class="v-1-0-0-Button-Minor" @click="goBack">返回</el-button>
        </div>
    </div>
</template>

<script>
    export default {
        methods: {
            goBack() {
                this.$router.go(-1);
            }
        }
    }
</script>
<style scoped>
    .error-page {
        display: flex;
        justify-content: center;
        align-items: center;
        flex-direction: column;
        width: 100%;
        height: 100%;
        background: #ffffff;
        box-sizing: border-box;
    }

    .error-title {
        width: 100%;
        height: 420px;
        padding-top: 50px;
        position: absolute;
        background-color: #f8f8f8;
        top: 0;
    }

    .error-code span {
        color: #00a854;
    }

    .error-desc {
        font-size: 30px;
        color: #777;
    }

    .error-handle {
        position: absolute;
        top: 500px;

    }

    .error-btn {
        margin-left: 100px;
    }
</style>
