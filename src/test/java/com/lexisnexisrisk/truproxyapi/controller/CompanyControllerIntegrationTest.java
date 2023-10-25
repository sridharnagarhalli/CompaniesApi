package com.lexisnexisrisk.truproxyapi.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.lexisnexisrisk.truproxyapi.dto.CompanyDTO;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StreamUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CompanyControllerIntegrationTest {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @LocalServerPort
    private int port;

    @Autowired
    private ResourceLoader resourceLoader;

    @Autowired
    private TestRestTemplate restTemplate;

    private WireMockServer wireMockServer;

    @BeforeEach
    public void setup() {
        wireMockServer = new WireMockServer();
        wireMockServer.start();
        WireMock.configureFor("localhost", wireMockServer.port());
    }

    @AfterEach
    public void tearDown() {
        wireMockServer.stop();
    }

    @Test
    public void testGetCompanies() throws IOException {
        final String url = "http://localhost:" + port + "/TruProxyAPI/rest/Companies/v1?active=true";
        Resource jsonResource = resourceLoader.getResource("classpath:OneCompany.json");
        String jsonContent = StreamUtils.copyToString(jsonResource.getInputStream(), StandardCharsets.UTF_8);
        WireMock.stubFor(WireMock.get(WireMock.urlMatching(url))
                .willReturn(WireMock.aResponse().withHeader("Content-Type", "application/json")
                        .withBody(jsonContent)));
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", MediaType.APPLICATION_JSON_VALUE);
        String request = """
                {
                    "companyNumber": "201"
                }""";
        HttpEntity<String> requestEntity = new HttpEntity<>(request, headers);
        ResponseEntity<CompanyDTO> responseEntity = restTemplate.exchange(url, HttpMethod.POST, requestEntity, CompanyDTO.class);
        CompanyDTO companyDTO = responseEntity.getBody();
        assertNotNull(companyDTO);
        assertNotNull(companyDTO.getCompanyName());
        assertNotNull(companyDTO.getAddresses());
        assertNotNull(companyDTO.getAddresses().getCountry());
        assertNotNull(companyDTO.getOfficers());
        assertNotNull(companyDTO.getOfficers().get(0).getName());
        assertNotNull(companyDTO.getOfficers().get(0).getAddress());
        assertNotNull(companyDTO.getOfficers().get(0).getAddress().getCountry());
    }

    @Test
    public void testGetAll() throws IOException {
        final String url = "http://localhost:" + port + "/TruProxyAPI/rest/Companies/v1?active=true";
        Resource jsonResource = resourceLoader.getResource("classpath:companies-data.json");
        String jsonContent = StreamUtils.copyToString(jsonResource.getInputStream(), StandardCharsets.UTF_8);

        WireMock.stubFor(WireMock.get(WireMock.urlEqualTo(url))
                .willReturn(WireMock.aResponse().withHeader("Content-Type", "application/json")
                        .withBody(jsonContent)));
        final ResponseEntity<List<CompanyDTO>> responseEntity = restTemplate.exchange(url, HttpMethod.GET,
                null, new ParameterizedTypeReference<List<CompanyDTO>>() {});
        final List<CompanyDTO> list = responseEntity.getBody();
        assertFalse(list.isEmpty());
        assertEquals(1, list.size());
        assertNotNull(list.get(0).getCompanyName());
        assertNotNull(list.get(0).getAddresses());
        assertNotNull(list.get(0).getAddresses().getCountry());
        assertNotNull(list.get(0).getOfficers());
        assertNotNull(list.get(0).getOfficers().get(0).getAddress().getCountry());
    }
}

