package com.gs.buluo.app.view.widget;

import android.app.Dialog;
import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.gs.buluo.app.Constant;
import com.gs.buluo.app.R;
import com.gs.buluo.app.adapter.GoodsLevel1Adapter1;
import com.gs.buluo.app.adapter.GoodsLevel1Adapter2;
import com.gs.buluo.app.bean.ListGoodsDetail;
import com.gs.buluo.app.bean.GoodsStandard;
import com.gs.buluo.app.utils.FresoUtils;

import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by hjn on 2016/11/17.
 */
public class GoodsChoosePanel extends Dialog implements View.OnClickListener {
    @Bind(R.id.goods_level1)
    RecyclerView leve1View1;
    @Bind(R.id.goods_level2)
    RecyclerView leve1View2;

    @Bind(R.id.goods_board_number)
    TextView mNumber;
    @Bind(R.id.goods_board_choose_kind)
    TextView mKind;
    @Bind(R.id.goods_board_choose_price)
    TextView mPrice;
    @Bind(R.id.goods_board_choose_remain)
    TextView mRemainNumber;
    @Bind(R.id.goods_choose_icon)
    SimpleDraweeView mIcon;

    @Bind(R.id.goods_standard_type2)
    TextView type2;
    @Bind(R.id.goods_standard_type1)
    TextView type1;

    Context mContext;

    private int nowNum = 1;
    private String leve11Key;
    private String level2Key;
    private ListGoodsDetail defaultEntity;

    public GoodsChoosePanel(Context context) {
        super(context, R.style.my_dialog);
        mContext = context;
        initView();
    }

    public void setData(GoodsStandard goodsEntity){
        initData(goodsEntity);
    }

    private void initData(final GoodsStandard entity) {
        if (entity==null){   //一级都没有
            findViewById(R.id.goods_board_repertory).setVisibility(View.GONE);
        }else if (entity.descriptions.secondary==null) {    //只有一级
            setLevelOneData(entity);
        }else if (entity.descriptions.secondary!=null){  //两级
            setLevelTwoData(entity);
        }

        FresoUtils.loadImage(Constant.BASE_IMG_URL + defaultEntity.mainPicture, mIcon);
        mPrice.setText("￥ " + defaultEntity.salePrice);
        mRemainNumber.setText(defaultEntity.repertory+"");
    }

    private void setLevelOneData(GoodsStandard entity) {
        GoodsStandard.GoodsType type = entity.descriptions.primary;
        type1.setText(type.label);
        GoodsLevel1Adapter1 adapter1 =new GoodsLevel1Adapter1(mContext,type.types);
        leve1View1.setAdapter(adapter1);
        leve11Key = (entity.descriptions.primary.types).get(0);
        final Map<String, ListGoodsDetail> goodsMap = entity.goodsIndexes;
        defaultEntity = goodsMap.get(leve11Key);

        adapter1.setOnLevelClickListener(new GoodsLevel1Adapter1.OnLevelClickListener() {
            @Override
            public void onClick(String s) {
                mKind.setText(s);
                leve11Key = s;
                changeSelectGoods(goodsMap);
            }
        });
    }

    private void setLevelTwoData(GoodsStandard entity) {
        GoodsStandard.GoodsType t1 = entity.descriptions.primary;
        type1.setText(t1.label);
        GoodsLevel1Adapter1 adapter1 =new GoodsLevel1Adapter1(mContext,t1.types);
        leve1View1.setAdapter(adapter1);
        GoodsStandard.GoodsType t2 = entity.descriptions.secondary;
        type2.setText(t2.label);
        GoodsLevel1Adapter2 adapter2 =new GoodsLevel1Adapter2(mContext,t2.types);
        leve1View2.setAdapter(adapter2);
        leve11Key = t1.types.get(0);
        level2Key = t2.types.get(0);
        final Map<String, ListGoodsDetail> goodsMap = entity.goodsIndexes;
        changeSelectGoods(goodsMap);
        adapter1.setOnLevelClickListener(new GoodsLevel1Adapter1.OnLevelClickListener() {
            @Override
            public void onClick(String s) {
                mKind.setText(s);
                leve11Key = s;
                changeSelectGoods(goodsMap);
            }
        });
        adapter2.setOnLevelClickListener(new GoodsLevel1Adapter2.OnLevelClickListener() {
            @Override
            public void onClick(String s) {
                level2Key = s;
                changeSelectGoods(goodsMap);
            }
        });
    }


    private void changeSelectGoods(Map<String, ListGoodsDetail> goodsIndexes) {
        defaultEntity = goodsIndexes.get(leve11Key + "^" + level2Key);
        if (defaultEntity==null){
            mRemainNumber.setText("0");
        }else {
            mRemainNumber.setText(defaultEntity.repertory+"");
        }
    }

    private void initView() {
        View rootView = LayoutInflater.from(mContext).inflate(R.layout.choose_board, null);
        setContentView(rootView);
        ButterKnife.bind(this);

        Window window = getWindow();
        WindowManager.LayoutParams params = window.getAttributes();
        params.width = ViewGroup.LayoutParams.MATCH_PARENT;
        params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        params.gravity = Gravity.BOTTOM;
        window.setAttributes(params);

        leve1View1.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false));
        leve1View2.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false));
        findViewById(R.id.goods_board_add).setOnClickListener(this);
        findViewById(R.id.goods_board_reduce).setOnClickListener(this);
        findViewById(R.id.goods_board_buy).setOnClickListener(this);
        findViewById(R.id.goods_board_add_car).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.goods_board_add:
                nowNum += 1;
                mNumber.setText(nowNum + "");
                break;
            case R.id.goods_board_reduce:
                if (nowNum > 1) {
                    nowNum -= 1;
                    mNumber.setText(nowNum + "");
                }
                break;
            case R.id.goods_board_buy:
                break;
            case R.id.goods_board_add_car:
                break;

        }
    }

    public void setRepertory(ListGoodsDetail goodsDetail) {
        defaultEntity = goodsDetail;
    }
}
