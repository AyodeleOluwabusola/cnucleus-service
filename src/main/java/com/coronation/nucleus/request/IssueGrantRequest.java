package com.coronation.nucleus.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
public class IssueGrantRequest {

    @NotNull(message = "Company ID must be provided")
    private Long companyId;

    @NotNull(message = "Equity Class ID must be provided")
    private Long equityClassId;

    @NotNull(message = "Number of shares to assign must be provided")
    private Double numberOfShares;

    @NotNull(message = "Date issued is required")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    private LocalDate dateIssued;

    private List<@Valid ShareholderRequest> shareholders;
}
