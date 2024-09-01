package org.example.api;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
@JsonIgnoreProperties(ignoreUnknown = true)
public record CreateBooking(
        @JsonProperty("bookingid")
        Number bookingId,
        @JsonProperty("booking")
        UserBooking booking
) {
}
