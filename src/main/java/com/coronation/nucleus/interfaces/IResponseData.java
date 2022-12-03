package com.coronation.nucleus.interfaces;

import com.coronation.nucleus.enums.IResponseEnum;

/**
 * @author toyewole
 */
public interface IResponseData<T>  extends  IResponse{

    T getData();
    void setData (T data);

    void setResponse(IResponseEnum iResponseEnum);

    void setFormattedResponse(IResponseEnum iResponseEnum, String value);
}
