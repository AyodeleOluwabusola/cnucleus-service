package com.coronation.nucleus.entities;

import lombok.Data;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.time.LocalDate;
import java.util.HashSet;

/**
 * @author toyewole
 */
@Data
@Entity
@Table(name = "SHARE")
public class Share extends BaseEntity{


    @Column(name = "DATE_ISSUED")
    private LocalDate dateIssued;

    @Column(name = "TOTAL_SHARES")
    private Double totalShares;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "EQUITY_CLASS_FK")
    private EquityClass equityClass;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "SHARE_HOLDER_FK")
    private Shareholder shareholder;

}
