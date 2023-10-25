package com.lexisnexisrisk.truproxyapi.util;

import com.lexisnexisrisk.truproxyapi.domain.CompanySearchResult;
import com.lexisnexisrisk.truproxyapi.domain.OfficerSearchResult;
import com.lexisnexisrisk.truproxyapi.entity.Addresses;
import com.lexisnexisrisk.truproxyapi.entity.Companies;
import com.lexisnexisrisk.truproxyapi.entity.CompanyStatus;
import com.lexisnexisrisk.truproxyapi.entity.Officers;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

public final class CompanyMapper {
    public static Companies mapCompanySearchResultToCompanies(final CompanySearchResult.Company company, final List<OfficerSearchResult.Officer> officerList) {
        final Companies companies = new Companies();
        companies.setRegistrationNumber(company.getCompany_number());
        companies.setTitle(company.getTitle());
        companies.setDescription(company.getDescription());
        companies.setCompanyType(company.getCompany_type());

        // Map date_of_creation from String to LocalDate
        companies.setDateOfCreation(LocalDate.parse(company.getDate_of_creation()));

        // Assuming you have a CompanyStatus enum with values matching "company_status"
        companies.setCompanyStatus(CompanyStatus.valueOf(company.getCompany_status()));

        // Assuming you have an Addresses class for the address information
        final Addresses addresses = new Addresses();
        CompanySearchResult.Company.Address address = company.getAddress();
        addresses.setPremises(address.getPremises());
        addresses.setPostalCode(address.getPostal_code());
        addresses.setCountry(address.getCountry());
        addresses.setLocality(address.getLocality());
        addresses.setAddressLine1(address.getAddress_line_1());
        companies.setAddresses(addresses);
        companies.setOfficers(officerList.stream().map(CompanyMapper::mapToOfficers).collect(Collectors.toSet()));
        return companies;
    }

    public static Officers mapToOfficers(OfficerSearchResult.Officer officer) {
        final Officers officers = new Officers();
        officers.setName(officer.getName());
        officers.setOfficerRole(officer.getOfficer_role());
        officers.setAppointedOn(LocalDate.parse(officer.getAppointed_on()));
        final Addresses addresses = new Addresses();
        OfficerSearchResult.Address address = officer.getAddress();
        addresses.setPremises(address.getPremises());
        addresses.setPostalCode(address.getPostal_code());
        addresses.setCountry(address.getCountry());
        addresses.setLocality(address.getLocality());
        addresses.setAddressLine1(address.getAddress_line_1());
        officers.setAddresses(addresses);
        return officers;
    }
}

