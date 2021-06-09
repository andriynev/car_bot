package com.andriynev.driver_helper_bot.services;

import com.andriynev.driver_helper_bot.dao.CarRepairTreeRepository;
import com.andriynev.driver_helper_bot.dto.*;
import com.andriynev.driver_helper_bot.enums.InputMessageType;
import com.andriynev.driver_helper_bot.enums.ResponseType;
import com.andriynev.driver_helper_bot.handlers.Handler;
import com.andriynev.driver_helper_bot.messages.MessagesProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class ExpertSystemService {

    private final CarRepairTreeRepository carRepairTreeRepository;

    @Autowired
    public ExpertSystemService(CarRepairTreeRepository carRepairTreeRepository, MessagesProperties messagesProperties) {
        this.carRepairTreeRepository = carRepairTreeRepository;
    }

    public Optional<CarRepairTree> find() {
        return carRepairTreeRepository.findFirstByIdIsNotNull();
    }

}
