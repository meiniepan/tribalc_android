package com.gs.buluo.app.bean.ResponseBody;

import com.gs.buluo.app.bean.ShoppingCart;

import java.util.List;

/**
 * Created by hjn on 2016/12/2.
 */
public class ShoppingCartResponse {
    public int code;
    public ShoppingCartResponseBody data;

    public class ShoppingCartResponseBody{
        public List<ShoppingCart> content;
        public boolean hasMore;
        public String preSkip;
        public String nextSkip;
        public String sort;
        public String status;
    }
}
