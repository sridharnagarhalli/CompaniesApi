package com.lexisnexisrisk.truproxyapi.repository;

import com.lexisnexisrisk.truproxyapi.entity.Companies;
import com.lexisnexisrisk.truproxyapi.entity.CompanyStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CompanyRepository extends JpaRepository<Companies, Long> {

    Companies findByRegistrationNumberAndCompanyStatusIn(String registrationNumber, List<CompanyStatus> companyStatus);
    Companies findByTitleAndCompanyStatusIn(String title, List<CompanyStatus> companyStatus);
    /*@Query("select company from Companies company where company.companyStatus in :companyStatus" +
            " and (:title IS NULL OR company.title = :title) and company.registrationNumber = :registrationNumber")
    Companies findByTitleOrRegistrationNumberAndCompanyStatus(@Param("title") String title,
                                                              @Param("registrationNumber") String registrationNumber,
                                                              @Param("companyStatus") List<CompanyStatus> companyStatus);*/

    @Query("select company from Companies company where company.companyStatus in :companyStatus")
    List<Companies> findAllByActive(@Param("companyStatus") List<CompanyStatus> companyStatus);

    @Query("SELECT c FROM Companies c JOIN FETCH c.officers o WHERE c.registrationNumber = :registrationNumber " +
            "AND (c.title = :title OR :title IS NULL) AND o.resignedOn IS NULL")
    Companies findCompaniesByRegistrationNumberAndTitle(
            @Param("registrationNumber") String registrationNumber,
            @Param("title") String title
    );
}
