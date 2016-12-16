package com.gs.buluo.app.view.widget;

import android.app.Dialog;
import android.content.Intent;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import com.gs.buluo.app.Constant;
import com.gs.buluo.app.R;
import com.gs.buluo.app.view.activity.FindActivity;
import com.gs.buluo.app.view.activity.GoodsListActivity;
import com.gs.buluo.app.view.activity.ServeActivity;
import com.gs.buluo.app.view.activity.MainActivity;

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
        Intent intent = new Intent(activity, ServeActivity.class);
        switch (view.getId()){
            case R.id.around_food:
                intent.putExtra(Constant.TYPE,Constant.REPAST);
                activity.startActivity(intent);
                break;
            case R.id.around_fun:
                intent.putExtra(Constant.TYPE,Constant.ENTERTAINMENT);
                activity.startActivity(intent);
                break;
            case R.id.around_shop:
                activity.startActivity(new Intent(activity, GoodsListActivity.class));
                break;
        }
        dismiss();
    }
}
