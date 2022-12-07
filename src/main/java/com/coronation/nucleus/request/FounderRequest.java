package com.coronation.nucleus.request;

import com.coronation.nucleus.enums.ShareholderTypeEnum;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Getter
@Setter
public class FounderRequest {

    private Long founderId;

    private String firstName;

    private String lastName;

    @NotBlank(message = "Email address of the shareholder is required")
    private String emailAddress;

    @NotNull(message = "Please specify total shares issued to the shareholder")
    private Double totalShares;

    @NotNull(message = "Please specify date the share was issued to the shareholder")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    private LocalDate dateIssued;

    private Long shareId;

    @NotNull(message = "Please provide a valid shareholder type")
    private ShareholderTypeEnum shareholderType = ShareholderTypeEnum.FOUNDER;

}
