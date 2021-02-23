package com.yumore.example.surface;

import android.Manifest;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.android.arouter.facade.annotation.Route;
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
import butterknife.ButterKnife;

/**
 * @author yumore
 */
@Route(path = RouterConstants.EXAMPLE_HOME)
public class ExampleActivity extends AppCompatActivity {
    private static final int TIME_INTERVAL = 2000;
    private static final int COLUMN_COUNT_DEFAULT = 3;
    @BindView(R2.id.recyclerView)
    RecyclerView recyclerview;
    private List<ModelMainItem> modelMainItemList;
    private ExampleActivity mContext;
    private long mBackPressed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_example);
        ButterKnife.bind(this);
        mContext = this;
        initData();
        initView();

        RxPermissionsTool.with(mContext).
                addPermission(Manifest.permission.ACCESS_FINE_LOCATION).
                addPermission(Manifest.permission.ACCESS_COARSE_LOCATION).
                addPermission(Manifest.permission.READ_EXTERNAL_STORAGE).
                addPermission(Manifest.permission.CAMERA).
                addPermission(Manifest.permission.CALL_PHONE).
                addPermission(Manifest.permission.READ_PHONE_STATE).
                initPermission();
    }

    private void initData() {
        modelMainItemList = new ArrayList<>();
        modelMainItemList.add(new ModelMainItem("RxPhotoTool操作UZrop裁剪图片", R.drawable.circle_elves_ball, ProfileActivity.class));
        modelMainItemList.add(new ModelMainItem("二维码与条形码的扫描与生成", R.drawable.circle_dynamic_generation_code, ActivityCodeTool.class));
        modelMainItemList.add(new ModelMainItem("动态生成码", R.drawable.circle_qr_code, ActivityCreateQRCode.class));

        modelMainItemList.add(new ModelMainItem("WebView的封装可播放视频", R.drawable.circle_webpage, BrowserActivity.class));
        modelMainItemList.add(new ModelMainItem("图片的缩放艺术", R.drawable.circle_scale_icon, ActivityRxScaleImageView.class));

        modelMainItemList.add(new ModelMainItem("RxDataTool操作Demo", R.drawable.circle_data, ActivityRxDataTool.class));
        modelMainItemList.add(new ModelMainItem("设备信息", R.drawable.circle_device_info, ActivityDeviceInfo.class));
        modelMainItemList.add(new ModelMainItem("RxTextTool操作Demo", R.drawable.circle_text, ActivityTextTool.class));

        modelMainItemList.add(new ModelMainItem("进度条的艺术", R.drawable.circle_bar, ActivityProgressBar.class));
        modelMainItemList.add(new ModelMainItem("加载的艺术", R.drawable.circle_loading_icon, LoadingActivity.class));
        modelMainItemList.add(new ModelMainItem("点赞控件", R.drawable.circle_heart_circle, ActivityLike.class));

        modelMainItemList.add(new ModelMainItem("旋转引擎View", R.drawable.circle_rotate, ActivityRxRotateBar.class));
        modelMainItemList.add(new ModelMainItem("蛛网等级View", R.drawable.circle_cobweb, ActivityCobweb.class));
        modelMainItemList.add(new ModelMainItem("添加购物车控件", R.drawable.circle_shop_cart, ActivityShoppingView.class));

        modelMainItemList.add(new ModelMainItem("网速控件", R.drawable.circle_net_speed, ActivityNetSpeed.class));
        modelMainItemList.add(new ModelMainItem("验证码", R.drawable.circle_captcha, ActivityRxCaptcha.class));
        modelMainItemList.add(new ModelMainItem("横向滑动选择控件", R.drawable.circle_bookshelf, ActivityWheelHorizontal.class));

        modelMainItemList.add(new ModelMainItem("横向左右自动滚动的ImageView", R.drawable.circle_two_way, ActivityAutoImageView.class));
        modelMainItemList.add(new ModelMainItem("SlidingDrawerSingle使用", R.drawable.circle_up_down, ActivitySlidingDrawerSingle.class));
        modelMainItemList.add(new ModelMainItem("RxSeekBar", R.drawable.circle_seek, ActivitySeekBar.class));

        modelMainItemList.add(new ModelMainItem("登录界面", R.drawable.circle_clound, ActivityLoginAct.class));
        modelMainItemList.add(new ModelMainItem("PopupView的使用", R.drawable.circle_bullet, ActivityPopupView.class));
        modelMainItemList.add(new ModelMainItem("RxToast的使用", R.drawable.circle_toast, ActivityRxToast.class));

        modelMainItemList.add(new ModelMainItem("RunTextView的使用", R.drawable.circle_wrap_text, ActivityRunTextView.class));
        modelMainItemList.add(new ModelMainItem("选座控件", R.drawable.circle_seat, ActivitySeat.class));
        modelMainItemList.add(new ModelMainItem("银行卡组堆叠控件", R.drawable.circle_credit_card, ActivityCardStack.class));

        modelMainItemList.add(new ModelMainItem("联系人侧边快速导航", R.drawable.circle_phone, ActivityContact.class));
        modelMainItemList.add(new ModelMainItem("GPS原生定位", R.drawable.circle_gps_icon, ActivityLocation.class));
        modelMainItemList.add(new ModelMainItem("震动的艺术", R.drawable.circle_vibrate, ActivityVibrate.class));

        modelMainItemList.add(new ModelMainItem("压缩与加密的艺术", R.drawable.circle_zip, ActivityZipEncrypt.class));
        modelMainItemList.add(new ModelMainItem("图片添加经纬度信息", R.drawable.circle_picture_location, ActivityRxExifTool.class));
        modelMainItemList.add(new ModelMainItem("RxWaveView", R.drawable.circle_wave, ActivityRxWaveView.class));
        modelMainItemList.add(new ModelMainItem("app检测更新与安装", R.mipmap.icon_pikachu, ActivitySplash.class));
        modelMainItemList.add(new ModelMainItem("PULL解析XML", R.mipmap.icon_pikachu, ActivityXmlParse.class));
        modelMainItemList.add(new ModelMainItem("支付宝支付Demo", R.drawable.circle_alipay, AliPayActivity.class));
        modelMainItemList.add(new ModelMainItem("流利说欢迎动画", R.mipmap.icon_pikachu, PreviewActivity.class));
        modelMainItemList.add(new ModelMainItem("相册选择界面", R.mipmap.icon_pikachu, DaemonActivity.class));
        modelMainItemList.add(new ModelMainItem("考试模式", R.mipmap.icon_pikachu, SubjectActivity.class));
        modelMainItemList.add(new ModelMainItem("Presenter复用", R.mipmap.icon_pikachu, ExampleActivity1.class));
    }

    private void initView() {
        if (COLUMN_COUNT_DEFAULT <= 1) {
            recyclerview.setLayoutManager(new LinearLayoutManager(mContext));
        } else {
            recyclerview.setLayoutManager(new GridLayoutManager(mContext, COLUMN_COUNT_DEFAULT));
        }

        recyclerview.addItemDecoration(new RxRecyclerViewDividerTool(RxImageTool.dp2px(5f)));
        AdapterRecyclerViewMain recyclerViewMain = new AdapterRecyclerViewMain(modelMainItemList);

        recyclerview.setAdapter(recyclerViewMain);
    }

    @Override
    public void onBackPressed() {
        if (mBackPressed + TIME_INTERVAL > System.currentTimeMillis()) {
            super.onBackPressed();
            return;
        } else {
            Toast.makeText(getBaseContext(), "再次点击返回键退出", Toast.LENGTH_SHORT).show();
        }
        mBackPressed = System.currentTimeMillis();
    }
}
