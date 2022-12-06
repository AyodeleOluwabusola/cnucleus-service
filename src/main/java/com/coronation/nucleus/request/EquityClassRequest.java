package com.coronation.nucleus.request;

import com.coronation.nucleus.enums.EquityTypeEnum;
import com.coronation.nucleus.validator.ValidEnumString;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
public class EquityClassRequest {

    private Long id;

    @ValidEnumString(enumClass = EquityTypeEnum.class, message = "Equity type is invalid")
    private String type;

    @NotBlank(message = "Equity class name must be provided")
    private String name;

    private String code;

    @NotNull(message = "Provide price per share")
    private Double pricePerShare;

    @NotNull(message = "Provide total share")
    private Double totalShares;

    public EquityClassRequest(){}

}
