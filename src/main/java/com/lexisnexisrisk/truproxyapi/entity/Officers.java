package com.lexisnexisrisk.truproxyapi.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.context.annotation.Lazy;

import java.time.LocalDate;

@ToString
@NoArgsConstructor
@Getter
@Setter
@Entity
public class Officers {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long officerId;

    private String name;
    private String officerRole;
    private LocalDate appointedOn;
    private LocalDate resignedOn;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "address_id")
    private Addresses addresses;

    @ManyToOne
    @JoinColumn(name = "company_id")
    private Companies companies;
}
