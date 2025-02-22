import { createApp } from 'vue';
import { createPinia } from 'pinia';
import ElementPlus from 'element-plus';
import * as ElementPlusIconsVue from '@element-plus/icons-vue';
import 'element-plus/dist/index.css';
import App from './App.vue';
import router from './router/index.js'; // 引入 JavaScript 路由配置文件
import request from './utils/request';

const app = createApp(App);

for (const [key, component] of Object.entries(ElementPlusIconsVue)) {
    app.component(key, component);
}

app.config.globalProperties.$request = request;
app.use(createPinia());
app.use(router);
app.use(ElementPlus);

app.mount('#app');
