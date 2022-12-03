package com.coronation.nucleus.pojo.response;

import com.coronation.nucleus.entities.EquityClass;
import com.coronation.nucleus.request.ShareholderRequest;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Set;

@Getter
@Setter
public class CompanyProfileResponse {

    private Long companyProfileId;

    private String companyName;

    private String companyType;

    private String incorporationDate;

    private String countryIncorporated;

    private String currency;

    private String totalAuthorisedShares;

    private Long parValue;

    private Long requestingUser;

    private String stage;

    private List<ShareholderRequest> shareholders;

    private Set<EquityClass> equityClasses;

}
