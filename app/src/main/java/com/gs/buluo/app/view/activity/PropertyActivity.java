package com.gs.buluo.app.view.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.gs.buluo.app.R;

import okhttp3.internal.framed.Variant;

public class PropertyActivity extends BaseActivity implements View.OnClickListener {



    @Override
    protected void bindView(Bundle savedInstanceState) {
        findViewById(R.id.property_part_fix).setOnClickListener(this);
        findViewById(R.id.property_public_light).setOnClickListener(this);
        findViewById(R.id.property_water_pipe_fix).setOnClickListener(this);
        findViewById(R.id.property_electric_fix).setOnClickListener(this);
        findViewById(R.id.property_other).setOnClickListener(this);
        findViewById(R.id.property_back).setOnClickListener(this);
    }

    @Override
    public void setBarColor(int colorInt) {
        super.setBarColor(colorInt);
    }

    @Override
    protected int getContentLayout() {
        return R.layout.activity_property;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.property_back:
                finish();
                break;
            case R.id.property_setting:

                break;
            case R.id.property_part_fix:
                startActivity(new Intent(this,AddPartFixActivity.class));
                break;
            case R.id.property_public_light:
                break;
            case R.id.property_water_pipe_fix:
                break;
            case R.id.property_electric_fix:
                break;
            case R.id.property_other:
                break;
        }
    }
}
