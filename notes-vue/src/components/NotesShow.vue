<template>
    <div class="flex">
        <VueDraggable ref="el" v-model="list" :animation="150" ghost-class="ghost" class="card-container"
            @start="onStart" @update="onUpdate" @end="onEnd">
            <div v-for="item in list" :key="item.id" class="card-item" @click="openEditor(item)">
                <el-card shadow="always" :style="{ backgroundColor: item.color }" class="fixed-card">
                    <template #header>
                        <div v-for="(img, index) in item.imgList" :key="index" class="card-header">
                            <el-image :src="`/assets/Notesimages/${img}`" alt="Card image" class="image" />
                        </div>
                    </template>
                    <div>
                        <h1>{{ item.title }}</h1>
                    </div>
                    <div>
                        {{ item.text }}
                    </div>
                    <template #footer>
                        <div class="flex gap-2 mb-2">
                            <el-tag v-for="(tag, index) in item.tagList" :key="index" type="info" :class="getTagClass(item.color)">
                                {{ tag }}
                            </el-tag>
                        </div>
                        <div class="flex justify-start gap-2" style="margin-top: 10px;">
                            <el-button circle title="归档" icon="FolderAdd" @click.stop="Archive(item.id)" :class="getButtonClass(item.color)" />
                            <el-button circle title="删除便签" icon="Failed" @click.stop="confirmTrash(item.id)" :class="getButtonClass(item.color)" />
                        </div>
                    </template>
                </el-card>
            </div>
        </VueDraggable>
        <NotesEdit v-model:visible="dialogVisible" :data="selectedCard" @refreshNotes="fetchNotes" @close="closeEditor" />
        <FloatingButton @refreshNotes="fetchNotes"/>
        <div class="flex justify-between">
            <preview-list :list="list" />
        </div>
    </div>
</template>

<script setup lang="ts">
import { ref, watch, onMounted } from "vue";
import { type DraggableEvent, VueDraggable } from "vue-draggable-plus";
import axios from "axios";
import NotesEdit from "./NotesEdit.vue";
import FloatingButton from "./FloatingButton.vue";
import { ElMessage, ElMessageBox } from 'element-plus';

// 定义接口
interface Notecard {
    id: number;
    userid: number;
    title: string;
    text: string;
    tag: string;
    img: string | null;
    color: string;
    order: number;
    imgList: string[];
    tagList: string[];
}

// 接收父组件传递的 notes 属性
const props = defineProps<{ notes: Notecard[] }>();

// 初始化 list
const list = ref<Notecard[]>(props.notes);

// 监听 props.notes 的变化并更新 list
watch(() => props.notes, (newNotes) => {
  list.value = newNotes
    .map((item) => ({
      ...item,
      imgList: item.img ? item.img.split(",").filter((img) => img.trim() !== "") : [], // 过滤空图片
      tagList: item.tag ? item.tag.split(",").filter((tag) => tag.trim() !== "") : [], // 过滤空标签
    }))
    .sort((a, b) => a.order - b.order); // 按 order 排序
});

// 卡片点击事件
const dialogVisible = ref(false);

// 用于存储当前选中的便签
const selectedCard = ref<Notecard | null>(null);

// 获取数据
const fetchNotes = async () => {
    try {
        const response = await axios.get("http://localhost:8080/Notes/Noteslist");
        if (response.data && Array.isArray(response.data.data)) {
            list.value = response.data.data.map((item) => ({
                ...item,
                imgList: item.img ? item.img.split(",") : [],
                tagList: item.tag ? item.tag.split(",") : [],
            })).sort((a, b) => a.order - b.order); // 按 order 排序
            console.log("list updated:", list.value); 
        } else {
            console.error("Unexpected response format:", response.data);
        }
    } catch (error) {
        console.error("Failed to fetch notes:", error);
    }
};

// 拖拽结束事件，更新顺序
const onEnd = async () => {
    try {
        const newOrder = list.value.map((item, index) => ({
            id: item.id,
            order: index, // 设置新的顺序
        }));
        await axios.put("http://localhost:8080/Notes/UpdateOrder", newOrder);
        console.log("Order updated successfully!");
    } catch (error) {
        console.error("Failed to update order:", error);
    }
};

// 拖拽事件处理函数
const onStart = (e: DraggableEvent) => {
    console.log("start", e);
};

const onUpdate = (e: DraggableEvent) => {
    console.log("update", e);
    // 更新顺序
    list.value.forEach((item, index) => {
        item.order = index; // 每个项的 order 更新为新的顺序
    });
};

// 打开编辑页
const openEditor = (item: Notecard) => {
    selectedCard.value = { ...item }; // 克隆对象，避免直接修改原始数据
    dialogVisible.value = true;
};

// 关闭编辑页
const closeEditor = () => {
  dialogVisible.value = false;
};

// 归档事件
const Archive = async (id: number) => {
    try {
        const response = await axios.put("http://localhost:8080/Notes/Archive/Add", null, {
            params: { id }
        });
        if (response.data.code === "200") {
            ElMessage.success("便签归档成功");
            fetchNotes(); // 重新获取便签列表
        } else {
            ElMessage.error("便签归档失败");
        }
    } catch (error) {
        ElMessage.error("便签归档失败，请检查网络连接");
    }
};

// 确认删除便签事件
const confirmTrash = (id: number) => {
    ElMessageBox.confirm(
        '是否将便签放入回收站（便签将在回收站存放10天）?',
        '提示',
        {
            confirmButtonText: '确定',
            cancelButtonText: '取消',
            type: 'warning',
        }
    ).then(() => {
        Trash(id);
    }).catch(() => {
        ElMessage.info('已取消删除');
    });
};

// 删除便签事件
const Trash = async (id: number) => {
    try {
        const response = await axios.put("http://localhost:8080/Notes/Trash/Add", null, {
            params: { id }
        });
        if (response.data.code === "200") {
            ElMessage.success("便签已移到回收站");
            fetchNotes(); // 重新获取便签列表
        } else {
            ElMessage.error("便签删除失败");
        }
    } catch (error) {
        ElMessage.error("便签删除失败，请检查网络连接");
    }
};

// 判断颜色是否为白色
const isWhiteColor = (color: string) => {
    const whiteColors = ['#ffffff', 'rgb(255, 255, 255)', 'rgba(255, 255, 255, 1)'];
    return whiteColors.includes(color.toLowerCase());
};

// 获取标签样式
const getTagClass = (color: string) => {
    return isWhiteColor(color) ? '' : 'transparent-tag';
};

// 获取按钮样式
const getButtonClass = (color: string) => {
    return isWhiteColor(color) ? '' : 'transparent-button';
};

// 初始化时加载数据
onMounted(() => {
    fetchNotes();
});
</script>

<style scoped>
.card-container {
    display: flex;
    flex-wrap: wrap;
    gap: 16px;
    justify-content: center;
    padding: 16px;
}

.card-item {
    flex: 1 1 250px; /* 固定宽度 */
    max-width: 250px; /* 固定宽度 */
    box-sizing: border-box;
}

.ghost {
    opacity: 0.5;
    background: #c8ebfb;
}

.card-header {
    text-align: center;
}

/* 透明背景色样式 */
.transparent-tag {
    background-color: rgba(255, 255, 255, 0.5) !important;
    border: 1px solid rgba(0, 0, 0, 0.1);
}

.transparent-button {
    background-color: rgba(255, 255, 255, 0.5) !important;
    border: none !important;
}
</style>
