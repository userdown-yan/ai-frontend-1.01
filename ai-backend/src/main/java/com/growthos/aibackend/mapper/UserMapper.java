package com.growthos.aibackend.mapper;

import com.growthos.aibackend.entity.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;

public interface UserMapper {

    @Select("select * from users where username=#{username}")
    User selectByUsername(String username);

    @Insert("insert into users(username,password) values(#{username},#{password})")
    int insert(User user);
}