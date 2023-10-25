package com.lexisnexisrisk.truproxyapi.service;

import com.lexisnexisrisk.truproxyapi.domain.CompanySearchResult;
import com.lexisnexisrisk.truproxyapi.domain.OfficerSearchResult;
import com.lexisnexisrisk.truproxyapi.dto.CompanyDTO;
import com.lexisnexisrisk.truproxyapi.entity.Addresses;
import com.lexisnexisrisk.truproxyapi.entity.Companies;
import com.lexisnexisrisk.truproxyapi.entity.CompanyStatus;
import com.lexisnexisrisk.truproxyapi.entity.Officers;
import com.lexisnexisrisk.truproxyapi.repository.AddressRepository;
import com.lexisnexisrisk.truproxyapi.repository.CompanyRepository;
import com.lexisnexisrisk.truproxyapi.repository.OfficerRepository;
import com.lexisnexisrisk.truproxyapi.util.CompanyMapper;
import com.lexisnexisrisk.truproxyapi.util.EntityToMapper;
import io.micrometer.common.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static com.lexisnexisrisk.truproxyapi.util.CompanyMapper.mapToOfficers;

@Slf4j
@Service
public class CompanyService {

    private final CompanyRepository companyRepository;
    private final AddressRepository addressRepository;
    private final OfficerRepository officerRepository;

    @Autowired
    public CompanyService(final CompanyRepository companyRepository, final AddressRepository addressRepository, final OfficerRepository officerRepository) {
        this.companyRepository = companyRepository;
        this.addressRepository = addressRepository;
        this.officerRepository = officerRepository;
    }

    public CompanyDTO searchCompany(final String registrationNumber, final String companyName, boolean active) {
        final List<CompanyStatus> companyStatuses = active ? Arrays.asList(CompanyStatus.active) : Arrays.asList(CompanyStatus.values());
        final Companies companiesEntity;
        if(StringUtils.isNotBlank(registrationNumber) && StringUtils.isNotBlank(companyName)) {
            companiesEntity = companyRepository.findByRegistrationNumberAndCompanyStatusIn(registrationNumber, companyStatuses);
        } else if(StringUtils.isNotBlank(companyName) && StringUtils.isBlank(registrationNumber)) {
            companiesEntity = companyRepository.findByTitleAndCompanyStatusIn(companyName, companyStatuses);
        } else {
            companiesEntity = companyRepository.findByRegistrationNumberAndCompanyStatusIn(registrationNumber, companyStatuses);
        }
        return EntityToMapper.mapToCompanyDTO(companiesEntity);
    }

    public List<CompanyDTO> getAllCompanies(final Boolean active) {
        final List<Companies> companiesList = companyRepository.findAllByActive(active ? Arrays.asList(CompanyStatus.active) : Arrays.asList(CompanyStatus.values()));
        return companiesList.stream().map(EntityToMapper::mapToCompanyDTO).collect(Collectors.toList());
    }

    public void loadCompanyOfficerAddressData(final CompanySearchResult companySearchResult,
                                              final Map<String, List<OfficerSearchResult.Officer>> companyOfficersMap) {
        final List<Companies> companiesList = companySearchResult.getItems().stream()
                .map(company -> CompanyMapper.mapCompanySearchResultToCompanies(company, companyOfficersMap.get(company.getCompany_number())))
                        .collect(Collectors.toList());
        companiesList.forEach(company -> {
            final Addresses persistedAddress = addressRepository.save(company.getAddresses());
            log.info("Saved company address {}", persistedAddress.getAddressId());
            company.setAddresses(persistedAddress);
            final Companies persistedCompany = companyRepository.save(company);
            log.info("Saved Companies {}", persistedCompany.getId());
            final List<OfficerSearchResult.Officer> officerList = companyOfficersMap.get(company.getRegistrationNumber());
            officerList.forEach(officer -> {
                final Officers officersEntity = mapToOfficers(officer);
                final Addresses officerPersistedAddress = addressRepository.save(officersEntity.getAddresses());
                log.info("Saved officer address {}", officerPersistedAddress.getAddressId());
                officersEntity.setCompanies(persistedCompany);
                officersEntity.setAddresses(officerPersistedAddress);
                final Officers persistedOfficers = officerRepository.save(officersEntity);
                log.info("Saved officer {} ", persistedOfficers.getOfficerId());
            });
        });
    }
}
