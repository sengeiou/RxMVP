package com.yumore.example.surface;

import android.os.Bundle;
import android.widget.SeekBar;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.yumore.example.R;
import com.yumore.example.R2;
import com.yumore.utility.activity.ActivityBase;
import com.yumore.utility.entity.ModelSpider;
import com.yumore.utility.widget.RxCobwebView;
import com.yumore.utility.widget.RxTitle;
import com.yumore.utility.widget.colorpicker.ColorPickerView;
import com.yumore.utility.widget.colorpicker.OnColorChangedListener;
import com.yumore.utility.widget.colorpicker.OnColorSelectedListener;
import com.yumore.utility.widget.colorpicker.slider.AlphaSlider;
import com.yumore.utility.widget.colorpicker.slider.LightnessSlider;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * @author yumore
 */
public class ActivityCobweb extends ActivityBase implements SeekBar.OnSeekBarChangeListener {

    @BindView(R2.id.rx_title)
    RxTitle mRxTitle;
    @BindView(R2.id.cobweb_view)
    RxCobwebView mCobwebView;
    @BindView(R2.id.seekbar_level)
    SeekBar mSeekbarLevel;
    @BindView(R2.id.seekbar_spider_number)
    SeekBar mSeekbarSpiderNumber;
    @BindView(R2.id.color_picker_view)
    ColorPickerView mColorPickerView;
    @BindView(R2.id.v_lightness_slider)
    LightnessSlider mVLightnessSlider;
    @BindView(R2.id.v_alpha_slider)
    AlphaSlider mVAlphaSlider;
    @BindView(R2.id.color_picker_view_level)
    ColorPickerView mColorPickerViewLevel;
    @BindView(R2.id.v_lightness_slider_level)
    LightnessSlider mVLightnessSliderLevel;
    @BindView(R2.id.v_alpha_slider_level)
    AlphaSlider mVAlphaSliderLevel;

    private String[] nameStrs = {
            "金钱", "能力", "美貌", "智慧", "交际",
            "口才", "力量", "智力", "体力", "体质",
            "敏捷", "精神", "耐力", "精通", "急速",
            "暴击", "回避", "命中", "跳跃", "反应",
            "幸运", "魅力", "感知", "活力", "意志"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        RxBarTool.setTransparentStatusBar(mContext);
        setContentView(R.layout.activity_cobweb);
        ButterKnife.bind(this);
        mRxTitle.setLeftFinish(mContext);
        mSeekbarLevel.setOnSeekBarChangeListener(this);
        mSeekbarSpiderNumber.setOnSeekBarChangeListener(this);

        mColorPickerView.addOnColorChangedListener(new OnColorChangedListener() {
            @Override
            public void onColorChanged(int selectedColor) {
                // Handle on color change
                mCobwebView.setSpiderColor(selectedColor);
            }
        });
        mColorPickerView.addOnColorSelectedListener(new OnColorSelectedListener() {
            @Override
            public void onColorSelected(int selectedColor) {
                //mCobwebView.setSpiderColor(selectedColor);
            }
        });
        mColorPickerViewLevel.addOnColorChangedListener(new OnColorChangedListener() {
            @Override
            public void onColorChanged(int selectedColor) {
                mCobwebView.setSpiderLevelColor(selectedColor);
            }
        });
    }


    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        int id = seekBar.getId();
        if (id == R.id.seekbar_level) {
            mCobwebView.setSpiderMaxLevel(progress + 1);
        } else if (id == R.id.seekbar_spider_number) {
            int number = progress + 1;
            List<ModelSpider> modelSpiders = new ArrayList<>();
            for (int i = 0; i < number; i++) {
                modelSpiders.add(new ModelSpider(nameStrs[i], 1 + new Random().nextInt(mCobwebView.getSpiderMaxLevel())));
            }
            mCobwebView.setSpiderList(modelSpiders);
        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }
}
