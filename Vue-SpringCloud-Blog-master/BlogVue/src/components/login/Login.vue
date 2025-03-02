<template>
  <div class="container">
    <div class="row clearfix">
      <div class="col-md-12 column">
        <div>
          <el-avatar src="../../../static/img/login/tian.gif"></el-avatar>
        </div>
        <br>
        <h2 style="font-style: italic" class="text-center">
          基于Vue+SpringCloud博客的设计和实现&emsp;
          <small>作者-汪海潮</small>
        </h2>
        <br><br>
        <div class="login-wrap">
          <router-link :to="{ name: 'Register' }" style="text-decoration: none;color: #2c3e50" target="_blank">
            没有账号？马上注册
          </router-link>
          <br><br>
          <input type="text" placeholder="请输入您的用户名" v-model="username">
          <input type="password" placeholder="请输入您的密码" v-model="password">
          <br><br>

          <!-- 滑动验证码 -->
          <div id="captcha-element"></div>
          <br>
          <button id="button" @click="triggerCaptcha">登录</button>
        </div>
        <br>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  data() {
    return {
      username: "",
      password: "",
      captchaInstance: null, // 存储验证码实例
      captchaVerifyParam: null, //后端参数
      captchaParam:null,//解析后验证码参数
      sig: "ceshi"
    };
  },
  mounted() {
    this.loadCaptcha();
  },
  methods: {
    // ✅ 加载阿里云验证码
    loadCaptcha() {
      if (!window.AliyunCaptcha) {
        let script = document.createElement("script");
        script.src =
          "https://o.alicdn.com/captcha-frontend/aliyunCaptcha/AliyunCaptcha.js";
        script.async = true;
        script.onload = this.initCaptcha;
        document.head.appendChild(script);
      } else {
        this.initCaptcha();
      }
    },

    // ✅ 初始化阿里云滑动验证码
    initCaptcha() {
      window.initAliyunCaptcha({
        SceneId: "1lrjjj2w", // 请填写你的阿里云验证码场景ID
        mode: "embed",
        element: "#captcha-element",
        button: "#button",
        captchaVerifyCallback: this.captchaVerifyCallback,
        onBizResultCallback: this.onBizResultCallback,
        getInstance: (instance) => {
          this.captchaInstance = instance;
        },
        slideStyle: {
          width: 360,
          height: 40,
        },
        language: "cn",
      });
    },

    // ✅ 触发验证码（点击登录按钮）
    triggerCaptcha() {
      if (this.captchaInstance) {
        this.captchaInstance.show();
      } else {
        console.error("验证码实例未初始化！");
      }
    },

    // ✅ 验证码回调，发送到后端
    async captchaVerifyCallback(captchaVerifyParam) {
      console.log("验证码验证成功", captchaVerifyParam);
      try {
        this.captchaParam = JSON.parse(captchaVerifyParam);
      } catch (error) {
        console.error('无法解析captchaVerifyParam:', error);
        return {
          captchaResult: false, // 如果解析失败，返回验证码失败
          bizResult: false, // 业务验证失败
        };
      }
      console.log('解析后的 captchaVerifyParam:', this.captchaParam);
      // 这里模拟向后端发送请求
      const verifyResult = await this.sendCaptchaToBackend(captchaVerifyParam);
      return verifyResult;
    },

    // ✅ 模拟向后端发送验证码参数
    async sendCaptchaToBackend(captchaVerifyParam) {

      this.$http.post('/whc/blog-customer-user/loginToken', {
        username: this.username,
        password: this.password,
        captchaVerifyParam:captchaVerifyParam
      }).then(res => {
        console.log(res);
        if (res.data.data.status === 200) {
          console.log(res);
          //保存Id,只用来模拟连接websocket
          window.localStorage.setItem('id', res.data.data.id);
          //保存Token
          window.localStorage.setItem('token', res.data.data.token);
          //保存blogName
          window.localStorage.setItem('blogName', res.data.data.blogName);
          //保存用户头像
          window.localStorage.setItem('userPicture', res.data.data.userPicture);
          //保存加密的手机号
          window.localStorage.setItem('myPhone', res.data.data.myPhone);
          //保存会员标志,只要是会员就行，只返回可以看的标志，结果与后端的数据库中的数据为准。
          window.localStorage.setItem('memberSign', res.data.data.memberSign);
          this.$message({
            type: "success",
            message: JSON.stringify(res.data.message),
          }).then(
            this.$router.replace({ name: 'Article' }).then(this.$router.go(0))
          )
        } else if (res.data.data === false) {
          this.$message.error('智能验证失败');
          this.reload();
        } else {
          this.$message.error('账号与密码不匹配');
          this.reload();
        }
      })
      console.log("向后端发送验证码信息:", captchaVerifyParam);
      // 这里应该是实际的 API 请求，你需要替换成你的后端接口
      return {
        captchaResult: true, // 这里模拟验证码成功
        bizResult: true, // 这里模拟业务验证成功
      };
    },

    // ✅ 业务验证结果回调
    onBizResultCallback(bizResult) {
      if (bizResult === true) {
        alert("登录成功！");
        // this.$router.replace({name: 'Article'}).then(this.$router.go(0)) // 这里替换成你的跳转页面
      } else {
        alert("业务验证失败！");
      }
    },
  },
};
</script>

<style>
.container {
  max-width: 600px;
  margin: 50px auto;
  padding: 20px;
  border-radius: 10px;
  box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
  text-align: center;
  background-color: #fff;
}

.login-wrap {
  display: flex;
  flex-direction: column;
  align-items: center;
}

.login-wrap input {
  width: 80%;
  padding: 10px;
  margin: 10px 0;
  border: 1px solid #ddd;
  border-radius: 5px;
  font-size: 16px;
}

.login-wrap button {
  width: 85%;
  padding: 10px;
  background-color: #409eff;
  color: white;
  border: none;
  border-radius: 5px;
  font-size: 18px;
  cursor: pointer;
  transition: background-color 0.3s ease;
}

.login-wrap button:hover {
  background-color: #3077cc;
}

#captcha-element {
  width: 360px;
  height: 40px;
  margin: 10px auto;
}
</style>