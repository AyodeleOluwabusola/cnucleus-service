package com.coronation.nucleus.pojo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;

/**
 * @author toyewole
 */
@Data
public class ShareRequest {

    private Long shareId;

    private Long equityId;

    @NotNull(message = "Please specify total shares issued to the shareholder")
    private Double totalShares;

    @NotNull(message = "Please specify the price per share issued to the shareholder")
    private Long pricePerShare;

    @NotNull(message = "Please specify date the share was issued to the shareholder")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    private LocalDate dateIssued;

}
