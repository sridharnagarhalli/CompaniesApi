package com.lexisnexisrisk.truproxyapi.controller;

import com.lexisnexisrisk.truproxyapi.domain.CompanySearchResult;
import com.lexisnexisrisk.truproxyapi.domain.OfficerSearchResult;
import com.lexisnexisrisk.truproxyapi.service.CompanyService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/populateData")
public class CompanyInitController {

    private final String apiKey;
    private final String officerUrl;
    private final String companiesUrl;
    private final RestTemplate restTemplate;
    private final CompanyService companyService;

    @Autowired
    public CompanyInitController(@Value("${truproxyapi.API_KEY}") final String apiKey,
                                 @Value("${truproxyapi.GET_OFFICER_URL}") final String officerUrl,
                                 @Value("${truproxyapi.GET_COMPANIES_URL}") final String companiesUrl,
                                 final RestTemplate restTemplate, final CompanyService companyService) {
        this.apiKey = apiKey;
        this.officerUrl = officerUrl;
        this.companiesUrl = companiesUrl;
        this.restTemplate = restTemplate;
        this.companyService = companyService;
    }

    @GetMapping
    public void loadDataToDB() {
        final HttpHeaders headers = new HttpHeaders();
        headers.set("x-api-key", apiKey);
        final HttpEntity<Void> requestEntity = new HttpEntity<>(headers);
        final ResponseEntity<CompanySearchResult> companySearchResultResponseEntity = restTemplate.exchange(this.companiesUrl, HttpMethod.GET, requestEntity, CompanySearchResult.class);
        final CompanySearchResult companySearchResult = companySearchResultResponseEntity.getBody();
        final Set<String> companyNumbers = companySearchResult.getItems().stream().map(CompanySearchResult.Company::getCompany_number).collect(Collectors.toSet());
        final Map<String, List<OfficerSearchResult.Officer>> companyOfficersMap = new HashMap<>();
        companyNumbers.forEach(companyNumber -> {
            final String url = String.format(this.officerUrl, companyNumber);
            log.info("Executing request to get officers {}", url);
            final ResponseEntity<OfficerSearchResult> officersResponse = restTemplate.exchange(url, HttpMethod.GET, requestEntity, OfficerSearchResult.class);
            companyOfficersMap.put(companyNumber, officersResponse.getBody().getItems());
        });
        companyService.loadCompanyOfficerAddressData(companySearchResult, companyOfficersMap);
    }
}
