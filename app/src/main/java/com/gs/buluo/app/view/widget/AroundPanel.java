package com.gs.buluo.app.view.widget;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.SystemClock;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import com.gs.buluo.app.R;
import com.gs.buluo.app.bean.ResponseBody.GoodsResponseBean;
import com.gs.buluo.app.model.GoodsModel;
import com.gs.buluo.app.view.activity.FindActivity;
import com.gs.buluo.app.view.activity.FoodActivity;
import com.gs.buluo.app.view.activity.FunActivity;
import com.gs.buluo.app.view.activity.MainActivity;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by hjn on 2016/11/16.
 */
public class AroundPanel extends Dialog implements ArcMenu.OnMenuItemClickListener {
    MainActivity activity;
    private ArcMenu menu;

    public AroundPanel(MainActivity context) {
        super(context,R.style.around_dialog);
        activity = context;
        initView();
    }

    private void initView() {
        View rootView = LayoutInflater.from(activity).inflate(R.layout.arround_board, null);
        setContentView(rootView);
        menu = (ArcMenu) findViewById(R.id.am_root);
        ImageView view = (ImageView) findViewById(R.id.id_arcmenu_button);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                menu.close();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        dismiss();
                    }
                },300);
            }
        });

        Window window = getWindow();
        WindowManager.LayoutParams params = window.getAttributes();
        params.width= ViewGroup.LayoutParams.MATCH_PARENT;
        params.height= ViewGroup.LayoutParams.MATCH_PARENT;
        window.setAttributes(params);

        menu.setOnMenuItemClickListener(this);
    }


    public void showMenu() {
        menu.begin();
    }

    @Override
    public void onClick(View view, int pos) {
        switch (view.getId()){
            case R.id.around_food:
                activity.startActivity(new Intent(activity, FoodActivity.class));
                break;
            case R.id.around_fun:
                activity.startActivity(new Intent(activity, FunActivity.class));
                break;
            case R.id.around_find:
                activity.startActivity(new Intent(activity, FindActivity.class));
                break;
        }
        dismiss();
    }
}
