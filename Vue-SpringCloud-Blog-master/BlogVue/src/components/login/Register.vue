<template>
  <div class="container">
    <div class="row clearfix">
      <div class="col-md-12 column">
        <div>
          <el-avatar src="../../../static/img/login/tian.gif"></el-avatar>
        </div>
        <br>
        <div class="login-wrap">
          <h2 style="font-style: italic" class="text-center">基于Vue+SpringCloud博客的设计和实现&emsp;<small>作者-张哲霄</small></h2>
          <br>
          <br>
          <router-link :to="{ name: 'Login' }" style="text-decoration: none;color: #2c3e50"
            target="_blank">已有账号？马上登录</router-link>
          <br>
          <br>
          <input type="text" placeholder="小写字母开头且满足8-16位" maxlength="16" show-word-limit v-model="newUsername">
          <input type="password" placeholder="请输入密码,密码的长度为8-16位" maxlength="16" show-word-limit v-model="newPassword">
          <input type="password" placeholder="请再次输入相同的密码" maxlength="16" show-word-limit v-model="newPassword2">
          <br>
          <!-- 滑动验证码 -->
          <div id="captcha-element"></div>
          <br>
          <button id="button" @click="triggerCaptcha">注册</button>
        </div>
      </div>
    </div>
  </div>
</template>
<script>
export default {
  inject: ['reload'],
  data() {
    return {
      scene: '',
      newUsername: '',
      newPassword: '',
      newPassword2: '',
      aliToken: '',
      sessionId: '',
      captchaInstance: null, // 存储验证码实例
      captchaVerifyParam: null, //后端参数
      captchaParam:null,//解析后验证码参数
      sig: "ceshi"
    }
  },
  components: {
    "remote-js": {
      render(createElement) {
        const self = this;
        return createElement("script", {
          attrs: { type: "text/javascript", src: this.src },
          on: {
            load() {
              self.$emit("loaded");
            }
          }
        });
      },
      props: {
        src: { type: String, required: true }
      }
    },
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
    /* //初始化智能验证组件
    initCaptchaRegister() {
      let _thisR = this;
      let ic2 = new smartCaptcha({
        renderTo: '#sc2',
        width: 300,
        height: 42,
        default_txt: "点击按钮开始智能验证",
        success_txt: "验证成功，请点击注册",
        fail_txt: "验证失败，请在此点击按钮刷新",
        scaning_txt: "智能检测中",
        success: function (data) {
          console.log(NVC_Opt.token);
          console.log(data.sessionId);
          console.log(data.sig);
          _thisR.aliToken = NVC_Opt.token;
          _thisR.sessionId = data.sessionId;
          _thisR.sig = data.sig;
        },
      });
      ic2.init();
    }, */
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
      const verifyResult = await this.register(captchaVerifyParam);
      return verifyResult;
    },
    //注册账号
    register() {
      //账号名字的规则
      let patterName = /^[a-z][a-zA-Z0-9\-]{6,15}$/;

      //密码的长度
      let patterPassword = /^.{7,15}$/;

      if (this.newUsername === '' || this.newPassword === '' || this.newPassword2 === '' ||
        this.newPassword !== this.newPassword2 || !patterPassword.test(this.newPassword) ||
        this.value === false || !patterName.test(this.newUsername)
        //   || this.aliToken === '' || this.sessionId === '' || this.sig === ''
      ) {
        if (this.newUsername === '' || this.newPassword === '' || this.newPassword2 === '') {
          this.$message({
            type: "error",
            message: '账号和密码不能为空',
          })
        } else if (this.newPassword !== this.newPassword2) {
          this.$message({
            type: "error",
            message: '两个密码不相同',
          })
        } else if (!patterName.test(this.newUsername)) {
          this.$message({
            type: "error",
            message: '用户名不符合',
          })
        } else if (!patterPassword.test(this.newPassword)) {
          this.$message({
            type: "error",
            message: '密码不符合长度要求',
          })
        }
        //else if (this.aliToken === '' || this.sessionId === '' || this.sig === '') {
        // this.$message({
        //     type: "error",
        // message: '请点进验证!',
        // })
        //}
        /* else {
           this.$message({
               type: "error",
               message: '请选择绿色',
           })
       } */
      } else {
        this.$http.post('/whc/blog-customer-user/register', {
          newUsername: this.newUsername,
          newPassword: this.newPassword,
          captchaVerifyParam: this.captchaVerifyParam
        }).then(res => {
          console.log(res);
          //let registerLogin=JSON.stringify(res.data.success);
          if (res.data.data.status === 200) {
            //保存Id,只用来模拟连接websocket
            window.localStorage.setItem('id', res.data.data.id);
            //保存注册的Token
            window.localStorage.setItem('token', res.data.data.token);
            this.$message({
              type: "success",
              message: '注册成功',
            }.then(
              this.$router.replace({
                name: 'Mation',
                query: {
                  newUsername: this.newUsername,
                }
              }), this.$router.go(0)))
          } else if (res.data.data === false) {
            this.$message.error('智能验证失败');
            this.reload();
          } else {
            this.$message.error(res.data.message);
          }
        })
      }
    }
  },
}
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
