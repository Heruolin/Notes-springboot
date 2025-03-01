package com.sca.notesspringboot.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sca.notesspringboot.entity.Task;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface TaskMapper extends BaseMapper<Task> {

    @Select("SELECT * FROM task WHERE taskgroup_id = #{taskgroupId}")
    List<Task> selectTasksByTaskgroupId(int taskgroupId);

    // 删除任务组内的所有任务
    @Delete("DELETE FROM task WHERE taskgroup_id = #{taskgroupId}")
    void deleteTasksByTaskgroupId(int taskgroupId);
}
