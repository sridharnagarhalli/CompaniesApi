package com.lexisnexisrisk.truproxyapi.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDate;

@ToString
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
@Builder(setterPrefix = "with")
public class OfficerDTO {
    @JsonProperty("name")
    private String name;
    @JsonProperty("office_role")
    private String officerRole;
    @JsonProperty("appointed_on")
    private LocalDate appointedOn;
    @JsonProperty("address")
    private AddressDTO address;
}
