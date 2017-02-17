package com.gs.buluo.app.bean.ResponseBody;

/**
 * Created by hjn on 2016/11/23.
 */
public class BaseResponse<T extends IBaseResponse> {
    public int code;
    public T data;
}
