package com.lexisnexisrisk.truproxyapi.domain;

import lombok.Data;
import java.util.List;

@Data
public class OfficerSearchResult {
    private String etag;
    private Links links;
    private String kind;
    private int items_per_page;
    private List<Officer> items;
    private int active_count;
    private int total_results;
    private int resigned_count;

    @Data
    public static class Officer {
        private Address address;
        private String name;
        private String appointed_on;
        private String officer_role;
        private OfficerLinks links;
    }

    @Data
    public static class Address {
        private String premises;
        private String postal_code;
        private String country;
        private String locality;
        private String address_line_1;
    }

    @Data
    public static class Links {
        private String self;
    }

    @Data
    public static class OfficerLinks {
        private String appointments;
    }
}
