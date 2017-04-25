package com.gs.buluo.app.bean.ResponseBody;

import com.gs.buluo.app.bean.OrderBean;

import java.util.List;

/**
 * Created by hjn on 2016/11/24.
 */
public class OrderResponse {
    public String status;
    public String nextSkip;
    public String preSkip;
    public boolean haseMore;
    public List<OrderBean> content;
}
