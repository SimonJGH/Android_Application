package com.simon.application;


import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.alibaba.android.arouter.launcher.ARouter;
import com.simon.base_library.BaseActivity;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        Log.i("Simon", "MainActivity onCreate");
    }

    @OnClick({R.id.tv_aaa})
    public void onViewClick(View view) {
        switch (view.getId()) {
            case R.id.tv_aaa:
                ARouter.getInstance().build("/push/push").withString("DATA", "跳转数据").navigation();
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i("Simon", "MainActivity onResume");
    }

}