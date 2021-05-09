package com.andriynev.driver_helper_bot.dto;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotNull;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Document(collection = "RSSNewsSource")
public class RSSNewsSource {

    @NotNull
    @Id
    private String id;
    @NotNull
    private String category;
    @NotNull
    private URL source;
    private Date lastUpdatedAt;

    public RSSNewsSource() {
    }

    public RSSNewsSource(String category, URL source) {
        this.category = category;
        this.source = source;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public URL getSource() {
        return source;
    }

    public void setSource(URL source) {
        this.source = source;
    }

    public Date getLastUpdatedAt() {
        return lastUpdatedAt;
    }

    public void setLastUpdatedAt(Date lastUpdatedAt) {
        this.lastUpdatedAt = lastUpdatedAt;
    }

    @Override
    public String toString() {
        return "RSSNewsSource{" +
                "id='" + id + '\'' +
                ", category='" + category + '\'' +
                ", source=" + source +
                ", lastUpdatedAt=" + lastUpdatedAt +
                '}';
    }
}
