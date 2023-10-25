package com.lexisnexisrisk.truproxyapi.service;

import com.lexisnexisrisk.truproxyapi.dto.CompanyDTO;
import com.lexisnexisrisk.truproxyapi.entity.Addresses;
import com.lexisnexisrisk.truproxyapi.entity.Companies;
import com.lexisnexisrisk.truproxyapi.entity.CompanyStatus;
import com.lexisnexisrisk.truproxyapi.entity.Officers;
import com.lexisnexisrisk.truproxyapi.repository.AddressRepository;
import com.lexisnexisrisk.truproxyapi.repository.CompanyRepository;
import com.lexisnexisrisk.truproxyapi.repository.OfficerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class CompanyServiceTest {

    @InjectMocks
    private CompanyService companyService;

    @Mock
    private CompanyRepository companyRepository;

    @Mock
    private AddressRepository addressRepository;

    @Mock
    private OfficerRepository officerRepository;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testSearchCompany() {
        Companies mockCompany = new Companies();
        mockCompany.setRegistrationNumber("12345");
        mockCompany.setTitle("ABC Corp");
        mockCompany.setDescription("Sample description");

        Addresses mockAddress = new Addresses();
        mockAddress.setPremises("1");
        mockAddress.setAddressLine1("Sample Address");
        mockCompany.setAddresses(mockAddress);

        Officers mockOfficer = new Officers();
        mockOfficer.setName("John Doe");
        mockOfficer.setOfficerRole("Sample Role");
        Addresses mockOfficerAddress = new Addresses();
        mockOfficerAddress.setPremises("2");
        mockOfficerAddress.setAddressLine1("Officer's Address");
        mockOfficer.setAddresses(mockOfficerAddress);
        mockCompany.setOfficers(Set.of(mockOfficer));

        when(companyRepository.findByRegistrationNumberAndCompanyStatusIn(any(), any())).thenReturn(mockCompany);

        CompanyDTO result = companyService.searchCompany("12345", "ABC Corp", true);
        assertEquals("12345", result.getCompanyNumber());
        assertEquals("ABC Corp", result.getCompanyName());
        assertEquals(1, result.getOfficers().size());
        assertEquals("John Doe", result.getOfficers().get(0).getName());
    }

    @Test
    public void testGetAllCompanies() {
        Addresses mockAddress = new Addresses();
        mockAddress.setPremises("1");
        mockAddress.setAddressLine1("Sample Address");

        Officers mockOfficer = new Officers();
        mockOfficer.setName("John Doe");
        mockOfficer.setOfficerRole("Sample Role");
        mockOfficer.setAddresses(mockAddress);

        Companies company1 = new Companies();
        company1.setRegistrationNumber("12345");
        company1.setTitle("Company 1");
        company1.setCompanyStatus(CompanyStatus.active);
        company1.setOfficers(Set.of(mockOfficer));
        company1.setAddresses(mockAddress);

        Companies company2 = new Companies();
        company2.setRegistrationNumber("67890");
        company2.setTitle("Company 2");
        company2.setCompanyStatus(CompanyStatus.dissolved);
        company2.setAddresses(mockAddress);
        company2.setOfficers(Set.of(mockOfficer));

        List<Companies> mockCompanyList = Arrays.asList(company1);

        when(companyRepository.findAllByActive(any())).thenReturn(mockCompanyList);

        List<CompanyDTO> result = companyService.getAllCompanies(false);

        assertEquals(1, result.size());
        assertEquals("12345", result.get(0).getCompanyNumber());
        assertEquals("Company 1", result.get(0).getCompanyName());

        List<Companies> mockCompanyList1 = Arrays.asList(company1, company2);

        when(companyRepository.findAllByActive(any())).thenReturn(mockCompanyList1);

        List<CompanyDTO> result1 = companyService.getAllCompanies(true);

        assertEquals(2, result1.size());
    }
}
