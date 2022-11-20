package com.coronation.nucleus.pojo;

import com.coronation.nucleus.IResponseEnum;
import com.coronation.nucleus.interfaces.IResponseData;
import lombok.Getter;
import lombok.Setter;

/**
 * @author toyewole
 */

@Getter
@Setter
public class ResponseData<T> implements IResponseData<T> {

    private int code;
    private String description;
    private T data;


    @Override
    public void setResponse(IResponseEnum iResponseEnum) {
        this.code = iResponseEnum.getCode();
        this.description = iResponseEnum.getDescription();
    }

    @Override
    public void setFormattedResponse(IResponseEnum iResponseEnum, String description) {
        this.code = iResponseEnum.getCode();
        this.description = String.format(iResponseEnum.getDescription(), description);
    }


}
