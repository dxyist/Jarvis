package com.ecnu.leon.jarvis.news;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.util.Log;
import android.webkit.WebView;

import com.cootek.feedsnews.item.FeedsItem;
import com.cootek.feedsnews.sdk.INewsUtil;
import com.cootek.feedsnews.util.FeedsConst;
import com.cootek.feedsnews.util.ShareInterface;
import com.cootek.feedsnews.util.ShortUrlInterface;
import com.ecnu.leon.jarvis.JarvisApplication;

import java.net.SocketAddress;
import java.util.Map;

import okhttp3.Headers;

/**
 * Created by frankyang on 5/19/17.
 */

public class MockNewsUtil implements INewsUtil {
    @Override
    public String getToken() {
        return "07e9caf8-6d8a-47a7-8034-685913d3b6fd";
    }

    @Override
    public int getAppVersionCode() {
        return 6071;
    }

    @Override
    public String getAppPackageName() {
        return "com.cootek.smartdialer";
    }

    @Override
    public String getLongitude() {
        return "";
    }

    @Override
    public String getLatitude() {
        return "";
    }

    @Override
    public String getAddress() {
        return "";
    }

    @Override
    public String getCity() {
        return "";
    }

    @Override
    public Context getContext() {
        return JarvisApplication.getContext();
    }

    @Override
    public String getGeoCity() {
        return "";
    }

    @Override
    public boolean isDebugMode() {
        return true;
    }

    @Override
    public Typeface getTypeface(String s) {
        Typeface ret;
        switch (s) {
            case FeedsConst.ICON1:
                ret = getFont("icon1");
                break;

            case FeedsConst.ICON2:
                ret = getFont("icon2");
                break;

            case FeedsConst.ICON3:
                ret = getFont("icon3");
                break;

            case FeedsConst.ICON4:
                ret = getFont("icon4");
                break;

            case FeedsConst.YP_ICON3:
                ret = getFont("yellowpage_icon3");
                break;

            case FeedsConst.ICON1_V6:
                ret = getFont("dialer_icon1");
                break;
            case FeedsConst.ICON2_V6:
                ret = getFont("dialer_icon2");
                break;
            case FeedsConst.ICON3_V6:
                ret = getFont("dialer_icon3");
                break;
            default:
                throw new IllegalStateException("must use valid interface");

        }

        return ret;
    }

    @Override
    public void doShare(String s, Context context, String title, String content, String url, String imgUrl, ShareInterface shareInterface) {

    }

    @Override
    public void doShortUrlConvert(String s, ShortUrlInterface shortUrlInterface) {

    }

    @Override
    public void startOutsideBrowserActivity(Context context, FeedsItem feedsItem, int i, String s) {
    }

    @Override
    public void startDetailActivity(Context context, Intent intent) {
//        intent.setClassName(context, FeedsDetailActivity.class.getName());
//        context.startActivity(intent);
    }

    @Override
    public void startListActivity(Context context, int ftu, String from) {
//        Intent intent = new Intent(context, FeedsListActivity.class);
//        context.startActivity(intent);

    }

    @Override
    public void record(String s, Map<String, Object> map) {
        Log.e("Frank", s);
    }

    @Override
    public SocketAddress getLooopProxy() {
        return null;
    }

    @Override
    public void setWebViewProxy(WebView webView) {

    }

    @Override
    public Headers getHeaders() {
        Headers.Builder builder = new Headers.Builder();
        builder.add("User-Agent", "Mozilla/5.0 (Linux; Android 5.0.2; NX511J Build/LRX22G; wv) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/43.0.2357.121 Mobile Safari/537.36");
        return builder.build();
    }

    @Override
    public String provideTestAddress(int mode) {
//        if (FeedsConst.TEST_ADDRESS_AD == mode) {
//            return "http://183.136.223.35:8881";
//        }
        return null;
    }

    @Override
    public void doExtra(String s, Context context) {

    }

    private Typeface getFont(String name) {
        String filename = "fonts/" + name + ".ttf";
        return Typeface.createFromAsset(JarvisApplication.getContext().getAssets(), filename);
    }

}
