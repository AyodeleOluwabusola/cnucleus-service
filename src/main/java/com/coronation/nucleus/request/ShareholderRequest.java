package com.coronation.nucleus.request;

import com.coronation.nucleus.enums.ShareholderCategoryEnum;
import com.coronation.nucleus.enums.ShareholderTypeEnum;
import com.coronation.nucleus.pojo.ShareRequest;
import com.coronation.nucleus.validator.NotNullIfAnotherFieldHasCertainValue;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
@Setter
@NotNullIfAnotherFieldHasCertainValue(fieldName = "category",  fieldValue = "INDIVIDUAL", dependFieldName = "firstName", message = "First Name of the shareholder is mandatory")
@NotNullIfAnotherFieldHasCertainValue(fieldName = "category",  fieldValue = "INDIVIDUAL", dependFieldName = "lastName", message = "Last Name of the shareholder is mandatory")
@NotNullIfAnotherFieldHasCertainValue(fieldName = "category",  fieldValue = "COMPANY", dependFieldName = "companyName", message = "Company Name of the shareholder is mandatory")
public class ShareholderRequest {

    private Long shareholderId;

    private String companyName;

    private String firstName;

    private String lastName;

    @NotBlank(message = "Email address of the shareholder is required")
    private String emailAddress;

    private Long companyId;

    private List<ShareRequest> shares;

    private ShareholderCategoryEnum category = ShareholderCategoryEnum.INDIVIDUAL;

    @NotNull(message = "Please provide a valid shareholder type")
    private ShareholderTypeEnum shareholderType = ShareholderTypeEnum.FOUNDER;

}
