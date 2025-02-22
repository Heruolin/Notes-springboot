import axios from "axios";

const request = axios.create({
  baseURL: 'http://localhost:8080', 
  timeout: 30000, // 设置请求超时
});

request.interceptors.request.use(
  config => {
    // 在请求发送前，设置请求头
    config.headers['Content-Type'] = 'application/json;charset=utf-8';
    return config;
  },
  error => {
    console.error('Request error: ' + error); // 修正错误日志
    return Promise.reject(error);
  }
);

request.interceptors.response.use(
  response => {
    let res = response.data;

    // 处理返回的字符串数据（如果有必要）
    if (typeof res === 'string') {
      res = res ? JSON.parse(res) : res;
    }
    return res;
  },
  error => {
    console.error('Response error: ', error); // 修正错误日志
    return Promise.reject(error);
  }
);

export default request;
