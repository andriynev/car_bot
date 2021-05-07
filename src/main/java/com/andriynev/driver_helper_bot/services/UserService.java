package com.andriynev.driver_helper_bot.services;

import com.andriynev.driver_helper_bot.dao.UserRepository;
import com.andriynev.driver_helper_bot.dto.State;
import com.andriynev.driver_helper_bot.dto.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Optional;

@Service
public class UserService {
    private UserRepository userRepository;
    public static final State initialState = new State("MainMenu", "initial");

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

    public User save(User user) {
        return userRepository.save(user);
    }

    @EventListener(ApplicationReadyEvent.class)
    public void runAfterStartup() {
        System.out.println("UserService is running........");
        //userRepository.deleteAll();

        // save a couple of customers
        //userRepository.save(new User(12346L, new State("main_menu", "initial"), new ArrayList<>()));
        //userRepository.save(new User(12345L, new State("main_menu", "initial"), new ArrayList<>()));

        // fetch all customers
        System.out.println("Customers found with findAll():");
        System.out.println("-------------------------------");
        for (User user : userRepository.findAll()) {
            System.out.println(user);
        }
        System.out.println();

        // fetch an individual customer
        System.out.println("Customer found with findUserByChatID(12345L):");
        System.out.println("--------------------------------");
        System.out.println(userRepository.findUserByChatID(12345L));

        System.out.println("Customers found with findAll:");
        System.out.println("--------------------------------");
        for (User customer : userRepository.findAll()) {
            System.out.println(customer);
        }
    }
}
