package com.sca.notesspringboot.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class Taskgroup {
    private Integer id;
    private Integer userid;
    private String title;
    @TableField("`order`") // 指定数据库字段名
    private Integer order;
    private String status;
    private LocalDateTime trashTime;
    @TableField(exist = false)
    private List<String> taskList;

}
