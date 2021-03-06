package com.yumore.example.surface;

import android.content.ContentResolver;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.core.app.ActivityCompat;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestOptions;
import com.yalantis.ucrop.UCrop;
import com.yalantis.ucrop.UCropActivity;
import com.yumore.example.R;
import com.yumore.example.R2;
import com.yumore.utility.activity.BaseActivity;
import com.yumore.utility.utility.RxPhotoTool;
import com.yumore.utility.utility.RxSPTool;
import com.yumore.utility.widget.dialog.RxDialogChooseImage;
import com.yumore.utility.widget.dialog.RxDialogScaleView;
import com.yumore.utility.widget.dialog.RxDialogSureCancel;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 * @author yumore
 */
public class ProfileActivity extends BaseActivity {
    @BindView(R2.id.tv_bg)
    TextView mTvBg;
    @BindView(R2.id.iv_avatar)
    ImageView mIvAvatar;
    @BindView(R2.id.ll_anchor_left)
    LinearLayout mLlAnchorLeft;
    @BindView(R2.id.rl_avatar)
    RelativeLayout mRlAvatar;
    @BindView(R2.id.tv_name)
    TextView mTvName;
    @BindView(R2.id.tv_constellation)
    TextView mTvConstellation;
    @BindView(R2.id.tv_birthday)
    TextView mTvBirthday;
    @BindView(R2.id.tv_address)
    TextView mTvAddress;
    @BindView(R2.id.tv_lables)
    TextView mTvLables;
    @BindView(R2.id.textView2)
    TextView mTextView2;
    @BindView(R2.id.editText2)
    TextView mEditText2;
    @BindView(R2.id.btn_exit)
    TextView mBtnExit;
    @BindView(R2.id.activity_user)
    LinearLayout mActivityUser;
    private Uri resultUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        ButterKnife.bind(this);
        initView();
    }

    protected void initView() {
        Resources resources = baseActivity.getResources();
        resultUri = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://"
                + resources.getResourcePackageName(R.drawable.circle_elves_ball) + "/"
                + resources.getResourceTypeName(R.drawable.circle_elves_ball) + "/"
                + resources.getResourceEntryName(R.drawable.circle_elves_ball));

        ImageView imageView = findViewById(R.id.common_header_back_iv);
        imageView.setVisibility(View.VISIBLE);
        imageView.setOnClickListener(view -> {
            finish();
        });
        TextView textView = findViewById(R.id.common_header_title_tv);
        textView.setText("????????????");

        mIvAvatar.setOnClickListener(view -> initDialogChooseImage());
        mIvAvatar.setOnLongClickListener(view -> {
            RxDialogScaleView rxDialogScaleView = new RxDialogScaleView(baseActivity);
            rxDialogScaleView.setImage(resultUri);
            rxDialogScaleView.show();
            return false;
        });
    }

    private void initDialogChooseImage() {
        RxDialogChooseImage dialogChooseImage = new RxDialogChooseImage(baseActivity, RxDialogChooseImage.LayoutType.TITLE);
        dialogChooseImage.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case RxPhotoTool.GET_IMAGE_FROM_PHONE://???????????????????????????
                if (resultCode == RESULT_OK) {
//                    RxPhotoTool.cropImage(ActivityUser.this, );// ????????????
                    initUCrop(data.getData());
                }

                break;
            case RxPhotoTool.GET_IMAGE_BY_CAMERA://??????????????????????????????
                if (resultCode == RESULT_OK) {
                    /* data.getExtras().get("data");*/
//                    RxPhotoTool.cropImage(ActivityUser.this, RxPhotoTool.imageUriFromCamera);// ????????????
                    initUCrop(RxPhotoTool.imageUriFromCamera);
                }

                break;
            case RxPhotoTool.CROP_IMAGE://????????????????????????
                RequestOptions options = new RequestOptions()
                        .placeholder(R.drawable.circle_elves_ball)
                        //???????????????(???????????????????????????????????????)
                        .error(R.drawable.circle_elves_ball)
                        //??????Glide??????????????????
                        .diskCacheStrategy(DiskCacheStrategy.RESOURCE);

                Glide.with(baseActivity).
                        load(RxPhotoTool.cropImageUri).
                        apply(options).
                        thumbnail(0.5f).
                        into(mIvAvatar);
                break;

            case UCrop.REQUEST_CROP://UCrop?????????????????????
                if (resultCode == RESULT_OK) {
                    resultUri = UCrop.getOutput(data);
                    roadImageView(resultUri, mIvAvatar);
                    RxSPTool.putContent(baseActivity, "AVATAR", resultUri.toString());
                } else if (resultCode == UCrop.RESULT_ERROR) {
                    final Throwable cropError = UCrop.getError(data);
                }
                break;
            case UCrop.RESULT_ERROR://UCrop???????????????????????????
                final Throwable cropError = UCrop.getError(data);
                break;
            default:
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    //???Uri??????????????? ??????????????????File????????????
    private File roadImageView(Uri uri, ImageView imageView) {
        RequestOptions options = new RequestOptions()
                .placeholder(R.drawable.circle_elves_ball)
                //???????????????(???????????????????????????????????????)
                .error(R.drawable.circle_elves_ball)
                .transform(new CircleCrop())
                //??????Glide??????????????????
                .diskCacheStrategy(DiskCacheStrategy.RESOURCE);

        Glide.with(baseActivity).
                load(uri).
                apply(options).
                thumbnail(0.5f).
                into(imageView);

        return (new File(RxPhotoTool.getImageAbsolutePath(this, uri)));
    }

    private void initUCrop(Uri uri) {
        SimpleDateFormat timeFormatter = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.CHINA);
        long time = System.currentTimeMillis();
        String imageName = timeFormatter.format(new Date(time));

        Uri destinationUri = Uri.fromFile(new File(getCacheDir(), imageName + ".jpeg"));

        UCrop.Options options = new UCrop.Options();
        //????????????????????????????????????
        options.setAllowedGestures(UCropActivity.SCALE, UCropActivity.ROTATE, UCropActivity.ALL);
        //???????????????????????????????????????
        //options.setHideBottomControls(true);
        //??????toolbar??????
        options.setToolbarColor(ActivityCompat.getColor(this, R.color.colorPrimary));
        //?????????????????????
        options.setStatusBarColor(ActivityCompat.getColor(this, R.color.colorPrimaryDark));

        //????????????
        //????????????????????????
        options.setMaxScaleMultiplier(5);
        //???????????????????????????????????????
        options.setImageToCropBoundsAnimDuration(666);
        //?????????????????????????????????
        //options.setCircleDimmedLayer(true);
        //?????????????????????????????????
        // options.setShowCropFrame(false);
        //?????????????????????????????????
        //options.setCropGridStrokeWidth(20);
        //?????????????????????????????????
        //options.setCropGridColor(Color.GREEN);
        //?????????????????????
        //options.setCropGridColumnCount(2);
        //?????????????????????
        //options.setCropGridRowCount(1);

        UCrop.of(uri, destinationUri)
                .withAspectRatio(1, 1)
                .withMaxResultSize(1000, 1000)
                .withOptions(options)
                .start(this);
    }

    @OnClick(R2.id.btn_exit)
    public void onClick() {
        final RxDialogSureCancel rxDialogSureCancel = new RxDialogSureCancel(this);
        rxDialogSureCancel.getCancelView().setOnClickListener(v -> rxDialogSureCancel.cancel());
        rxDialogSureCancel.getSureView().setOnClickListener(v -> finish());
        rxDialogSureCancel.show();
    }
}
