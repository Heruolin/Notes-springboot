<template>
  <div class="common-layout">
    <el-container>
      <!-- 头部 -->
      <el-header>
        <div>
          <el-radio-group v-model="isCollapse" style="margin-bottom: 20px">
            <el-button
              @click="toggleCollapse"
              style="display: flex; align-items: center; margin-left: -7px;"
              size="large"
              circle 
            >
              <el-icon v-if="isCollapse" size="large"><Expand /></el-icon>
              <el-icon v-else size="large"><Fold /></el-icon>
            </el-button>
          </el-radio-group>
        </div>  
        
      </el-header>
    </el-container>
    <el-container>
      <!-- 侧边栏 -->
      <el-aside style="width: 200px; min-height: 100vh;">
        <el-menu
          :default-active="$route.path"
          class="el-menu-vertical-demo"
          :collapse="isCollapse"
          @open="handleOpen"
          @close="handleClose"
          style="min-height: 100vh;"
        >
          <!-- 便签 -->
          <el-sub-menu index="notes">
            <template #title>
              <el-icon><House /></el-icon>
              <span>便签</span>
            </template>
            <el-menu-item-group>
              <el-menu-item index="all" @click="showAllNotes">
                全部
              </el-menu-item>
              <el-menu-item 
              v-for="(item,index) in menuItems"
              :key = "item.index"
              :index="item.index"
              @click="handleItemClick(item)"
              >
                {{ item.label }}
              </el-menu-item>
            </el-menu-item-group>
          </el-sub-menu>
          <!-- 清单 -->
          <el-menu-item index="list" @click="showList">
              <el-icon><Tickets /></el-icon>
              <span>清单</span>
          </el-menu-item>
          <!-- 提醒 -->
          <el-menu-item index="remind" @click="showRemind">
              <el-icon><AlarmClock /></el-icon>
              <span>提醒</span>
          </el-menu-item>
          <!-- 标签修改 -->
          <el-menu-item @click="openTagEdit">
              <el-icon><EditPen /></el-icon>
              <span>标签修改</span>
          </el-menu-item>
          <!-- 归档 -->
          <el-sub-menu index="archive">
            <template #title>
              <el-icon><Folder /></el-icon>
              <span>归档</span>
            </template>
            <el-menu-item-group>
              <el-menu-item index="archiveNotes" @click="showArchiveNotes">
                便签
              </el-menu-item>
              <el-menu-item index="archiveList" @click="showArchiveList">
                清单
              </el-menu-item>
              <el-menu-item index="archiveRemind" @click="showArchiveRemind">
                提醒
              </el-menu-item>
            </el-menu-item-group>
          </el-sub-menu>
          <!-- 垃圾桶 -->
          <el-sub-menu index="recycle">
            <template #title>
              <el-icon><Delete /></el-icon>
              <span>回收站</span>
            </template>
            <el-menu-item-group>
              <el-menu-item index="recycleNotes" @click="showTrashNotes">
                便签
              </el-menu-item>
              <el-menu-item index="recycleList" @click="showTrashList">
                清单
              </el-menu-item>
              <el-menu-item index="recycleRemind" @click="showTrashRemind">
                提醒
              </el-menu-item>
            </el-menu-item-group>
          </el-sub-menu>
        </el-menu>
      </el-aside>
    <!-- 主体 -->
      <el-main>
        <NotesShow v-if="currentView === 'notes'" :notes="filteredNotes" />
        <ArchiveNotesShow v-if="currentView === 'archiveNotes'" :notes="notes" @noteRestored="handleNoteRestored" />
        <TrashNotesShow v-if="currentView === 'TrashNotes'" :notes="notes" @noteRestored="handleNoteRestored" />
      </el-main>
    </el-container>
    <TagEdit v-model:visible="tagEditVisible" @refreshTags="fetchTags" />
  </div>
</template>

<script lang="ts" setup>

import { ref, onMounted } from 'vue'
import {
  EditPen,
  House,
  Folder,
  Delete,
  Expand,
  Fold,
  Tickets,
  AlarmClock
} from '@element-plus/icons-vue'
import { ElMessage } from 'element-plus'
import axios from 'axios'
import NotesShow from '@/components/NotesShow.vue';
import TagEdit from '@/components/TagEdit.vue';
import ArchiveNotesShow from '@/components/ArchiveNotesShow.vue';
import TrashNotesShow from '@/components/TrashNotesShow.vue';

// 折叠状态
const isCollapse = ref(true);

// 标签编辑对话框的显示状态
const tagEditVisible = ref(false);

// 当前显示的视图
const currentView = ref('notes');

// 切换折叠状态
const toggleCollapse = () => {
  isCollapse.value = !isCollapse.value;
};

// 打开标签编辑对话框
const openTagEdit = () => {
  tagEditVisible.value = true;
};

// 动态菜单数据
const menuItems = ref([]);

// 便签数据
const notes = ref([]);

// 筛选后的便签数据
const filteredNotes = ref([]);

// 获取标签数据
const fetchTags = async () => {
  try {
    const response = await axios.get('http://localhost:8080/Tag/Taglist')
    if (response.data.code === '200') {
      menuItems.value = response.data.data.map((tag: { id: number; tag: string }) => ({
        index: `2-${tag.id}`,
        label: tag.tag,
        action: () => filterNotesByTag(tag.tag)
      }))
    } else {
      ElMessage.error('获取标签数据失败')
    }
  } catch (error) {
    ElMessage.error('获取标签数据失败，请检查网络连接')
  }
}

// 获取便签数据
const fetchNotes = async () => {
  try {
    const response = await axios.get('http://localhost:8080/Notes/Noteslist')
    if (response.data.code === '200') {
      notes.value = response.data.data.map((item) => ({
        ...item,
        imgList: item.img ? item.img.split(",").filter((img) => img.trim() !== "") : [], // 过滤空图片
        tagList: item.tag ? item.tag.split(",").filter((tag) => tag.trim() !== "") : [], // 过滤空标签
      }));
      filteredNotes.value = [...notes.value] // 默认显示全部便签
    } else {
      ElMessage.error('获取便签数据失败')
    }
  } catch (error) {
    ElMessage.error('获取便签数据失败，请检查网络连接')
  }
}

// 在组件挂载时获取标签和便签列表
onMounted(() => {
  fetchTags();
  fetchNotes();
});

// 显示全部便签
const showAllNotes = () => {
  filteredNotes.value = [...notes.value];
  currentView.value = 'notes';
};

// 根据标签筛选便签
const filterNotesByTag = (tag) => {
  filteredNotes.value = notes.value.filter(note => note.tagList.includes(tag));
  currentView.value = 'notes';
};

// 显示清单
const showList = () => {
  currentView.value = 'list';
};

// 显示提醒
const showRemind = () => {
  currentView.value = 'remind';
};

// 显示归档便签
const showArchiveNotes = () => {
  currentView.value = 'archiveNotes';
};

// 显示归档清单
const showArchiveList = () => {
  currentView.value = 'archiveList';
};

// 显示归档提醒
const showArchiveRemind = () => {
  currentView.value = 'archiveRemind';
};

// 显示回收站便签
const showTrashNotes = () => {
  currentView.value = 'TrashNotes';
};

// 显示回收站清单
const showTrashList = () => {
  currentView.value = 'TrashList';
};

// 显示回收站提醒
const showTrashRemind = () => {
  currentView.value = 'TrashRemind';
};

// 处理点击事件
const handleItemClick = (item) => {
  if (item.action) {
    item.action(); // 调用数据中定义的 action 函数
  } else {
    console.log(`${item.label} 被点击，但未定义 action`);
  }
};

// 处理便签还原事件
const handleNoteRestored = (id: number) => {
  // 从归档便签列表中移除已还原的便签
  notes.value = notes.value.filter(note => note.id !== id);
  // 重新获取便签数据
  fetchNotes();
};

const handleOpen = (key: string, keyPath: string[]) => {
  console.log(key, keyPath)
}
const handleClose = (key: string, keyPath: string[]) => {
  console.log(key, keyPath)
}
</script>