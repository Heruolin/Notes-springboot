<template>
    <div class="side_btn_box" :style="{ cursor: isDragging ? 'move' : 'pointer' }" id="dragElement"
         @mouseenter="showSubButtons = true" @mouseleave="showSubButtons = false">
        <el-icon>
            <Plus />
        </el-icon>
        <!-- 次级按钮 -->
        <div class="sub-btns">
            <el-button :icon="Plus" @click="handleNotes" circle class="sub-btn" />
            <el-button :icon="Tickets" @click="handleList" circle class="sub-btn" />
            <el-button :icon="AlarmClock" @click="handleRemind" circle class="sub-btn" />
        </div>
    </div>
    <NotesEdit v-model:visible="dialogVisible" @close="closeEditor" @refreshNotes="refreshNotes" />
  </template>
  
  <script setup lang="ts">
  import { ref, onMounted, defineEmits } from 'vue';
  import { Plus, Tickets, AlarmClock } from '@element-plus/icons-vue';
  import NotesEdit from "./NotesEdit.vue";
  
  const isDragging = ref(false);
  const showSubButtons = ref(false); 
  const dialogVisible = ref(false);
  
  // 定义 emit 事件
  const emit = defineEmits(['update:visible', 'close', 'refreshNotes']);
  
  const refreshNotes = () => {
      emit('refreshNotes');
  };
  
  // 添加便签按钮
  const handleNotes = () => {
      resetNoteData(); // 重置便签数据
      dialogVisible.value = true;
  };
  
  // 添加清单按钮
  const handleList = () => {
      resetNoteData(); // 重置清单数据
      dialogVisible.value = true;
  };
  // 添加提醒按钮
  const handleRemind = () => {
      resetNoteData(); // 重置提醒数据
      dialogVisible.value = true;
  };
  const closeEditor = () => {
      dialogVisible.value = false;
  };
  
  // 侧边栏拖拽功能
  const SideBtnDrag = () => {
      const dragElement = document.getElementById('dragElement') as HTMLElement;
      let startY = 0;
      let currentY = 0;
  
      function handleMouseMove(event: any) {
          isDragging.value = true;
          currentY = event.clientY;
          const distance = currentY - startY;
          let currentTop = parseInt(dragElement.style.top) || dragElement.offsetTop || 0;
          const viewportHeight = window.innerHeight;
  
          if (currentTop <= 0) { currentTop = 0; }
          else if (currentTop + 40 > viewportHeight) { currentTop = viewportHeight - 40; }
  
          dragElement.style.top = currentTop + distance + 'px';
          startY = currentY;
      }
  
      function handleMouseUp() {
          isDragging.value = false;
          document.removeEventListener('mousemove', handleMouseMove);
          document.removeEventListener('mouseup', handleMouseUp);
      }
  
      dragElement.addEventListener('mousedown', (event) => {
          startY = event.clientY;
          document.addEventListener('mousemove', handleMouseMove);
          document.addEventListener('mouseup', handleMouseUp);
      });
  };
  
  onMounted(() => {
      SideBtnDrag();
  });

  // 重置便签数据
  const resetNoteData = () => {
      // 触发 NotesEdit 组件中的 resetNoteData 方法
      const notesEditComponent = document.querySelector('NotesEdit') as any;
      if (notesEditComponent && notesEditComponent.resetNoteData) {
          notesEditComponent.resetNoteData();
      }
  };
  </script>
  

<style scoped lang="less">
.el-icon {
    color: white;
}

.side_btn_box {
    position: fixed;
    right: 0;
    width: 40px;
    height: 40px;
    border-radius: 50%;
    background-color: #409eff;
    bottom: 100px;
    z-index: 3000;
    display: flex;
    align-items: center;
    justify-content: center;
    cursor: pointer;
    transition: all 0.3s ease;

    .sub-btns {
        display: flex;
        flex-direction: column;
        gap: 8px;
        position: absolute;
        top: -150px;
        /* 次级按钮上移 */
        right: 0;
        transition: opacity 0.3s ease-in-out;
        opacity: 0;
        visibility: hidden;
        align-items: flex-start;
        /* 确保按钮左对齐 */
    }

    &:hover {
        .sub-btns {
            opacity: 1;
            visibility: visible;
        }
    }
}

.sub-btns .sub-btn {
    margin: 0;
    /* 清除默认的 margin */
    width: 40px;
    /* 确保按钮大小一致 */
    height: 40px;
    /* 确保按钮大小一致 */
    display: flex;
    justify-content: center;
    align-items: center;
}

.sub-btns el-button {
    margin: 0;
    /* 清除默认的 margin */
}
</style>