package com.sca.notesspringboot.web.controller;

import com.sca.notesspringboot.common.Result;
import com.sca.notesspringboot.entity.Tag;
import com.sca.notesspringboot.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("Tag")
public class TagController {

    @Autowired
    private TagService tagService;

    // 查询所有标签
    @GetMapping("/Taglist")
    public Result findAll() {
        List<Tag> list = tagService.findAll();
        return Result.success(list);
    }

    // 根据ID查询标签
    @GetMapping("/Tag/{id}")
    public Result findById(@PathVariable int id) {
        Tag tag = tagService.findById(id);
        return Result.success(tag);
    }

    // 添加标签
    @PostMapping("/TagAdd")
    public Result insertTag(@RequestBody Tag tag) {
        // 检查标签是否已存在
        List<Tag> existingTags = tagService.findAll();
        for (Tag existingTag : existingTags) {
            if (existingTag.getTag().equals(tag.getTag())) {
                return Result.error("标签已存在，无法重复添加");
            }
        }
        tagService.insertTag(tag);
        return Result.success("标签添加成功");
    }

    // 更新标签
    @PutMapping("/TagUpdate")
    public Result updateTag(@RequestBody Tag tag) {
        tagService.updateTag(tag);
        return Result.success("标签更新成功");
    }

    // 删除标签
    @DeleteMapping("/TagDelete/{id}")
    public Result deleteTag(@PathVariable int id) {
        tagService.deleteTag(id);
        return Result.success("标签删除成功");
    }
}
