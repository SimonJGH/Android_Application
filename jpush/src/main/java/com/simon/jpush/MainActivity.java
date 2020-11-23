package com.simon.jpush;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.simon.base_library.BaseActivity;

import java.util.Set;

import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;

@Route(path = "/push/push")
public class MainActivity extends BaseActivity {
    private final TagAliasCallback mAliasCallback = new TagAliasCallback() {
        @Override
        public void gotResult(int code, String alias, Set<String> tags) {
            String logs ;
            switch (code) {
                case 0:
                    logs = "Set tag and alias success";
                    Log.i("Simon", logs);
                    // 建议这里往 SharePreference 里写一个成功设置的状态。成功设置一次后，以后不必再次设置了。
                    break;
                case 6002:
                    logs = "Failed to set alias and tags due to timeout. Try again after 60s.";
                    Log.i("Simon", logs);
                    // 延迟 60 秒来调用 Handler 设置别名
                    JPushInterface.setAliasAndTags(getApplicationContext(),"666",
                            null,
                            mAliasCallback);
                    break;
                default:
                    logs = "Failed with errorCode = " + code;
                    Log.i("Simon", logs);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        JPushInterface.setAliasAndTags(getApplicationContext(),"666",
                null,
                mAliasCallback);

    }
}
