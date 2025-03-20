package com.sca.notesspringboot.service;

import com.sca.notesspringboot.entity.Remind;

import java.util.List;

public interface RemindService {
    List<Remind> selectRemind();
    void insertRemind(Remind remind);
    void updateRemind(Remind remind);
    void updateRemindOrder(List<Remind> reminds);
    void deleteRemind(int id);
    void trashRemind(int id);
    void restoreTrashRemind(int id);
    void deleteTrashRemind(int id);
    List<Remind> selectTrashReminds();
    void archiveRemind(int id);
    void restoreArchivedRemind(int id);
    List<Remind> selectArchiveReminds();

    List<Remind> selectRemindByUserId(int userid);

    List<Remind> selectTrashRemindsByUserId(int userid);

    List<Remind> selectArchiveRemindsByUserId(int userid);

    void updateRemindLock(int id, String lock);
}
