package com.andriynev.driver_helper_bot.services;

import com.andriynev.driver_helper_bot.dao.UserRepository;
import com.andriynev.driver_helper_bot.dto.State;
import com.andriynev.driver_helper_bot.dto.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Optional;

@Service
public class UserService {
    private UserRepository userRepository;
    private static final State initialState = new State("main_menu", "initial");

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    public User getOrCreateUser (Long chatID) {
        Optional<User> user = userRepository.findUserByChatID(chatID);

        if (user.isPresent()) {
            return user.get();
        }

        return userRepository.save(new User(chatID, initialState, new ArrayList<>()));
    }
}
