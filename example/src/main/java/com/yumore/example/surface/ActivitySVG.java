package com.yumore.example.surface;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.jaredrummler.android.widget.AnimatedSvgView;
import com.yumore.example.R;
import com.yumore.example.R2;
import com.yumore.example.entity.ModelSVG;
import com.yumore.utility.activity.BaseActivity;
import com.yumore.utility.utility.RxActivityTool;
import com.yumore.utility.utility.RxBarTool;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author yumore
 */
public class ActivitySVG extends BaseActivity {

    @BindView(R2.id.animated_svg_view)
    AnimatedSvgView mSvgView;
    @BindView(R2.id.activity_svg)
    RelativeLayout mActivitySvg;
    @BindView(R2.id.app_name)
    ImageView mAppName;
    private final Handler checkhandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
//            mAppName.setVisibility(View.VISIBLE);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        RxBarTool.hideStatusBar(this);
        setContentView(R.layout.activity_svg);
        ButterKnife.bind(this);
        setSvg(ModelSVG.values()[4]);
        CheckUpdate();
    }

    private void setSvg(ModelSVG modelSvg) {
        mSvgView.setGlyphStrings(modelSvg.glyphs);
        mSvgView.setFillColors(modelSvg.colors);
        mSvgView.setViewportSize(modelSvg.width, modelSvg.height);
        mSvgView.setTraceResidueColor(0x32000000);
        mSvgView.setTraceColors(modelSvg.colors);
        mSvgView.rebuildGlyphData();
        mSvgView.start();
    }

    /**
     * 检查是否有新版本，如果有就升级
     */
    private void CheckUpdate() {
        new Thread() {
            @Override
            public void run() {
                try {
                    Thread.sleep(500);
                    Message msg = checkhandler.obtainMessage();
                    checkhandler.sendMessage(msg);
                    Thread.sleep(2000);
                    toMain();
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }.start();
    }

    public void toMain() {
        RxActivityTool.skipActivityAndFinish(this, ExampleActivity.class);
    }
}
