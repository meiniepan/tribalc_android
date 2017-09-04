package com.gs.buluo.app.view.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewCompat;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.gs.buluo.app.Constant;
import com.gs.buluo.app.R;
import com.gs.buluo.app.adapter.GoodNewDetailAdapter;
import com.gs.buluo.app.bean.GoodsStandard;
import com.gs.buluo.app.bean.ListGoodsDetail;
import com.gs.buluo.app.presenter.BasePresenter;
import com.gs.buluo.app.presenter.GoodsDetailPresenter;
import com.gs.buluo.app.utils.CommonUtils;
import com.gs.buluo.app.utils.FrescoImageLoader;
import com.gs.buluo.app.utils.FresoUtils;
import com.gs.buluo.app.utils.ToastUtils;
import com.gs.buluo.app.view.impl.IGoodDetialView;
import com.gs.buluo.app.view.widget.panel.GoodsChoosePanel;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;

import org.xutils.x;

import java.util.ArrayList;

import butterknife.Bind;

/**
 * Created by hjn on 2016/11/17.
 */
public class GoodsDetailActivity extends BaseActivity implements View.OnClickListener, IGoodDetialView, GoodsChoosePanel.AddCartListener, GoodsChoosePanel.OnShowInDetail {
    private ArrayList<String> list;
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
    @Bind(R.id.goods_detail_standard)
    TextView tvStandard;

    @Bind(R.id.goods_detail_price_old)
    TextView tvPriceOld;
    @Bind(R.id.goods_detail_tip)
    TextView tvTip;
    @Bind(R.id.goods_detail_price_point)
    TextView tvPricePoint;
    @Bind(R.id.goods_detail_detail)
    ListView listView;

    ImageView ivDefault;
    Context context;
    private GoodsChoosePanel panel;
    private String id;
    private ListGoodsDetail goodsEntity;
    private Button addCart;

    @Override
    protected void bindView(Bundle savedInstanceState) {
        setBarColor(R.color.transparent);
        ivDefault = (ImageView) findViewById(R.id.goods_detail_default);
        ViewCompat.setTransitionName(ivDefault, Constant.DETAIL_HEADER_IMAGE);
        x.image().bind(ivDefault, FrescoImageLoader.formatImageUrl(getIntent().getStringExtra(Constant.GOODS_PIC)));
        context = this;
        addCart = (Button) findViewById(R.id.goods_detail_add_car);
        mBanner.setImageLoader(new FrescoImageLoader());
        mBanner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR);
        mBanner.isAutoPlay(false);
        id = getIntent().getStringExtra(Constant.GOODS_ID);
        ((GoodsDetailPresenter) mPresenter).getGoodsDetail(id);

        findViewById(R.id.goods_detail_back).setOnClickListener(this);
        addCart.setOnClickListener(this);
        findViewById(R.id.goods_detail_shopping_car).setOnClickListener(this);
        findViewById(R.id.goods_detail_buy).setOnClickListener(this);
        panel = new GoodsChoosePanel(this, this);
        panel.setAddCartListener(this);
        listView.setFocusable(false);
    }


    @Override
    protected int getContentLayout() {
        return R.layout.activity_goods_detail;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.goods_detail_back:
                finish();
                break;
            case R.id.goods_detail_choose:
                if (!checkUser(context)) return;
                if (panel != null) {
                    panel.setType(0,goodsEntity.tMarkStore);
                    panel.show();
                }
                break;
            case R.id.goods_detail_add_car:
                if (!checkUser(context)) return;
                if (panel != null) {
                    panel.setType(1,goodsEntity.tMarkStore);
                    panel.show();
                }
                break;
            case R.id.goods_detail_shopping_car:
                if (!checkUser(context)) return;
                startActivity(new Intent(context, ShoppingCarActivity.class));
                break;
            case R.id.goods_detail_buy:
                if (!checkUser(context)) return;
                if (panel != null) {
                    panel.setType(2,goodsEntity.tMarkStore);
                    panel.show();
                }
                break;
        }
    }

    public void setGoodsPrice(String goodsPrice) {
        String[] array = goodsPrice.split("\\.");
        if (array.length > 1) {
            tvPrice.setText("￥" + array[0] + ".");
            tvPricePoint.setText(array[1]);
        } else {
            tvPrice.setText("￥" + goodsPrice);
        }
    }

    @Override
    protected BasePresenter getPresenter() {
        return new GoodsDetailPresenter();
    }

    @Override
    public void showError(int res) {
        ToastUtils.ToastMessage(this, R.string.connect_fail);
        addCart.setEnabled(false);
    }

    @Override
    public void getDetailSuccess(ListGoodsDetail goodsEntity) {
        findViewById(R.id.goods_detail_choose).setOnClickListener(this);
        mBanner.setVisibility(View.VISIBLE);
        this.goodsEntity = goodsEntity;
        panel.setRepertory(goodsEntity);
        if (this.goodsEntity.standardId != null) {
            showLoadingDialog();
            ((GoodsDetailPresenter) mPresenter).getGoodsStandard(goodsEntity.standardId);
        } else {         //商品无规格信息，使用默认商品信息
            panel.setData(null);
        }
        setData(goodsEntity);
    }

    private void setData(ListGoodsDetail goodsEntity) {
        list = new ArrayList<>();
        if (goodsEntity.pictures != null && goodsEntity.pictures.size() > 0) {
            list.addAll(goodsEntity.pictures);
            list.remove(goodsEntity.mainPicture);
            list.add(0, goodsEntity.mainPicture);
        }
        tvName.setText(goodsEntity.title);
        setGoodsPrice(goodsEntity.salePrice);
        tvCount.setText(goodsEntity.saleQuantity);
        if (goodsEntity.detail != null)
            listView.setAdapter(new GoodNewDetailAdapter(getCtx(), goodsEntity.detail, true));
        CommonUtils.setListViewHeightBasedOnChildren(listView);
        if (goodsEntity.tMarkStore != null) {
            FresoUtils.loadImage(goodsEntity.tMarkStore.logo, brandImg);
            tvBrand.setText(goodsEntity.tMarkStore.name);
            tvPhone.setText(goodsEntity.tMarkStore.phone);
        }

        tvPriceOld.setText("¥" + (goodsEntity.originPrice == null ? 0 : goodsEntity.originPrice));
        if (goodsEntity.tags != null && goodsEntity.tags.size() > 0) {
            StringBuffer tag = new StringBuffer();
            for (String s : goodsEntity.tags) {
                tag.append(s).append("/");
            }
            tvTip.setText(tag.toString().substring(0, tag.length() - 1));
        }
        mBanner.setImages(list);
        mBanner.start();

        if (goodsEntity.snapshot || !goodsEntity.published) {
            addCart.setEnabled(false);
        }
    }

    @Override
    public void getStandardSuccess(GoodsStandard standard) {
        panel.setData(standard);
        if (standard == null || standard.descriptions == null || standard.descriptions.primary == null)
            return;
        tvStandard.setText("请选择 " + standard.descriptions.primary.label + "  " + (standard.descriptions.secondary == null ? "" : standard.descriptions.secondary.label));
    }

    @Override
    public void addSuccess() {
        ToastUtils.ToastMessage(this, R.string.add_success);
        panel.dismiss();
    }

    public void addToShoppingCart(String id, int num) {
        showLoadingDialog();
        ((GoodsDetailPresenter) mPresenter).addCartItem(id, num);
    }

    @Override
    public void onAddCart(String id, int nowNum) {
        addToShoppingCart(id, nowNum);
    }

    @Override
    public void onShow(ListGoodsDetail goodsDetail, int num) {
        String standard = goodsDetail.standardSnapshot;
        if (standard != null) {
            String[] split = standard.split("\\|");
            if (split.length > 1) {
                tvStandard.setText("已选：" + split[0].split(":")[1] + " " + split[1].split(":")[1] + "        " + num + "件");
            } else {
                tvStandard.setText(split[0].split(":")[1] + "        " + num + "件");
            }
        } else {
            tvStandard.setText(num + "件");
        }
        tvStandard.setTextColor(0xff9a9a9a);
        setData(goodsDetail);
    }
}
