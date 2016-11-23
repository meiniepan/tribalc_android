package com.gs.buluo.app.view.activity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.gs.buluo.app.Constant;
import com.gs.buluo.app.R;
import com.gs.buluo.app.bean.ListGoodsDetail;
import com.gs.buluo.app.bean.GoodsStandard;
import com.gs.buluo.app.presenter.BasePresenter;
import com.gs.buluo.app.presenter.GoodsDetailPresenter;
import com.gs.buluo.app.utils.FrescoImageLoader;
import com.gs.buluo.app.view.impl.IGoodDetialView;
import com.gs.buluo.app.view.widget.GoodsChoosePanel;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

/**
 * Created by hjn on 2016/11/17.
 */
public class GoodsDetailActivity extends BaseActivity implements View.OnClickListener,IGoodDetialView {
    private List<String> list;
    @Bind(R.id.goods_detail_pictures)
    Banner mBanner;
    @Bind(R.id.goods_detail_name)
    TextView tvName;
    @Bind(R.id.goods_detail_price)
    TextView tvPrice;
    @Bind(R.id.goods_detail_brand)
    TextView tvBrand;
    @Bind(R.id.goods_detail_phone)
    TextView tvPhone;
    @Bind(R.id.goods_detail_count)
    TextView tvCount;
    @Bind(R.id.good_brand_img)
    SimpleDraweeView brandImg;

    @Bind(R.id.goods_detail_price_old)
    TextView tvPriceOld;
    @Bind(R.id.goods_detail_tip)
    TextView tvTip;
    @Bind(R.id.goods_detail_price_point)
    TextView tvPricePoint;

    Context context;
    private GoodsChoosePanel panel;

    @Override
    protected void bindView(Bundle savedInstanceState) {
        context=this;
        String id = getIntent().getStringExtra(Constant.GOODS_ID);

        ((GoodsDetailPresenter)mPresenter).getGoodsDetaii(id);
        showLoadingDialog();

        findViewById(R.id.goods_detail_back).setOnClickListener(this);
        findViewById(R.id.goods_detail_choose).setOnClickListener(this);
        findViewById(R.id.goods_detail_add_car).setOnClickListener(this);
        findViewById(R.id.goods_detail_shopping_car).setOnClickListener(this);
        findViewById(R.id.goods_detail_collect).setOnClickListener(this);
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
                if (!checkUser(context))return;
                if(panel!=null){
                    panel.show();
                }
                break;
            case R.id.goods_detail_add_car:
                if (!checkUser(context))return;
                break;
            case R.id.goods_detail_shopping_car:
                if (!checkUser(context))return;
                break;
            case R.id.goods_detail_collect:
                if (!checkUser(context))return;
                break;

        }
    }

    public void setGoodsPrice(String goodsPrice) {
        String[] array = goodsPrice.split("\\.");

        if (array.length>1){
            tvPrice.setText("￥ "+ array[0]+".");
            tvPricePoint.setText(array[1]);
        }else {
            tvPrice.setText("￥ "+ goodsPrice);
        }
    }

    @Override
    protected BasePresenter getPresenter() {
        return new GoodsDetailPresenter();
    }

    @Override
    public void showError(int res) {

    }

    @Override
    public void getDetailSuccess(ListGoodsDetail goodsEntity) {
        dismissDialog();
        if (goodsEntity.standardId!=null){
            ((GoodsDetailPresenter)mPresenter).getGoodsStandard(goodsEntity.standardId);
        }else {         //商品无规格信息，使用默认商品信息
            panel = new GoodsChoosePanel(this);
            panel.setRepertory(goodsEntity);
            panel.setData(null);
        }

        list = new ArrayList<>();
        list= goodsEntity.pictures;

        tvName.setText(goodsEntity.title);
        setGoodsPrice(goodsEntity.salePrice);

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
    }

    @Override
    public void getStandardSuccess(GoodsStandard standard) {
        panel = new GoodsChoosePanel(this);
        panel.setData(standard);
    }
}
