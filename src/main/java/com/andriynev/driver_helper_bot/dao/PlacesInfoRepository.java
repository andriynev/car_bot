package com.andriynev.driver_helper_bot.dao;

import com.andriynev.driver_helper_bot.dto.PlacesInfo;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface PlacesInfoRepository extends MongoRepository<PlacesInfo, String> {
    public Optional<PlacesInfo> findFirstByIdIsNotNull();
}
