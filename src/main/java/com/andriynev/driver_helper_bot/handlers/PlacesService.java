package com.andriynev.driver_helper_bot.handlers;

import com.andriynev.driver_helper_bot.dto.*;
import com.andriynev.driver_helper_bot.enums.MessageType;
import com.andriynev.driver_helper_bot.enums.PlaceOrderBy;
import com.andriynev.driver_helper_bot.enums.PlaceType;
import com.andriynev.driver_helper_bot.enums.ResponseType;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Service
public class PlacesService implements Handler {
    private final static String type = "Places";
    private final static String initialStep = "initial";
    private final static String placeTypeStep = "place_type";
    private final static String openNowStep = "open_now";
    private final static String orderByStep = "order_by";
    private final static String giveLocationStep = "give_location";
    private final static String placeInfoStep = "place_info";

    // localization constants
    private final String openNowUkr = "Відкрито зараз";
    private final String addressUkr = "Адреса";
    private final String ratingUkr = "Рейтинг";
    private final String distanceUkr = "Відстань";
    private final String durationUkr = "Час";
    private final String locationUkr = "На карті";
    private final String tryAgainUkr = "Спробувати знову";

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

    private Output getPlaceInfoStepOutput(InputMessage userInput) {
        if (userInput.getMessage().equals(tryAgainUkr)) {
            List<ReplyButton> menuButtons = new ArrayList<>(Collections.singletonList(new ReplyButton("Main menu")));
            Output output = new Output(
                    new State(type, initialStep),
                    ResponseType.MESSAGE,
                    "OK",
                    menuButtons,
                    null);
            output.setRedirect(true);
            return output;
        }
        ObjectMapper mapper = new ObjectMapper();
        try {
            Output output = new Output(
                    new State(type, placeInfoStep),
                    ResponseType.CALLBACK_ANSWER,
                    "Place info"
            );
            PlaceData deserializedValue = mapper.readValue(userInput.getMessage(), PlaceData.class);
            List<ReplyButton> menuButtons = new ArrayList<>(Arrays.asList(
                    new ReplyButton(tryAgainUkr),
                    new ReplyButton("Main menu")));
            List<InlineButton> buttons = new ArrayList<>(Collections.singletonList(
                    new InlineButton(locationUkr + " ✅", userInput.getMessage())));
            output.setMessages(Arrays.asList(
                    new Output(
                            new State(type, placeInfoStep),
                            ResponseType.MESSAGE,
                            deserializedValue.getN(),
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
        Output output = new Output(
                new State(type, placeInfoStep),
                ResponseType.MESSAGE,
                "Sorry, try again"
        );
        return output;
    }

    private Output getGiveLocationStepOutput(User user, InputMessage userInput) {
        if (userInput.getLocation() != null) {
            List<ReplyButton> menuButtons = new ArrayList<>(Collections.singletonList(new ReplyButton("Main menu")));
            PlaceItem item = new PlaceItem(
                    "Place name +",
                    "вулиця Київ",
                    "2,2 км",
                    "5 хв",
                    "4.1",
                    new Location(30.44899530, 50.4476860),
                    false
            );
            Output example = preparePlaceItem(new State(type, placeInfoStep), item);
            PlaceItem item2 = new PlaceItem(
                    "Place name 2.0 +",
                    "вулиця Київ",
                    "2,6 км",
                    "8 хв",
                    "3.4",
                    new Location(30.64899530, 50.4486860),
                    false
            );
            Output example2 = preparePlaceItem(new State(type, placeInfoStep), item2);
            Output output = new Output(
                    new State(type, placeInfoStep),
                    ResponseType.MESSAGE,
                    "Places near you",
                    menuButtons,
                    null
            );
            output.setMessages(Arrays.asList(example, example2));

            return output;
        }
        return new Output(
                new State(type, giveLocationStep),
                ResponseType.QUESTION,
                "Sorry, please provide your location again"
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
                    "Please provide place type",
                    placeTypesButtons);
        }

        List<InlineButton> buttons = getOpenNowButtons();
        Output output = new Output(
                new State(type, openNowStep),
                ResponseType.QUESTION,
                "Open now",
                buttons);
        output.setMessages(Collections.singletonList(new Output(
                new State(type, openNowStep),
                ResponseType.EDIT_BUTTONS,
                buttons
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
                    "Open now",
                    openNowButtons);
        }

        List<InlineButton> buttons = getOrderByButtons();
        Output output = new Output(
                new State(type, orderByStep),
                ResponseType.QUESTION,
                "Order by",
                buttons);
        output.setMessages(Collections.singletonList(new Output(
                new State(type, orderByStep),
                ResponseType.EDIT_BUTTONS,
                openNowButtons
        )));
        PlacesRequest req = user.getPlacesRequest();
        req.setOpenNow(userInput.getMessage().equals("Yes"));
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
                    "Order by",
                    orderByButtons);
        }

        List<ReplyButton> menuButtons = new ArrayList<>(Arrays.asList(
                new ReplyButton("Give location", true),
                new ReplyButton("Main menu")));
        Output output = new Output(
                new State(type, giveLocationStep),
                ResponseType.MENU,
                "Please provide your location",
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
                "Please provide place type",
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

        String distance = item.getDistance();

        String duration = item.getDuration();

        String caption = String.format("^bold%s^bold\n" + // bold name
                        "%s: %s, %s: %s\n" +
                        "%s: %s\n" +
                        "\n" + // new line to separate text and category
                        "%s: %s, %s: %s",
                name,
                openNowUkr, openNow, ratingUkr, rating,
                addressUkr, address,
                distanceUkr, distance, durationUkr, duration);


        ObjectMapper mapper = new ObjectMapper();
        String placeJson = "";
        try {
            placeJson = mapper.writeValueAsString(item.getPlaceData());
        } catch (Exception exception) {
            exception.printStackTrace();
        }

        List<InlineButton> buttons = new ArrayList<>(Collections.singletonList(
                new InlineButton(locationUkr, placeJson)
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
            buttons.add(new InlineButton(type.getLocalizedValue(), type.toString()));
        }
        return buttons;
    }

    private List<InlineButton> getOpenNowButtons() {
        return new ArrayList<>(Arrays.asList(new InlineButton("Yes"), new InlineButton("No")));
    }

    private List<InlineButton> getOrderByButtons() {
        List<InlineButton> buttons = new ArrayList<>();
        for (PlaceOrderBy orderBy : PlaceOrderBy.values()) {
            buttons.add(new InlineButton(orderBy.getLocalizedValue(), orderBy.toString()));
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
