package com.sca.notesspringboot.service.impl;

import com.sca.notesspringboot.entity.Taskgroup;
import com.sca.notesspringboot.mapper.TaskgroupMapper;
import com.sca.notesspringboot.mapper.TaskMapper; // 导入 TaskMapper
import com.sca.notesspringboot.service.TaskgroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TaskgroupServiceImpl implements TaskgroupService {

    @Autowired
    private TaskgroupMapper taskgroupMapper;

    @Autowired
    private TaskMapper taskMapper; // 注入 TaskMapper

    @Override
    public List<Taskgroup> selectTaskgroup() {
        return taskgroupMapper.selectList(null);
    }

    @Override
    public void insertTaskgroup(Taskgroup taskgroup) {
        taskgroupMapper.insert(taskgroup);
    }

    @Override
    public void updateTaskgroup(Taskgroup taskgroup) {
        taskgroupMapper.updateById(taskgroup);
    }

    @Override
    public void updateTaskgroupOrder(List<Taskgroup> taskgroups) {
        for (Taskgroup taskgroup : taskgroups) {
            taskgroupMapper.updateTaskgroupOrder(taskgroup);
        }
    }

    @Override
    public void deleteTaskgroup(int id) {
        taskgroupMapper.deleteById(id);
    }

    @Override
    public void archiveTaskgroup(int id) {
        Taskgroup taskgroup = taskgroupMapper.selectById(id);
        if (taskgroup != null) {
            taskgroup.setStatus("archived");
            taskgroupMapper.updateById(taskgroup);
        } else {
            throw new RuntimeException("任务组不存在");
        }
    }

    @Override
    public void restoreArchivedTaskgroup(int id) {
        Taskgroup taskgroup = taskgroupMapper.selectById(id);
        if (taskgroup != null && "archived".equals(taskgroup.getStatus())) {
            taskgroup.setStatus("active");
            taskgroupMapper.updateById(taskgroup);
        } else {
            throw new RuntimeException("归档任务组不存在");
        }
    }

    @Override
    public List<Taskgroup> selectArchiveTaskgroups() {
        return taskgroupMapper.selectArchiveTaskgroups();
    }

    @Override
    public void trashTaskgroup(int id) {
        Taskgroup taskgroup = taskgroupMapper.selectById(id);
        if (taskgroup != null) {
            taskgroup.setStatus("trashed");
            taskgroupMapper.updateById(taskgroup);
        } else {
            throw new RuntimeException("任务组不存在");
        }
    }

    @Override
    public void restoreTrashTaskgroup(int id) {
        Taskgroup taskgroup = taskgroupMapper.selectById(id);
        if (taskgroup != null && "trashed".equals(taskgroup.getStatus())) {
            taskgroup.setStatus("active");
            taskgroupMapper.updateById(taskgroup);
        } else {
            throw new RuntimeException("回收站任务组不存在");
        }
    }

    @Override
    public void deleteTrashTaskgroup(int id) {
        // 删除任务组内的所有任务
        taskMapper.deleteTasksByTaskgroupId(id);
        // 删除任务组
        taskgroupMapper.deleteById(id);
    }

    @Override
    public List<Taskgroup> selectTrashTaskgroups() {
        return taskgroupMapper.selectTrashTaskgroups();
    }
}
