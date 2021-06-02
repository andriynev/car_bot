package com.andriynev.driver_helper_bot.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.data.annotation.Id;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class TutorialRequest {
    @JsonProperty("name")
    @NotNull
    @Size(min = 3, max = 20)
    private String name;

    @JsonProperty("service_name")
    @NotNull
    @Size(min = 3, max = 20)
    private String serviceName;

    @JsonProperty("image")
    @NotNull
    @Size(min = 10)
    private String image;

    @JsonProperty("text")
    @NotNull
    @Size(min = 10)
    private String text;

    public TutorialRequest() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return "TutorialRequest{" +
                "name='" + name + '\'' +
                ", serviceName='" + serviceName + '\'' +
                ", image='" + image + '\'' +
                ", text='" + text + '\'' +
                '}';
    }
}
