package com.andriynev.driver_helper_bot.dao;

import com.andriynev.driver_helper_bot.dto.Tutorial;
import com.andriynev.driver_helper_bot.dto.User;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface TutorialRepository extends MongoRepository<Tutorial, String> {
    public Optional<Tutorial> findTutorialById(String id);
}
