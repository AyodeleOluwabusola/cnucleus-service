package com.coronation.nucleus.entities;

import com.coronation.nucleus.enums.ShareholderCategoryEnum;
import com.coronation.nucleus.enums.ShareholderTypeEnum;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.HashSet;
import java.util.Set;

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

    @Column(name = "EMAIL")
    private String emailAddress;

    @Column(name = "CATEGORY")
    @Enumerated(EnumType.STRING)
    private ShareholderCategoryEnum category;

    @Column(name = "TYPE")
    @Enumerated(EnumType.STRING)
    private ShareholderTypeEnum shareholderTypeEnum;

    @OneToMany(mappedBy = "shareholder")
    @Cascade(CascadeType.ALL)
    private Set<Share> shares;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @ManyToOne
    @JoinColumn(name = "COMPANY_PROFILE_FK")
    private CompanyProfile companyProfile;

    public boolean addShare(Share share) {
        if(shares == null){
            shares = new HashSet<>();
        }
        if(!shares.contains(share)){
            return shares.add(share);
        }
        return false;
    }

}
