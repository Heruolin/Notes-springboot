package com.sca.notesspringboot.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;

import javax.management.relation.Role;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
public class User {
    @Id
    private Integer userid;
    private String username;
    private String password;

}
