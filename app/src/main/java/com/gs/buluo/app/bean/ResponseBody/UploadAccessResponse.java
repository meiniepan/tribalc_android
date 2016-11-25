package com.gs.buluo.app.bean.ResponseBody;

/**
 * Created by hjn on 2016/11/24.
 */
public class UploadAccessResponse {
    public  int code;
    public UploadResponseBody data;

    public  class UploadResponseBody {
        public String url;
        public String objectKey;
    }
}
