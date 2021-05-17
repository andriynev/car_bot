package com.andriynev.driver_helper_bot.handlers;

import com.andriynev.driver_helper_bot.messages.MessagesProperties;
import com.andriynev.driver_helper_bot.dto.*;
import com.andriynev.driver_helper_bot.enums.ResponseType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class MainMenuService implements GroupHandler {
    private final static String type = "MainMenu";
    private final static String nameMessageKey = "main-menu";
    private String humanReadableName = type;
    private final static String initialStep = "initial";
    private final static String viewMenuStep = "view_menu";
    private final List<ReplyButton> menuButtons = new ArrayList<>();
    private final List<Handler> handlers = new ArrayList<>();
    private final MessagesProperties messagesProperties;

    @Autowired
    public MainMenuService(MessagesProperties messagesProperties) {
        this.messagesProperties = messagesProperties;
        this.setHumanReadableName(this.messagesProperties.getMessage(nameMessageKey));
    }

    @Override
    public String getHumanReadableName() {
        return humanReadableName;
    }

    @Override
    public void setHumanReadableName(String humanReadableName) {
        if (humanReadableName == null || humanReadableName.isEmpty()) {
            return;
        }
        this.humanReadableName = humanReadableName;
    }

    @Override
    public Output handle(User user, State state, InputMessage userInput) {
        switch (state.getStep()){
            case initialStep:
                return new Output(
                        new State(type, viewMenuStep),
                        ResponseType.MENU,
                        humanReadableName,
                        menuButtons,
                        null
                );
            case viewMenuStep:
                Optional<Handler> handler = handlers.stream()
                        .filter((h) -> h.getHumanReadableName().equals(userInput.getMessage()))
                        .findFirst();
                if (handler.isPresent()) {
                    Output out = new Output(
                            new State(handler.get().getType(), initialStep),
                            ResponseType.QUESTION,
                            handler.get().getHumanReadableName()
                    );
                    out.setRedirect(true);
                    return out;
                }

        }

        return new Output(
                new State(type, viewMenuStep),
                ResponseType.MENU,
                humanReadableName,
                menuButtons,
                null
        );
    }

    @Override
    public String getType() {
        return type;
    }

    @Override
    public void setHandlers(List<Handler> group) {
        for (Handler handler: group) {
            menuButtons.add(new ReplyButton(handler.getHumanReadableName()));
            handlers.add(handler);
        }
    }
}
