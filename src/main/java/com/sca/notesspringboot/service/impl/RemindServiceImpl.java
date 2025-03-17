package com.sca.notesspringboot.service.impl;

import com.sca.notesspringboot.entity.Remind;
import com.sca.notesspringboot.mapper.RemindMapper;
import com.sca.notesspringboot.service.RemindService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
public class RemindServiceImpl implements RemindService {

    @Autowired
    private RemindMapper remindMapper;

    @Override
    public List<Remind> selectRemind() {
        return remindMapper.selectList(null);
    }

    @Override
    public void insertRemind(Remind remind) {
        remindMapper.insert(remind);
    }

    @Override
    public void updateRemind(Remind remind) {
        remindMapper.updateById(remind);
    }

    @Override
    public void updateRemindOrder(List<Remind> reminds) {
        for (Remind remind : reminds) {
            remindMapper.updateRemindOrder(remind.getId(), remind.getOrder());
        }
    }

    @Override
    public void deleteRemind(int id) {
        remindMapper.deleteById(id);
    }

    @Override
    public void trashRemind(int id) {
        Remind remind = remindMapper.selectById(id);
        if (remind != null) {
            remind.setStatus("trashed");
            remind.setTrashTime(LocalDateTime.now()); // 设置 trash_time
            remindMapper.updateRemindStatusAndTrashTime(id, "trashed", LocalDateTime.now());
        } else {
            throw new RuntimeException("提醒不存在");
        }
    }

    @Override
    public List<Remind> selectTrashReminds() {
        return remindMapper.selectTrashReminds();
    }

    @Override
    public void restoreTrashRemind(int id) {
        Remind remind = remindMapper.selectTrashRemindById(id);
        if (remind != null) {
            remind.setStatus("active");
            remindMapper.updateById(remind);
            remindMapper.resetRemindTrashTime(id); // 重置 trash_time
        } else {
            throw new RuntimeException("回收站提醒不存在");
        }
    }

    @Override
    public void deleteTrashRemind(int id) {
        remindMapper.deleteFromTrashRemind(id);
    }

    @Override
    public void archiveRemind(int id) {
        Remind remind = remindMapper.selectById(id);
        if (remind != null) {
            remindMapper.insertToArchiveRemind(id);
            remindMapper.deleteFromRemind(id);
        } else {
            throw new RuntimeException("提醒不存在");
        }
    }

    @Override
    public void restoreArchivedRemind(int id) {
        Remind remind = remindMapper.selectArchivedRemindById(id);
        if (remind != null) {
            remindMapper.insertToRemindFromArchive(id);
            remindMapper.deleteFromArchiveRemind(id);
        } else {
            throw new RuntimeException("归档提醒不存在");
        }
    }

    @Override
    public List<Remind> selectArchiveReminds() {
        return remindMapper.selectArchiveReminds();
    }

    // 定时任务，每天凌晨 2 点执行一次
    @Scheduled(cron = "0 0 2 * * ?")
    public void deleteExpiredTrashReminds() {
        List<Remind> trashReminds = remindMapper.selectTrashReminds();
        LocalDateTime now = LocalDateTime.now();
        for (Remind remind : trashReminds) {
            LocalDateTime trashTime = remind.getTrashTime(); // 获取提醒被移到回收站的时间
            if (trashTime != null && ChronoUnit.DAYS.between(trashTime, now) >= 10) {
                remindMapper.deleteFromTrashRemind(remind.getId()); // 删除超过十日的提醒
            }
        }
    }
}



