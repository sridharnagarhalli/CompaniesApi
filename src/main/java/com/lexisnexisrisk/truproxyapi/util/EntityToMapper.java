package com.lexisnexisrisk.truproxyapi.util;

import com.lexisnexisrisk.truproxyapi.dto.AddressDTO;
import com.lexisnexisrisk.truproxyapi.dto.CompanyDTO;
import com.lexisnexisrisk.truproxyapi.dto.OfficerDTO;
import com.lexisnexisrisk.truproxyapi.entity.Addresses;
import com.lexisnexisrisk.truproxyapi.entity.Companies;
import com.lexisnexisrisk.truproxyapi.entity.Officers;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public final class EntityToMapper {

    public static CompanyDTO mapToCompanyDTO(final Companies companies) {
        CompanyDTO companyDTO = CompanyDTO.builder()
                .withCompanyNumber(companies.getRegistrationNumber())
                .withCompanyType(companies.getCompanyType())
                .withCompanyName(companies.getTitle())
                .withCompanyStatus(companies.getCompanyStatus())
                .withDateOfCreation(companies.getDateOfCreation())
                .withAddresses(mapToAddressDTO(companies.getAddresses()))
                .withOfficers(mapToOfficerDTO(companies.getOfficers()))
                .build();
        return companyDTO;
    }

    private static List<OfficerDTO> mapToOfficerDTO(final Set<Officers> officers) {
        final List<OfficerDTO> officerDTOs = new ArrayList<>();
        officers.forEach(officer -> officerDTOs.add(OfficerDTO.builder()
                .withAppointedOn(officer.getAppointedOn())
                .withOfficerRole(officer.getOfficerRole())
                .withName(officer.getName())
                .withAddress(mapToAddressDTO(officer.getAddresses()))
                .build()));
        return officerDTOs;
    }

    private static AddressDTO mapToAddressDTO(final Addresses addresses) {
        return AddressDTO.builder().withPremises(addresses.getPremises())
                .withAddressLine1(addresses.getAddressLine1())
                .withLocality(addresses.getLocality())
                .withCountry(addresses.getCountry())
                .withPostalCode(addresses.getPostalCode())
                .build();
    }
}
