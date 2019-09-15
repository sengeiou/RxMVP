package com.yumore.example.surface;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.yumore.example.R;
import com.yumore.example.R2;
import com.yumore.rxui.tool.RxImageTool;
import com.yumore.rxui.view.colorpicker.ColorPickerView;
import com.yumore.rxui.view.colorpicker.OnColorChangedListener;
import com.yumore.rxui.view.colorpicker.OnColorSelectedListener;
import com.yumore.rxui.view.colorpicker.slider.AlphaSlider;
import com.yumore.rxui.view.colorpicker.slider.LightnessSlider;
import com.yumore.rxui.view.waveview.RxWaveHelper;
import com.yumore.rxui.view.waveview.RxWaveView;

/**
 * @author yumore
 */
public class ActivityRxWaveView extends AppCompatActivity {


    @BindView(R2.id.wave)
    RxWaveView mWave;
    @BindView(R2.id.border)
    TextView mBorder;
    @BindView(R2.id.seekBar)
    SeekBar mSeekBar;
    @BindView(R2.id.shape)
    TextView mShape;
    @BindView(R2.id.shapeCircle)
    RadioButton mShapeCircle;
    @BindView(R2.id.shapeSquare)
    RadioButton mShapeSquare;
    @BindView(R2.id.shapeChoice)
    RadioGroup mShapeChoice;
    @BindView(R2.id.color_picker_view)
    ColorPickerView mColorPickerView;
    @BindView(R2.id.v_lightness_slider)
    LightnessSlider mVLightnessSlider;
    @BindView(R2.id.v_alpha_slider)
    AlphaSlider mVAlphaSlider;
    private RxWaveHelper mWaveHelper;

    private int mBorderColor = Color.parseColor("#4489CFF0");
    private int mBorderWidth = 10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rx_wave_view);
        ButterKnife.bind(this);

        mWave.setBorder(mBorderWidth, mBorderColor);
        mWaveHelper = new RxWaveHelper(mWave);

        ((RadioGroup) findViewById(R.id.shapeChoice))
                .setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(RadioGroup radioGroup, int i) {
                        if (i == R.id.shapeCircle) {
                            mWave.setShapeType(RxWaveView.ShapeType.CIRCLE);
                        } else if (i == R.id.shapeSquare) {
                            mWave.setShapeType(RxWaveView.ShapeType.SQUARE);
                        }
                    }
                });

        ((SeekBar) findViewById(R.id.seekBar))
                .setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                        mBorderWidth = i;
                        mWave.setBorder(mBorderWidth, mBorderColor);
                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {

                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {

                    }
                });

        mColorPickerView.addOnColorChangedListener(new OnColorChangedListener() {
            @Override
            public void onColorChanged(int selectedColor) {
                // Handle on color change
                Log.d("selectedColor", "selectedColor: " + selectedColor);

                mWave.setWaveColor(RxImageTool.changeColorAlpha(selectedColor, 40),
                        RxImageTool.changeColorAlpha(selectedColor, 60));
                mBorderColor = RxImageTool.changeColorAlpha(selectedColor, 68);
                mWave.setBorder(mBorderWidth, mBorderColor);

//                mCobwebView.setSpiderColor(selectedColor);
            }
        });
        mColorPickerView.addOnColorSelectedListener(new OnColorSelectedListener() {
            @Override
            public void onColorSelected(int selectedColor) {
                //mCobwebView.setSpiderColor(selectedColor);
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        mWaveHelper.cancel();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mWaveHelper.start();
    }
}
