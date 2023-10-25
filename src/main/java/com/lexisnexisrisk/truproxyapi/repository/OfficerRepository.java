package com.lexisnexisrisk.truproxyapi.repository;

import com.lexisnexisrisk.truproxyapi.entity.Officers;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OfficerRepository extends JpaRepository<Officers, Long> {
}
