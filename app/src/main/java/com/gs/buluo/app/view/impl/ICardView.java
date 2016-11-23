package com.gs.buluo.app.view.impl;

import com.gs.buluo.app.bean.BankCard;

import java.util.List;

/**
 * Created by hjn on 2016/11/23.
 */
public interface ICardView extends IBaseView{
    void getCardInfoSuccess(List<BankCard> data);
}
