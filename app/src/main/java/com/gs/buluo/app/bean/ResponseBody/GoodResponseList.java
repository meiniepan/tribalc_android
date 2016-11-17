package com.gs.buluo.app.bean.ResponseBody;

import com.gs.buluo.app.bean.Goods;

import java.util.List;

/**
 * Created by hjn on 2016/11/16.
 */
public class GoodResponseList {
    /*
    * category: "MAKEUP",                                                     //当前类别
        sort: "saleQuantity,desc",                                              //当前排序规则
        prevSkip: "1478513563773,3",                                            //当前结果中的前置跳过
        nextSkip: "1478513944321,1",                                            //当前结果中的最后跳过规则，可用于下次查询
        hasMore: true,                                                          //是否还有条目待获取
        content: [                                                              //商品列表
            class Goods
        ]
    **/
    public String category;
    public String sort;
    public String prevSkip;
    public String nextSkip;
    public Boolean hasMore;
    public List<Goods> content;

}
