<template>
    <div class="flex">
        <VueDraggable ref="el" v-model="list" :animation="150" ghost-class="ghost" class="card-container"
            @start="onStart" @update="onUpdate" @end="onEnd">
            <div v-for="item in list" :key="item.id" class="card-item" @click="openEditor(item)">
                <el-card shadow="always" :style="{ backgroundColor: item.color }">
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
                            <el-tag v-for="(tag, index) in item.tagList" :key="index" type="info">
                                {{ tag }}
                            </el-tag>
                        </div>
                        <div class="flex justify-start gap-2">
                            <el-button circle title="归档" icon="FolderAdd" @click.stop="Archive(item.id)" />
                            <el-button circle title="删除便签" icon="Failed" @click.stop="Failed(item.id)" />
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
import { ref, onMounted } from "vue";
import { type DraggableEvent, VueDraggable } from "vue-draggable-plus";
import axios from "axios";
import NotesEdit from "./NotesEdit.vue";
import FloatingButton from "./FloatingButton.vue";
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

// 初始化 list
const list = ref<Notecard[]>([]);

// 卡片点击事件
const dialogVisible = ref(false)

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
            })).sort((a, b) => a.order - b.order);
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
            order: index,
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

const onUpdate = () => {
    console.log("update");
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
const Archive = (id: number) => {
    console.log(`归档事件 for id: ${id}`);
};

// 删除便签事件
const Failed = (id: number) => {
    console.log(`便签删除事件 for id: ${id}`);
};


// 初始化时加载数据
onMounted(() => {
    fetchNotes();
});


</script>

<style scoped>
/* 样式保持不变 */
.card-container {
    display: flex;
    flex-wrap: wrap;
    gap: 16px;
    justify-content: center;
    padding: 16px;
}

.card-item {
    flex: 1 1 calc(25% - 8px);
    max-width: calc(25% - 18px);
    box-sizing: border-box;
}

.ghost {
    opacity: 0.5;
    background: #c8ebfb;
}

.card-header {
    text-align: center;
}
</style>
