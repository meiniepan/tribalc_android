package com.gs.buluo.app.bean;

import java.util.List;

/**
 * Created by hjn on 2016/12/2.
 */
public class ShoppingCart {
    public String id;
    public List<ListGoodsList> tListGoodsList;
    public MarkStore tMarkStore;


    public class ListGoodsList{
        public int amount;
        public ListGoods goods;
    }
}
