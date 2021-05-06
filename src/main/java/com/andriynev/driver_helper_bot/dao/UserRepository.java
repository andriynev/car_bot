package com.andriynev.driver_helper_bot.dao;

import com.andriynev.driver_helper_bot.dto.User;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface UserRepository extends MongoRepository<User, String> {
    public User findUserById(String id);
    public Optional<User> findUserByChatID(Long chatID);
}
