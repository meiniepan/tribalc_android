package com.gs.buluo.app.view.widget;

import android.app.Dialog;
import android.content.Context;
import android.support.v4.widget.TextViewCompat;
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
import com.gs.buluo.app.bean.GoodsEntity;
import com.gs.buluo.app.utils.FresoUtils;

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

    Context mContext;

    private int nowNum=1;
    private String leve11Key;
    private String level2Key;

    public GoodsChoosePanel(Context context, GoodsEntity goodsEntity) {
        super(context, R.style.my_dialog);
        mContext = context;
        initView();
        initData(goodsEntity);
    }

    private void initData(final GoodsEntity entity) {
        GoodsLevel1Adapter1 adapter1=new GoodsLevel1Adapter1(mContext,entity.standardDesc.level1);
        leve1View1.setAdapter(adapter1);

        GoodsLevel1Adapter2 adapter2=new GoodsLevel1Adapter2(mContext,entity.standardDesc.level2);
        leve1View2.setAdapter(adapter2);

        leve11Key = entity.standardDesc.level1.get(0);
        level2Key = entity.standardDesc.level2.get(0);
        String res = entity.standardRepertory.get(leve11Key + "^" + level2Key);
        setRemainNumber(res);

        adapter1.setOnLevelClickListener(new GoodsLevel1Adapter1.OnLevelClickListener() {
            @Override
            public void onClick(String s) {
                mKind.setText(s);
                leve11Key =s;
                String res = entity.standardRepertory.get(leve11Key + "^" + level2Key);
                setRemainNumber(res);
            }
        });

        adapter2.setOnLevelClickListener(new GoodsLevel1Adapter2.OnLevelClickListener() {
            @Override
            public void onClick(String s) {
                level2Key = s;
                String res = entity.standardRepertory.get(leve11Key + "^" + level2Key);
               setRemainNumber(res);
            }
        });
        FresoUtils.loadImage(Constant.BASE_IMG_URL+entity.mainPicture,mIcon);
        mPrice.setText("￥ "+entity.salePrice);
    }

    private void setRemainNumber(String res) {
        if (TextUtils.isEmpty(res)){
            mRemainNumber.setText("0");
        }else {
            mRemainNumber.setText(res);
        }
    }

    private void initView() {
        View rootView = LayoutInflater.from(mContext).inflate(R.layout.choose_board,null);
        setContentView(rootView);
        ButterKnife.bind(this);

        Window window = getWindow();
        WindowManager.LayoutParams params = window.getAttributes();
        params.width= ViewGroup.LayoutParams.MATCH_PARENT;
        params.height= ViewGroup.LayoutParams.WRAP_CONTENT;
        params.gravity = Gravity.BOTTOM;
        window.setAttributes(params);

        leve1View1.setLayoutManager(new LinearLayoutManager(mContext,LinearLayoutManager.HORIZONTAL,false));
        leve1View2.setLayoutManager(new LinearLayoutManager(mContext,LinearLayoutManager.HORIZONTAL,false));
        findViewById(R.id.goods_board_add).setOnClickListener(this);
        findViewById(R.id.goods_board_reduce).setOnClickListener(this);
        findViewById(R.id.goods_board_buy).setOnClickListener(this);
        findViewById(R.id.goods_board_add_car).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.goods_board_add:
                nowNum+=1;
                mNumber.setText(nowNum+"");
                break;
            case R.id.goods_board_reduce:
                if (nowNum>1){
                    nowNum-=1;
                    mNumber.setText(nowNum+"");
                }
                break;
            case R.id.goods_board_buy:
                break;
            case R.id.goods_board_add_car:
                break;

        }
    }
}
