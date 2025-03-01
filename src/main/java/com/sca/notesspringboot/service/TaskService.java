package com.sca.notesspringboot.service;

import com.sca.notesspringboot.entity.Task;

import java.util.List;

public interface TaskService {

    List<Task> selectTasksByTaskgroupId(int taskgroupId);

    void insertTask(Task task);

    void updateTask(Task task);

    void deleteTask(int id);
}
