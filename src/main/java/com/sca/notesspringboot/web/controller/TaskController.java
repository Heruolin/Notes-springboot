package com.sca.notesspringboot.web.controller;

import com.sca.notesspringboot.common.Result;
import com.sca.notesspringboot.entity.Task;
import com.sca.notesspringboot.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("Task")
public class TaskController {

    @Autowired
    TaskService taskService;

    // 查询指定任务组的所有任务
    @GetMapping("/Tasklist")
    public Result selectTasksByTaskgroupId(@RequestParam("taskgroupId") int taskgroupId) {
    try {
        List<Task> list = taskService.selectTasksByTaskgroupId(taskgroupId);
        return Result.success(list);
    } catch (Exception e) {
        return Result.error("查询任务失败: " + e.getMessage());
    }
}

    // 添加任务
    @PostMapping("/TaskAdd")
    public Result insertTask(@RequestBody Task task) {
        taskService.insertTask(task);
        return Result.success("任务添加成功");
    }

    // 更新任务
    @PutMapping("/TaskUpdate")
    public Result updateTask(@RequestBody Task task) {
        taskService.updateTask(task);
        return Result.success("任务更新成功");
    }

    // 删除任务
    @DeleteMapping("/TaskDelete")
    public Result deleteTask(@RequestParam("id") int id) {
        taskService.deleteTask(id);
        return Result.success("任务删除成功");
    }
}
