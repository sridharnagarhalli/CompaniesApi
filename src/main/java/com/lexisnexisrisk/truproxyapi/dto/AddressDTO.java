package com.lexisnexisrisk.truproxyapi.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@Builder(setterPrefix = "with")
public class AddressDTO {
    @JsonProperty("premises")
    private String premises;
    @JsonProperty("address_line_1")
    private String addressLine1;
    @JsonProperty("locality")
    private String locality;
    @JsonProperty("country")
    private String country;
    @JsonProperty("postal_code")
    private String postalCode;
}
