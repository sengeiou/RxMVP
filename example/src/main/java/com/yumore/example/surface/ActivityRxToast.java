package com.yumore.example.surface;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.yumore.example.R;
import com.yumore.example.R2;
import com.yumore.utility.activity.BaseActivity;
import com.yumore.utility.widget.RxTitle;
import com.yumore.utility.widget.RxToast;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author yumore
 */
public class ActivityRxToast extends BaseActivity {

    @BindView(R2.id.button_error_toast)
    Button mButtonErrorToast;
    @BindView(R2.id.button_success_toast)
    Button mButtonSuccessToast;
    @BindView(R2.id.button_info_toast)
    Button mButtonInfoToast;
    @BindView(R2.id.button_warning_toast)
    Button mButtonWarningToast;
    @BindView(R2.id.button_normal_toast_wo_icon)
    Button mButtonNormalToastWoIcon;
    @BindView(R2.id.button_normal_toast_w_icon)
    Button mButtonNormalToastWIcon;
    @BindView(R2.id.activity_main)
    RelativeLayout mActivityMain;
    @BindView(R2.id.rx_title)
    RxTitle mRxTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rx_toast);
        ButterKnife.bind(this);
        initView();
    }

    protected void initView() {
        mRxTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @OnClick({R2.id.button_error_toast,
            R2.id.button_success_toast,
            R2.id.button_info_toast,
            R2.id.button_warning_toast,
            R2.id.button_normal_toast_wo_icon,
            R2.id.button_normal_toast_w_icon})
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.button_error_toast) {
            RxToast.error(baseActivity, "这是一个提示错误的Toast！", Toast.LENGTH_SHORT, true).show();
        } else if (id == R.id.button_success_toast) {
            RxToast.success(baseActivity, "这是一个提示成功的Toast!", Toast.LENGTH_SHORT, true).show();
        } else if (id == R.id.button_info_toast) {
            RxToast.info(baseActivity, "这是一个提示信息的Toast.", Toast.LENGTH_SHORT, true).show();
        } else if (id == R.id.button_warning_toast) {
            RxToast.warning(baseActivity, "这是一个提示警告的Toast.", Toast.LENGTH_SHORT, true).show();
        } else if (id == R.id.button_normal_toast_wo_icon) {
            RxToast.normal(baseActivity, "这是一个普通的没有ICON的Toast").show();
        } else if (id == R.id.button_normal_toast_w_icon) {
            Drawable icon = getResources().getDrawable(R.drawable.set);
            RxToast.normal(baseActivity, "这是一个普通的包含ICON的Toast", icon).show();
        }
    }
}
