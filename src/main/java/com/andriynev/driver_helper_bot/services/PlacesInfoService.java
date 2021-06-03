package com.andriynev.driver_helper_bot.services;

import com.andriynev.driver_helper_bot.dao.PlacesInfoRepository;
import com.andriynev.driver_helper_bot.dto.InlineButton;
import com.andriynev.driver_helper_bot.dto.PlacesInfo;
import com.andriynev.driver_helper_bot.enums.PlaceType;
import com.andriynev.driver_helper_bot.messages.MessagesProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@Service
public class PlacesInfoService {
    private final PlacesInfoRepository placesInfoRepository;
    private final MessagesProperties messagesProperties;

    @Autowired
    public PlacesInfoService(PlacesInfoRepository placesInfoRepository, MessagesProperties messagesProperties) {
        this.placesInfoRepository = placesInfoRepository;
        this.messagesProperties = messagesProperties;
    }

    public Optional<PlacesInfo> find() {
        return placesInfoRepository.findFirstByIdIsNotNull();
    }


    public PlacesInfo update(PlacesInfo request) {
        Optional<PlacesInfo> placesInfo = placesInfoRepository.findFirstByIdIsNotNull();
        if (!placesInfo.isPresent()) {
            return placesInfoRepository.save(request);
        }

        placesInfo.get().setPlaces(request.getPlaces());
        return placesInfoRepository.save(placesInfo.get());
    }

    public HashMap<String, String> allowedTypes() {
        HashMap<String, String> types = new HashMap<>();
        for (PlaceType type : PlaceType.values()) {
            types.put(type.toString(), this.messagesProperties.getMessage(type.toString()));
        }

        return types;
    }
}
