package com.gs.buluo.app.bean;

import java.util.ArrayList;

/**
 * Created by Solang on 2017/7/19.
 */

public class StoreListResponse {
    public String sort;
    public String category;
    public String prevSkip;
    public String nextSkip;
    public boolean hasMore;
    public ArrayList<StoreInfo> content;
}
