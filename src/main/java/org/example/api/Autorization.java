package org.example.api;

import com.fasterxml.jackson.annotation.JsonProperty;

public record Autorization(
        @JsonProperty("username")
        String userName,
        @JsonProperty("password")
        String password
) {


}
