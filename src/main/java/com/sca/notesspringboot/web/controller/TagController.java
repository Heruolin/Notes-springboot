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
    TagService tagService;

    // 获取标签列表
    @GetMapping("/Taglist")
    public Result selectTags(@RequestParam(required = false) Integer userid) {
        if (userid == null) {
            return Result.error("缺少必要的参数: userid");
        }
        List<Tag> list = tagService.selectTagsByUserId(userid); // 根据userid查询标签
        return Result.success(list);
    }

    // 添加标签
    @PostMapping("/TagAdd")
    public Result insertTag(@RequestBody Tag tag) {
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
