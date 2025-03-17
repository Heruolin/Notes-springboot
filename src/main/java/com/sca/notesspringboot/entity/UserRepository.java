package com.sca.notesspringboot.entity;
import org.springframework.data.jpa.repository.JpaRepository;
import com.sca.notesspringboot.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {
    // 根据用户名查找用户
    User findByUsername(String username);
}
