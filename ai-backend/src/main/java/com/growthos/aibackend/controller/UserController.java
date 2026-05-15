package com.growthos.aibackend.controller;

import com.growthos.aibackend.entity.User;
import com.growthos.aibackend.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/user")
@CrossOrigin(origins = "http://localhost:5174", allowCredentials = "true")
public class UserController {

    @Autowired
    private UserMapper userMapper;

    @PostMapping("/register")
    public Map<String, Object> register(@RequestBody User user) {
        Map<String, Object> map = new HashMap<>();

        User exist = userMapper.selectByUsername(user.getUsername());
        if (exist != null) {
            map.put("code", 500);
            map.put("message", "用户名已存在");
            return map;
        }

        userMapper.insert(user);
        map.put("code", 200);
        map.put("message", "注册成功");
        return map;
    }

    @PostMapping("/login")
    public Map<String, Object> login(@RequestBody User user) {
        Map<String, Object> map = new HashMap<>();

        User exist = userMapper.selectByUsername(user.getUsername());
        if (exist == null) {
            map.put("code", 500);
            map.put("message", "账号或密码错误");
            return map;
        }

        if (!exist.getPassword().equals(user.getPassword())) {
            map.put("code", 500);
            map.put("message", "账号或密码错误");
            return map;
        }

        map.put("code", 200);
        map.put("data", "token_success");
        return map;
    }
}