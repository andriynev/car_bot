package com.andriynev.driver_helper_bot.services;

import com.andriynev.driver_helper_bot.dao.UserRepository;
import com.andriynev.driver_helper_bot.dto.State;
import com.andriynev.driver_helper_bot.dto.User;
import com.andriynev.driver_helper_bot.dto.UserUpdateRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;
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

    public User partialUpdate(String id, UserUpdateRequest request) {
        Optional<User> user = userRepository.findUserById(id);
        if (!user.isPresent()) {
            throw new IllegalArgumentException("User not found: " + id);
        }

        user.get().setEnabled(request.isEnabled());
        return userRepository.save(user.get());
    }

    public User save(User user) {
        return userRepository.save(user);
    }
}
