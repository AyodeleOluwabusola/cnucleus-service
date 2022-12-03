package com.coronation.nucleus.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Getter
@Setter
public class ShareholderRequest {

    private Long shareholderId;

    @NotBlank(message = "First Name of the shareholder is mandatory")
    private String firstName;

    @NotBlank(message = "Last Name of the shareholder is mandatory")
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

    private @Valid EquityClassRequest equityClass;

}
