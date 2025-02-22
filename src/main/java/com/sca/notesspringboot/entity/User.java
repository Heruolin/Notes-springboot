package com.sca.notesspringboot.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import javax.management.relation.Role;
import java.util.ArrayList;
import java.util.List;

@Data

public class User {

//    private Integer id;
//
//    private String username;
//
//    private String password;
//
//    private String email;
//
//    private String tel;
//
//    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
//    private Data createTime;
//
//    private Boolean sex;
//
//    private String headImg;
//
//    private Integer type;
//
//    private List<Role> roles = new ArrayList();

    private Integer userid;
    private String username;
    private String password;
    private String name;

}
