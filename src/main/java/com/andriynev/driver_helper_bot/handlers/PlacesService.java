package com.andriynev.driver_helper_bot.handlers;

import com.andriynev.driver_helper_bot.messages.MessagesProperties;
import com.andriynev.driver_helper_bot.dto.*;
import com.andriynev.driver_helper_bot.enums.MessageType;
import com.andriynev.driver_helper_bot.enums.PlaceOrderBy;
import com.andriynev.driver_helper_bot.enums.PlaceType;
import com.andriynev.driver_helper_bot.enums.ResponseType;
import com.andriynev.driver_helper_bot.places.PlacesApiClient;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Service
public class PlacesService implements Handler {
    private final static String type = "Places";
    private final static String nameMessageKey = "places-nearby";
    private String humanReadableName = type;
    private final static String initialStep = "initial";
    private final static String placeTypeStep = "place_type";
    private final static String openNowStep = "open_now";
    private final static String orderByStep = "order_by";
    private final static String giveLocationStep = "give_location";
    private final static String placeInfoStep = "place_info";
    private final MessagesProperties messagesProperties;
    private String description;

    private PlacesApiClient placesApiClient;

    @Autowired
    public PlacesService(PlacesApiClient placesApiClient, MessagesProperties messagesProperties) {
        this.placesApiClient = placesApiClient;
        this.messagesProperties = messagesProperties;
        this.setHumanReadableName(this.messagesProperties.getMessage(nameMessageKey));
        this.setDescription(this.messagesProperties.getMessage(nameMessageKey+"-description"));
    }

    @Override
    public Output handle(User user, State state, InputMessage userInput) {

        switch (state.getStep()){
            case placeTypeStep:
                return getPlaceTypeStepOutput(user, userInput);
            case openNowStep:
                return getOpenNowStepOutput(user, userInput);
            case orderByStep:
                return getOrderByStepOutput(user, userInput);
            case giveLocationStep:
                return getGiveLocationStepOutput(user, userInput);
            case placeInfoStep:
                return getPlaceInfoStepOutput(userInput);
            default:
                return getInitialStepOutput(user);
        }
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
    public String getDescription() {
        return description;
    }

    @Override
    public void setDescription(String description) {
        this.description = description;
    }

    private Output getPlaceInfoStepOutput(InputMessage userInput) {
        Output tryAgainOutput = handleTryAgain(userInput);
        if (tryAgainOutput != null) return tryAgainOutput;

        ObjectMapper mapper = new ObjectMapper();
        try {
            Output output = new Output(
                    new State(type, placeInfoStep),
                    ResponseType.CALLBACK_ANSWER,
                    this.messagesProperties.getMessage("on-map")
            );
            PlaceData deserializedValue = mapper.readValue(userInput.getMessage(), PlaceData.class);
            List<ReplyButton> menuButtons = new ArrayList<>(Arrays.asList(
                    new ReplyButton(this.messagesProperties.getMessage("try-again")),
                    new ReplyButton(this.messagesProperties.getMessage("main-menu"))
            ));
            List<InlineButton> buttons = new ArrayList<>(Collections.singletonList(
                    new InlineButton(
                            this.messagesProperties.getMessage("on-map") + " \uD83D\uDC47",
                            userInput.getMessage()
                    )
            ));
            output.setMessages(Arrays.asList(
                    new Output(
                            new State(type, placeInfoStep),
                            ResponseType.MESSAGE,
                            this.messagesProperties.getMessage("on-map"),
                            menuButtons,
                            null
                    ),
                    new Output(
                            new State(type, placeInfoStep),
                            deserializedValue.getLt(),
                            deserializedValue.getLg()
                    ),
                    new Output(
                            new State(type, placeInfoStep),
                            ResponseType.EDIT_BUTTONS,
                            buttons
                    )
            ));
            return output;
        } catch (Exception exception) {
            exception.printStackTrace();
        }

        return new Output(
                new State(type, placeInfoStep),
                ResponseType.MESSAGE,
                this.messagesProperties.getMessage("sorry-try-again")
        );
    }

    private Output handleTryAgain(InputMessage userInput) {
        if (userInput.getMessage().equals(this.messagesProperties.getMessage("try-again"))) {
            List<ReplyButton> menuButtons = new ArrayList<>(
                    Collections.singletonList(new ReplyButton(this.messagesProperties.getMessage("main-menu")))
            );
            Output output = new Output(
                    new State(type, initialStep),
                    ResponseType.MESSAGE,
                    "OK",
                    menuButtons,
                    null);
            output.setRedirect(true);
            return output;
        }
        return null;
    }

    private Output getGiveLocationStepOutput(User user, InputMessage userInput) {
        Output tryAgainOutput = handleTryAgain(userInput);
        if (tryAgainOutput != null) return tryAgainOutput;

        if (user.getPlacesRequest() == null) {
            return getInitialStepOutput(user);
        }

        if (userInput.getLocation() != null) {
            List<PlaceItem> placeItems = placesApiClient.getPlacesByRequest(user.getPlacesRequest(), userInput.getLocation());
            List<ReplyButton> menuButtons = new ArrayList<>(Arrays.asList(
                    new ReplyButton(this.messagesProperties.getMessage("try-again")),
                    new ReplyButton(this.messagesProperties.getMessage("main-menu"))));
            if (placeItems.size() == 0) {
                return new Output(
                        new State(type, placeInfoStep),
                        ResponseType.MESSAGE,
                        this.messagesProperties.getMessage("no-places-found-near-you"),
                        menuButtons,
                        null
                );
            }

            List<Output> placesOutputs = new ArrayList<>();
            for (PlaceItem place :
                    placeItems) {
                placesOutputs.add(preparePlaceItem(new State(type, placeInfoStep), place));
            }

            Output output = new Output(
                    new State(type, placeInfoStep),
                    ResponseType.MESSAGE,
                    this.messagesProperties.getMessage("places-near-you"),
                    menuButtons,
                    null
            );

            output.setMessages(placesOutputs);

            return output;
        }
        return new Output(
                new State(type, giveLocationStep),
                ResponseType.QUESTION,
                this.messagesProperties.getMessage("sorry-please-provide-your-location-again")
        );
    }

    private Output getPlaceTypeStepOutput(User user, InputMessage userInput) {
        List<InlineButton> placeTypesButtons = getPlaceTypesButtons();

        boolean isCorrectAnswer = false;
        for (InlineButton btn: placeTypesButtons) {
            if (btn.getData().equals(userInput.getMessage())) {
                isCorrectAnswer = true;
                btn.setTitle(btn.getTitle()+" ✅");
            }
        }

        if (!isCorrectAnswer) {
            return new Output(
                    new State(type, placeTypeStep),
                    ResponseType.QUESTION,
                    this.messagesProperties.getMessage("please-provide-place-type"),
                    placeTypesButtons);
        }

        List<InlineButton> buttons = getOpenNowButtons();
        Output output = new Output(
                new State(type, openNowStep),
                ResponseType.QUESTION,
                this.messagesProperties.getMessage("open-now"),
                buttons);
        output.setMessages(Collections.singletonList(new Output(
                new State(type, openNowStep),
                ResponseType.EDIT_BUTTONS,
                placeTypesButtons
        )));
        PlacesRequest req = user.getPlacesRequest();
        req.setPlaceType(getPlaceTypeByUserMessage(userInput.getMessage()));
        user.setPlacesRequest(req);
        return output;
    }

    private Output getOpenNowStepOutput(User user, InputMessage userInput) {
        List<InlineButton> openNowButtons = getOpenNowButtons();

        boolean isCorrectAnswer = false;
        for (InlineButton btn: openNowButtons) {
            if (btn.getData().equals(userInput.getMessage())) {
                isCorrectAnswer = true;
                btn.setTitle(btn.getTitle()+" ✅");
            }
        }

        if (!isCorrectAnswer) {
            return new Output(
                    new State(type, openNowStep),
                    ResponseType.QUESTION,
                    this.messagesProperties.getMessage("open-now"),
                    openNowButtons);
        }

        List<InlineButton> buttons = getOrderByButtons();
        Output output = new Output(
                new State(type, orderByStep),
                ResponseType.QUESTION,
                this.messagesProperties.getMessage("order-by"),
                buttons);
        output.setMessages(Collections.singletonList(new Output(
                new State(type, orderByStep),
                ResponseType.EDIT_BUTTONS,
                openNowButtons
        )));
        PlacesRequest req = user.getPlacesRequest();
        req.setOpenNow(userInput.getMessage().equals("yes"));
        user.setPlacesRequest(req);
        return output;
    }

    private Output getOrderByStepOutput(User user, InputMessage userInput) {
        List<InlineButton> orderByButtons = getOrderByButtons();

        boolean isCorrectAnswer = false;
        for (InlineButton btn: orderByButtons) {
            if (btn.getData().equals(userInput.getMessage())) {
                isCorrectAnswer = true;
                btn.setTitle(btn.getTitle()+" ✅");
            }
        }

        if (!isCorrectAnswer) {
            return new Output(
                    new State(type, orderByStep),
                    ResponseType.QUESTION,
                    this.messagesProperties.getMessage("order-by"),
                    orderByButtons);
        }

        List<ReplyButton> menuButtons = new ArrayList<>(Arrays.asList(
                new ReplyButton(this.messagesProperties.getMessage("give-location"), true),
                new ReplyButton(this.messagesProperties.getMessage("main-menu"))));
        Output output = new Output(
                new State(type, giveLocationStep),
                ResponseType.MENU,
                this.messagesProperties.getMessage("please-provide-your-location"),
                menuButtons,
                null);
        output.setMessages(Collections.singletonList(new Output(
                new State(type, giveLocationStep),
                ResponseType.EDIT_BUTTONS,
                orderByButtons
        )));

        PlacesRequest req = user.getPlacesRequest();
        req.setOrderBy(getOrderByByUserMessage(userInput.getMessage()));
        user.setPlacesRequest(req);
        return output;
    }

    private Output getInitialStepOutput(User user) {
        List<InlineButton> buttons = getPlaceTypesButtons();
        user.setPlacesRequest(new PlacesRequest());
        return new Output(
                new State(type, placeTypeStep),
                ResponseType.QUESTION,
                this.messagesProperties.getMessage("please-provide-place-type"),
                buttons);
    }

    @Override
    public String getType() {
        return type;
    }

    private Output preparePlaceItem(State state, PlaceItem item) {


        String name = item.getName();

        String openNow = item.isOpenNow() ? "✅" : "\uD83D\uDD12";

        String address = item.getAddress();

        String rating = item.getRating();

        String distance = item.getDistanceHumanReadable();

        String duration = item.getDurationHumanReadable();

        String caption = String.format("^bold%s^bold - %s\n" + // bold name
                        "%s: %s, %s: %s\n" +
                        "%s: %s, %s: %s",
                name, address,
                this.messagesProperties.getMessage("open-now"), openNow, this.messagesProperties.getMessage("rating"), rating,
                this.messagesProperties.getMessage("distance"), distance, this.messagesProperties.getMessage("duration"), duration);


        ObjectMapper mapper = new ObjectMapper();
        String placeJson = "";
        try {
            placeJson = mapper.writeValueAsString(item.getPlaceData());
        } catch (Exception exception) {
            exception.printStackTrace();
        }

        List<InlineButton> buttons = new ArrayList<>(Collections.singletonList(
                new InlineButton(
                        this.messagesProperties.getMessage("on-map"), placeJson
                )
        ));

        Output output = new Output(
                state,
                ResponseType.MESSAGE,
                caption,
                buttons
        );

        output.setMessageType(MessageType.MARKDOWN);

        return output;
    }

    private List<InlineButton> getPlaceTypesButtons() {
        List<InlineButton> buttons = new ArrayList<>();
        for (PlaceType type : PlaceType.values()) {
            buttons.add(new InlineButton(this.messagesProperties.getMessage(type.toString()), type.toString()));
        }
        return buttons;
    }

    private List<InlineButton> getOpenNowButtons() {
        return new ArrayList<>(Arrays.asList(
                new InlineButton(this.messagesProperties.getMessage("yes"), "yes"),
                new InlineButton(this.messagesProperties.getMessage("does-not-matter"), "does-not-matter")
        ));
    }

    private List<InlineButton> getOrderByButtons() {
        List<InlineButton> buttons = new ArrayList<>();
        for (PlaceOrderBy orderBy : PlaceOrderBy.values()) {
            buttons.add(new InlineButton(this.messagesProperties.getMessage(orderBy.toString()), orderBy.toString()));
        }
        return buttons;
    }

    private PlaceType getPlaceTypeByUserMessage(String message) {
        for (PlaceType type : PlaceType.values()) {
            if (type.toString().equals(message)) {
                return type;
            }
        }

        return PlaceType.CAR_REPAIR;
    }

    private PlaceOrderBy getOrderByByUserMessage(String message) {
        for (PlaceOrderBy orderBy : PlaceOrderBy.values()) {
            if (orderBy.toString().equals(message)) {
                return orderBy;
            }
        }

        return PlaceOrderBy.DISTANCE;
    }
}
