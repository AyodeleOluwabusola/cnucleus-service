package com.coronation.nucleus.pojo.response;

import com.coronation.nucleus.interfaces.IEquityDistribution;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class CompanyDashboardResponse {

    private Long numberOfShareholders;
    private Long totalAuthorizedShares;
    private Long totalIssuedShares;
    private List<IEquityDistribution> equityClassDistribution;
}
