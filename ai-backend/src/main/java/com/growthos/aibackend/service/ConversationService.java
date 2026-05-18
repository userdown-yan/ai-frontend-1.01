package com.growthos.aibackend.service;

import com.growthos.aibackend.entity.Conversation;
import com.growthos.aibackend.mapper.ConversationMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ConversationService {

    @Autowired
    private ConversationMapper conversationMapper;

    public Conversation create(Integer userId) {
        Conversation conversation = new Conversation();
        conversation.setUserId(userId);
        conversation.setTitle("新对话");
        conversationMapper.insert(conversation);
        return conversation;
    }

    public List<Conversation> list(Integer userId) {
        return conversationMapper.findByUserId(userId);
    }

    public boolean updateTitle(Integer id, Integer userId, String title) {
        return conversationMapper.updateTitle(id, userId, title) > 0;
    }

    public boolean delete(Integer id, Integer userId) {
        return conversationMapper.deleteById(id, userId) > 0;
    }
}