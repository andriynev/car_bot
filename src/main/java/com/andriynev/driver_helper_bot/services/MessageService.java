package com.andriynev.driver_helper_bot.services;

import com.andriynev.driver_helper_bot.dto.*;
import com.andriynev.driver_helper_bot.enums.MessageType;
import com.andriynev.driver_helper_bot.enums.ResponseType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import java.util.List;
import java.util.Optional;

@Service
public class MessageService {
    private final UserService userService;
    private final ResponseService responseService;

    @Autowired
    public MessageService(UserService userService, ResponseService responseService) {
        this.userService = userService;
        this.responseService = responseService;
    }

    public void sendMessage(SendMessageRequest request) {
        if (request.getUserId() == null) {
            sendMessageToAllUsers(request.getMessage());
            return;
        }

        Optional<User> user = userService.findUserById(request.getUserId());
        if (!user.isPresent()) {
            throw new IllegalArgumentException("User with id not found: " + request.getUserId());
        }

        sendMessageToUser(request.getMessage(), user.get());
    }

    private void sendMessageToAllUsers(String message) {
        List<User> users = userService.getUsers();
        if (users.isEmpty()) {
            return;
        }
        for (User user: users) {
            // skip disabled users
            if (!user.isEnabled()) {
                continue;
            }

            sendMessageToUser(message, user);
        }
    }

    private void sendMessageToUser(String message, User user) {
        responseService.sendMessage(message, user.getChatID(), MessageType.MARKDOWN);
    }
}
