package com.lexisnexisrisk.truproxyapi.domain;

import lombok.Data;

@Data
public class CompanySearchRequest {
    private String companyName;
    private String companyNumber;
}
