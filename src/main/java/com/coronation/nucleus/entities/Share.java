package com.coronation.nucleus.entities;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

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
@Table(name = "SHARE")
public class Share extends BaseEntity{


    @Column(name = "DATE_ISSUED")
    private LocalDate dateIssued;

    @Column(name = "TOTAL_SHARES")
    private Double totalShares;

    @ManyToOne
    @JoinColumn(name = "EQUITY_CLASS_FK")
    @Cascade(CascadeType.ALL)
    private EquityClass equityClass;

    @ManyToOne
    @JoinColumn(name = "SHARE_HOLDER_FK", nullable = false)
    @Cascade(CascadeType.ALL)
    private Shareholder shareholder;

}
