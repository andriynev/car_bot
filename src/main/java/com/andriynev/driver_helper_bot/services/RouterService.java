package com.andriynev.driver_helper_bot.services;

import com.andriynev.driver_helper_bot.dto.OutputMessage;
import com.andriynev.driver_helper_bot.dto.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class RouterService {
    private Map<String, Handler> handlers = new HashMap<>();

    @Autowired
    public RouterService(Handler expertService) {
        handlers.put(expertService.getType(), expertService);
    }

//    public OutputMessage route(User user, OutputMessage outputMessage) {
//
//    }

}
