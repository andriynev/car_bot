package com.andriynev.driver_helper_bot.handlers;


import com.andriynev.driver_helper_bot.dto.*;
import com.andriynev.driver_helper_bot.enums.MessageType;
import com.andriynev.driver_helper_bot.enums.ResponseType;
import com.andriynev.driver_helper_bot.messages.MessagesProperties;
import com.andriynev.driver_helper_bot.services.TutorialService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class TutorialsService implements Handler {

    private final TutorialService tutorialService;
    private static final String initialStep = "initial";
    private static final String selectItemStep = "select_item";
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
        switch (state.getStep()) {
            case selectItemStep:
                return processSelectItemStepOutput(userInput);
            default:
                return processInitialStepOutput(userInput);
        }
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

    private List<ReplyButton> getMenuButtons(List<Tutorial> tutorials) {
        List<ReplyButton> menuButtons = new ArrayList<>();
        if (tutorials.isEmpty()) {
            return menuButtons;
        }
        for (Tutorial tutorial :
                tutorials) {
            menuButtons.add(new ReplyButton(tutorial.getName()));
        }

        menuButtons.add(new ReplyButton(this.messagesProperties.getMessage("main-menu")));
        return menuButtons;
    }

    private Output processInitialStepOutput(InputMessage userInput) {
        List<Tutorial> tutorials = tutorialService.getTutorials();
        if (tutorials.isEmpty()) {
            // Need to log this exception
            return new Output(
                    new State(type, initialStep),
                    ResponseType.QUESTION,
                    this.messagesProperties.getMessage("sorry-try-again")
            );
        }

        List<ReplyButton> menuButtons = getMenuButtons(tutorials);
        return new Output(
                new State(type, selectItemStep),
                ResponseType.MESSAGE,
                this.messagesProperties.getMessage("please-select-tutorial"),
                menuButtons,
                null
        );
    }

    private Output processSelectItemStepOutput(InputMessage userInput) {

        List<Tutorial> tutorials = tutorialService.getTutorials();
        if (tutorials.isEmpty()) {
            // Need to log this exception
            return new Output(
                    new State(type, selectItemStep),
                    ResponseType.QUESTION,
                    this.messagesProperties.getMessage("sorry-try-again")
            );
        }

        List<ReplyButton> menuButtons = getMenuButtons(tutorials);
        Optional<Tutorial> tutorial = tutorials.stream()
                .filter((t) -> t.getName().equals(userInput.getMessage()))
                .findFirst();

        if (!tutorial.isPresent()) {
            return new Output(
                    new State(type, selectItemStep),
                    ResponseType.QUESTION,
                    this.messagesProperties.getMessage("no-tutorials-with-name-found"),
                    menuButtons,
                    null
            );
        }

        List<String> messages = splitMessageToParts(tutorial.get().getText());
        if (messages.size() == 0) {
            return new Output(
                    new State(type, selectItemStep),
                    ResponseType.QUESTION,
                    this.messagesProperties.getMessage("sorry-try-again")
            );
        }

        Output output = new Output(
                new State(type, selectItemStep),
                ResponseType.MESSAGE,
                messages.get(messages.size() - 1),
                menuButtons,
                null
        );
        output.setMessageType(MessageType.MARKDOWN);

        messages.remove(messages.size() - 1);

        List<Output> outputMessages = new ArrayList<>();
        outputMessages.add(new Output(new State(type, selectItemStep), tutorial.get().getImage()));

        for (String message: messages) {
            Output subOutput = new Output(new State(type, selectItemStep), ResponseType.MESSAGE, message);
            subOutput.setMessageType(MessageType.MARKDOWN);
            outputMessages.add(subOutput);
        }

        output.setMessages(outputMessages);
        return output;
    }

    private List<String> splitMessageToParts(String message) {
        int maxMessageLength = 4096;
        List<String> messages = new ArrayList<>();
        if (message.length() < maxMessageLength) {
            messages.add(message);
            return messages;
        }

        int beginIndex = 0;
        int endIndex = maxMessageLength;
        int length = message.length();

        endIndex = findLastSplitChar(message.substring(beginIndex, endIndex));
        String substring = message.substring(beginIndex, endIndex);
        messages.add(substring);
        length = length - substring.length();
        beginIndex = endIndex;

        while (length > maxMessageLength) {
            endIndex = endIndex + maxMessageLength;
            endIndex = findLastSplitChar(message.substring(beginIndex, endIndex));
            substring = message.substring(beginIndex, endIndex);
            messages.add(substring);
            length = length - substring.length();
            beginIndex = endIndex;
        }

        messages.add(message.substring(beginIndex));
        return messages;
    }

    private int findLastSplitChar(String message) {
        int index = 0;
        index = message.lastIndexOf(".");
        if (index != -1) {
            return index;
        }

        index = message.lastIndexOf("\n");
        if (index != -1) {
            return index;
        }

        index = message.lastIndexOf(" ");
        if (index != -1) {
            return index;
        }

        return message.length() - 1;
    }
}
