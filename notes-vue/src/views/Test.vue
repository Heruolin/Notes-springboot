<template>
  <div style="height: 100vh; display: flex; align-items: center; justify-content: center; background-color: rgb(148.6, 212.3, 117.1);">
    <div style="display: flex; background-color: white; width: 50%; border-radius: 5px; overflow: hidden;">
      <div style="flex: 1;">
        <img src="../assets/index.jpg" alt="" style="width:100%">
      </div>
      <div style="flex: 1; display: flex; align-items: center; justify-content: center;">
        <el-form ref="userRef" :model="user" style="width:80%" status-icon :rules="rules" label-width="auto" class="demo-user">
          <div style="font-size: 20px; font-weight: bold; text-align: center; margin-bottom: 20px;">便签系统</div>
          <el-form-item label="帐号" prop="username">
            <el-input prefix-icon="User" size="default" placeholder="请输入帐号" v-model="user.username" autocomplete="off" />
          </el-form-item>
          <el-form-item label="密码" prop="password">
            <el-input show-password prefix-icon="Lock" size="default" placeholder="请输入密码" v-model="user.password" autocomplete="off" />
          </el-form-item>
          <el-form-item label="验证码" prop="validCode">
            <div style="display: flex;">
              <el-input prefix-icon="Check" size="default" style="flex: 1" placeholder="请输入验证码" v-model="user.validCode" autocomplete="off" />
              <div style="flex: 1; height: 36px;">
                <ValidCode @update:value="getCode" />
              </div>
            </div>
          </el-form-item>
          <el-form-item>
            <el-button type="primary" style="width: 100%;" @click="submitForm(userRef)">登录</el-button>
          </el-form-item>
          <div>还没有帐号？请<span style="color: #0f9876; cursor: pointer;">注册</span></div>
        </el-form>
      </div>
    </div>
  </div>
</template>

<script lang="ts" setup>
import { ref, reactive } from 'vue'
import ValidCode from '@/components/ValidCode.vue'
import type { FormInstance, FormRules } from 'element-plus'
import request from '@/utils/request'

const userRef = ref<FormInstance>()

const validateUser = (rule: any, value: any, callback: any) => {
  if (value === '') {
    callback(new Error('请输入帐号'))
  } else {
    request.post('/login', user)
      .then(res => {
        console.log(res);
        callback();  // 验证成功，调用 callback
      })
      .catch(error => {
        callback(new Error('验证失败'));  // 验证失败，调用 callback 并传入错误
      })
  }
}

const validatePass = (rule: any, value: any, callback: any) => {
  if (value === '') {
    callback(new Error('请输入密码'))
  } else {
    callback()
  }
}

const getCode = (code: string) => {
  console.log(code)
  user.validCode = code  // 获取验证码并保存
}

const validateCode = (rule: any, value: any, callback: any) => {
  if (value === '') {
    callback(new Error('请输入验证码'))
  } else if (value !== user.validCode) {
    callback(new Error('验证码错误'))  // 校验验证码是否正确
  } else {
    callback()
  }
}

const user = reactive({
  username: '',
  password: '',
  validCode: '',
})

const rules = reactive<FormRules<typeof user>>({
  username: [{ validator: validateUser, trigger: 'blur' }],
  password: [{ validator: validatePass, trigger: 'blur' }],
  validCode: [{ validator: validateCode, trigger: 'blur' }],
})

const submitForm = (formEl: FormInstance | undefined) => {
  if (!formEl) return
  formEl.validate((valid) => {
    if (valid) {
      console.log('submit!')
    } else {
      console.log('error submit!')
    }
  })
}
</script>

<style lang="scss" scoped>
.login-page {
  border: 1.5px solid rgb(123, 109, 30);
  border-radius: 8px;
  width: 400px;
  margin: 0 auto;
  margin-top: 50px;
  padding: 20px;
  .code {
    text-align: center;
    margin-bottom: 10px;
  }
  .login_btn {
    width: 100%;
  }
}
</style>
