//package com.gs.buluo.app.view.fragment;
//
//import android.content.Intent;
//import android.os.Bundle;
//import android.view.View;
//import android.view.WindowManager;
//import android.widget.RelativeLayout;
//import android.widget.TextView;
//
//import com.gs.buluo.app.R;
//import com.gs.buluo.app.presenter.AroundPresenter;
//import com.gs.buluo.app.presenter.BasePresenter;
//import com.gs.buluo.app.utils.ToastUtils;
//import com.gs.buluo.app.view.activity.ServeActivity;
//import com.gs.buluo.app.view.activity.FunActivity;
//import com.gs.buluo.app.view.widget.ArcMenu;
//
//import butterknife.Bind;
//
//
///**
// * Created by admin on 2016/11/1.
// */
//public class AroundFragment extends BaseFragment implements View.OnClickListener {
//    @Bind(R.id.around_rl_bottom)
//    RelativeLayout mBottom;
//
//    @Override
//    protected int getContentLayout() {
//        return R.layout.fragment_arround;
//    }
//
//    @Override
//    protected void bindView(Bundle savedInstanceState) {
//        getActivity().findViewById(R.id.around_find).setOnClickListener(this);
//        getActivity().findViewById(R.id.around_food).setOnClickListener(this);
//        getActivity().findViewById(R.id.around_fun).setOnClickListener(this);
//        getActivity().findViewById(R.id.id_arcmenu_button).setOnClickListener(this);
//    }
//
//    @Override
//    protected BasePresenter getPresenter() {
//        return new AroundPresenter();
//    }
//
//
//    @Override
//    public void onClick(View v) {
//        switch (v.getId()){
//            case R.id.around_find:
////                startActivity(new Intent(getActivity(),FindActivity.class));
//                break;
//            case R.id.around_food:
//                startActivity(new Intent(getActivity(),ServeActivity.class));
//                break;
//            case R.id.around_fun:
//                startActivity(new Intent(getActivity(),FunActivity.class));
//                break;
//            case R.id.id_arcmenu_button:
//                break;
//        }
//    }
//}
