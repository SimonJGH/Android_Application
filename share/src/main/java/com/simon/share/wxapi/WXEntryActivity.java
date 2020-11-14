package com.simon.share.wxapi;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.simon.share.R;
import com.tencent.mm.opensdk.constants.ConstantsAPI;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

public class WXEntryActivity extends AppCompatActivity implements IWXAPIEventHandler {

    private IWXAPI wxapi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_w_x_entry);

        wxapi = WXAPIFactory.createWXAPI(this, "wxfbfa763c21551e7a", false);
        wxapi.registerApp("wxfbfa763c21551e7a");
        wxapi.handleIntent(getIntent(), this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
    }

    @Override
    public void onReq(BaseReq baseReq) {
        switch (baseReq.getType()) {
            case ConstantsAPI.COMMAND_GETMESSAGE_FROM_WX:
                Log.i("Simon", "onReq.errCode = COMMAND_GETMESSAGE_FROM_WX");
                break;
            case ConstantsAPI.COMMAND_SHOWMESSAGE_FROM_WX:
                Log.i("Simon", "onReq.errCode = COMMAND_SHOWMESSAGE_FROM_WX");
                break;
        }
    }

    @Override
    public void onResp(BaseResp baseResp) {
        if (baseResp.errCode == 0) {
            Toast.makeText(this, "分享成功", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "分享失败", Toast.LENGTH_SHORT).show();
        }

        switch (baseResp.errCode) {
            case ConstantsAPI.COMMAND_LAUNCH_WX_MINIPROGRAM:
                // val launchMiniProResp = resp as WXLaunchMiniProgram.Resp
                // 对应JsApi navigateBackApplication中的extraData字段数据
                // val extraData = launchMiniProResp.extMsg
                break;
        }

        finish();
    }
}