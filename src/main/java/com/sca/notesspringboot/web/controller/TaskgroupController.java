package com.sca.notesspringboot.web.controller;

import com.sca.notesspringboot.common.Result;
import com.sca.notesspringboot.entity.Taskgroup;
import com.sca.notesspringboot.service.TaskgroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("Taskgroup")
public class TaskgroupController {

    @Autowired
    TaskgroupService taskgroupService;

    // 查询所有任务组
    @GetMapping("/Taskgrouplist")
    public Result selectTaskgroup(@RequestParam int userid) {
        List<Taskgroup> list = taskgroupService.selectTaskgroupsByUserId(userid);
        return Result.success(list);
    }

    // 添加任务组
    @PostMapping("/TaskgroupAdd")
    public Result insertTaskgroup(@RequestBody Taskgroup taskgroup) {
        taskgroupService.insertTaskgroup(taskgroup);
        return Result.success(taskgroup.getId()); // 返回新任务组的 ID
    }

    // 更新任务组
    @PutMapping("/TaskgroupUpdate")
    public Result updateTaskgroup(@RequestBody Taskgroup taskgroup) {
        taskgroupService.updateTaskgroup(taskgroup);
        return Result.success("任务组更新成功");
    }

    // 更新任务组顺序
    @PutMapping("/UpdateOrder")
    public Result updateTaskgroupOrder(@RequestBody List<Taskgroup> taskgroups) {
        taskgroupService.updateTaskgroupOrder(taskgroups);
        return Result.success("任务组顺序更新成功");
    }

    // 更新任务组锁定状态
    @PutMapping("/UpdateLock")
    public Result updateTaskgroupLock(@RequestBody Taskgroup taskgroup) {
        try {
            taskgroupService.updateTaskgroupLock(taskgroup.getId(), taskgroup.getLock());
            return Result.success("任务组锁定状态更新成功");
        } catch (Exception e) {
            return Result.error("任务组锁定状态更新失败: " + e.getMessage());
        }
    }

    // 删除任务组
    @DeleteMapping("/TaskgroupDelete")
    public Result deleteTaskgroup(@RequestParam int id) {
        taskgroupService.deleteTaskgroup(id);
        return Result.success("任务组删除成功");
    }

    // 删除任务组（放入回收站）
    @PutMapping("/Trash/Add")
    public Result trashTaskgroup(@RequestParam int id) {
        try {
            taskgroupService.trashTaskgroup(id);
            return Result.success("任务组已移到回收站");
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error("任务组删除失败: " + e.getMessage());
        }
    }

    // 还原删除的任务组
    @PutMapping("/Trash/Restore")
    public Result restoreTrashTaskgroup(@RequestParam int id) {
        try {
            taskgroupService.restoreTrashTaskgroup(id);
            return Result.success("任务组还原成功");
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error("任务组还原失败: " + e.getMessage());
        }
    }

    // 彻底删除任务组
    @DeleteMapping("/Trash/Delete")
    public Result deleteTrashTaskgroup(@RequestParam int id) {
        try {
            taskgroupService.deleteTrashTaskgroup(id);
            return Result.success("任务组彻底删除成功");
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error("任务组彻底删除失败: " + e.getMessage());
        }
    }

    // 查询回收站内所有任务组
    @GetMapping("/Trash/Taskgrouplist")
    public Result selectTrashTaskgroups() {
        List<Taskgroup> list = taskgroupService.selectTrashTaskgroups();
        return Result.success(list);
    }

    // 归档任务组
    @PutMapping("/Archive/Add")
    public Result archiveTaskgroup(@RequestParam int id) {
        try {
            taskgroupService.archiveTaskgroup(id);
            return Result.success("任务组归档成功");
        } catch (Exception e) {
            return Result.error("任务组归档失败: " + e.getMessage());
        }
    }

    // 还原归档任务组
    @PutMapping("/Archive/Restore")
    public Result restoreArchivedTaskgroup(@RequestParam int id) {
        try {
            taskgroupService.restoreArchivedTaskgroup(id);
            return Result.success("任务组还原成功");
        } catch (Exception e) {
            return Result.error("任务组还原失败: " + e.getMessage());
        }
    }

    // 查询归档内所有任务组
    @GetMapping("/Archive/Taskgrouplist")
    public Result selectArchiveTaskgroups() {
        List<Taskgroup> list = taskgroupService.selectArchiveTaskgroups();
        return Result.success(list);
    }
}
