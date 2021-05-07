package com.andriynev.driver_helper_bot.handlers;

import com.andriynev.driver_helper_bot.dto.InputMessage;
import com.andriynev.driver_helper_bot.dto.Output;
import com.andriynev.driver_helper_bot.dto.State;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface GroupHandler extends Handler {
    public void setHandlers(List<Handler> group);
}
