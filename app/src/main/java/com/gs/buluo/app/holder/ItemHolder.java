package com.gs.buluo.app.holder;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AlertDialog;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.gs.buluo.app.R;
import com.gs.buluo.common.utils.CommonUtils;
import com.gs.buluo.common.utils.DensityUtils;
import com.gs.buluo.common.utils.ToastUtils;

import java.util.List;

import em.sang.com.allrecycleview.holder.CustomHolder;


public class ItemHolder extends CustomHolder<String> {
    private AlertDialog dialog = null;
    private Activity mActivity;

    public ItemHolder(View itemView) {
        super(itemView);
    }

    public ItemHolder(List datas, View itemView) {
        super(datas, itemView);
    }

    public ItemHolder(Activity activity, Context context, List lists, int itemID) {
        super(context, lists, itemID);
        mActivity = activity;
    }

    @Override
    public void initView(int position, List datas, Context context) {
        TextView textView = (TextView) itemView.findViewById(R.id.tv);
        TextView textView2 = (TextView) itemView.findViewById(R.id.tv0);
        final String msg = (String) datas.get(position);
        textView.setText(msg);
        textView.setHeight(300);
        textView.setPadding(20, 10, 20, 10);
        if (position % 2 == 0) {
            textView.setBackgroundColor(Color.parseColor("#abcdef"));
        } else {
            textView.setBackgroundColor(Color.parseColor("#fedcba"));
        }
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastUtils.ToastMessage(getContext(), msg);
            }
        });
        textView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopupWindow(v);
            }
        });

    }

    private void showPopupWindow(View view) {

        // 一个自定义的布局，作为显示的内容
        View contentView = LayoutInflater.from(getContext()).inflate(
                R.layout.main_frag_list_button, null);
        // 设置按钮的点击事件


        final PopupWindow popupWindow = new PopupWindow(contentView,
                CommonUtils.getScreenWidth(getContext()) , DensityUtils.dip2px(getContext(), 80), true);

        popupWindow.setTouchable(true);
        popupWindow.setOnDismissListener(new poponDismissListener());
        CommonUtils.backgroundAlpha(mActivity, 0.61f);
        popupWindow.setTouchInterceptor(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return false;
                // 这里如果返回true的话，touch事件将被拦截
                // 拦截后 PopupWindow的onTouchEvent不被调用，这样点击外部区域无法dismiss
            }
        });

        // 如果不设置PopupWindow的背景，无论是点击外部区域还是Back键都无法dismiss弹框
        // 我觉得这里是API的一个bug
        popupWindow.setBackgroundDrawable(new ColorDrawable());

        // 设置好参数之后再show
//        popupWindow.showAsDropDown(view);

        View windowContentViewRoot = contentView;
        int windowPos[] = calculatePopWindowPos(view, windowContentViewRoot);
        int xOff = 10;// 可以自己调整偏移
        windowPos[0] -= xOff;
        popupWindow.showAtLocation(view, Gravity.TOP | Gravity.START, windowPos[0], windowPos[1]);

    }

    /**
     * 计算出来的位置，y方向就在anchorView的上面和下面对齐显示，x方向就是与屏幕右边对齐显示
     * 如果anchorView的位置有变化，就可以适当自己额外加入偏移来修正
     *
     * @param anchorView  呼出window的view
     * @param contentView window的内容布局
     * @return window显示的左上角的xOff, yOff坐标
     */
    private static int[] calculatePopWindowPos(final View anchorView, final View contentView) {
        final int windowPos[] = new int[2];
        final int anchorLoc[] = new int[2];
        // 获取锚点View在屏幕上的左上角坐标位置
        anchorView.getLocationOnScreen(anchorLoc);
        final int anchorHeight = anchorView.getHeight();
        // 获取屏幕的高宽
        final int screenHeight = CommonUtils.getScreenHeight(anchorView.getContext());
        final int screenWidth = CommonUtils.getScreenWidth(anchorView.getContext());
        contentView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        // 计算contentView的高宽
        final int windowHeight = contentView.getMeasuredHeight();
        final int windowWidth = contentView.getMeasuredWidth();
        // 判断需要向上弹出还是向下弹出显示
        final boolean isNeedShowUp = (screenHeight - anchorLoc[1] - anchorHeight < (windowHeight + DensityUtils.dip2px(contentView.getContext(), 50)));
        if (isNeedShowUp) {
            windowPos[0] = screenWidth - windowWidth;
            windowPos[1] = anchorLoc[1] - windowHeight;
            contentView.setBackgroundDrawable(contentView.getContext().getResources().getDrawable(
                    R.drawable.pop_up));
        } else {
            windowPos[0] = screenWidth - windowWidth;
            windowPos[1] = anchorLoc[1] + anchorHeight;
            contentView.setBackgroundDrawable(contentView.getContext().getResources().getDrawable(
                    R.drawable.pop_down));
        }
        return windowPos;
    }

    class poponDismissListener implements PopupWindow.OnDismissListener {

        @Override
        public void onDismiss() {
            // TODO Auto-generated method stub
            //Log.v("List_noteTypeActivity:", "我是关闭事件");
            CommonUtils.backgroundAlpha(mActivity, 1f);
        }

    }
}
