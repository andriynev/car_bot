package com.andriynev.driver_helper_bot.services;

import com.andriynev.driver_helper_bot.dao.TutorialRepository;
import com.andriynev.driver_helper_bot.dao.UserRepository;
import com.andriynev.driver_helper_bot.dto.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TutorialService {
    private final UserRepository userRepository;
    private final TutorialRepository tutorialRepository;

    @Autowired
    public TutorialService(UserRepository userRepository, TutorialRepository tutorialRepository) {
        this.userRepository = userRepository;
        this.tutorialRepository = tutorialRepository;
    }

    public Optional<Tutorial> findTutorialById(String id) {
        return tutorialRepository.findTutorialById(id);
    }

    public List<Tutorial> getTutorials() {

        return tutorialRepository.findAll();
    }

    public Tutorial update(String moderatorId, String id, TutorialRequest request) {
        Optional<Tutorial> tutorial = tutorialRepository.findTutorialById(id);
        if (!tutorial.isPresent()) {
            throw new IllegalArgumentException("Tutorial not found: " + id);
        }

        tutorial.get().setName(request.getName());
        tutorial.get().setServiceName(request.getServiceName());
        tutorial.get().setImage(request.getImage());
        tutorial.get().setText(request.getText());
        tutorial.get().setUpdatedBy(moderatorId);


        return tutorialRepository.save(tutorial.get());
    }

    public Tutorial create(String moderatorId, TutorialRequest request) {
        Tutorial tutorial = new Tutorial();
        tutorial.setName(request.getName());
        tutorial.setServiceName(request.getServiceName());
        tutorial.setImage(request.getImage());
        tutorial.setText(request.getText());
        tutorial.setCreatedBy(moderatorId);
        tutorial.setUpdatedBy(moderatorId);

        return tutorialRepository.save(tutorial);
    }

    public void delete(String id) {
        Optional<Tutorial> tutorial = tutorialRepository.findById(id);
        if (!tutorial.isPresent()) {
            throw new IllegalArgumentException("Tutorial not found: " + id);
        }

        tutorialRepository.delete(tutorial.get());
    }
}
