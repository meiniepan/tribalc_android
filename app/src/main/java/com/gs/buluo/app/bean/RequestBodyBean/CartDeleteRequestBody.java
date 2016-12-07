package com.gs.buluo.app.bean.RequestBodyBean;

import java.util.List;

/**
 * Created by hjn on 2016/12/7.
 */
public class CartDeleteRequestBody {
    public String id;
    public List<CartDelete> goodsList;


    public class CartDelete{
        CartDeleteGoods good;
    }

    public class CartDeleteGoods{
        public String id;
    }
}
