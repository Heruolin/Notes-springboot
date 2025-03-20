package com.sca.notesspringboot.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sca.notesspringboot.entity.Remind;
import org.apache.ibatis.annotations.*;

import java.time.LocalDateTime;
import java.util.List;

@Mapper
public interface RemindMapper extends BaseMapper<Remind> {

    @Update("UPDATE remind SET `order` = #{order} WHERE id = #{id}")
    void updateRemindOrder(@Param("id") int id, @Param("order") int order);

    // 插入到 archive-remind 表
    @Insert("INSERT INTO `archive-remind` (id, userid, text, remind_time, `order`, status) " +
            "SELECT id, userid, text, remind_time, `order`, status FROM remind WHERE id = #{id}")
    void insertToArchiveRemind(@Param("id") int id);

    // 从 remind 表中删除
    @Delete("DELETE FROM remind WHERE id = #{id}")
    void deleteFromRemind(@Param("id") int id);

    // 查询归档提醒列表，按 id 降序排列
    @Select("SELECT * FROM `archive-remind` ORDER BY id DESC")
    List<Remind> selectArchiveReminds();

    // 查询归档提醒
    @Select("SELECT * FROM `archive-remind` WHERE id = #{id}")
    Remind selectArchivedRemindById(@Param("id") int id);

    // 从归档插入到 remind 表
    @Insert("INSERT INTO remind (id, userid, text, remind_time, `order`, status) " +
            "SELECT id, userid, text, remind_time, `order`, status FROM `archive-remind` WHERE id = #{id}")
    void insertToRemindFromArchive(@Param("id") int id);

    // 从 archive-remind 表中删除
    @Delete("DELETE FROM `archive-remind` WHERE id = #{id}")
    void deleteFromArchiveRemind(@Param("id") int id);

    // 插入到 trash-remind 表
    @Insert("INSERT INTO `trash-remind` (id, userid, text, remind_time, `order`, trash_time, status) " +
            "SELECT id, userid, text, remind_time, `order`, NOW(), status FROM remind WHERE id = #{id}")
    void insertToTrashRemind(int id);

    // 查询回收站提醒列表，按 id 降序排列
    @Select("SELECT * FROM `trash-remind` ORDER BY id DESC")
    List<Remind> selectTrashReminds();

    // 查询回收站提醒
    @Select("SELECT * FROM `trash-remind` WHERE id = #{id}")
    Remind selectTrashRemindById(@Param("id") int id);

    // 从回收站插入到 remind 表
    @Insert("INSERT INTO remind (id, userid, text, remind_time, `order`, status) " +
            "SELECT id, userid, text, remind_time, `order`, status FROM `trash-remind` WHERE id = #{id}")
    void insertToRemindFromTrash(@Param("id") int id);

    // 从 trash-remind 表中删除
    @Delete("DELETE FROM `trash-remind` WHERE id = #{id}")
    void deleteFromTrashRemind(@Param("id") int id);

    // 更新提醒状态和 trash_time
    @Update("UPDATE remind SET status = #{status}, trash_time = #{trashTime} WHERE id = #{id}")
    void updateRemindStatusAndTrashTime(@Param("id") int id, @Param("status") String status, @Param("trashTime") LocalDateTime trashTime);

    // 重置提醒的 trash_time
    @Update("UPDATE remind SET trash_time = NULL WHERE id = #{id}")
    void resetRemindTrashTime(@Param("id") int id);

    // 查询提醒列表，按 userid 和 order 排序
    @Select("SELECT * FROM remind WHERE userid = #{userid} ORDER BY `order`")
    List<Remind> selectRemindByUserId(@Param("userid") int userid);

    // 查询回收站提醒列表，按 userid 和 id 降序排列
    @Select("SELECT * FROM `trash-remind` WHERE userid = #{userid} ORDER BY id DESC")
    List<Remind> selectTrashRemindsByUserId(@Param("userid") int userid);

    // 查询归档提醒列表，按 userid 和 id 降序排列
    @Select("SELECT * FROM `archive-remind` WHERE userid = #{userid} ORDER BY id DESC")
    List<Remind> selectArchiveRemindsByUserId(@Param("userid") int userid);

    // 更新任务组锁定状态
    @Update("UPDATE remind SET lock = #{lock} WHERE id = #{id}")
    void updateRemindLock(@Param("id") int id, @Param("lock") String lock);
}
