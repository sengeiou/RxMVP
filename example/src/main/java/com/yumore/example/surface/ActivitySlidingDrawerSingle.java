package com.yumore.example.surface;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.*;
import android.widget.*;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.yumore.example.R;
import com.yumore.example.R2;
import com.yumore.utility.activity.ActivityBase;
import com.yumore.utility.utility.RxBarTool;
import com.yumore.utility.utility.RxConstants;
import com.yumore.utility.widget.RxTitle;

/**
 * @author yumore
 */
public class ActivitySlidingDrawerSingle extends ActivityBase {


    @BindView(R2.id.rx_title)
    RxTitle mRxTitle;
    @BindView(R2.id.textView1)
    TextView mTextView1;
    @BindView(R2.id.textView2)
    TextView mTextView2;
    @BindView(R2.id.textView3)
    TextView mTextView3;
    @BindView(R2.id.textView4)
    TextView mTextView4;
    @BindView(R2.id.iv_slide)
    ImageView mIvSlide;
    @BindView(R2.id.textView8)
    TextView mTextView8;
    @BindView(R2.id.handle)
    LinearLayout mHandle;
    @BindView(R2.id.textView14)
    TextView mTextView14;
    @BindView(R2.id.pb_web_base)
    ProgressBar mPbWebBase;
    @BindView(R2.id.web_base)
    WebView mWebBase;
    @BindView(R2.id.LinearLayout2)
    LinearLayout mLinearLayout2;
    @BindView(R2.id.content)
    LinearLayout mContent;
    @BindView(R2.id.slidingdrawer)
    SlidingDrawer mSlidingdrawer;
    String webPath = "";
    private Boolean flag = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        RxBarTool.noTitle(this);
        setContentView(R.layout.activity_sliding_drawer_single);
        RxBarTool.setTransparentStatusBar(this);
        ButterKnife.bind(this);

        mRxTitle.setLeftFinish(mContext);

        initData();
    }

    @SuppressWarnings("deprecation")
    private void initData() {
        mSlidingdrawer.setOnDrawerOpenListener(new SlidingDrawer.OnDrawerOpenListener() {
            @Override
            public void onDrawerOpened() {
                flag = true;
                mIvSlide.setImageResource(R.drawable.slibe_down);
            }
        });
        mSlidingdrawer.setOnDrawerCloseListener(new SlidingDrawer.OnDrawerCloseListener() {
            @Override
            public void onDrawerClosed() {
                flag = false;
                mIvSlide.setImageResource(R.drawable.slibe_up);
            }
        });
        mSlidingdrawer.setOnDrawerScrollListener(new SlidingDrawer.OnDrawerScrollListener() {
            @Override
            public void onScrollEnded() {
            }

            @Override
            public void onScrollStarted() {
            }
        });

        mPbWebBase.setMax(100);
//        webPath = getIntent().getStringExtra("URL");
        webPath = RxConstants.URL_VONTOOLS;
        if (webPath.equals("")) {
            webPath = "http://www.baidu.com";
        }
        WebSettings webSettings = mWebBase.getSettings();
        if (Build.VERSION.SDK_INT >= 19) {
            webSettings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        }

        if (Build.VERSION.SDK_INT >= 19) {
            webSettings.setLoadsImagesAutomatically(true);
        } else {
            webSettings.setLoadsImagesAutomatically(false);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            mWebBase.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        }
        mWebBase.setLayerType(View.LAYER_TYPE_HARDWARE, null);
//        mWebBase.setLayerType(View.LAYER_TYPE_SOFTWARE, null);

//        webSettings.setAllowContentAccess(true);
//        webSettings.setAllowFileAccessFromFileURLs(true);
//        webSettings.setAppCacheEnabled(true);
   /*     if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            webSettings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }*/

        webSettings.setJavaScriptEnabled(true); // 设置支持javascript脚本
//        webSettings.setPluginState(WebSettings.PluginState.ON);
        webSettings.setSupportZoom(true);// 设置可以支持缩放
        webSettings.setBuiltInZoomControls(true);// 设置出现缩放工具
        webSettings.setDisplayZoomControls(false);//隐藏缩放工具
        webSettings.setUseWideViewPort(true);// 扩大比例的缩放

        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);//自适应屏幕
        webSettings.setLoadWithOverviewMode(true);

        webSettings.setDatabaseEnabled(true);
        webSettings.setSavePassword(true);
        webSettings.setDomStorageEnabled(true);
        mWebBase.setSaveEnabled(true);
        mWebBase.setKeepScreenOn(true);


        // 设置setWebChromeClient对象
        mWebBase.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
            }

            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                // TODO Auto-generated method stub
                mPbWebBase.setProgress(newProgress);
                super.onProgressChanged(view, newProgress);
            }
        });
        mWebBase.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                if (!mWebBase.getSettings().getLoadsImagesAutomatically()) {
                    mWebBase.getSettings().setLoadsImagesAutomatically(true);
                }
                mPbWebBase.setVisibility(View.GONE);
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                // TODO Auto-generated method stub
                mPbWebBase.setVisibility(View.VISIBLE);
                super.onPageStarted(view, url, favicon);
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {

                if (url.startsWith("http:") || url.startsWith("https:")) {
                    view.loadUrl(url);
                    return false;
                }

                // Otherwise allow the OS to handle things like tel, mailto, etc.
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                startActivity(intent);
                return true;
            }
        });
        mWebBase.setDownloadListener(new DownloadListener() {
            @Override
            public void onDownloadStart(String paramAnonymousString1, String paramAnonymousString2, String paramAnonymousString3, String paramAnonymousString4, long paramAnonymousLong) {
                Intent intent = new Intent();
                intent.setAction("android.intent.action.VIEW");
                intent.setData(Uri.parse(paramAnonymousString1));
                startActivity(intent);
            }
        });

        mWebBase.loadUrl(webPath);
        Log.v("帮助类完整连接", webPath);
    }
}
