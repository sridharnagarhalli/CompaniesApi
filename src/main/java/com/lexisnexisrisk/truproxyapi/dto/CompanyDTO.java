package com.lexisnexisrisk.truproxyapi.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.lexisnexisrisk.truproxyapi.entity.CompanyStatus;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDate;
import java.util.List;

@ToString
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonDeserialize(builder = CompanyDTO.CompanyDTOBuilder.class)
@Builder(setterPrefix = "with")
public class CompanyDTO {
    @JsonProperty("company_number")
    private String companyNumber;
    @JsonProperty("company_type")
    private String companyType;
    @JsonProperty("company_name")
    private String companyName;
    @JsonProperty("date_of_creation")
    private LocalDate dateOfCreation;
    @JsonProperty("company_status")
    private CompanyStatus companyStatus;
    @JsonProperty("address")
    private AddressDTO addresses;
    @JsonProperty("officers")
    private List<OfficerDTO> officers;
}
