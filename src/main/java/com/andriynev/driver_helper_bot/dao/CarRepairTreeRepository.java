package com.andriynev.driver_helper_bot.dao;

import com.andriynev.driver_helper_bot.dto.CarRepairTree;
import com.andriynev.driver_helper_bot.dto.User;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface CarRepairTreeRepository extends MongoRepository<CarRepairTree, String> {
    public Optional<CarRepairTree> findFirstByIdIsNotNull();
}
