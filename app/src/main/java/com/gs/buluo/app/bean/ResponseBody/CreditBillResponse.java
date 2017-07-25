package com.gs.buluo.app.bean.ResponseBody;

import com.gs.buluo.app.bean.CreditBill;

import java.util.List;

/**
 * Created by hjn on 2017/7/25.
 */

public class CreditBillResponse {
    public List<CreditBill> content;
    public String prevSkip;
    public String nextSkip;
    public boolean hasMore;
    public String sort;
}
