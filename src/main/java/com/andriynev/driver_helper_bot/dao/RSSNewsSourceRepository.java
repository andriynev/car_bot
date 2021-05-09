package com.andriynev.driver_helper_bot.dao;

import com.andriynev.driver_helper_bot.dto.RSSNewsSource;
import org.springframework.data.mongodb.repository.MongoRepository;


public interface RSSNewsSourceRepository extends MongoRepository<RSSNewsSource, String> {
}
