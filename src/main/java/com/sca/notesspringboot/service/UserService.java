package com.sca.notesspringboot.service;

import com.sca.notesspringboot.entity.User;
import org.springframework.stereotype.Service;



public interface UserService {



    void insertUser(User user);

    User login(User user);
}
