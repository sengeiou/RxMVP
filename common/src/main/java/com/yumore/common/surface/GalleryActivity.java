package com.yumore.common.surface;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.viewpager.widget.ViewPager;
import butterknife.BindView;
import butterknife.OnClick;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.yumore.common.R;
import com.yumore.common.R2;
import com.yumore.common.basic.AbstractActivity;
import com.yumore.common.mvp.BaseContract;
import com.yumore.common.utility.DataUtils;
import com.yumore.common.widget.CustomPopupWindow;
import io.reactivex.functions.Consumer;

import java.util.ArrayList;
import java.util.List;

/**
 * 左右切换和点击放大的图片浏览器
 *
 * @author Nathaniel
 * @date 18-8-6-上午9:56
 */
public class GalleryActivity extends AbstractActivity implements ViewPager.OnPageChangeListener, BaseQuickAdapter.OnItemClickListener, View.OnClickListener {
    @BindView(R2.id.common_header_back_iv)
    ImageView commonHeaderBackIv;
    @BindView(R2.id.common_header_title_tv)
    TextView commonHeaderTitleTv;
    @BindView(R2.id.common_header_option_iv)
    ImageView commonHeaderOptionIv;
    @BindView(R2.id.viewPager)
    ViewPager viewPager;
    @BindView(R2.id.common_header_root)
    RelativeLayout commonHeaderRoot;

    private List<String> imageUrlList = new ArrayList<>();
    private int defaultIndex;
    private GalleryAdapter galleryAdapter;
    private CustomPopupWindow customPopupWindow;
    private OptionAdapter optionAdapter;
    private boolean optionEnable;

    public static void startCustomActivity(Context context, int position, ArrayList<String> imageUrlList, boolean optionEnable) {
        Intent intent = new Intent(context, GalleryActivity.class);
        intent.putStringArrayListExtra("imageUrls", imageUrlList);
        intent.putExtra("defaultIndex", position);
        intent.putExtra("optionEnable", optionEnable);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    @Override
    protected int getPlatformType() {
        return 999;
    }

    @Override
    protected BaseContract initPresenter() {
        return null;
    }

    @Override
    public int getLayoutId() {
        return R.layout.common_gallery_activity;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void loadData() {
        List<String> imageUrls = getIntent().getStringArrayListExtra("imageUrls");
        imageUrlList.addAll(imageUrls);
        defaultIndex = getIntent().getIntExtra("defaultIndex", 0);
        optionEnable = getIntent().getBooleanExtra("optionEnable", false);
        galleryAdapter = new GalleryAdapter();
        galleryAdapter.setImageUrlList(imageUrlList);
        optionAdapter = new OptionAdapter(R.layout.item_category_recycler_list, DataUtils.getFileOptionList(getContext()));
        optionAdapter.setShowEnable(true);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void bindView() {
        commonHeaderBackIv.setVisibility(View.VISIBLE);
        commonHeaderTitleTv.setText((defaultIndex + 1) + "/" + imageUrlList.size());
        commonHeaderOptionIv.setImageResource(R.mipmap.icon_more_grey);
        commonHeaderOptionIv.setVisibility(optionEnable ? View.VISIBLE : View.GONE);
        viewPager.setAdapter(galleryAdapter);
        viewPager.addOnPageChangeListener(this);
        viewPager.setCurrentItem(defaultIndex);
//        Sofia.with(getActivity())
//                .statusBarBackground(getResources().getColor(R.color.common_color_black_light))
//                .statusBarLightFont();
    }

    @SuppressLint("CheckResult")
    @Override
    @OnClick({
            R2.id.common_header_back_iv,
            R2.id.common_header_option_iv
    })
    public void onClick(View view) {
        if (view.getId() == R.id.common_header_back_iv) {
            finish();
        } else if (view.getId() == R.id.common_header_option_iv) {
            dismissPopupWindow();
            String[] permissions = new String[]{
                    Manifest.permission.CAMERA,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.READ_EXTERNAL_STORAGE
            };
            new RxPermissions(this).request(permissions).subscribe(new Consumer<Boolean>() {
                @Override
                public void accept(Boolean permission) {
                    if (permission) {
                        customPopupWindow = new CustomPopupWindow.Builder(getContext())
                                .setBaseQuickAdapter(optionAdapter)
                                .setWidthFull(true)
                                .setHeightFull(false)
                                .setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false))
                                .setOnItemClickListener(GalleryActivity.this)
                                .setOnClickListener(GalleryActivity.this)
                                .create();
                        customPopupWindow.showAtLocation(getWindow().getDecorView(), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
                    } else {
                        new AlertDialog.Builder(getContext())
                                .setTitle("温馨提示")
                                .setCancelable(false)
                                .setMessage("如果没有相机权限、录音权限、存储权限将不能使用该功能")
                                .setPositiveButton("好的", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                })
                                .create()
                                .show();
                    }
                }
            });
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onPageSelected(int position) {
        defaultIndex = position;
        commonHeaderTitleTv.setText((defaultIndex + 1) + "/" + imageUrlList.size());
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    private void dismissPopupWindow() {
        if (customPopupWindow != null && customPopupWindow.isShowing()) {
            customPopupWindow.dismiss();
            customPopupWindow = null;
        }
    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        if (adapter instanceof OptionAdapter) {
            switch (position) {
                case 0:

                    break;
                case 1:

                    break;
                default:
                    break;
            }
            dismissPopupWindow();
        }
    }
}
