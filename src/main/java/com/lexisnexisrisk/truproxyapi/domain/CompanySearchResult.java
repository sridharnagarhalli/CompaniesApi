package com.lexisnexisrisk.truproxyapi.domain;

import lombok.Data;

import java.util.List;

@Data
public class CompanySearchResult {
    private int page_number;
    private String kind;
    private int total_results;
    private List<Company> items;

    @Data
    public static class Company {
        private String company_status;
        private String address_snippet;
        private String date_of_creation;
        private Matches matches;
        private String description;
        private Links links;
        private String company_number;
        private String title;
        private String company_type;
        private Address address;
        private String kind;
        private List<String> description_identifier;
        private String date_of_cessation;

        @Data
        public static class Matches {
            private List<Integer> title;
        }

        @Data
        public static class Links {
            private String self;
        }

        @Data
        public static class Address {
            private String premises;
            private String postal_code;
            private String country;
            private String locality;
            private String address_line_1;
        }
    }
}


