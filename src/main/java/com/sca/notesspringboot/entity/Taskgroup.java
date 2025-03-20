package com.sca.notesspringboot.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@TableName("taskgroup")
public class Taskgroup {
    private Integer id;
    private Integer userid;
    private String title;
    @TableField("`lock`")
    private String lock; 
    @TableField("`order`") // 指定数据库字段名
    private Integer order;
    private String status;
    @TableField("trash_time")
    private LocalDateTime trashTime; // 添加 trash_time 字段
    @TableField(exist = false)
    private List<String> taskList;

}
