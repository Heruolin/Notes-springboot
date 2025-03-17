package com.sca.notesspringboot.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sca.notesspringboot.entity.User;
import org.apache.ibatis.annotations.*;

@Mapper
public interface UserMapper extends BaseMapper<User> {

}
