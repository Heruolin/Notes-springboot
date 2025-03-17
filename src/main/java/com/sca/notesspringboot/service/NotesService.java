package com.sca.notesspringboot.service;

import com.sca.notesspringboot.entity.Notes;

import java.util.List;

public interface NotesService {

    // 查询便签列表
    List<Notes> selectNotes();

    Notes selectById(int id);

    //添加/插入便签
    void insertNotes(Notes notes);

    //更新便签
    void updateNotes(Notes notes);

    //更新便签顺序
    void updateOrder(int id, int order);

    //删除便签
    void deleteNotes(Notes notes);

    //添加图片
    void updateImage(int id, String imagePath);

//    //根据关键字进行查询
//    List<Notes> selectNotesByKeyword(String Keyword);
//
//    //根据标签进行查询
//    List<Notes> selectNotesByTag(String tag); // 新增方法

    List<Notes> selectNotesByKeyword(String keyword);

    List<Notes> selectNotesByTag(String tag);

    // 归档便签
    void archiveNotes(int id);

    // 查询归档便签列表
    List<Notes> selectArchiveNotes();

    //还原便签（归档）
    void restoreArchivedNote(int id);

    //将便签移至回收站
    void trashNotes(int id);

    List<Notes> selectTrashNotes();

    void restoreTrashNote(int id);

    void deleteTrashNote(int id);

    List<Notes> selectNotesByUserId(int userid);

    List<Notes> selectNotesByKeywordAndUserId(String keyword, int userid);

    List<Notes> selectNotesByTagAndUserId(String tag, int userid);

    List<Notes> selectArchiveNotesByUserId(int userid);

    List<Notes> selectTrashNotesByUserId(int userid);
}
