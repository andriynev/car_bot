package com.andriynev.driver_helper_bot.handlers;

import com.andriynev.driver_helper_bot.dao.CarRepairTreeRepository;
import com.andriynev.driver_helper_bot.dto.*;
import com.andriynev.driver_helper_bot.enums.ResponseType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ExpertService implements Handler {

    private final CarRepairTreeRepository carRepairTreeRepository;
    private static final int maxDepth = 50;
    private static final String defaultMessage = "Take your car to a mechanic";
    private static final String initialStep = "initial";

    @Autowired
    public ExpertService(CarRepairTreeRepository carRepairTreeRepository) {
        this.carRepairTreeRepository = carRepairTreeRepository;
    }

    private final static String type = "Expert";

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
            for (String answer: subTree.get().getAnswers()) {
                buttons.add(new InlineButton(answer));
            }
            return new Output(
                    new State(type, subTree.get().getStep()),
                    ResponseType.QUESTION,
                    "❓ " + subTree.get().getQuestion(),
                    buttons
            );
        }

        List<InlineButton> previousButtons = new ArrayList<>();
        for (String answer: subTree.get().getAnswers()) {
            if (userInput.getMessage().equals(answer)) {
                previousButtons.add(new InlineButton(answer + " ✅", answer));
                continue;
            }

            previousButtons.add(new InlineButton(answer));
        }

        CarRepairTree selectedSubTree = subTree.get().getOutcomes().get(userInput.getMessage());
        if (selectedSubTree.getResult() != null) {
            Output out = new Output(
                    new State(type, initialStep),
                    ResponseType.QUESTION,
                    "\uD83D\uDC49 " + selectedSubTree.getResult()
            );
            out.setMessages(Collections.singletonList(new Output(
                    new State(type, initialStep),
                    ResponseType.EDIT_BUTTONS,
                    previousButtons
            )));
            out.setRedirect(true);
            return out;
        }

        List<InlineButton> buttons = new ArrayList<>();
        for (String answer: selectedSubTree.getAnswers()) {
            buttons.add(new InlineButton(answer));
        }
        Output output = new Output(
                new State(type, selectedSubTree.getStep()),
                ResponseType.QUESTION,
                "❓ " + selectedSubTree.getQuestion(),
                buttons
        );
        output.setMessages(Collections.singletonList(new Output(
                new State(type, initialStep),
                ResponseType.EDIT_BUTTONS,
                previousButtons
        )));

        return output;
    }

    @Override
    public String getType() {
        return type;
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
        for (String answer: tree.getAnswers()) {
            if (!tree.getOutcomes().containsKey(answer)) {
                throw new IllegalArgumentException("Answer not present in outcomes");
            }
        }

        // validate outcomes
        for (Map.Entry<String, CarRepairTree> entry : tree.getOutcomes().entrySet()) {
            boolean found = false;
            for (String answer: tree.getAnswers()) {
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
