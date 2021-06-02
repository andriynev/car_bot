package com.andriynev.driver_helper_bot.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.data.annotation.Id;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

public class Tutorial {
    @NotNull
    @Id
    private String id;

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

    @JsonProperty("created_by")
    private String createdBy;
    @JsonProperty("updated_by")
    private String updatedBy;

    public Tutorial() {
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }

    @Override
    public String toString() {
        return "Tutorial{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", serviceName='" + serviceName + '\'' +
                ", image='" + image + '\'' +
                ", text='" + text + '\'' +
                '}';
    }
}
