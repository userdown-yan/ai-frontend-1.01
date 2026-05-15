package com.growthos.aibackend.mapper;

import com.growthos.aibackend.entity.Conversation;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface ConversationMapper {

    @Insert("""
            insert into conversation
            (user_id,title)
            values
            (#{userId},#{title})
            """)
    @Options(
            useGeneratedKeys = true,
            keyProperty = "id"
    )
    void insert(Conversation conversation);

    @Select("""
            select *
            from conversation
            where user_id = #{userId}
            order by id desc
            """)
    List<Conversation> findByUserId(
            Integer userId
    );
}