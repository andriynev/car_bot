package com.andriynev.driver_helper_bot.handlers;

import com.andriynev.driver_helper_bot.dto.*;
import com.andriynev.driver_helper_bot.enums.MessageType;
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
    private final static String requestLocationStep = "request_location";
    private final static String giveLocationStep = "give_location";
    private final static String placeInfoStep = "place_info";

    // localization constants
    private final String openNowUkr = "Відкрито зараз";
    private final String addressUkr = "Адреса";
    private final String ratingUkr = "Рейтинг";
    private final String distanceUkr = "Відстань";
    private final String durationUkr = "Час";
    private final String locationUkr = "На карті";

    @Override
    public Output handle(User user, State state, InputMessage userInput) {
        List<InlineButton> buttons = new ArrayList<>(Arrays.asList(
                new InlineButton("Car Repair"),
                new InlineButton("Car Wash")));
        switch (state.getStep()){
            case initialStep:

                return new Output(
                        new State(type, requestLocationStep),
                        ResponseType.QUESTION,
                        "Please provide place type",
                        buttons,
                        null);
            case requestLocationStep:
                List<ReplyButton> menuButtons = new ArrayList<>(Arrays.asList(
                        new ReplyButton("Give location", true),
                        new ReplyButton("Main menu")));
                return new Output(
                        new State(type, giveLocationStep),
                        ResponseType.MENU,
                        "Please provide your location",
                        menuButtons,
                        null);
            case giveLocationStep:
                if (userInput.getLocation() != null) {
                    menuButtons = new ArrayList<>(Collections.singletonList(new ReplyButton("Main menu")));
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
                    Output output = new Output(
                            new State(type, placeInfoStep),
                            ResponseType.MESSAGE,
                            userInput.getLocation().toString(),
                            menuButtons,
                            null
                    );
                    output.setMessages(Collections.singletonList(example));
                    return output;
                }
                return new Output(
                        new State(type, giveLocationStep),
                        ResponseType.QUESTION,
                        "Sorry, please provide your location again"
                );
            case placeInfoStep:
                ObjectMapper mapper = new ObjectMapper();
                try {
                    Location deserializedValue = mapper.readValue(userInput.getMessage(), Location.class);
                    Output output = new Output(
                            new State(type, initialStep),
                            ResponseType.MESSAGE,
                            deserializedValue.toString()
                    );
                    output.setRedirect(true);
                } catch (Exception exception) {
                    exception.printStackTrace();
                }
                Output output = new Output(
                        new State(type, initialStep),
                        ResponseType.MESSAGE,
                        userInput.getMessage()
                );
                output.setRedirect(true);
            default:
                return new Output(new State(type, initialStep), ResponseType.MENU, "Please provide place type");
        }
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
        String location = "";
        try {
            location = mapper.writeValueAsString(item.getLocation());
        } catch (Exception exception) {
            exception.printStackTrace();
        }

        List<InlineButton> buttons = new ArrayList<>(Collections.singletonList(
                new InlineButton(locationUkr, location)
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
}
