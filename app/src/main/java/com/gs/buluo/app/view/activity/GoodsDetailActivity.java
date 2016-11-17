package com.gs.buluo.app.view.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.gs.buluo.app.Constant;
import com.gs.buluo.app.R;
import com.gs.buluo.app.bean.Goods;
import com.gs.buluo.app.utils.FrescoImageLoader;
import com.gs.buluo.app.utils.FresoUtils;
import com.gs.buluo.app.view.widget.GoodsChoosePanel;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.loader.ImageLoader;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

/**
 * Created by hjn on 2016/11/17.
 */
public class GoodsDetailActivity extends BaseActivity implements View.OnClickListener {
    private List<String> list;
    @Bind(R.id.goods_detail_pictures)
    Banner mBanner;
    @Bind(R.id.goods_detail_name)
    TextView tvName;
    @Bind(R.id.goods_detail_price)
    TextView tvPrice;
    @Bind(R.id.goods_detail_brand)
    TextView tvBrand;
    @Bind(R.id.goods_detail_income)
    TextView tvIncome;
    @Bind(R.id.goods_detail_count)
    TextView tvCount;
    @Bind(R.id.good_brand_img)
    SimpleDraweeView brandImg;
    @Override
    protected void bindView(Bundle savedInstanceState) {
        Goods goods= (Goods) getIntent().getSerializableExtra(Constant.GOODS);
        list = new ArrayList<>();
        list=goods.pictures;
        tvName.setText(goods.name);
        tvPrice.setText("￥ "+goods.salePrice);
        tvBrand.setText(goods.brand);
        tvCount.setText(goods.saleQuantity);
        mBanner.setImageLoader(new FrescoImageLoader());
        mBanner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR);
        mBanner.isAutoPlay(false);
        mBanner.setImages(list);
        mBanner.start();

        findViewById(R.id.goods_detail_back).setOnClickListener(this);
        findViewById(R.id.goods_detail_choose).setOnClickListener(this);
    }

    @Override
    protected int getContentLayout() {
        return R.layout.activity_goods_detail;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.goods_detail_back:
                finish();
                break;
            case R.id.goods_detail_choose:
                GoodsChoosePanel panel=new GoodsChoosePanel(this);
                panel.show();
                break;
        }
    }
}
