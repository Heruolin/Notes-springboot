package com.sca.notesspringboot.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.sca.notesspringboot.entity.Notes;
import com.sca.notesspringboot.mapper.NotesMapper;
import com.sca.notesspringboot.service.NotesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class NotesServiceImpl implements NotesService {

    @Autowired
    private NotesMapper notesMapper;

    private void convertTagAndImgToList(List<Notes> notesList) {
        for (Notes note : notesList) {
            if (note.getTag() != null) {
                note.setTagList(Arrays.asList(note.getTag().split(",")));
            }
            if (note.getImg() != null) {
                note.setImgList(Arrays.asList(note.getImg().split(",")));
            }
            if (note.getColor() == null) {
                note.setColor("#ffffff"); // 默认白色
            }
        }
    }

    @Override
    //查询便签列表
    public List<Notes> selectNotes() {
        List<Notes> notesList = notesMapper.selectList(null);

        // 遍历 notesList，处理 tag 和 img 字段
        convertTagAndImgToList(notesList);
        return notesList;
    }

    @Override
    public Notes selectById(int id) {
        return notesMapper.selectById(id);
    }

    private List<String> convertStringToList(String str) {
        if (str == null || str.trim().isEmpty()) {
            return new ArrayList<>(); // 返回空列表
        }
        return Arrays.asList(str.split(",")); // 按逗号分隔并转换为 List
    }

    @Override
    // 添加/插入便签
    public void insertNotes(Notes notes) {
        // 将 tagList 和 imgList 转换为字符串
        if (notes.getTagList() != null) {
            notes.setTag(String.join(",", notes.getTagList())); // 转换为逗号分隔字符串
        }
        if (notes.getImgList() != null) {
            notes.setImg(String.join(",", notes.getImgList())); // 转换为逗号分隔字符串
        }

        notesMapper.insert(notes);
    }

    @Override
    //更新便签
    public void updateNotes(Notes notes) {
        // 将 tagList 和 imgList 转换为字符串
        if (notes.getTagList() != null) {
            notes.setTag(String.join(",", notes.getTagList())); // 转换为逗号分隔字符串
        }
        if (notes.getImgList() != null) {
            notes.setImg(String.join(",", notes.getImgList())); // 转换为逗号分隔字符串
        }

        notesMapper.updateById(notes);
    }

    @Override
    //更改便签顺序
    public void updateOrder(int id, int order) {
        try {
            notesMapper.updateOrder(id, order);
        } catch (Exception e) {
            throw new RuntimeException("更新顺序失败", e);
        }
    }

    @Override
    //删除便签
    public void deleteNotes(Notes notes) {
        notesMapper.deleteById(notes.getId());
    }

    @Override
    // 更新便签图片
    public void updateImage(int id, String imagePath) {
        Notes notes = notesMapper.selectById(id);
        if (notes != null) {
            notes.setImg(imagePath);  // 更新 img 字段
            notesMapper.updateById(notes);  // 执行更新
        }
    }

    @Override
    public List<Notes> selectNotesByKeyword(String keyword){
        // 创建 QueryWrapper 对象
        QueryWrapper<Notes> queryWrapper = new QueryWrapper<>();

        // 匹配 title 或 text 中包含关键字(模糊查询)
        queryWrapper.like("title", keyword).or().like("text", keyword);

        // 查询便签
        List<Notes> notesList = notesMapper.selectList(queryWrapper);

        // 转换 tag 和 img 为数组
        convertTagAndImgToList(notesList);
        return notesList;
    }

    @Override
    public List<Notes> selectNotesByTag(String tag) {
        // 创建 QueryWrapper 对象
        QueryWrapper<Notes> queryWrapper = new QueryWrapper<>();

        // 匹配 tag 中包含指定标签
        queryWrapper.like("tag", tag);

        // 查询便签
        List<Notes> notesList = notesMapper.selectList(queryWrapper);

        // 转换 tag 和 img 为数组
        convertTagAndImgToList(notesList);
        return notesList;
    }

    @Override
    public void archiveNotes(int id) {
        // 查询便签
        Notes notes = notesMapper.selectById(id);
        if (notes != null) {
            // 插入到 archive-notes表表
            notesMapper.insertToArchiveNotes(id);
            // 从 notes 表中删除
            notesMapper.deleteFromNotes(id);
        } else {
            throw new RuntimeException("便签不存在");
        }
    }

    @Override
    public List<Notes> selectArchiveNotes() {
        List<Notes> notesList = notesMapper.selectArchiveNotes();
        convertTagAndImgToList(notesList);
        return notesList;
    }

    @Override
    public void restoreArchivedNote(int id) {
        // 查询归档便签
        Notes notes = notesMapper.selectArchivedNoteById(id);
        if (notes != null) {
            // 插入到 notes 表
            notesMapper.insertToNotesFromArchive(id);
            // 从 archive-notes 表中删除
            notesMapper.deleteFromArchiveNotes(id);
        } else {
            // 添加日志记录
            System.err.println("归档便签不存在，id: " + id);
            throw new RuntimeException("归档便签不存在");
        }
    }

    @Override
    public void trashNotes(int id) {
        // 查询便签
        Notes notes = notesMapper.selectById(id);
        if (notes != null) {
            // 插入到 trash-notes 表
            notesMapper.insertToTrashNotes(id);
            // 记录 trash_time 字段
            notes.setTrashTime(LocalDateTime.now());
            notesMapper.updateById(notes);
            // 从 notes 表中删除
            notesMapper.deleteFromNotes(id);
        } else {
            throw new RuntimeException("便签不存在");
        }
    }

    @Override
    public List<Notes> selectTrashNotes() {
        List<Notes> notesList = notesMapper.selectTrashNotes();
        convertTagAndImgToList(notesList);
        return notesList;
    }

    @Override
    public void restoreTrashNote(int id) {
        // 查询回收站便签
        Notes notes = notesMapper.selectTrashNoteById(id);
        if (notes != null) {
            // 插入到 notes 表
            notesMapper.insertToNotesFromTrash(id);
            // 从 trash-notes 表中删除
            notesMapper.deleteFromTrashNotes(id);
        } else {
            // 添加日志记录
            System.err.println("回收站便签不存在，id: " + id);
            throw new RuntimeException("回收站便签不存在");
        }
    }

    @Override
    public void deleteTrashNote(int id) {
        // 从 trash-notes 表中删除
        notesMapper.deleteFromTrashNotes(id);
    }

    @Override
    public List<Notes> selectNotesByUserId(int userid) {
        QueryWrapper<Notes> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("userid", userid);
        List<Notes> notesList = notesMapper.selectList(queryWrapper);
        convertTagAndImgToList(notesList);
        return notesList;
    }

    @Override
    public List<Notes> selectNotesByKeywordAndUserId(String keyword, int userid) {
        return List.of();
    }

    @Override
    public List<Notes> selectNotesByTagAndUserId(String tag, int userid) {
        QueryWrapper<Notes> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("userid", userid).like("tag", tag);
        List<Notes> notesList = notesMapper.selectList(queryWrapper);
        convertTagAndImgToList(notesList);
        return notesList;
    }

    @Override
    public List<Notes> selectArchiveNotesByUserId(int userid) {
        return List.of();
    }

    @Override
    public List<Notes> selectTrashNotesByUserId(int userid) {
        return List.of();
    }


    // 定时任务，每天凌晨 2 点执行一次
    @Scheduled(cron = "0 0 2 * * ?")
    public void deleteExpiredTrashNotes() {
        List<Notes> trashNotes = notesMapper.selectTrashNotes();
        LocalDateTime now = LocalDateTime.now();
        for (Notes note : trashNotes) {
            LocalDateTime trashTime = note.getTrashTime(); // 获取便签被移到回收站的时间
            if (trashTime != null && ChronoUnit.DAYS.between(trashTime, now) >= 10) {
                notesMapper.deleteFromTrashNotes(note.getId()); // 删除超过十日的便签
            }
        }
    }
}