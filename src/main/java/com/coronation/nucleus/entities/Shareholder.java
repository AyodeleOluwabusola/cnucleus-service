package com.coronation.nucleus.entities;

import com.coronation.nucleus.enums.ShareholderCategoryEnum;
import com.coronation.nucleus.enums.ShareholderTypeEnum;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
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

    @Column(name = "PRICE_PER_VALUE")
    private Long pricePerShare;

    @Column(name = "DATE_ISSUED")
    private LocalDate dateIssued;

    @Column(name = "CATEGORY")
    @Enumerated(EnumType.STRING)
    private ShareholderCategoryEnum category;

    @Column(name = "TYPE")
    @Enumerated(EnumType.STRING)
    private ShareholderTypeEnum shareholderTypeEnum;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "EQUITY_CLASS_FK")
    private EquityClass equityClass;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @ManyToOne
    @JoinColumn(name = "COMPANY_PROFILE_FK")
    private CompanyProfile companyProfile;
}
