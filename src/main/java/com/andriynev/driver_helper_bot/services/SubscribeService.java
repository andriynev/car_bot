package com.andriynev.driver_helper_bot.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SubscribeService {
    private UserService userService;
    private ResponseService responseService;

    @Autowired
    public SubscribeService(UserService userService, ResponseService responseService) {
        this.userService = userService;
        this.responseService = responseService;
    }
}
