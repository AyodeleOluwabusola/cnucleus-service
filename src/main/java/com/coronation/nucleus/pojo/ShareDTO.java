package com.coronation.nucleus.pojo;

import com.coronation.nucleus.enums.ShareholderTypeEnum;
import lombok.Data;

/**
 * @author toyewole
 */
@Data
public class ShareDTO {

    Long id;
    Long shareholderId;
    String firstName;
    String lastName;
    String emailAddress;
    ShareholderTypeEnum shareholderType;
    String equityClass;

    public ShareDTO(Long id, Long shareholderId, String firstName, String lastName, String emailAddress, ShareholderTypeEnum shareholderType, String equityClass) {
        this.id = id;
        this.shareholderId = shareholderId;
        this.lastName = lastName;
        this.firstName = firstName;
        this.emailAddress = emailAddress;
        this.shareholderType = shareholderType;
        this.equityClass = equityClass;
    }
}
