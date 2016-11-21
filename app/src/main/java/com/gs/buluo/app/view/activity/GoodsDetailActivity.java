package com.gs.buluo.app.view.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.gs.buluo.app.Constant;
import com.gs.buluo.app.R;
import com.gs.buluo.app.bean.GoodsEntity;
import com.gs.buluo.app.utils.FrescoImageLoader;
import com.gs.buluo.app.view.widget.GoodsChoosePanel;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;

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
    private GoodsEntity goodsEntity;
    @Bind(R.id.goods_detail_price_old)
    TextView tvPriceOld;
    @Bind(R.id.goods_detail_tip)
    TextView tvTip;
    @Override
    protected void bindView(Bundle savedInstanceState) {
        goodsEntity = (GoodsEntity) getIntent().getSerializableExtra(Constant.GOODS);
        list = new ArrayList<>();
        list= goodsEntity.pictures;
        tvName.setText(goodsEntity.name);
        tvPrice.setText("￥ "+ goodsEntity.salePrice);
        tvBrand.setText(goodsEntity.brand);
        tvCount.setText(goodsEntity.saleQuantity);
        tvPriceOld.setText(goodsEntity.originalPrice);
        StringBuffer tag=new StringBuffer() ;
        for (String s :goodsEntity.tags){
            tag.append(s).append("/");
        }
        tvTip.setText(tag.toString().substring(0,tag.length()-1));
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
                GoodsChoosePanel panel=new GoodsChoosePanel(this,goodsEntity);
                panel.show();
                break;
        }
    }
}
