package com.coronation.nucleus.entities;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

//TODO AUDIT table
@Getter
@Setter
@Entity
@Table(name = "EQUITY_CLASS")
public class EquityClass extends BaseEntity {

    @Column(name = "TYPE", nullable = false)
    private String type;

    @Column(name = "NAME", nullable = false)
    private String name;

    @Column(name = "CODE", nullable = false, unique = true)
    private String code;

    @Column(name = "PRICE_PER_SHARE", nullable = false)
    private Double pricePerShare;

    @Column(name = "TOTAL_SHARES", nullable = false)
    private Double totalShares;

    @Column(name = "TOTAL_ALLOCATED")
    private Double totalAllocated = 0d;

}
