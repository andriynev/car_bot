package com.andriynev.driver_helper_bot.services;

import com.andriynev.driver_helper_bot.dao.ModeratorRepository;
import com.andriynev.driver_helper_bot.dao.UserRepository;
import com.andriynev.driver_helper_bot.dto.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ModeratorService {
    private final ModeratorRepository moderatorRepository;

    @Autowired
    public ModeratorService(ModeratorRepository moderatorRepository) {
        this.moderatorRepository = moderatorRepository;
    }

    public List<Moderator> getModerators() {
        return moderatorRepository.findAll();
    }

    public Moderator save(Moderator moderator) {
        return moderatorRepository.save(moderator);
    }

    public Optional<Moderator> findModeratorByTelegramId(long id) {
        return this.moderatorRepository.findByTelegramID(id);
    }

    public Optional<Moderator> findModeratorById(String id) {
        return this.moderatorRepository.findById(id);
    }

    public Moderator partialUpdate(String id, ModeratorUpdateRequest request) {
        Optional<Moderator> moderator = moderatorRepository.findById(id);
        if (!moderator.isPresent()) {
            throw new IllegalArgumentException("User not found: " + id);
        }

        moderator.get().setPermissions(request.getPermissions());
        moderator.get().setEnabled(request.isEnabled());
        return moderatorRepository.save(moderator.get());
    }

    public void delete(String id) {
        Optional<Moderator> moderator = moderatorRepository.findById(id);
        if (!moderator.isPresent()) {
            throw new IllegalArgumentException("User not found: " + id);
        }

        moderatorRepository.delete(moderator.get());
    }
}
