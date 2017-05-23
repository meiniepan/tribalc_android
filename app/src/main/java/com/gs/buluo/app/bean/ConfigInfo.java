package com.gs.buluo.app.bean;

/**
 * Created by hjn on 2017/5/22.
 */

public class ConfigInfo {
    public AppInfo app;
    public PromotionInfo promotions;
    public PaySwitch switches;

    public class AppInfo {
        public String lastVersion;
        public boolean supported;
        public String router;
    }

    public class PaySwitch {
        public boolean bf_recharge;
        public boolean bf_withdraw;
    }
}
