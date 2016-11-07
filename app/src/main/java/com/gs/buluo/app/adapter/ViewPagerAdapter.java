package com.gs.buluo.app.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import android.widget.ImageView;

/**
 * Created by hjn on 2016/11/1.
 */
public class ViewPagerAdapter extends PagerAdapter{
    List<Integer> lists;
    Context ct;
    private ImageView imgView;
    private int currentPosition = 0;

    public ViewPagerAdapter(Context ct, List<Integer> mList) {
        lists = mList;
        this.ct = ct;
//        options = new DisplayImageOptions.Builder().cacheInMemory(true)
//                .cacheOnDisk(true)
//                .showImageOnLoading(R.drawable.default_news_logo).build();
    }

    /**
     * 获得页面的总数
     */
    public int getCount() {
        return lists.size();
    }

    /**
     * 获得相应位置上的view container view的容器
     */
    public Object instantiateItem(ViewGroup container, final int position) {
        // imgView.setOnClickListener(this);

        imgView = new ImageView(ct);

        imgView.setScaleType(ImageView.ScaleType.FIT_XY);
        if (lists.size() == 0) {

        } else {
            //写自己的逻辑
            currentPosition = position % lists.size();
            Integer integer = lists.get(position);
            imgView.setBackgroundResource(integer);
        }

        // 给 container 添加一个view
        container.addView(imgView);
        // 返回一个和该view相对的object
        return imgView;
    }

    /**
     * 判断 view和object的对应关系
     */
    public boolean isViewFromObject(View view, Object object) {
        if (view == object) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 销毁对应位置上的object
     */
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
        object = null;
    }

    @Override
    public int getItemPosition(Object object) {
        // TODO Auto-generated method stub
        return super.getItemPosition(object);
    }
}
