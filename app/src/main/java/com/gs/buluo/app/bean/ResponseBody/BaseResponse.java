package com.gs.buluo.app.bean.ResponseBody;

/**
 * Created by hjn on 2016/11/23.
 */
public class BaseResponse<T> {
    public int code;
    public T data;
    public String message;

    public boolean isCodeInvalid() {
        return code>300;
    }
}
