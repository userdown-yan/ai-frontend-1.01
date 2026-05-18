package com.growthos.aibackend.mapper;

import com.growthos.aibackend.entity.Conversation;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface ConversationMapper {

    @Insert("""
            insert into conversation
            (user_id, title)
            values
            (#{userId}, #{title})
            """)
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void insert(Conversation conversation);

    @Select("""
            select *
            from conversation
            where user_id = #{userId}
            order by id desc
            """)
    List<Conversation> findByUserId(Integer userId);

    @Update("""
            update conversation
            set title = #{title}
            where id = #{id}
              and user_id = #{userId}
            """)
    int updateTitle(
            @Param("id") Integer id,
            @Param("userId") Integer userId,
            @Param("title") String title
    );

    @Delete("""
            delete from conversation
            where id = #{id}
              and user_id = #{userId}
            """)
    int deleteById(
            @Param("id") Integer id,
            @Param("userId") Integer userId
    );
}