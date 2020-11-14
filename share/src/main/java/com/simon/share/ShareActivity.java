package com.simon.share;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.permissionx.guolindev.PermissionX;
import com.permissionx.guolindev.callback.ExplainReasonCallbackWithBeforeParam;
import com.permissionx.guolindev.callback.ForwardToSettingsCallback;
import com.permissionx.guolindev.callback.RequestCallback;
import com.permissionx.guolindev.request.ExplainScope;
import com.permissionx.guolindev.request.ForwardScope;
import com.tencent.mm.opensdk.modelmsg.SendMessageToWX;
import com.tencent.mm.opensdk.modelmsg.WXImageObject;
import com.tencent.mm.opensdk.modelmsg.WXMediaMessage;
import com.tencent.mm.opensdk.modelmsg.WXMiniProgramObject;
import com.tencent.mm.opensdk.modelmsg.WXMusicObject;
import com.tencent.mm.opensdk.modelmsg.WXTextObject;
import com.tencent.mm.opensdk.modelmsg.WXVideoObject;
import com.tencent.mm.opensdk.modelmsg.WXWebpageObject;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;

import butterknife.OnClick;

public class ShareActivity extends AppCompatActivity {
    private IWXAPI wxapi;
    //微信APP_ID
    private String APP_ID = "wxfbfa763c21551e7a";
    private int THUMB_SIZE = 150;
    private String imageUrl = "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1605169847878&di=c929e022169e459481d70df6b629c986&imgtype=0&src=http%3A%2F%2Fd.paper.i4.cn%2Fmax%2F2016%2F11%2F11%2F11%2F1478834975141_580647.jpg";
    private String saveFilePath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath() + "/" + parseFileName(imageUrl);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share);

        wxapi = WXAPIFactory.createWXAPI(this, "wxfbfa763c21551e7a", false);
        wxapi.registerApp("wxfbfa763c21551e7a");

        PermissionX.init(this)
                .permissions(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .onExplainRequestReason(new ExplainReasonCallbackWithBeforeParam() {
                    @Override
                    public void onExplainReason(ExplainScope scope, List<String> deniedList, boolean beforeRequest) {
                        scope.showRequestReasonDialog(deniedList, "即将申请的权限是程序必须依赖的权限", "我已明白");
                    }
                })
                .onForwardToSettings(new ForwardToSettingsCallback() {
                    @Override
                    public void onForwardToSettings(ForwardScope scope, List<String> deniedList) {
                        scope.showForwardToSettingsDialog(deniedList, "您需要去应用程序设置当中手动开启权限", "我已明白");
                    }
                })
                .request(new RequestCallback() {
                    @Override
                    public void onResult(boolean allGranted, List<String> grantedList, List<String> deniedList) {
                        if (allGranted) {
                            Toast.makeText(ShareActivity.this, "所有申请的权限都已通过", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(ShareActivity.this, "您拒绝了如下权限：" + deniedList, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    @OnClick({R.id.bt_share_text, R.id.bt_share_image, R.id.bt_share_web, R.id.bt_share_mini,
            R.id.bt_share_music, R.id.bt_share_video})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.bt_share_text:
                shareText("文字分享测试", 0);
                break;
            case R.id.bt_share_image:
                downloadFile(imageUrl, saveFilePath, new onDownloadResultListener() {
                    @Override
                    public void onSuccess() {
                        shareImage(0);
                    }

                    @Override
                    public void onFailed(String failMsg) {

                    }
                });
                break;
            case R.id.bt_share_web:
                downloadFile(imageUrl, saveFilePath, new onDownloadResultListener() {
                    @Override
                    public void onSuccess() {
                        shareWeb(0);
                    }

                    @Override
                    public void onFailed(String failMsg) {

                    }
                });
                break;
            case R.id.bt_share_mini:
                downloadFile(imageUrl, saveFilePath, new onDownloadResultListener() {
                    @Override
                    public void onSuccess() {
                        shareMiniProgram(0, "", "标题", "描述");
                    }

                    @Override
                    public void onFailed(String failMsg) {

                    }
                });
                break;
            case R.id.bt_share_music:
                downloadFile(imageUrl, saveFilePath, new onDownloadResultListener() {
                    @Override
                    public void onSuccess() {
                        shareMusic(0);
                    }

                    @Override
                    public void onFailed(String failMsg) {

                    }
                });
                break;
            case R.id.bt_share_video:
                downloadFile(imageUrl, saveFilePath, new onDownloadResultListener() {
                    @Override
                    public void onSuccess() {
                        shareVideo(0);
                    }

                    @Override
                    public void onFailed(String failMsg) {

                    }
                });
                break;
        }
    }

    /**
     * 分享文字
     */
    private void shareText(String text, int scene) {
        //初始化一个 WXTextObject 对象，填写分享的文本内容
        WXTextObject textObj = new WXTextObject();
        textObj.text = text;

        //用 WXTextObject 对象初始化一个 WXMediaMessage 对象
        WXMediaMessage msg = new WXMediaMessage();
        msg.mediaObject = textObj;
        msg.description = text;

        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = "text";
        req.message = msg;
        req.scene = scene;
        //调用api接口，发送数据到微信
        wxapi.sendReq(req);
    }

    /**
     * 分享图片
     */
    private void shareImage(int scene) {
        //判断封面文件是否存在
        File file = new File(saveFilePath);
        if (!file.exists()) {
            Toast.makeText(this, "图片加载失败", Toast.LENGTH_SHORT).show();
            return;
        }
        //初始化 WXImageObject 和 WXMediaMessage 对象
        WXImageObject imgObj = new WXImageObject();
        imgObj.setImagePath(file.getPath());
        WXMediaMessage msg = new WXMediaMessage();
        msg.mediaObject = imgObj;

        //设置缩略图
        Bitmap bitmap = BitmapFactory.decodeFile(file.getPath());
        Bitmap thumbBmp = Bitmap.createScaledBitmap(bitmap, THUMB_SIZE, THUMB_SIZE, true);
        bitmap.recycle();
        msg.thumbData = bitmapToByteArray(thumbBmp, true);
        //构造一个Req
        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = "img";
        req.message = msg;
        req.scene = scene;
        // req.userOpenId = getOpenId();
        //调用api接口，发送数据到微信
        wxapi.sendReq(req);
    }

    /**
     * 分享web
     */
    private void shareWeb(int scene) {
        //判断封面文件是否存在
        File file = new File(saveFilePath);
        if (!file.exists()) {
            Toast.makeText(this, "图片加载失败", Toast.LENGTH_SHORT).show();
            return;
        }
        //初始化一个WXWebpageObject，填写url
        WXWebpageObject webpage = new WXWebpageObject();
        webpage.webpageUrl = "https://www.baidu.com";
        //用 WXWebpageObject 对象初始化一个 WXMediaMessage 对象
        WXMediaMessage msg = new WXMediaMessage(webpage);
        msg.title = "百度 ";
        msg.description = "就没有我不知道的";

        Bitmap bitmap = BitmapFactory.decodeFile(file.getPath());
        Bitmap thumbBmp = Bitmap.createScaledBitmap(bitmap, THUMB_SIZE, THUMB_SIZE, true);
        bitmap.recycle();
        msg.thumbData = bitmapToByteArray(thumbBmp, true);

        //构造一个Req
        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = "webpage";
        req.message = msg;
        req.scene = scene;
        //req.userOpenId = getOpenId()

        //调用api接口，发送数据到微信
        wxapi.sendReq(req);
    }

    /**
     * 分享小程序
     */
    private void shareMiniProgram(int miniprogramType, String path, String title, String descriptions) {
        //判断封面文件是否存在
        File file = new File(saveFilePath);
        if (!file.exists()) {
            Toast.makeText(this, "图片加载失败", Toast.LENGTH_SHORT).show();
            return;
        }

        //判断文件大小是否超过128k
        int fileSize = getFileSize(file) / 1024;
        if (fileSize > 128) {
            Toast.makeText(this, "图片加载失败", Toast.LENGTH_SHORT).show();
            return;
        }

        WXMiniProgramObject miniProgramObj = new WXMiniProgramObject();
        miniProgramObj.webpageUrl = "https://hongqi.faw-hongqiacademy.com"; // 兼容低版本的网页链接
        miniProgramObj.miniprogramType = miniprogramType; // 正式版:0，测试版:1，体验版:2
        miniProgramObj.userName = "gh_2463741f7321";// 小程序原始id
        miniProgramObj.path = path;//小程序页面路径；对于小游戏，可以只传入 query 部分，来实现传参效果，如：传入 "?foo=bar"

        WXMediaMessage msg = new WXMediaMessage(miniProgramObj);
        msg.title = title; // 小程序消息title
        msg.description = descriptions; // 小程序消息desc

        Bitmap bitmap = BitmapFactory.decodeFile(file.getPath());
        Bitmap thumbBmp = Bitmap.createScaledBitmap(bitmap, THUMB_SIZE, THUMB_SIZE, true);
        bitmap.recycle();
        msg.thumbData = bitmapToByteArray(thumbBmp, true); // 小程序消息封面图片，小于128k

        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = "miniProgram";
        req.message = msg;
        req.scene = SendMessageToWX.Req.WXSceneSession; // 目前只支持会话

        wxapi.sendReq(req);
    }

    /**
     * 分享音乐
     */
    private void shareMusic(int scene) {
        //判断封面文件是否存在
        File file = new File(saveFilePath);
        if (!file.exists()) {
            Toast.makeText(this, "图片加载失败", Toast.LENGTH_SHORT).show();
            return;
        }

        //初始化一个WXMusicObject，填写url
        WXMusicObject music = new WXMusicObject();
        music.musicUrl = "http://antiserver.kuwo.cn/anti.s?rid=MUSIC_145962494&response=res&format=mp3|aac&type=convert_url&br=128kmp3&agent=iPhone&callback=getlink&jpcallback=getlink.mp3";

        //用 WXMusicObject 对象初始化一个 WXMediaMessage 对象
        WXMediaMessage msg = new WXMediaMessage();
        msg.mediaObject = music;
        msg.title = "忘川彼岸";
        msg.description = "音乐描述";

        Bitmap bitmap = BitmapFactory.decodeFile(file.getPath());
        Bitmap thumbBmp = Bitmap.createScaledBitmap(bitmap, THUMB_SIZE, THUMB_SIZE, true);
        bitmap.recycle();
        msg.thumbData = bitmapToByteArray(thumbBmp, true);

        //构造一个Req
        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = "music";
        req.message = msg;
        req.scene = scene;
        //req.userOpenId = getOpenId()

        //调用api接口，发送数据到微信
        wxapi.sendReq(req);
    }

    /**
     * 分享视频
     */
    private void shareVideo(int scene) {
        //判断封面文件是否存在
        File file = new File(saveFilePath);
        if (!file.exists()) {
            Toast.makeText(this, "图片加载失败", Toast.LENGTH_SHORT).show();
            return;
        }

        //初始化一个WXMusicObject，填写url
        WXVideoObject video = new WXVideoObject();
        video.videoUrl = "http://vod.faw-hongqiacademy.com/48a6d0b97ec54e6587a46a65d4bdc255/1bc12de9809f48c9affbcb5e4cf50698-6b2ca862255ca7e3f657617057385fd1-sd.mp4";

        //用 WXVideoObject 对象初始化一个 WXMediaMessage 对象
        WXMediaMessage msg = new WXMediaMessage(video);
        msg.title = "红旗课程";
        msg.description = "视频描述";

        Bitmap bitmap = BitmapFactory.decodeFile(file.getPath());
        Bitmap thumbBmp = Bitmap.createScaledBitmap(bitmap, THUMB_SIZE, THUMB_SIZE, true);
        bitmap.recycle();
        msg.thumbData = bitmapToByteArray(thumbBmp, true);

        //构造一个Req
        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = "video";
        req.message = msg;
        req.scene = scene;
        //req.userOpenId = getOpenId()

        //调用api接口，发送数据到微信
        wxapi.sendReq(req);
    }


    /**
     * 获取文件大小
     */
    private int getFileSize(File file) {
        int size = 0;
        if (!file.exists()) {
            return 0;
        }
        try {
            FileInputStream fis = new FileInputStream(file);
            size = fis.available();
        } catch (Exception e) {
            return 0;
        }
        return size;
    }

    /**
     * bitmap转byte[]
     */
    private byte[] bitmapToByteArray(Bitmap bitmap, Boolean needRecycle) {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, output);
        if (needRecycle) {
            bitmap.recycle();
        }
        byte[] result = output.toByteArray();
        try {
            output.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 下载文件到本地
     *
     * @param urlString 被下载的文件地址
     * @param filename  本地文件名
     * @throws Exception 各种异常
     */
    private void downloadFile(final String urlString, final String filename, final onDownloadResultListener listner) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    // 构造URL
                    URL url = new URL(urlString);
                    // 打开连接
                    URLConnection con = url.openConnection();
                    // 输入流
                    InputStream inputStream = con.getInputStream();
                    // 1K的数据缓冲
                    byte[] bs = new byte[1024];
                    // 读取到的数据长度
                    int len = 0;
                    // 输出的文件流
                    OutputStream os = new FileOutputStream(filename);
                    // 开始读取
                    while ((len = inputStream.read(bs)) != -1) {
                        os.write(bs, 0, len);
                    }

                    // 完毕，关闭所有链接
                    os.close();
                    inputStream.close();
                    listner.onSuccess();
                } catch (Exception e) {
                    listner.onFailed(e.getMessage());
                }
            }
        }).start();
    }

    interface onDownloadResultListener {
        void onSuccess();

        void onFailed(String failMsg);
    }

    /**
     * 利用文件url转换出文件名
     *
     * @param url
     * @return
     */
    private String parseFileName(String url) {
        String fileName = "";
        try {
            fileName = url.substring(url.lastIndexOf("/") + 1);
        } finally {
            if (TextUtils.isEmpty(fileName)) {
                fileName = System.currentTimeMillis() + "";
            }
        }
        return fileName;
    }

}
