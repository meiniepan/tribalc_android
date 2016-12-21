package com.gs.buluo.app.view.fragment;

import android.app.ActivityOptions;
import android.content.Context;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;
import android.support.annotation.RequiresApi;
import android.transition.Explode;
import android.transition.Fade;
import android.view.View;
import android.view.WindowManager;

import com.gs.buluo.app.Constant;
import com.gs.buluo.app.R;

import com.gs.buluo.app.view.activity.OpenDoorActivity;
import com.gs.buluo.app.view.activity.PropertyActivity;

import java.io.ByteArrayOutputStream;

/**
 * Created by admin on 2016/11/1.
 */
public class UsualFragment extends BaseFragment implements View.OnClickListener {
    @Override
    protected int getContentLayout() {
        return R.layout.fragment_usual;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void bindView(Bundle savedInstanceState) {
        getActivity().findViewById(R.id.usual_open_door).setOnClickListener(this);
        getActivity().findViewById(R.id.usual_property).setOnClickListener(this);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.usual_property:
                startActivity(new Intent(getActivity(), PropertyActivity.class));
                break;
            case R.id.usual_open_door:
                Bitmap flur = getFlur(getScreenshot(getView()));
                Intent intent = new Intent(getActivity(), OpenDoorActivity.class);
                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                flur.compress(Bitmap.CompressFormat.JPEG, 50, outputStream);
                byte[] bytes = outputStream.toByteArray();
                intent.putExtra(Constant.PICTURE, bytes);
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.around_alpha, R.anim.around_alpha_out);
                break;
        }
    }

    public Bitmap getFlur(Bitmap sentBitmap) {
        Bitmap bitmap = sentBitmap.copy(sentBitmap.getConfig(), true);
        final RenderScript rs = RenderScript.create(getContext());
        final Allocation input = Allocation.createFromBitmap(rs, sentBitmap, Allocation.MipmapControl.MIPMAP_NONE,
                Allocation.USAGE_SCRIPT);
        final Allocation output = Allocation.createTyped(rs, input.getType());
        final ScriptIntrinsicBlur script = ScriptIntrinsicBlur.create(rs, Element.U8_4(rs));
        script.setRadius(25 /* e.g. 3.f */);
        script.setInput(input);
        script.forEach(output);
        output.copyTo(bitmap);
        return bitmap;
    }

    private Bitmap getScreenshot(View v) {
        WindowManager wm = (WindowManager) getContext()
                .getSystemService(Context.WINDOW_SERVICE);
        int width = wm.getDefaultDisplay().getWidth();
        int height = wm.getDefaultDisplay().getHeight();
        Bitmap b = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_4444);
        Canvas c = new Canvas(b);
        v.draw(c);
        return b;
    }

}
