package com.coronation.nucleus.request;

import com.coronation.nucleus.validator.NotNullIfAnotherFieldHasCertainValue;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;

/**
 * @author AyodeleOluwabusola
 */
@Getter
@Setter
@NotNullIfAnotherFieldHasCertainValue(fieldName = "stage",  fieldValue = "2, FINAL", dependFieldName = "totalAuthorisedShares", message = "Total authorised shares issued to the company is mandatory")
@NotNullIfAnotherFieldHasCertainValue(fieldName = "stage",  fieldValue = "2, FINAL", dependFieldName = "parValue", message = "Par value is mandatory")
@NotNullIfAnotherFieldHasCertainValue(fieldName = "stage",  fieldValue = "FINAL", dependFieldName = "shareholders", message =  "Shareholder(s) must be provided")
public class CompanyProfileRequest {

    private Long companyProfileId;

    @NotBlank(message = "Company name is required")
    private String companyName;

    @NotBlank(message = "Company type is required")
    private String companyType;

    @NotNull(message = "Company Incorporation date is required")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    private LocalDate incorporationDate;

    @NotBlank(message = "Please specify country where the company was incorporated")
    private String countryIncorporated;

    @NotBlank(message = "Specify currency the shares were issued in")
    private String currency;

    private Double totalAuthorisedShares;

    private Double parValue;

    @NotNull(message = "Requesting user is required")
    private Long requestingUser;

    @NotBlank(message = "Stage is required")
    private String stage;

    private List<@Valid ShareholderRequest> shareholders;
}
