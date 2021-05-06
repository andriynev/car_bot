package com.andriynev.driver_helper_bot.services;


import org.springframework.beans.factory.annotation.Autowired;

public class DispatcherService {
    private UserService userService;
    private RouterService routerService;
    private ResponseService responseService;

    @Autowired
    public DispatcherService(UserService userService, RouterService routerService, ResponseService responseService) {
        this.userService = userService;
        this.routerService = routerService;
        this.responseService = responseService;
    }
}
