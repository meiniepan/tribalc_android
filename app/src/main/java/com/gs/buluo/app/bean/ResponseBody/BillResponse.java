package com.gs.buluo.app.bean.ResponseBody;

import com.gs.buluo.app.bean.BillEntity;

import java.util.List;

import static com.gs.buluo.app.bean.BillEntity.*;

/**
 * Created by hjn on 2016/11/18.
 */
public class BillResponse {
    public int code;
    public BillResponseData data;

    public class BillResponseData{
        public TradingType tradingType;
        public String preSkip;
        public String nextSkip;
        public boolean hasMoren;
        public List<BillEntity> content;
    }
}
