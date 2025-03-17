package com.sca.notesspringboot.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.sca.notesspringboot.entity.Tag;
import com.sca.notesspringboot.mapper.TagMapper;
import com.sca.notesspringboot.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TagServiceImpl implements TagService {

    @Autowired
    private TagMapper tagMapper;

    @Override
    public List<Tag> findAll() {
        return tagMapper.selectList(null);
    }

    @Override
    public Tag findById(int id) {
        return tagMapper.selectById(id);
    }

    @Override
    public List<Tag> selectTagsByUserId(int userid) {
        QueryWrapper<Tag> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("userid", userid);
        return tagMapper.selectList(queryWrapper);
    }

    @Override
    public void insertTag(Tag tag) {
        tagMapper.insert(tag);
    }

    @Override
    public void updateTag(Tag tag) {
        tagMapper.updateById(tag);
    }

    @Override
    public void deleteTag(int id) {
        tagMapper.deleteById(id);
    }
}
