package com.sca.notesspringboot.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Listgroup {
    private Integer id;
    private Integer userid;
    private String title;
    private String listgroup;
    @TableField("`order`") // 指定数据库字段名
    private Integer order;
    private LocalDateTime trashTime;


}
