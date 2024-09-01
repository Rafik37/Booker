package org.example.api;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDate;

@JsonIgnoreProperties(ignoreUnknown = true)
public record Bookingdates(
    @JsonProperty("checkin")
    @JsonFormat(pattern = "yyyy-MM-dd")
    LocalDate checkIn,
    @JsonProperty("checkout")
    @JsonFormat(pattern = "yyyy-MM-dd")
    LocalDate checkOut
) {

}
