package com.sca.notesspringboot.service;

import com.sca.notesspringboot.entity.Tag;

import java.util.List;

public interface TagService {
    //查询Tag列表
    List<Tag> findAll();
    Tag findById(int id);
    //添加Tag
    void insertTag(Tag tag);
    //更新Tag
    void updateTag(Tag tag);
    //删除Tag
    void deleteTag(int id);
}
