package com.andriynev.driver_helper_bot.handlers;

import com.andriynev.driver_helper_bot.messages.MessagesProperties;
import com.andriynev.driver_helper_bot.dao.CarRepairTreeRepository;
import com.andriynev.driver_helper_bot.dto.*;
import com.andriynev.driver_helper_bot.enums.InputMessageType;
import com.andriynev.driver_helper_bot.enums.ResponseType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ExpertService implements Handler {

    private final CarRepairTreeRepository carRepairTreeRepository;
    private static final int maxDepth = 50;
    private static final String defaultLanguage = "en";
    private static final String UKLanguage = "uk";
    private static final String initialStep = "initial";
    private final static String type = "Expert";
    private final static String nameMessageKey = "expert-system";
    private final static String defaultMessageKey = "take-your-car-to-a-mechanic";
    private String defaultMessage;
    private String humanReadableName = type;
    private final MessagesProperties messagesProperties;
    private String description;

    @Autowired
    public ExpertService(CarRepairTreeRepository carRepairTreeRepository, MessagesProperties messagesProperties) {
        this.carRepairTreeRepository = carRepairTreeRepository;
        this.messagesProperties = messagesProperties;
        this.setHumanReadableName(this.messagesProperties.getMessage(nameMessageKey));
        this.defaultMessage = this.messagesProperties.getMessage(defaultMessageKey);
        this.setDescription(this.messagesProperties.getMessage(nameMessageKey+"-description"));
    }

    @Override
    public Output handle(User user, State state, InputMessage userInput) {
        if (!state.getType().equals(type)) {
            // Need to log this exception
            Output out = new Output(
                    new State(type, initialStep),
                    ResponseType.QUESTION,
                    "\uD83D\uDC49 " + defaultMessage
            );
            out.setRedirect(true);
            return out;
        }

        Optional<CarRepairTree> tree = carRepairTreeRepository.findFirstByIdIsNotNull();
        if (!tree.isPresent()) {
            // Need to log this exception
            Output out = new Output(
                    new State(type, initialStep),
                    ResponseType.QUESTION,
                    "\uD83D\uDC49 " + defaultMessage
            );
            out.setRedirect(true);
            return out;
        }

        try {
            validateTree(tree.get(), 0);
        } catch (Exception ex) {
            // Need to log this exception
            Output out = new Output(
                    new State(type, initialStep),
                    ResponseType.QUESTION,
                    "\uD83D\uDC49 " +defaultMessage
            );
            out.setRedirect(true);
            return out;
        }

        Optional<CarRepairTree> subTree = findByState(tree.get(), state);
        if (!subTree.isPresent()) {
            // Need to log this exception
            Output out = new Output(
                    new State(type, initialStep),
                    ResponseType.QUESTION,
                    "\uD83D\uDC49 " +defaultMessage
            );
            out.setRedirect(true);
            return out;
        }

        if (!subTree.get().getOutcomes().containsKey(userInput.getMessage())) {
            List<InlineButton> buttons = new ArrayList<>();
            for (Map.Entry<String, Map<String, String>> answer: subTree.get().getAnswers().entrySet()) {
                String answerValue = "";
                if (answer.getValue().containsKey(UKLanguage)) {
                    answerValue = answer.getValue().get(UKLanguage);
                }

                if (answerValue.isEmpty()) {
                    answerValue = answer.getKey();
                }

                buttons.add(new InlineButton(answerValue, answer.getKey()));
            }

            String question = "";
            if (subTree.get().getQuestion().containsKey(UKLanguage)) {
                question = subTree.get().getQuestion().get(UKLanguage);
            }

            if (subTree.get().getQuestion().isEmpty()) {
                question = subTree.get().getQuestion().get(defaultLanguage);
            }
            return new Output(
                    new State(type, subTree.get().getStep()),
                    ResponseType.QUESTION,
                    "??? " + question,
                    buttons
            );
        }

        List<InlineButton> previousButtons = new ArrayList<>();
        for (Map.Entry<String, Map<String, String>> answer: subTree.get().getAnswers().entrySet()) {
            String answerValue = "";
            if (answer.getValue().containsKey(UKLanguage)) {
                answerValue = answer.getValue().get(UKLanguage);
            }

            if (answerValue.isEmpty()) {
                answerValue = answer.getKey();
            }

            if (userInput.getMessage().equals(answer.getKey())) {
                previousButtons.add(new InlineButton(answerValue + " ???", answer.getKey()));
                continue;
            }

            previousButtons.add(new InlineButton(answerValue, answer.getKey()));
        }

        CarRepairTree selectedSubTree = subTree.get().getOutcomes().get(userInput.getMessage());
        if (selectedSubTree.getResult() != null) {
            String result = "";
            if (selectedSubTree.getResult().containsKey(UKLanguage)) {
                result = selectedSubTree.getResult().get(UKLanguage);
            }

            if (selectedSubTree.getResult().isEmpty()) {
                result = selectedSubTree.getResult().get(defaultLanguage);
            }
            Output out = new Output(
                    new State(type, initialStep),
                    ResponseType.MESSAGE,
                    "\uD83D\uDC49 " + result
            );

            List<Output> messages = getAnswerMessages(userInput, previousButtons);
            out.setMessages(messages);
            out.setRedirect(true);
            return out;
        }

        List<InlineButton> buttons = new ArrayList<>();
        for (Map.Entry<String, Map<String, String>> answer: selectedSubTree.getAnswers().entrySet()) {
            String answerValue = "";
            if (answer.getValue().containsKey(UKLanguage)) {
                answerValue = answer.getValue().get(UKLanguage);
            }

            if (answerValue.isEmpty()) {
                answerValue = answer.getValue().get(defaultLanguage);
            }

            buttons.add(new InlineButton(answerValue, answer.getKey()));
        }

        String question = "";
        if (selectedSubTree.getQuestion().containsKey(UKLanguage)) {
            question = selectedSubTree.getQuestion().get(UKLanguage);
        }

        if (selectedSubTree.getQuestion().isEmpty()) {
            question = selectedSubTree.getQuestion().get(defaultLanguage);
        }
        Output output = new Output(
                new State(type, selectedSubTree.getStep()),
                ResponseType.QUESTION,
                "??? " + question,
                buttons
        );

        List<Output> messages = getAnswerMessages(userInput, previousButtons);
        output.setMessages(messages);

        return output;
    }

    private List<Output> getAnswerMessages(InputMessage userInput, List<InlineButton> previousButtons) {
        List<Output> messages = new ArrayList<>();
        messages.add(new Output(
                new State(type, initialStep),
                ResponseType.EDIT_BUTTONS,
                previousButtons
        ));
        if (userInput.getType().equals(InputMessageType.CALLBACK)) {
            messages.add(new Output(
                    new State(type, initialStep),
                    ResponseType.CALLBACK_ANSWER,
                    this.messagesProperties.getMessage("you-select")
            ));
        }
        return messages;
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

    private Optional<CarRepairTree> findByState(CarRepairTree tree, State state) {

        if (state.getStep().equals(initialStep) && tree.getStep().equals(initialStep)) {
            return Optional.of(tree);
        }

        String[] stepValues = state.getStep().split(":", 2);
        if (stepValues.length < 2) {
            return Optional.empty();
        }

        if (!tree.getOutcomes().containsKey(stepValues[0])) {
            return Optional.empty();
        }

        CarRepairTree subTree = tree.getOutcomes().get(stepValues[0]);
        return findInSubTreeByState(subTree, state);
    }

    private Optional<CarRepairTree> findInSubTreeByState(CarRepairTree tree, State state) {
        if (tree.getStep().equals(state.getStep())) {
            return Optional.of(tree);
        }

        if (tree.getOutcomes() == null) {
            return Optional.empty();
        }

        for (Map.Entry<String, CarRepairTree> entry : tree.getOutcomes().entrySet()) {
            if (entry.getValue().getStep().equals(state.getStep())) {
                return Optional.of(entry.getValue());
            }

            Optional<CarRepairTree> subTree = findInSubTreeByState(entry.getValue(), state);
            if (subTree.isPresent()) {
                return subTree;
            }
        }

        return Optional.empty();
    }

    private void validateTree(CarRepairTree tree, int depth) {
        if (depth > maxDepth) {
            throw new IllegalArgumentException("Max depth exceeded");
        }

        if (tree.getResult() != null) {
            return;
        }

        // validate answers
        for (String answer: tree.getAnswers().keySet()) {
            if (!tree.getOutcomes().containsKey(answer)) {
                throw new IllegalArgumentException("Answer not present in outcomes");
            }
        }

        // validate outcomes
        for (Map.Entry<String, CarRepairTree> entry : tree.getOutcomes().entrySet()) {
            boolean found = false;
            for (String answer: tree.getAnswers().keySet()) {
                if (answer.equals(entry.getKey())) {
                    found = true;
                    break;
                }
            }

            if (!found) {
                throw new IllegalArgumentException("Outcome not present in answers");
            }

            validateTree(entry.getValue(), depth++);
        }

    }
}
