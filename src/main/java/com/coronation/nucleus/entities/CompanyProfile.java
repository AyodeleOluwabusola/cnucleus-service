package com.coronation.nucleus.entities;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

/**
 * @author toyewole
 */

@Getter
@Setter
@Entity
@Table(name = "COMPANY_PROFILE")
public class CompanyProfile extends BaseEntity implements Serializable {

    @Column(name = "COMPANY_NAME", nullable = false)
    private String companyName;

    @Column(name = "COMPANY_TYPE", nullable = false)
    private String companyType;

    @Column(name = "INCORPORATION_DATE", nullable = false)
    private LocalDate incorporationDate;

    @Column(name = "CURRENCY", nullable = false)
    private String countryIncorporated;

    @Column(name = "COUNTRY_INCORPORATEDE", nullable = false)
    private String currency;

    @Column(name = "STAGE")
    private String stage;

    @Column(name = "TOTAL_SHARES")
    private Double  totalAuthorisedShares;

    @Column(name = "TOTAL_ALLOCATED")
    private Double totalAllocated = 0d;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @ManyToOne
    @JoinColumn(name = "USER_FK", nullable = false)
    private CTUser user;


    @OneToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "COMPANY_EQUITY_CLASS", joinColumns = @JoinColumn(name = "COMPANY_FK", referencedColumnName = "ID"),
            inverseJoinColumns= @JoinColumn(name = "EQUITY_CLASS_FK", referencedColumnName = "ID"))
    @Cascade({CascadeType.ALL})
    private Set<EquityClass> equityClasses;


    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @OneToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "COMPANY_SHAREHOLDER", joinColumns = @JoinColumn(name = "COMPANY_FK", referencedColumnName = "ID"),
            inverseJoinColumns= @JoinColumn(name = "SHAREHOLDER_FK", referencedColumnName = "ID"))
    @Cascade({CascadeType.ALL})
    private Set<Shareholder> shareholders;

    public boolean addShareholder(Shareholder shareholder) {
        if(shareholders == null){
            shareholders = new HashSet<>();
        }
        if(!shareholders.contains(shareholder)){
            return shareholders.add(shareholder);
        }
        return false;
    }

    public boolean addEquityClass(EquityClass equityClass) {
        if(equityClasses == null){
            equityClasses = new HashSet<EquityClass>();
        }
        if(!equityClasses.contains(equityClass)){
            return equityClasses.add(equityClass);
        }
        return false;
    }

    public boolean removeShareShareholders(Shareholder shareholderToRemove) {
        if(shareholders == null){
            shareholders = new HashSet<Shareholder>();
        }
        if(shareholders.contains(shareholderToRemove)){
            return shareholders.remove(shareholderToRemove);
        }
        return false;
    }


    public boolean hasShareHolder(String emailAddress){
        if(shareholders == null){
            return false;
        }else{
            for(Shareholder shareholder: shareholders){
                if(shareholder.getCompanyProfile().equals(emailAddress)){
                    return true;
                }
            }
        }
        return false;
    }

    public Set<Shareholder> getShareholders() {
        if(shareholders == null){
            shareholders = new HashSet<Shareholder>();
        }
        return shareholders;
    }

}
