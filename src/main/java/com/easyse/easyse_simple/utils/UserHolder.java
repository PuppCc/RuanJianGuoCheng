package com.easyse.easyse_simple.utils;

import com.easyse.easyse_simple.pojo.DO.task.User;
import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentHashMap;

/**
 * @author: zky
 * @date: 2022/11/20
 * @description: 持有用户信息(多线程)，用于代替 session 对象
 */
@Component
public class UserHolder {
    private ThreadLocal<User> users = new ThreadLocal<>();

    private ConcurrentHashMap<Long, LRU> hobby = new ConcurrentHashMap();

    /**
     * 存储用户
     * @param user
     */
    public void setUser(User user) {
        users.set(user);
    }

    /**
     * 获取用户
     * @return
     */
    public User getUser() {
        return users.get();
    }

    /**
     * 清理用户
     */
    public void clear() {
        users.remove();
    }

    public LRU getHobby(Long userId) {
        return hobby.get(userId);
    }
}
