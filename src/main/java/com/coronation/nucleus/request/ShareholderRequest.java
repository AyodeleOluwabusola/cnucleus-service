package com.coronation.nucleus.request;

import com.coronation.nucleus.enums.ShareholderCategoryEnum;
import com.coronation.nucleus.enums.ShareholderTypeEnum;
import com.coronation.nucleus.validator.NotNullIfAnotherFieldHasCertainValue;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Getter
@Setter
@NotNullIfAnotherFieldHasCertainValue(fieldName = "category",  fieldValue = "INDIVIDUAL", dependFieldName = "firstName", message = "First Name of the shareholder is mandatory")
@NotNullIfAnotherFieldHasCertainValue(fieldName = "category",  fieldValue = "INDIVIDUAL", dependFieldName = "lastName", message = "Last Name of the shareholder is mandatory")
@NotNullIfAnotherFieldHasCertainValue(fieldName = "category",  fieldValue = "COMPANY", dependFieldName = "companyName", message = "Company Name of the shareholder is mandatory")
@NotNullIfAnotherFieldHasCertainValue(fieldName = "category",  fieldValue = "COMPANY", dependFieldName = "emailAddress", message = "Email address of the shareholder is mandatory")
@NotNullIfAnotherFieldHasCertainValue(fieldName = "shareholderType",  fieldValue = "DIRECTOR,INVESTOR,SECRETARY,EMPLOYEE", dependFieldName = "equityClass", message = "Equity class of the shareholder is mandatory")
public class ShareholderRequest {

    private Long shareholderId;

    private String companyName;

    private String firstName;

    private String lastName;

    private String emailAddress;

    @NotNull(message = "Please specify total shares issued to the shareholder")
    private Double totalShares;

    @NotNull(message = "Please specify date the share was issued to the shareholder")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    private LocalDate dateIssued;

    private EquityClassRequest equityClass;

    private Long shareId;

    private ShareholderCategoryEnum category = ShareholderCategoryEnum.INDIVIDUAL;

    @NotNull(message = "Please provide a valid shareholder type")
    private ShareholderTypeEnum shareholderType = ShareholderTypeEnum.FOUNDER;
}
