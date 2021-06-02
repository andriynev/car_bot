package com.andriynev.driver_helper_bot.handlers;


import com.andriynev.driver_helper_bot.dto.*;
import com.andriynev.driver_helper_bot.enums.ResponseType;
import com.andriynev.driver_helper_bot.messages.MessagesProperties;
import com.andriynev.driver_helper_bot.services.TutorialService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TutorialsService implements Handler {

    private final TutorialService tutorialService;
    private static final String initialStep = "initial";
    private final static String type = "Tutorials";
    private final static String nameMessageKey = "tutorials";
    private String humanReadableName = type;
    private final MessagesProperties messagesProperties;
    private String description;

    @Autowired
    public TutorialsService(TutorialService tutorialService, MessagesProperties messagesProperties) {
        this.tutorialService = tutorialService;
        this.messagesProperties = messagesProperties;
        this.setHumanReadableName(this.messagesProperties.getMessage(nameMessageKey));
        this.setDescription(this.messagesProperties.getMessage(nameMessageKey+"-description"));
    }

    @Override
    public Output handle(User user, State state, InputMessage userInput) {
        if (!state.getType().equals(type)) {
            // Need to log this exception
            Output out = new Output(
                    new State(type, initialStep),
                    ResponseType.QUESTION,
                    this.messagesProperties.getMessage("sorry-try-again")
            );
            out.setRedirect(true);
            return out;
        }

        List<Tutorial> tutorials = tutorialService.getTutorials();
        if (tutorials.isEmpty()) {
            // Need to log this exception
            Output out = new Output(
                    new State(type, initialStep),
                    ResponseType.QUESTION,
                    this.messagesProperties.getMessage("sorry-try-again")
            );
            out.setRedirect(true);
            return out;
        }



        Output out = new Output(
                new State(type, initialStep),
                ResponseType.QUESTION,
                this.messagesProperties.getMessage("sorry-try-again")
        );
        out.setRedirect(true);
        return out;
    }

    @Override
    public String getType() {
        return type;
    }

    @Override
    public String getHumanReadableName() {
        return humanReadableName;
    }

    @Override
    public void setHumanReadableName(String name) {
        if (name == null || name.isEmpty()) {
            return;
        }

        humanReadableName = name;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public void setDescription(String description) {
        this.description = description;
    }
}
