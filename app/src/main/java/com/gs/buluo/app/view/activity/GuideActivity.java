package com.gs.buluo.app.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.GestureDetector;
import android.view.MotionEvent;

import com.gs.buluo.app.R;
import com.gs.buluo.app.adapter.GuidePagerAdapter;
import com.gs.buluo.app.utils.CommonUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

/**
 * Created by hjn on 2016/12/27.
 */
public class GuideActivity extends BaseActivity {
    @Bind(R.id.guide_pager)
    ViewPager viewPager;

    GuidePagerAdapter vAdapter;
    private List<Integer> list;
    private int currentItem = 0;
    public GestureDetector mGestureDetector;

    @Override
    protected void bindView(Bundle savedInstanceState) {
        setBarColor(R.color.transparent);
        list = new ArrayList<>();
        list.add(R.mipmap.guide_1);
        list.add(R.mipmap.guide_2);
        list.add(R.mipmap.guide_3);
        vAdapter = new GuidePagerAdapter(this, list);
        viewPager.setAdapter(vAdapter);
        slipToMain();
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                currentItem = position;
                vAdapter.setCurrent(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    protected int getContentLayout() {
        return R.layout.activity_guide;
    }


    private void slipToMain() {
        mGestureDetector = new GestureDetector(this,
                new GestureDetector.SimpleOnGestureListener() {
                    @Override
                    public boolean onFling(MotionEvent e1, MotionEvent e2,
                                           float velocityX, float velocityY) {
                        if (currentItem == list.size() - 1) {
                            if ((e1.getRawX() - e2.getRawX()) >= CommonUtils.getScreenWidth(getCtx())/3) {
                                Intent intent = new Intent(getCtx(), MainActivity.class);
                                startActivity(intent);
                                finish();
                                return true;
                            }
                        }
                        return false;
                    }
                });
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mGestureDetector.onTouchEvent(event);
        return super.onTouchEvent(event);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if (mGestureDetector.onTouchEvent(event)) {
            event.setAction(MotionEvent.ACTION_CANCEL);
        }
        return super.dispatchTouchEvent(event);
    }
}
