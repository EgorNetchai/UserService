package ru.aston.intensive.common.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class UserNotificationDto {
    @JsonProperty
    private String email;

    @JsonProperty
    private String eventType;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }
}
