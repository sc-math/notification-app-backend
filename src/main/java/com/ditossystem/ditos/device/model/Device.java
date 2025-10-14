package com.ditossystem.ditos.device.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.data.annotation.Id;

import java.time.Instant;

public class Device {

    @Id
    @JsonProperty("id")
    private String id;
    private String firebaseToken;
    private Instant firstSeen;
    private Instant lastActive;

    public Device() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFirebaseToken() {
        return firebaseToken;
    }

    public void setFirebaseToken(String firebaseToken) {
        this.firebaseToken = firebaseToken;
    }

    public Instant getFirstSeen() {
        return firstSeen;
    }

    public void setFirstSeen(Instant firstSeen) {
        this.firstSeen = firstSeen;
    }

    public Instant getLastActive() {
        return lastActive;
    }

    public void setLastActive(Instant lastActive) {
        this.lastActive = lastActive;
    }

    @Override
    public String toString() {
        return "Device{" +
                "id='" + id + '\'' +
                ", firebaseToken='" + firebaseToken + '\'' +
                ", firstSeen=" + firstSeen +
                ", lastActive=" + lastActive +
                '}';
    }
}
