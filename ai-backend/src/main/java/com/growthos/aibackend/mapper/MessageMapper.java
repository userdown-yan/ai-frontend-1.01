package com.growthos.aibackend.mapper;

import com.growthos.aibackend.entity.Message;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface MessageMapper {

    @Insert("""
            INSERT INTO messages(conversation_id,role,content)
            VALUES(#{conversationId},#{role},#{content})
            """)
    void insert(Message message);

    @Select("""
            SELECT * FROM messages
            WHERE conversation_id = #{conversationId}
            ORDER BY id ASC
            """)
    List<Message> findByConversationId(Long conversationId);
}