package com.sca.notesspringboot.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sca.notesspringboot.entity.Taskgroup;
import org.apache.ibatis.annotations.*;

import java.time.LocalDateTime;
import java.util.List;

@Mapper
public interface TaskgroupMapper extends BaseMapper<Taskgroup> {

    @Select("SELECT * FROM taskgroup WHERE status != 'trashed' ORDER BY `order`")
    List<Taskgroup> selectTaskgroup();

    @Insert("INSERT INTO taskgroup (userid, title, `order`) VALUES (#{userid}, #{title}, #{order})")
    void insertTaskgroup(Taskgroup taskgroup);

    @Update("UPDATE taskgroup SET userid = #{userid}, title = #{title}, `order` = #{order} WHERE id = #{id}")
    void updateTaskgroup(Taskgroup taskgroup);

    @Update("UPDATE taskgroup SET `order` = #{taskgroup.order} WHERE id = #{taskgroup.id}")
    void updateTaskgroupOrder(@Param("taskgroup") Taskgroup taskgroup);

    @Delete("DELETE FROM taskgroup WHERE id = #{id}")
    void deleteTaskgroup(int id);

    // 查询归档任务组列表，按 id 降序排列
    @Select("SELECT * FROM taskgroup WHERE status = 'archived' ORDER BY id DESC")
    List<Taskgroup> selectArchiveTaskgroups();

    // 查询回收站任务组列表，按 id 降序排列
    @Select("SELECT * FROM taskgroup WHERE status = 'trashed' ORDER BY id DESC")
    List<Taskgroup> selectTrashTaskgroups();

    // 查询回收站任务组
    @Select("SELECT * FROM taskgroup WHERE id = #{id} AND status = 'trashed'")
    Taskgroup selectTrashTaskgroupById(@Param("id") int id);

    // 更新任务组状态
    @Update("UPDATE taskgroup SET status = #{status} WHERE id = #{id}")
    void updateTaskgroupStatus(@Param("id") int id, @Param("status") String status);

    // 更新任务组状态和 trash_time
    @Update("UPDATE taskgroup SET status = #{status}, trash_time = #{trashTime} WHERE id = #{id}")
    void updateTaskgroupStatusAndTrashTime(@Param("id") int id, @Param("status") String status, @Param("trashTime") LocalDateTime trashTime);

    // 重置任务组的 trash_time
    @Update("UPDATE taskgroup SET trash_time = NULL WHERE id = #{id}")
    void resetTaskgroupTrashTime(@Param("id") int id);
}
