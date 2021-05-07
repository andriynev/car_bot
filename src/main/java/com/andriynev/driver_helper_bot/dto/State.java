package com.andriynev.driver_helper_bot.dto;


import javax.validation.constraints.NotNull;

public class State {
    @NotNull
    private String type;
    @NotNull
    private String step;

    public State(String type, String step) {
        this.type = type;
        this.step = step;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getStep() {
        return step;
    }

    public void setStep(String step) {
        this.step = step;
    }

    @Override
    public String toString() {
        return "State{" +
                "type='" + type + '\'' +
                ", step='" + step + '\'' +
                '}';
    }
}
