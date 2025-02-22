<template>
  <el-dialog v-model="visibleProxy" title="编辑便签" width="650px" align-center :show-close="false">
    <template #header>
      <!-- 图片区域 -->
      <div class="image-gallery">
        <div v-for="(image, index) in localData.imgList" :key="index" class="image-container">
          <el-image :src="`/assets/Notesimages/${image}`" alt="test image" class="image"
            :preview-src-list="localData.imgList.map(img => `/assets/Notesimages/${img}`)" :initial-index="index" />
          <el-button circle type="danger" size="small" icon="Delete" @click="deleteImage(index)" class="delete-btn" />
        </div>
      </div>
    </template>
    <!-- 修改标题 -->
    <el-input v-model="localData.title" placeholder="请输入标题"></el-input>
    <!-- 便签内容 -->
    <div>
      <el-input v-model="localData.text" style="width: 619px;margin-top: 5px;margin-bottom: 8px;"
        :autosize="{ minRows: 6, maxRows: 20 }" type="textarea" placeholder="请输入内容" />
    </div>

    <!-- 标签 -->
    <div style="margin-bottom: -10px;">
      <el-tag v-for="(tag, index) in localData.tagList" :key="index" closable type="primary" effect="plain"
        @close="removeTag(index)" round>
        {{ tag }}
      </el-tag>
    </div>

    <template #footer>
      <!-- 底部按钮 -->
      <div class="footer-buttons">
        <div>
          <el-button circle title="添加图片" icon="Picture" @click="AddImg" />
          <el-button circle title="添加标签" icon="CollectionTag" @click="AddTag" />
        </div>
        <el-color-picker v-model="localData.color" title="修改背景色" show-alpha @change="handleColorChange" />
      </div>
      <div>
        <el-button @click="close">关闭</el-button>
      </div>
    </template>
    <el-dialog title="添加标签" v-model="centerDialogVisible" width="30%" :show-close="false" append-to-body align-center>
      <div>
        <el-table :data="tagList" @selection-change="handleSelectionChange">
          <el-table-column type="selection" width="55">
          </el-table-column>
          <el-table-column prop="tag" label="标签名称">
          </el-table-column>
        </el-table>
        <div style="text-align: right; margin-top: 10px;">
          <el-button @click="addSelectedTags">添加选中标签</el-button>
        </div>
      </div>
    </el-dialog>
  </el-dialog>
</template>

<script setup lang="ts">
import { ref, computed, watch, defineProps, defineEmits, onMounted } from 'vue';
import axios from "axios";
import { ElMessage } from 'element-plus';
const centerDialogVisible = ref(false);

// 定义接收的 props
const props = defineProps({
  data: Object,
  visible: Boolean,
});

// 定义 emit 事件
const emit = defineEmits(['update:visible', 'close', 'refreshNotes']);

// 创建一个 computed 代理 visible
const visibleProxy = computed({
  get: () => props.visible,
  set: (value: boolean) => {
    emit('update:visible', value);
  }
});

// 其他逻辑，如处理数据、保存、关闭等
const localData = ref({ ...props.data });

// 监听 props.data 的变化
watch(
  () => props.data,
  (newValue) => {
    if (newValue) {
      localData.value = { ...newValue };
    }
  },
  { deep: true, immediate: true }
);

const close = async () => {
  await save();
  emit('update:visible', false);
  emit('close');
};

const save = async () => {
  try {
    if (localData.value.id) {
      // 如果存在 id，说明是更新便签
      const response = await axios.put('http://localhost:8080/Notes/NotesUpdate', localData.value);
      if (response.data.code === "200") {
        console.log('便签更新成功', response.data);
        emit('refreshNotes');  // 刷新便签列表
      } else {
        console.error('更新失败', response.data);
      }
    } else {
      // 如果没有 id，说明是添加便签
      const response = await axios.post('http://localhost:8080/Notes/NotesAdd', localData.value);
      if (response.data.code === "200") {
        console.log('便签添加成功', response.data);
        emit('refreshNotes');  // 刷新便签列表
      } else {
        console.error('添加失败', response.data);
      }
    }
  } catch (error) {
    console.error('请求错误', error);
  }
};

// 添加标签
const AddTag = () => {
  centerDialogVisible.value = true;
  fetchTags();  // 在点击添加标签按钮时获取最新的标签数据
  console.log("添加标签事件");
};

//图片添加按钮
const AddImg = () => {
  // 创建一个隐藏的文件上传按钮
  const input = document.createElement('input');
  input.type = 'file';
  input.accept = 'image/*';

  // 监听文件上传事件
  input.addEventListener('change', handleImageUpload);

  // 模拟点击上传按钮
  input.click();
};

const handleImageUpload = (event: Event) => {
  const input = event.target as HTMLInputElement;
  const file = input.files ? input.files[0] : null;

  if (file) {
    const formData = new FormData();
    formData.append('file', file);
    formData.append('id', localData.value.id); // 传递便签的 ID

    // 上传文件到服务器
    axios.post('http://localhost:8080/Notes/uploadImage', formData)
      .then((response) => {
        if (response.data.code === "200") {
          const uniqueFileName = response.data.data.fileName; // 获取重命名后的图片名字
          if (uniqueFileName) {
            localData.value.imgList.push(uniqueFileName); // 将上传的图片添加到图片列表中
            ElMessage.success('图片上传成功'); // 确保传递正确的参数
          } else {
            console.error('图片上传失败，未获取到文件名', response.data);
            alert('图片上传失败，请重试');
          }
        } else {
          console.error('图片上传失败', response.data);
          alert('图片上传失败，请重试');
        }
      })
      .catch((error) => {
        console.error('图片上传失败', error);
        alert('图片上传失败，请检查网络连接');
      });
  } else {
    console.error('未选择文件');
    alert('请选择要上传的图片');
  }
};

// 删除图片
const deleteImage = (index: number) => {
  localData.value.imgList.splice(index, 1);
};

// 处理颜色变化
const handleColorChange = (color: string) => {
  localData.value.color = color;
};

//删除标签
const removeTag = (index: number) => {
  localData.value.tagList.splice(index, 1);
};

const tagList = ref([]);
const selectedTags = ref([]);

// 获取标签列表
const fetchTags = async () => {
  try {
    const response = await axios.get('http://localhost:8080/Tag/Taglist');
    if (response.data.code === "200") {
      tagList.value = response.data.data;  // 更新标签列表
    } else {
      console.error('获取标签失败', response.data);
    }
  } catch (error) {
    console.error('请求错误', error);
  }
};

// 在组件挂载时获取标签列表
onMounted(() => {
  fetchTags();
});

// 处理复选框选中变化
const handleSelectionChange = (val: any) => {
  selectedTags.value = val;
};

// 添加选中的标签到便签
const addSelectedTags = () => {
  let duplicateTags = [];
  selectedTags.value.forEach((tag: any) => {
    if (!localData.value.tagList.includes(tag.tag)) {
      localData.value.tagList.push(tag.tag);
    } else {
      duplicateTags.push(tag.tag);
    }
  });
  if (duplicateTags.length > 0) {
    ElMessage({
      message: `标签 ${duplicateTags.join(', ')} 已存在，无法重复添加`,
      type: 'warning',
    });
  }
  centerDialogVisible.value = false;
};
</script>


<style scoped>
/* 图片区域 */
.image-gallery {
  display: flex;
  flex-wrap: wrap;
  /* 换行支持 */
  justify-content: center;
  /* 水平居中 */
  align-items: center;
  /* 垂直居中 */
  gap: 10px;
  /* 图片间距 */
  padding: 10px;
}

/* 每张图片容器 */
.image-container {
  flex-shrink: 0;
  /* 图片不缩小 */
  max-width: 619px;
  /* 限制图片最大宽度 */
  position: relative;
  display: flex;
  justify-content: center;
  /* 图片居中 */
  align-items: center;
  /* 图片居中 */
}

/* 图片样式 */
.image {
  width: auto;
  max-width: 100%;
  /* 图片宽度限制为容器的宽度 */
  max-height: 350px;
  /* 图片最高限制 */
  border-radius: 4px;
  /* 圆角样式 */
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.2);
  /* 图片阴影 */
}

/* 隐藏按钮 */
.delete-btn {
  position: absolute;
  bottom: 10px;
  right: 10px;
  display: none;
  z-index: 10;
  transition: opacity 0.3s ease-in-out;
}

/* 鼠标悬停时显示按钮 */
.image-container:hover .delete-btn {
  display: inline-flex;
}

/* 底部按钮容器 */
.footer-buttons {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 10px; 
}
</style>
