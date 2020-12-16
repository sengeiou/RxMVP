package com.yumore.preview;

import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.yumore.provider.RouterConstants;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Subscription;
import rx.functions.Action1;

/**
 * @author Nathaniel
 */
@Route(path = RouterConstants.PREVIEW_HOME, group = "preview")
public class PreviewActivity extends AppCompatActivity {

    private PreviewVideoView previewVideoView;
    private ViewPager viewPager;
    private PreviewIndicator previewIndicator;

    private final List<View> viewList = new ArrayList<>();
    private final int[] imageResIds = new int[]{R.mipmap.intro_text_1, R.mipmap.intro_text_2, R.mipmap.intro_text_3};
    private CustomPagerAdapter customPagerAdapter;

    private int currentPage = 0;
    private Subscription subscription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preview);

        previewVideoView = findViewById(R.id.vv_preview);
        viewPager = findViewById(R.id.vp_image);
        previewIndicator = findViewById(R.id.indicator);

        previewVideoView.setVideoURI(Uri.parse(getVideoPath()));

        for (int mImageResId : imageResIds) {
            View view = LayoutInflater.from(this).inflate(R.layout.item_preview_recycler_list, null, false);
            ((ImageView) view.findViewById(R.id.iv_intro_text)).setImageResource(mImageResId);
            viewList.add(view);
        }
        previewIndicator.bringToFront();
        customPagerAdapter = new CustomPagerAdapter(viewList);
        viewPager.setAdapter(customPagerAdapter);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                currentPage = position;
                previewIndicator.setSelected(currentPage);
                startLoop();
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        startLoop();

    }

    /**
     * 获取video文件的路径
     *
     * @return 路径
     */
    private String getVideoPath() {
        return "android.resource://" + this.getPackageName() + "/" + R.raw.intro_video;
    }

    /**
     * 开启轮询
     */
    private void startLoop() {
        if (null != subscription) {
            subscription.unsubscribe();
        }
        subscription = Observable.interval(0, 6 * 1000, TimeUnit.MILLISECONDS)
                .subscribe(new Action1<Long>() {
                    @Override
                    public void call(Long aLong) {
                        previewVideoView.seekTo(currentPage * 6 * 1000);
                        if (!previewVideoView.isPlaying()) {
                            previewVideoView.start();
                        }
                    }
                });
    }

    @Override
    protected void onDestroy() {
        if (null != subscription) {
            subscription.unsubscribe();
        }
        super.onDestroy();
    }
}
