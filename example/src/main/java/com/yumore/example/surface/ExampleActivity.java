package com.yumore.example.surface;

import android.Manifest;
import android.os.Bundle;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.yumore.common.basic.AbsMVPActivity;
import com.yumore.common.basic.BaseContract;
import com.yumore.daemon.DaemonActivity;
import com.yumore.driving.SubjectActivity;
import com.yumore.easymvp.example.ExampleActivity1;
import com.yumore.example.R;
import com.yumore.example.R2;
import com.yumore.example.adapter.AdapterRecyclerViewMain;
import com.yumore.example.entity.ModelMainItem;
import com.yumore.feature.activity.ActivityCodeTool;
import com.yumore.preview.PreviewActivity;
import com.yumore.provider.RouterConstants;
import com.yumore.utility.activity.BrowserActivity;
import com.yumore.utility.utility.RxImageTool;
import com.yumore.utility.utility.RxPermissionsTool;
import com.yumore.utility.utility.RxRecyclerViewDividerTool;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * @author yumore
 */
@Route(path = RouterConstants.EXAMPLE_HOME)
public class ExampleActivity extends AbsMVPActivity {
    private static final int TIME_INTERVAL = 2000;
    private static final int COLUMN_COUNT_DEFAULT = 3;
    @BindView(R2.id.recyclerView)
    RecyclerView recyclerview;
    private List<ModelMainItem> modelMainItemList;
    private ExampleActivity mContext;
    private long lastClickTime;

    @Override
    public void beforeInit(Bundle savedInstanceState) {
        super.beforeInit(savedInstanceState);
        RxPermissionsTool.with(mContext).
            addPermission(Manifest.permission.ACCESS_FINE_LOCATION).
            addPermission(Manifest.permission.ACCESS_COARSE_LOCATION).
            addPermission(Manifest.permission.READ_EXTERNAL_STORAGE).
            addPermission(Manifest.permission.CAMERA).
            addPermission(Manifest.permission.CALL_PHONE).
            addPermission(Manifest.permission.READ_PHONE_STATE).
            initPermission();
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_example;
    }

    @Override
    public void initView() {

    }

    @Override
    public void loadData() {
        modelMainItemList = new ArrayList<>();
        modelMainItemList.add(new ModelMainItem("RxPhotoTool??????UZrop????????????", R.drawable.circle_elves_ball, ProfileActivity.class));
        modelMainItemList.add(new ModelMainItem("???????????????????????????????????????", R.drawable.circle_dynamic_generation_code, ActivityCodeTool.class));
        modelMainItemList.add(new ModelMainItem("???????????????", R.drawable.circle_qr_code, ActivityCreateQRCode.class));

        modelMainItemList.add(new ModelMainItem("WebView????????????????????????", R.drawable.circle_webpage, BrowserActivity.class));
        modelMainItemList.add(new ModelMainItem("?????????????????????", R.drawable.circle_scale_icon, ActivityRxScaleImageView.class));

        modelMainItemList.add(new ModelMainItem("RxDataTool??????Demo", R.drawable.circle_data, ActivityRxDataTool.class));
        modelMainItemList.add(new ModelMainItem("????????????", R.drawable.circle_device_info, ActivityDeviceInfo.class));
        modelMainItemList.add(new ModelMainItem("RxTextTool??????Demo", R.drawable.circle_text, ActivityTextTool.class));

        modelMainItemList.add(new ModelMainItem("??????????????????", R.drawable.circle_bar, ActivityProgressBar.class));
        modelMainItemList.add(new ModelMainItem("???????????????", R.drawable.circle_loading_icon, LoadingActivity.class));
        modelMainItemList.add(new ModelMainItem("????????????", R.drawable.circle_heart_circle, ActivityLike.class));

        modelMainItemList.add(new ModelMainItem("????????????View", R.drawable.circle_rotate, ActivityRxRotateBar.class));
        modelMainItemList.add(new ModelMainItem("????????????View", R.drawable.circle_cobweb, ActivityCobweb.class));
        modelMainItemList.add(new ModelMainItem("?????????????????????", R.drawable.circle_shop_cart, ActivityShoppingView.class));

        modelMainItemList.add(new ModelMainItem("????????????", R.drawable.circle_net_speed, ActivityNetSpeed.class));
        modelMainItemList.add(new ModelMainItem("?????????", R.drawable.circle_captcha, ActivityRxCaptcha.class));
        modelMainItemList.add(new ModelMainItem("????????????????????????", R.drawable.circle_bookshelf, ActivityWheelHorizontal.class));

        modelMainItemList.add(new ModelMainItem("???????????????????????????ImageView", R.drawable.circle_two_way, ActivityAutoImageView.class));
        modelMainItemList.add(new ModelMainItem("SlidingDrawerSingle??????", R.drawable.circle_up_down, ActivitySlidingDrawerSingle.class));
        modelMainItemList.add(new ModelMainItem("RxSeekBar", R.drawable.circle_seek, ActivitySeekBar.class));

        modelMainItemList.add(new ModelMainItem("????????????", R.drawable.circle_clound, ActivityLoginAct.class));
        modelMainItemList.add(new ModelMainItem("PopupView?????????", R.drawable.circle_bullet, ActivityPopupView.class));
        modelMainItemList.add(new ModelMainItem("RxToast?????????", R.drawable.circle_toast, ActivityRxToast.class));

        modelMainItemList.add(new ModelMainItem("RunTextView?????????", R.drawable.circle_wrap_text, ActivityRunTextView.class));
        modelMainItemList.add(new ModelMainItem("????????????", R.drawable.circle_seat, ActivitySeat.class));
        modelMainItemList.add(new ModelMainItem("????????????????????????", R.drawable.circle_credit_card, ActivityCardStack.class));

        modelMainItemList.add(new ModelMainItem("???????????????????????????", R.drawable.circle_phone, ActivityContact.class));
        modelMainItemList.add(new ModelMainItem("GPS????????????", R.drawable.circle_gps_icon, ActivityLocation.class));
        modelMainItemList.add(new ModelMainItem("???????????????", R.drawable.circle_vibrate, ActivityVibrate.class));

        modelMainItemList.add(new ModelMainItem("????????????????????????", R.drawable.circle_zip, ActivityZipEncrypt.class));
        modelMainItemList.add(new ModelMainItem("???????????????????????????", R.drawable.circle_picture_location, ActivityRxExifTool.class));
        modelMainItemList.add(new ModelMainItem("RxWaveView", R.drawable.circle_wave, ActivityRxWaveView.class));
        modelMainItemList.add(new ModelMainItem("app?????????????????????", R.mipmap.icon_pikachu, ActivitySplash.class));
        modelMainItemList.add(new ModelMainItem("PULL??????XML", R.mipmap.icon_pikachu, ActivityXmlParse.class));
        modelMainItemList.add(new ModelMainItem("???????????????Demo", R.drawable.circle_alipay, AliPayActivity.class));
        modelMainItemList.add(new ModelMainItem("?????????????????????", R.mipmap.icon_pikachu, PreviewActivity.class));
        modelMainItemList.add(new ModelMainItem("??????????????????", R.mipmap.icon_pikachu, DaemonActivity.class));
        modelMainItemList.add(new ModelMainItem("????????????", R.mipmap.icon_pikachu, SubjectActivity.class));
        modelMainItemList.add(new ModelMainItem("Presenter??????", R.mipmap.icon_pikachu, ExampleActivity1.class));
    }

    @Override
    public void bindView() {
        recyclerview.setLayoutManager(new LinearLayoutManager(mContext));
        recyclerview.addItemDecoration(new RxRecyclerViewDividerTool(RxImageTool.dp2px(5f)));
        AdapterRecyclerViewMain recyclerViewMain = new AdapterRecyclerViewMain(modelMainItemList);

        recyclerview.setAdapter(recyclerViewMain);
    }

    @Override
    protected BaseContract initPresenter() {
        return null;
    }

    @Override
    public void onBackPressed() {
        if (lastClickTime + TIME_INTERVAL > System.currentTimeMillis()) {
            super.onBackPressed();
            return;
        } else {
            Toast.makeText(getBaseContext(), "???????????????????????????", Toast.LENGTH_SHORT).show();
        }
        lastClickTime = System.currentTimeMillis();
    }
}
