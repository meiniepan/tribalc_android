package com.gs.buluo.app.view.impl;

/**
 * Created by hjn on 2016/11/10.
 */
public interface ILoginView extends IBaseView{
    void showError(int res);
    void actSuccess();
    void dealWithIdentify(int res, String displayMessage);
}
