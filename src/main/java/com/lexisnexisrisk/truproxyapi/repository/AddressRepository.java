package com.lexisnexisrisk.truproxyapi.repository;

import com.lexisnexisrisk.truproxyapi.entity.Addresses;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AddressRepository extends JpaRepository<Addresses, Long> {
}
