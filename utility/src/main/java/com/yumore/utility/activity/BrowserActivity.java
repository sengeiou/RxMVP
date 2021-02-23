package com.yumore.utility.activity;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.webkit.DownloadListener;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.yumore.utility.R;
import com.yumore.utility.utility.RxBarTool;
import com.yumore.utility.utility.RxConstants;
import com.yumore.utility.utility.RxImageTool;
import com.yumore.utility.utility.RxKeyboardTool;
import com.yumore.utility.widget.RxTextAutoZoom;

/**
 * @author yumore
 */
public class BrowserActivity extends BaseActivity {

    private static final int TIME_INTERVAL = 2000;
    TextView tvTitle;
    WebView webView;
    ImageView ivFinish;
    RxTextAutoZoom mRxTextAutoZoom;
    LinearLayout llIncludeTitle;
    private ProgressBar pbWebBase;
    private long mBackPressed;
    private View browserLayout, errorLayout;
    private String websiteUrl = RxConstants.URL_BAIDU_SEARCH;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        RxBarTool.setTransparentStatusBar(this);
        setContentView(R.layout.activity_browser);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        initView();// 初始化控件 - FindViewById之类的操作
        initData();// 初始化控件的数据及监听事件
    }

    private void initView() {
        // TODO Auto-generated method stub
        mRxTextAutoZoom = findViewById(R.id.afet_tv_title);
        llIncludeTitle = findViewById(R.id.ll_include_title);

        browserLayout = findViewById(R.id.browser_container);
        errorLayout = findViewById(R.id.browser_error);
        errorLayout.findViewById(R.id.network_retry_tv).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                browserLayout.setVisibility(View.VISIBLE);
                webView.loadUrl(websiteUrl);
            }
        });

        tvTitle = findViewById(R.id.tv_title);
        pbWebBase = findViewById(R.id.pb_web_base);
        webView = findViewById(R.id.web_base);
        ivFinish = findViewById(R.id.iv_finish);
        ivFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (webView.canGoBack()) {
                    webView.goBack();
                } else {
                    finish();
                }
            }
        });

        initAutoFitEditText();
    }

    public void initAutoFitEditText() {

        mRxTextAutoZoom.clearFocus();
        mRxTextAutoZoom.setEnabled(false);
        mRxTextAutoZoom.setFocusableInTouchMode(false);
        mRxTextAutoZoom.setFocusable(false);
        mRxTextAutoZoom.setEnableSizeCache(false);
        //might cause crash on some devices
        mRxTextAutoZoom.setMovementMethod(null);
        // can be added after layout inflation;
        mRxTextAutoZoom.setMaxHeight(RxImageTool.dip2px(55f));
        //don't forget to add min text size programmatically
        mRxTextAutoZoom.setMinTextSize(37f);

        RxTextAutoZoom.setNormalization(this, llIncludeTitle, mRxTextAutoZoom);

        RxKeyboardTool.hideSoftInput(this);
    }

    @SuppressLint("SetJavaScriptEnabled")
    private void initData() {
        websiteUrl = "https://v-cdn.zjol.com.cn/277004.mp4";
        pbWebBase.setMax(100);
        WebSettings webSettings = webView.getSettings();
        webSettings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        webSettings.setLoadsImagesAutomatically(true);

        webView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        webView.setLayerType(View.LAYER_TYPE_HARDWARE, null);

        webSettings.setJavaScriptEnabled(true);
        webSettings.setSupportZoom(true);
        // 设置出现缩放工具 是否使用WebView内置的缩放组件，由浮动在窗口上的缩放控制和手势缩放控制组成，默认false
        webSettings.setBuiltInZoomControls(true);

        //隐藏缩放工具
        webSettings.setDisplayZoomControls(false);
        // 扩大比例的缩放
        webSettings.setUseWideViewPort(true);

        //自适应屏幕
        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        webSettings.setLoadWithOverviewMode(true);

        webSettings.setDatabaseEnabled(true);
        //保存密码
        webSettings.setSavePassword(true);
        //是否开启本地DOM存储  鉴于它的安全特性（任何人都能读取到它，尽管有相应的限制，将敏感数据存储在这里依然不是明智之举），Android 默认是关闭该功能的。
        webSettings.setDomStorageEnabled(true);
        webView.setSaveEnabled(true);
        webView.setKeepScreenOn(true);


        // 设置setWebChromeClient对象
        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
                mRxTextAutoZoom.setText(title);
            }

            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                // TODO Auto-generated method stub
                pbWebBase.setProgress(newProgress);
                super.onProgressChanged(view, newProgress);
            }
        });
        //设置此方法可在WebView中打开链接，反之用浏览器打开
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                if (!webView.getSettings().getLoadsImagesAutomatically()) {
                    webView.getSettings().setLoadsImagesAutomatically(true);
                }
                pbWebBase.setVisibility(View.GONE);
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                // TODO Auto-generated method stub
                pbWebBase.setVisibility(View.VISIBLE);
                super.onPageStarted(view, url, favicon);
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                String url = request.getUrl().toString();
                if (url.startsWith("http:") || url.startsWith("https:")) {
                    view.loadUrl(url);
                    return false;
                }
                // Otherwise allow the OS to handle things like tel, mailto, etc.
                try {
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                    startActivity(intent);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return true;
            }

            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                super.onReceivedSslError(view, handler, error);
            }

            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                errorLayout.setVisibility(View.VISIBLE);
                browserLayout.setVisibility(View.GONE);
            }
        });


        webView.setDownloadListener(new DownloadListener() {
            @Override
            public void onDownloadStart(String paramAnonymousString1, String paramAnonymousString2, String paramAnonymousString3, String paramAnonymousString4, long paramAnonymousLong) {
                Intent intent = new Intent();
                intent.setAction("android.intent.action.VIEW");
                intent.setData(Uri.parse(paramAnonymousString1));
                startActivity(intent);
            }
        });
        webView.loadUrl(websiteUrl);
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle paramBundle) {
        super.onSaveInstanceState(paramBundle);
        paramBundle.putString("url", webView.getUrl());
    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            Log.v("Himi", "onConfigurationChanged_ORIENTATION_LANDSCAPE");
        } else if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            Log.v("Himi", "onConfigurationChanged_ORIENTATION_PORTRAIT");
        }
    }

    @Override
    public void onBackPressed() {

        if (webView.canGoBack()) {
            webView.goBack();
        } else {
            if (mBackPressed + TIME_INTERVAL > System.currentTimeMillis()) {
                super.onBackPressed();
                return;
            } else {
                Toast.makeText(getBaseContext(), "再次点击返回键退出", Toast.LENGTH_SHORT).show();
            }
            mBackPressed = System.currentTimeMillis();
        }
    }

}

