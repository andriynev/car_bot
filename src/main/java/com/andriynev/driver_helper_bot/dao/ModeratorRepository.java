package com.andriynev.driver_helper_bot.dao;

import com.andriynev.driver_helper_bot.dto.Moderator;
import com.andriynev.driver_helper_bot.dto.User;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface ModeratorRepository extends MongoRepository<Moderator, String> {
    public Optional<Moderator> findByUsername(String username);
}
