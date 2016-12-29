package net.sourceforge.simcpux.wxapi;

import android.app.Activity;

import com.gs.buluo.app.utils.ToastUtils;
import com.tencent.mm.sdk.constants.ConstantsAPI;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;

/**
 * Created by hjn on 2016/12/29.
 */

public class WXPayEntryActivity extends Activity implements IWXAPIEventHandler {

    @Override
    public void onReq(BaseReq baseReq) {
        if(baseReq.getType()== ConstantsAPI.COMMAND_PAY_BY_WX){
            ToastUtils.ToastMessage(this,"支付成功");
        }
    }

    @Override
    public void onResp(BaseResp baseResp) {

    }
}
