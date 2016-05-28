package com.beto4812.airqueue.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

public class User {
    private String name;
    private String lastName;
    private String email;
    @JsonProperty
    private Object timestamp;

    public User() {

    }

    public String getName() {
        return name;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    @JsonIgnore
    public Long getTimestamp() {
        return (Long) timestamp;
    }
}
