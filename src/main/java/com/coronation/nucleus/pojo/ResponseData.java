package com.coronation.nucleus.pojo;

import com.coronation.nucleus.enums.IResponseEnum;
import com.coronation.nucleus.interfaces.IResponseData;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang.StringUtils;

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


    public static <R> ResponseData<R> getResponseData(IResponseEnum iResponseEnum, String description, R data) {
        var resp = new ResponseData<R>();
        resp.setData(data);
        if (StringUtils.isBlank(description)) {
            resp.setResponse(iResponseEnum);
        } else {
            resp.setFormattedResponse(iResponseEnum, description);
        }
        return resp;
    }


}
