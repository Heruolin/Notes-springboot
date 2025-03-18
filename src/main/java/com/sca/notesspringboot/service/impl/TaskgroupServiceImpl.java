package com.sca.notesspringboot.service.impl;

import com.sca.notesspringboot.entity.Taskgroup;
import com.sca.notesspringboot.mapper.TaskgroupMapper;
import com.sca.notesspringboot.mapper.TaskMapper; // 导入 TaskMapper
import com.sca.notesspringboot.service.TaskgroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
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
            taskgroup.setTrashTime(LocalDateTime.now()); // 设置 trash_time
            taskgroupMapper.updateTaskgroupStatusAndTrashTime(id, "trashed", LocalDateTime.now());
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
            taskgroupMapper.resetTaskgroupTrashTime(id); // 重置 trash_time
        } else {
            throw new RuntimeException("回收站任务组不存在");
        }
    }

    @Override
    public void deleteTrashTaskgroup(int id) {
        taskMapper.deleteTasksByTaskgroupId(id);
        taskgroupMapper.deleteById(id);
    }

    @Override
    public List<Taskgroup> selectTrashTaskgroups() {
        return taskgroupMapper.selectTrashTaskgroups();
    }

    @Override
    public List<Taskgroup> selectTaskgroupsByUserId(int userid) {
        QueryWrapper<Taskgroup> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("userid", userid).orderByAsc("`order`"); // 根据用户ID查询并按顺序排序
        return taskgroupMapper.selectList(queryWrapper);
    }

    // 定时任务，每天凌晨 2 点执行一次
    @Scheduled(cron = "0 0 2 * * ?")
    public void deleteExpiredTrashTaskgroups() {
        List<Taskgroup> trashTaskgroups = taskgroupMapper.selectTrashTaskgroups();
        LocalDateTime now = LocalDateTime.now();
        for (Taskgroup taskgroup : trashTaskgroups) {
            LocalDateTime trashTime = taskgroup.getTrashTime(); // 获取任务组被移到回收站的时间
            if (trashTime != null && ChronoUnit.DAYS.between(trashTime, now) >= 10) {
                taskMapper.deleteTasksByTaskgroupId(taskgroup.getId()); // 删除任务组内的所有任务
                taskgroupMapper.deleteById(taskgroup.getId()); // 删除超过十日的任务组
            }
        }
    }
}
