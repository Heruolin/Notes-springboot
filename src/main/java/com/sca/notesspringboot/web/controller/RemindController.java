package com.sca.notesspringboot.web.controller;

import com.sca.notesspringboot.common.Result;
import com.sca.notesspringboot.entity.Remind;
import com.sca.notesspringboot.service.RemindService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("Remind")
public class RemindController {

    @Autowired
    RemindService remindService;

    // 查询所有提醒（按用户ID）
    @GetMapping("/Remindlist")
    public Result selectRemind(@RequestParam int userid) {
        List<Remind> list = remindService.selectRemindByUserId(userid);
        return Result.success(list);
    }

    // 添加提醒
    @PostMapping("/RemindAdd")
    public Result insertRemind(@RequestBody Remind remind) {
        remindService.insertRemind(remind);
        return Result.success(remind.getId()); // 返回新提醒的 ID
    }

    // 更新提醒
    @PutMapping("/RemindUpdate")
    public Result updateRemind(@RequestBody Remind remind) {
        remindService.updateRemind(remind);
        return Result.success("提醒更新成功");
    }

    // 更新提醒顺序
    @PutMapping("/UpdateOrder")
    public Result updateRemindOrder(@RequestBody List<Remind> reminds) {
        remindService.updateRemindOrder(reminds);
        return Result.success("提醒顺序更新成功");
    }

    // 删除提醒
    @DeleteMapping("/RemindDelete")
    public Result deleteRemind(@RequestParam int id) {
        remindService.deleteRemind(id);
        return Result.success("提醒删除成功");
    }

    // 删除提醒（放入回收站）
    @PutMapping("/Trash/Add")
    public Result trashRemind(@RequestParam int id) {
        try {
            remindService.trashRemind(id);
            return Result.success("提醒已移到回收站");
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error("提醒删除失败: " + e.getMessage());
        }
    }

    // 还原删除的提醒
    @PutMapping("/Trash/Restore")
    public Result restoreTrashRemind(@RequestParam int id) {
        try {
            remindService.restoreTrashRemind(id);
            return Result.success("提醒还原成功");
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error("提醒还原失败: " + e.getMessage());
        }
    }

    // 彻底删除提醒
    @DeleteMapping("/Trash/Delete")
    public Result deleteTrashRemind(@RequestParam int id) {
        try {
            remindService.deleteTrashRemind(id);
            return Result.success("提醒彻底删除成功");
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error("提醒彻底删除失败: " + e.getMessage());
        }
    }

    // 查询回收站内所有提醒（按用户ID）
    @GetMapping("/Trash/Remindlist")
    public Result selectTrashReminds(@RequestParam int userid) {
        List<Remind> list = remindService.selectTrashRemindsByUserId(userid);
        return Result.success(list);
    }

    // 归档提醒
    @PutMapping("/Archive/Add")
    public Result archiveRemind(@RequestParam int id) {
        try {
            remindService.archiveRemind(id);
            return Result.success("提醒归档成功");
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error("提醒归档失败: " + e.getMessage());
        }
    }

    // 还原归档提醒
    @PutMapping("/Archive/Restore")
    public Result restoreArchivedRemind(@RequestParam int id) {
        try {
            remindService.restoreArchivedRemind(id);
            return Result.success("提醒还原成功");
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error("提醒还原失败: " + e.getMessage());
        }
    }

    // 查询归档内所有提醒（按用户ID）
    @GetMapping("/Archive/Remindlist")
    public Result selectArchiveReminds(@RequestParam int userid) {
        List<Remind> list = remindService.selectArchiveRemindsByUserId(userid);
        return Result.success(list);
    }
}
