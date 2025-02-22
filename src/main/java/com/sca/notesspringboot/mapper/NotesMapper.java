package com.sca.notesspringboot.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sca.notesspringboot.entity.Notes;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface NotesMapper extends BaseMapper<Notes> {

    @Update("UPDATE notes SET `order` = #{order} WHERE id = #{id}")
    void updateOrder(@Param("id") int id, @Param("order") int order);

    @Update("UPDATE notes SET img = #{img} WHERE id = #{id}")
    void updateImage(@Param("id") int id, @Param("img") String img);

    // 插入到 archive-notes 表
    @Insert("INSERT INTO `archive-notes` (id, userid, title, text, img, color, `order`) " +
            "SELECT id, userid, title, text, img, color, `order` FROM notes WHERE id = #{id}")
    void insertToArchiveNotes(@Param("id") int id);

    // 从 notes 表中删除
    @Delete("DELETE FROM notes WHERE id = #{id}")
    void deleteFromNotes(@Param("id") int id);

    // 查询归档便签列表，按 id 降序排列
    @Select("SELECT * FROM `archive-notes` ORDER BY id DESC")
    List<Notes> selectArchiveNotes();

    // 查询归档便签
    @Select("SELECT * FROM `archive-notes` WHERE id = #{id}")
    Notes selectArchivedNoteById(@Param("id") int id);

    // 从归档插入到 notes 表
    @Insert("INSERT INTO notes (id, userid, title, text, img, color, `order`) " +
            "SELECT id, userid, title, text, img, color, `order` FROM `archive-notes` WHERE id = #{id}")
    void insertToNotesFromArchive(@Param("id") int id);

    // 从 archive-notes 表中删除
    @Delete("DELETE FROM `archive-notes` WHERE id = #{id}")
    void deleteFromArchiveNotes(@Param("id") int id);

    // 插入到 trash-notes 表
    @Insert("INSERT INTO `trash-notes` (id, userid, title, text, tag, img, color, `order`, trash_time) " +
            "SELECT id, userid, title, text, tag, img, color, `order`, NOW() FROM notes WHERE id = #{id}")
    void insertToTrashNotes(int id);

    // 查询回收站便签列表，按 id 降序排列
    @Select("SELECT * FROM `trash-notes` ORDER BY id DESC")
    List<Notes> selectTrashNotes();

    // 查询回收站便签
    @Select("SELECT * FROM `trash-notes` WHERE id = #{id}")
    Notes selectTrashNoteById(@Param("id") int id);

    // 从回收站插入到 notes 表
    @Insert("INSERT INTO notes (id, userid, title, text, tag, img, color, `order`) " +
            "SELECT id, userid, title, text, tag, img, color, `order` FROM `trash-notes` WHERE id = #{id}")
    void insertToNotesFromTrash(@Param("id") int id);

    // 从 trash-notes 表中删除
    @Delete("DELETE FROM `trash-notes` WHERE id = #{id}")
    void deleteFromTrashNotes(@Param("id") int id);
}


