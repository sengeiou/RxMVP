package com.yumore.rxdemo.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.yumore.rxdemo.R;
import com.yumore.rxdemo.R2;
import com.yumore.rxui.activity.ActivityBase;
import com.yumore.rxui.view.RxTitle;
import com.yumore.rxui.view.heart.RxHeartLayout;
import com.yumore.rxui.view.likeview.RxShineButton;

import java.util.Random;

/**
 * @author yumore
 */
public class ActivityLike extends ActivityBase {

    @BindView(R2.id.po_image0)
    RxShineButton mRxShineButton;
    @BindView(R2.id.po_image1)
    RxShineButton porterShapeImageView1;
    @BindView(R2.id.po_image2)
    RxShineButton porterShapeImageView2;
    @BindView(R2.id.po_image3)
    RxShineButton porterShapeImageView3;
    @BindView(R2.id.ll_top)
    LinearLayout mLlTop;
    @BindView(R2.id.wrapper)
    LinearLayout mWrapper;
    @BindView(R2.id.po_image8)
    RxShineButton mPoImage8;
    @BindView(R2.id.love)
    ImageView mLove;
    @BindView(R2.id.ll_control)
    LinearLayout mLlControl;
    @BindView(R2.id.ll_bottom)
    LinearLayout mLlBottom;
    @BindView(R2.id.heart_layout)
    RxHeartLayout mRxHeartLayout;
    @BindView(R2.id.tv_hv)
    TextView mTvHv;
    @BindView(R2.id.activity_like)
    RelativeLayout mActivityLike;
    @BindView(R2.id.rx_title)
    RxTitle mRxTitle;
    private Random random = new Random();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_like);
        ButterKnife.bind(this);
        mRxTitle.setLeftFinish(mContext);

        mRxShineButton.init(this);
        porterShapeImageView1.init(this);
        porterShapeImageView2.init(this);
        porterShapeImageView3.init(this);

        RxShineButton rxShineButtonJava = new RxShineButton(this);

        rxShineButtonJava.setBtnColor(Color.GRAY);
        rxShineButtonJava.setBtnFillColor(Color.RED);
        rxShineButtonJava.setShapeResource(R.raw.heart);
        rxShineButtonJava.setAllowRandomColor(true);
        rxShineButtonJava.setShineSize(100);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(100, 100);
        rxShineButtonJava.setLayoutParams(layoutParams);
        if (mWrapper != null) {
            mWrapper.addView(rxShineButtonJava);
        }


        mRxShineButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });
        mRxShineButton.setOnCheckStateChangeListener(new RxShineButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(View view, boolean checked) {
            }
        });

        porterShapeImageView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });
        porterShapeImageView3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });

    }

    @OnClick(R2.id.love)
    public void onClick() {
        mRxHeartLayout.post(new Runnable() {
            @Override
            public void run() {
                int rgb = Color.rgb(random.nextInt(255), random.nextInt(255), random.nextInt(255));
                mRxHeartLayout.addHeart(rgb);
            }
        });
    }
}
