package com.coronation.nucleus.pojo.response;

import com.coronation.nucleus.interfaces.IEquityDistribution;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CompanyDashboardResponse {

    private Long numberOfShareholders;
    private Long totalAuthorizedShares;
    private Long totalIssuedShares;
    private IEquityDistribution equityClassDistribution;
}
