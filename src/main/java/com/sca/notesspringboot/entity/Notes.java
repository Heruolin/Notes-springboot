package com.sca.notesspringboot.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class Notes {

    private Integer id;
    private Integer userid;
    private String title;
    private String text;
    private String tag;
    private String img;
    private String color;
    @TableField("`lock`")
    private String lock;
    @TableField("`order`") // 指定数据库字段名
    private Integer order;
    @TableField("trash_time")
    private LocalDateTime trashTime; // 记录便签被移到回收站的时间
    @TableField(exist = false)
    private List<String> tagList;
    @TableField(exist = false)
    private List<String> imgList;

}
