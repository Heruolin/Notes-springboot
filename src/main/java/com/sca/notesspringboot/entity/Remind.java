package com.sca.notesspringboot.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("remind")
public class Remind {
    private Integer id;
    private String userid;
    private String text;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime remindTime;
    @TableField("`order`") // 指定数据库字段名
    private Integer order;
    @TableField("trash_time")
    private LocalDateTime trashTime; // 记录提醒被移到回收站的时间
    private String status;
}
