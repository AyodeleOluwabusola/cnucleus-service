package com.coronation.nucleus.util;

import com.coronation.nucleus.entities.Shareholder;
import com.coronation.nucleus.request.ShareholderRequest;
import lombok.experimental.UtilityClass;

/**
 * @author toyewole
 */
@UtilityClass
public class ProxyTransformer {


    public Shareholder transformToShareholder (ShareholderRequest shareholderRequest) {
        Shareholder shareholder= new Shareholder();
        shareholder.setEmailAddress(shareholderRequest.getEmailAddress());
        shareholder.setFirstName(shareholderRequest.getFirstName());
        shareholder.setLastName(shareholderRequest.getLastName());

        shareholder.setShareholderTypeEnum(shareholderRequest.getShareholderType());
        shareholder.setCategory(shareholderRequest.getCategory());
        shareholder.setTotalShares(shareholderRequest.getTotalShares());
        shareholder.setDateIssued(shareholderRequest.getDateIssued());
        return shareholder;
    }



}
