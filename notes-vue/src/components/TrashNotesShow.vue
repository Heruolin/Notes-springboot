<template>
    <div class="flex card-container">
        <div v-for="item in list" :key="item.id" class="card-item">
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
                        <el-tag v-for="(tag, index) in item.tagList" :key="index" type="info">
                            {{ tag }}
                        </el-tag>
                    </div>
                    <div class="flex justify-start gap-2" style="margin-top: 10px;">
                        <el-button circle title="还原" icon="RefreshRight" type="success" @click.stop="restore(item.id)" />
                        <el-button circle title="删除" icon="Delete" type="danger" @click.stop="confirmDelete(item.id)" />
                    </div>
                </template>
            </el-card>
        </div>
        <div class="flex justify-between">
            <preview-list :list="list" />
        </div>
    </div>
</template>

<script setup lang="ts">
import { ref, onMounted, defineEmits } from "vue";
import axios from "axios";
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

// 定义事件
const emit = defineEmits(['noteRestored']);

// 初始化 list
const list = ref<Notecard[]>(props.notes);

// 还原便签事件
const restore = async (id: number) => {
    try {
        const response = await axios.put(`http://localhost:8080/Notes/Trash/Restore`, null, {
            params: { id }
        });
        if (response.data && response.data.code === '200') {
            // 从列表中移除已还原的便签
            list.value = list.value.filter(item => item.id !== id);
            ElMessage.success(`便签还原成功`);
            // 触发父组件的事件，通知便签已还原
            emit('noteRestored', id);
        } else {
            console.error("Failed to restore note:", response.data.message);
        }
    } catch (error) {
        console.error("Error restoring note:", error);
    }
};

// 确认删除便签事件
const confirmDelete = (id: number) => {
    ElMessageBox.confirm(
        '此操作将永久删除该便签, 是否继续?',
        '提示',
        {
            confirmButtonText: '确定',
            cancelButtonText: '取消',
            type: 'warning',
        }
    ).then(() => {
        deleteNote(id);
    }).catch(() => {
        ElMessage.info('已取消删除');
    });
};

// 删除便签事件
const deleteNote = async (id: number) => {
    try {
        const response = await axios.delete(`http://localhost:8080/Notes/Trash/Delete`, {
            params: { id }
        });
        if (response.data && response.data.code === '200') {
            // 从列表中移除已删除的便签
            list.value = list.value.filter(item => item.id !== id);
            ElMessage.success(`便签删除成功`);
        } else {
            console.error("Failed to delete note:", response.data.message);
        }
    } catch (error) {
        console.error("Error deleting note:", error);
    }
};

// 获取数据
const fetchNotes = async () => {
    try {
        const response = await axios.get("http://localhost:8080/Notes/Trash/Noteslist");
        if (response.data && Array.isArray(response.data.data)) {
            list.value = response.data.data.map((item) => ({
                ...item,
                imgList: item.img ? item.img.split(",") : [],
                tagList: item.tag ? item.tag.split(",") : [],
            })).sort((a, b) => b.order - a.order);
            console.log("list updated:", list.value); 
        } else {
            console.error("Unexpected response format:", response.data);
        }
    } catch (error) {
        console.error("Failed to fetch notes:", error);
    }
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
</style>
