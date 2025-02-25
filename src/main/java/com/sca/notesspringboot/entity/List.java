package com.sca.notesspringboot.entity;

import lombok.Data;

@Data
public class List {
    private Integer id;
    private Integer listid;
    private String text;
    private String complete;
}
