package com.sca.notesspringboot.mapper;


import com.sca.notesspringboot.entity.User;
import org.apache.ibatis.annotations.*;

@Mapper
public interface UserMapper {
//    //    根据用户名查询用户的方法
//    @Select("select * from user where username=#{username}")
//    User findUserByUsername(String username);
//
//    //    查询所有用户
//    @Select("select * from user")
//    List<User> queryAll();
//
//    //    保存用户
//    @Insert("insert into t_user(username,password,email,sex,tel,createTime,type)" +
//            "values(#{username},#{password},#{email},#{sex},#{tel},#{createTime},#{type})")
//    @Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "id")
//    void addUser(User user);
//
//    //    根据用户的更新头像
//    @Update("update t_user set headImg = #{headImg} where id=#{id}")
//    void updateHeadImgByUser(User user);
//
//    //    根据用户id修改数据
//    @Update("update t_user set username=#{username},email=#{email},tel=#{tel},sex=#{sex} " +
//            "where id=#{id}")
//    void editUser(User user);
//
//    //    根据用户id删除数据
//    @Delete("delete from t_user where id=#{id}")
//    void deleteUser(Long id);
//
//    //    批量删除
//    @Delete("<script>" +
//            "delete from t_user where id in" +
//            "<foreach collection='array' item='id' open='(' separator=',' close=')'>" +
//            "#{id}" +
//            "</foreach>" +
//            "</script>")
//    void deleleBatchUser(Long[] ids);
//
//    //    保存用户角色
//    @Insert("<script>" +
//            "insert into user_role (userid,roleid) values" +
//            "<foreach collection='list' item='item'  separator=',' >" +
//            "(#{item.userId},#{item.roleId})" +
//            "</foreach>" +
//            "</script>")
//    void saveUserRole(List<Map> paramList);
//
//    //    删除用户角色中间表
//    @Delete("delete from user_role where userid=#{id}")
//    void deleteUserRole(Long userId);

    @Insert("insert into `user` (Username, Password, Name, Id) " +
            "values (#{Username}, #{Password}, #{Name}, #{Id})")
    void insert(User user);

    //通过帐号查询用户信息
    @Select("select * from `user` where Username = #{Username} order by id desc")
    User selectByUsername(String Username);
}
