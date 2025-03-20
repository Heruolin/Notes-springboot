package com.sca.notesspringboot.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sca.notesspringboot.entity.User;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface UserMapper extends BaseMapper<User> {
    //修改密码
    @Update("UPDATE user SET password = #{password} WHERE userid = #{userid}")
    int updateByUserId(@Param("userid") Integer userid, @Param("password") String password);
    //注销账户
    @Delete("DELETE FROM user WHERE userid = #{userid}")
    int deleteUserByUserId(@Param("userid") Integer userid);
    // 根据用户 ID 查询用户信息
    @Select("SELECT * FROM user WHERE userid = #{userid}")
    User selectByUserId(@Param("userid") Integer userid);
}
