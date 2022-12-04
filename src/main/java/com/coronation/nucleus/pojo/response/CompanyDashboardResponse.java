package com.coronation.nucleus.pojo.response;

import com.coronation.nucleus.interfaces.IEquityDistribution;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class CompanyDashboardResponse {

    private Long numberOfShareholders;
    private Double totalAuthorizedShares;
    private Double totalIssuedShares;
    private List<IEquityDistribution> equityClassDistribution;
}
