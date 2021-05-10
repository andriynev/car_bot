package com.andriynev.driver_helper_bot.services;

import com.andriynev.driver_helper_bot.dao.UserRepository;
import com.andriynev.driver_helper_bot.dto.State;
import com.andriynev.driver_helper_bot.dto.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    private UserRepository userRepository;
    public static final State initialState = new State("MainMenu", "initial");

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    public User getOrCreateUser(Long chatID, String firstName, String lastName, String userName) {
        Optional<User> user = userRepository.findUserByChatID(chatID);

        if (user.isPresent()) {
            User u = user.get();
            boolean changed = false;
            if ((u.getFirstName().isEmpty())
                    && !firstName.isEmpty()) {
                u.setFirstName(firstName);
                changed = true;
            }
            if ((u.getLastName().isEmpty())
                    && !lastName.isEmpty()) {
                u.setLastName(lastName);
                changed = true;
            }
            if ((u.getUserName().isEmpty())
                    && !userName.isEmpty()) {
                u.setUserName(userName);
                changed = true;
            }
            if (changed) {
                u = userRepository.save(u);
            }
            return u;
        }

        User u = new User(chatID, initialState, User.allSubscriptions, firstName, lastName, userName);
        return userRepository.save(u);
    }

    public List<User> getUsers() {
        return userRepository.findAll();
    }

    public User save(User user) {
        return userRepository.save(user);
    }

    @EventListener(ApplicationReadyEvent.class)
    public void runAfterStartup() {
        System.out.println("UserService is running........");

        // save a couple of customers

        // fetch all customers
        System.out.println("Customers found with findAll():");
        System.out.println("-------------------------------");
        for (User user : userRepository.findAll()) {
            System.out.println(user);
        }
        System.out.println();
    }
}
