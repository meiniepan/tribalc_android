package com.gs.buluo.app.view.activity;

import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.TextView;

import com.gs.buluo.app.Constant;
import com.gs.buluo.app.R;

import butterknife.Bind;

/**
 * Created by hjn on 2017/2/28.
 */
public class WebActivity extends BaseActivity{
    @Bind(R.id.web_view)
    WebView webView;
    @Bind(R.id.web_title)
    TextView tvTitle;
    @Override
    protected void bindView(Bundle savedInstanceState) {
        webView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
//        webView.requestFocusFromTouch();
//        webView.requestFocus();
//        webView.requestFocus(View.FOCUS_DOWN|View.FOCUS_UP);
        String imgUrl = getIntent().getStringExtra(Constant.WEB_URL);

        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setLightTouchEnabled(true);
        if (imgUrl!=null){
            tvTitle.setText("今日活动");
            webView.loadUrl(imgUrl);
        }else {
            webView.loadUrl(Constant.Base.BASE+"page.html");
        }
    }

    @Override
    protected int getContentLayout() {
        return R.layout.activity_protocol;
    }
}
