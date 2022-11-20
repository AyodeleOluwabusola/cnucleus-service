package com.coronation.nucleus.entities;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.time.LocalDate;

/**
 * @author toyewole
 */

@Getter
@Setter
@Entity
@Table(name = "SHARE_HOLDER")
public class Shareholder extends BaseEntity {

    @Column(name = "FIRST_NAME", nullable = false)
    private String firstName;

    @Column(name = "LAST_NAME", nullable = false)
    private String lastName;

    @Column(name = "EMAIL", nullable = false)
    private String emailAddress;

    @Column(name = "TOTAL_SHARES")
    private Long totalShares;

    @Column(name = "PAR_VALUE")
    private Long parValue;

    @Column(name = "DATE_ISSUED")
    private LocalDate dateIssued;

    @Column(name = "EQUITY_CLASS")
    private String equityClass;

    @ManyToOne
    @JoinColumn(name = "COMPANY_PROFILE")
    private CompanyProfile companyProfile;
}
