package com.gs.buluo.app.bean;

/**
 * Created by hjn on 2017/5/22.
 */

public class ConfigInfo {
    public AppInfo app;
    public PromotionInfo promotions;
    public PaySwitch switches;

    private class AppInfo {
        public String lastVersion;
        public boolean supported;
        public String router;
    }

    private class PromotionInfo {
        public String url;
        public String router;
        public boolean canSkip;
        public int skipSeconds;
    }

    private class PaySwitch {
        public boolean bf_recharge;
        public boolean bf_withdraw;
    }
}
