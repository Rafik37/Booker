package org.example.api;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public record UserBooking(
    @JsonProperty("firstname")
    String firstName,
    @JsonProperty("lastname")
    String lastName,
    @JsonProperty("totalprice")
    Number totalPrice,
    @JsonProperty("depositpaid")
     Boolean depositPaid,
    @JsonProperty("bookingdates")
    Bookingdates bookingDates,
    @JsonProperty("additionalneeds")
     String additionalNeeds
) {
}

