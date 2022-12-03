package com.coronation.nucleus.request;

import com.coronation.nucleus.enums.ShareholderCategoryEnum;
import com.coronation.nucleus.enums.ShareholderTypeEnum;
import com.coronation.nucleus.validator.NotNullIfAnotherFieldCertainHasValue;
import com.coronation.nucleus.entities.EquityClass;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Getter
@Setter
@NotNullIfAnotherFieldCertainHasValue(fieldName = "category",  fieldValue = "INDIVIDUAL", dependFieldName = "firstName", message = "First Name of the shareholder is mandatory")
@NotNullIfAnotherFieldCertainHasValue(fieldName = "category",  fieldValue = "INDIVIDUAL", dependFieldName = "lastName", message = "Last Name of the shareholder is mandatory")
@NotNullIfAnotherFieldCertainHasValue(fieldName = "category",  fieldValue = "COMPANY", dependFieldName = "companyName", message = "Company Name of the shareholder is mandatory")
public class ShareholderRequest {

    private Long shareholderId;

    private String companyName;

    private String firstName;

    private String lastName;

    @NotBlank(message = "Email address of the shareholder is required")
    private String emailAddress;

    @NotNull(message = "Please specify total shares issued to the shareholder")
    private Long totalShares;

    @NotNull(message = "Please specify the price per share issued to the shareholder")
    private Long pricePerShare;

    @NotNull(message = "Please specify date the share was issued to the shareholder")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    private LocalDate dateIssued;

    private EquityClassRequest equityClass;

    private ShareholderCategoryEnum category = ShareholderCategoryEnum.INDIVIDUAL;

    @NotNull(message = "Please provide a valide shareholder type")
    private ShareholderTypeEnum shareholderType = ShareholderTypeEnum.FOUNDER;

}
