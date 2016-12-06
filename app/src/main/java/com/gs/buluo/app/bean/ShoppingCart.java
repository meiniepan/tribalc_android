package com.gs.buluo.app.bean;

import java.util.List;

/**
 * Created by hjn on 2016/12/2.
 */
public class ShoppingCart {
    public String id;
    public List<ListGoodsListItem> goodsList;
    public MarkStore store;
    public boolean isSelected;

    public class ListGoodsListItem {
        public int amount;
        public ListGoods goods;
        public boolean isSelected;
        public boolean isEdit;
    }
}
