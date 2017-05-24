package com.gs.buluo.app.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.gs.buluo.app.Constant;
import com.gs.buluo.app.R;
import com.gs.buluo.app.TribeApplication;
import com.gs.buluo.app.adapter.BankCardListAdapter;
import com.gs.buluo.app.bean.BankCard;
import com.gs.buluo.app.presenter.BankCardPresenter;
import com.gs.buluo.app.presenter.BasePresenter;
import com.gs.buluo.app.utils.ToastUtils;
import com.gs.buluo.app.view.impl.ICardView;
import com.gs.buluo.common.widget.StatusLayout;

import java.util.List;

import butterknife.Bind;

/**
 * Created by hjn on 2016/11/23.
 */
public class BankCardActivity extends BaseActivity implements ICardView{
    @Bind(R.id.card_list)
    ListView cardList;
    @Bind(R.id.card_list_layout)
    StatusLayout mStatusLayout;
    @Bind(R.id.card_manager)
    TextView manage;
    private BankCardListAdapter adapter;

    private boolean canDelete=false;
    private boolean isFromCash;

    @Override
    protected void bindView(Bundle savedInstanceState) {
        isFromCash = getIntent().getBooleanExtra(Constant.CASH_FLAG, false);
        adapter = new BankCardListAdapter(this);
        cardList.setAdapter(adapter);

        ((BankCardPresenter)mPresenter).getCardList(TribeApplication.getInstance().getUserInfo().getId());
        findViewById(R.id.card_add_card).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TribeApplication.getInstance().getUserInfo().getIdNo()==null){
                    ToastUtils.ToastMessage(getCtx(),"您尚未进行身份认证，无法绑定银行卡");
                    return;
                }
                startActivity(new Intent(BankCardActivity.this,AddBankCardActivity.class));
            }
        });
        findViewById(R.id.card_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        manage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (canDelete){
                    hideDeleteView();
                }else {
                    showDeleteView();
                }
            }
        });
        if (isFromCash) {
            cardList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    BankCard card = (BankCard) adapter.getItem(position);
                    Intent intent = new Intent();
                    intent.putExtra(Constant.BANK_CARD, card);
                    setResult(RESULT_OK, intent);
                    finish();
                }
            });
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        ((BankCardPresenter)mPresenter).getCardList(TribeApplication.getInstance().getUserInfo().getId());
    }

    private void hideDeleteView() {
        adapter.hideDelete();
        canDelete=false;
        manage.setText(R.string.manage);
    }

    private void showDeleteView() {
        adapter.showDelete();
        manage.setText(R.string.finish);
        canDelete=true;
    }

    @Override
    protected int getContentLayout() {
        return R.layout.activity_card_list;
    }

    @Override
    protected BasePresenter getPresenter() {
        return new BankCardPresenter();
    }

    @Override
    public void getCardInfoSuccess(List<BankCard> data) {
        adapter.setData(data);
    }

    @Override
    public void showError(int res) {
        ToastUtils.ToastMessage(this,getString(res));
    }
}
