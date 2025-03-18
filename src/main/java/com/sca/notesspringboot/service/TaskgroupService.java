package com.sca.notesspringboot.service;

import com.sca.notesspringboot.entity.Taskgroup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface TaskgroupService {

    List<Taskgroup> selectTaskgroup();

    void insertTaskgroup(Taskgroup taskgroup);

    void updateTaskgroup(Taskgroup taskgroup);

    void updateTaskgroupOrder(List<Taskgroup> taskgroups);

    void deleteTaskgroup(int id);

    void archiveTaskgroup(int id);

    void restoreArchivedTaskgroup(int id);

    List<Taskgroup> selectArchiveTaskgroups();

    void trashTaskgroup(int id);

    void restoreTrashTaskgroup(int id);

    void deleteTrashTaskgroup(int id);

    List<Taskgroup> selectTrashTaskgroups();

    List<Taskgroup> selectTaskgroupsByUserId(int userid);
}
