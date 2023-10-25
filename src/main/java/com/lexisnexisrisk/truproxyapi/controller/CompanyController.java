package com.lexisnexisrisk.truproxyapi.controller;

import com.lexisnexisrisk.truproxyapi.domain.CompanySearchRequest;
import com.lexisnexisrisk.truproxyapi.dto.CompanyDTO;
import com.lexisnexisrisk.truproxyapi.service.CompanyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/Companies/v1")
public class CompanyController {

    private final CompanyService companyService;

    @Autowired
    public CompanyController(final CompanyService companyService) {
        this.companyService = companyService;
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public CompanyDTO getCompanies(@RequestParam(required = false, name = "active") final String active,
                                   @RequestBody final CompanySearchRequest companySearchRequest) {
        return companyService.searchCompany(companySearchRequest.getCompanyNumber(),
                companySearchRequest.getCompanyName(),
                !Objects.isNull(active) && Boolean.valueOf(active));
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public List<CompanyDTO> getAll(@RequestParam("active") final String active) {
        return companyService.getAllCompanies(!Objects.isNull(active) && Boolean.valueOf(active));
    }
}
