package com.gs.buluo.app.view.impl;

import com.gs.buluo.app.bean.WalletAccount;

/**
 * Created by hjn on 2016/11/18.
 */
public interface IWalletView extends IBaseView {
    void getWalletInfoFinished(WalletAccount account);
}
