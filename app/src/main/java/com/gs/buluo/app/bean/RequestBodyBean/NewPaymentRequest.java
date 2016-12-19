package com.gs.buluo.app.bean.RequestBodyBean;


import com.gs.buluo.app.bean.OrderBean;

import java.util.List;

/**
 * Created by hjn on 2016/12/17.
 */

public class NewPaymentRequest {
    public String payChannel;
    public List<String> orderIds;
}
