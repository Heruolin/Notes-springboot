package com.sca.notesspringboot.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("task")
public class Task {
    @TableId
    private Integer id;
    @TableField("taskgroup_id")
    private Integer taskgroupId;
    private String name;
    private Boolean completed;
    @TableField("`order`") // 指定数据库字段名
    private Integer order;
}
