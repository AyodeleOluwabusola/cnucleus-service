package com.coronation.nucleus.request;

import com.coronation.nucleus.entities.EquityClass;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class EquityClassRequest {

    private Long id;

    @NotBlank(message = "Equity class type must be provided")
    private String type;

    @NotBlank(message = "Equity class name must be provided")
    private String name;

    public EquityClassRequest(){}

    public EquityClassRequest(EquityClass equityClass){
        this.id = equityClass.getId();
        this.type = equityClass.getType();
        this.name = equityClass.getName();
    }
}
