package com.sca.notesspringboot.service.impl;

import com.sca.notesspringboot.entity.Task;
import com.sca.notesspringboot.mapper.TaskMapper;
import com.sca.notesspringboot.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

import java.util.List;

@Service
public class TaskServiceImpl implements TaskService {

    @Autowired
    private TaskMapper taskMapper;

    @Override
    public List<Task> selectTasksByTaskgroupId(int taskgroupId) {
    List<Task> tasks = taskMapper.selectList(new QueryWrapper<Task>().eq("taskgroup_id", taskgroupId));
    // 打印任务列表以进行调试
    System.out.println("Tasks for taskgroup " + taskgroupId + ": " + tasks);
    return tasks;
}

    @Override
    public void insertTask(Task task) {
        taskMapper.insert(task);
    }

    @Override
    public void updateTask(Task task) {
        taskMapper.updateById(task);
    }

    @Override
    public void deleteTask(int id) {
        taskMapper.deleteById(id);
    }
}
