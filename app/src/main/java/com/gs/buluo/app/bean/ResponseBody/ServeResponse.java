package com.gs.buluo.app.bean.ResponseBody;

import com.gs.buluo.app.bean.ListStoreSetMeal;

import java.util.List;

/**
 * Created by hjn on 2016/11/29.
 */
public class ServeResponse {
    public int code;
    public ServeResponseBody data;

    public class ServeResponseBody{
        public String category;
        public String sort;
        public String prevSkip;
        public String nextSkip;
        public boolean hasMore;
        public List<ListStoreSetMeal> content;
    }
}
