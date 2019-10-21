package com.yumore.daemon;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.*;
import androidx.annotation.IdRes;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.yumore.daemon.adapter.GridImageAdapter;
import com.yumore.picture.PictureSelector;
import com.yumore.picture.config.PictureConfig;
import com.yumore.picture.config.PictureMimeType;
import com.yumore.picture.entity.LocalMedia;
import com.yumore.picture.permissions.RxPermissions;
import com.yumore.picture.tools.PictureFileUtils;
import com.yumore.provider.RouterConstants;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Nathaniel
 */
@Route(path = RouterConstants.DAEMON_HOME)
public class DaemonActivity extends AppCompatActivity implements View.OnClickListener, RadioGroup.OnCheckedChangeListener, CompoundButton.OnCheckedChangeListener {
    private final static String TAG = DaemonActivity.class.getSimpleName();
    private List<LocalMedia> selectList = new ArrayList<>();
    private GridImageAdapter adapter;
    private int maxSelectNum = 9;
    private TextView tv_select_num;
    private ImageView left_back, minus, plus;
    private RadioGroup rgb_crop, rgb_style, rgb_photo_mode;
    private int aspect_ratio_x, aspect_ratio_y;
    private CheckBox cb_voice, cb_choose_mode, cb_isCamera, cb_isGif,
            cb_preview_img, cb_preview_video, cb_crop, cb_compress,
            cb_mode, cb_hide, cb_crop_circular, cb_styleCrop, cb_showCropGrid,
            cb_showCropFrame, cb_preview_audio;
    private int themeId;
    private int chooseMode = PictureMimeType.ofAll();
    private GridImageAdapter.onAddPicClickListener onAddPicClickListener = new GridImageAdapter.onAddPicClickListener() {
        @Override
        public void onAddPicClick() {
            boolean mode = cb_mode.isChecked();
            if (mode) {
                // 进入相册 以下是例子：不需要的api可以不写
                PictureSelector.create(DaemonActivity.this)
                        .openGallery(chooseMode)// 全部.PictureMimeType.ofAll()、图片.ofImage()、视频.ofVideo()、音频.ofAudio()
                        .theme(themeId)// 主题样式设置 具体参考 values/styles   用法：R.style.picture.white.style
                        .maxSelectNum(maxSelectNum)// 最大图片选择数量
                        .minSelectNum(1)// 最小选择数量
                        .imageSpanCount(4)// 每行显示个数
                        .selectionMode(cb_choose_mode.isChecked() ?
                                PictureConfig.MULTIPLE : PictureConfig.SINGLE)// 多选 or 单选
                        .previewImage(cb_preview_img.isChecked())// 是否可预览图片
                        .previewVideo(cb_preview_video.isChecked())// 是否可预览视频
                        .enablePreviewAudio(cb_preview_audio.isChecked()) // 是否可播放音频
                        .isCamera(cb_isCamera.isChecked())// 是否显示拍照按钮
                        .isZoomAnim(true)// 图片列表点击 缩放效果 默认true
                        //.imageFormat(PictureMimeType.PNG)// 拍照保存图片格式后缀,默认jpeg
                        //.setOutputCameraPath("/CustomPath")// 自定义拍照保存路径
                        .enableCrop(cb_crop.isChecked())// 是否裁剪
                        .compress(cb_compress.isChecked())// 是否压缩
                        .synOrAsy(true)//同步true或异步false 压缩 默认同步
                        //.compressSavePath(getPath())//压缩图片保存地址
                        //.sizeMultiplier(0.5f)// glide 加载图片大小 0~1之间 如设置 .glideOverride()无效
                        .glideOverride(160, 160)// glide 加载宽高，越小图片列表越流畅，但会影响列表图片浏览的清晰度
                        .withAspectRatio(aspect_ratio_x, aspect_ratio_y)// 裁剪比例 如16:9 3:2 3:4 1:1 可自定义
                        .hideBottomControls(!cb_hide.isChecked())// 是否显示uCrop工具栏，默认不显示
                        .isGif(cb_isGif.isChecked())// 是否显示gif图片
                        .freeStyleCropEnabled(true)// 裁剪框是否可拖拽
                        .circleDimmedLayer(cb_crop_circular.isChecked())// 是否圆形裁剪
                        .showCropFrame(cb_showCropFrame.isChecked())// 是否显示裁剪矩形边框 圆形裁剪时建议设为false
                        .showCropGrid(cb_showCropGrid.isChecked())// 是否显示裁剪矩形网格 圆形裁剪时建议设为false
                        .openClickSound(cb_voice.isChecked())// 是否开启点击声音
                        .selectionMedia(selectList)// 是否传入已选图片
                        //.isDragFrame(false)// 是否可拖动裁剪框(固定)
//                        .videoMaxSecond(15)
//                        .videoMinSecond(10)
                        //.previewEggs(false)// 预览图片时 是否增强左右滑动图片体验(图片滑动一半即可看到上一张是否选中)
                        //.cropCompressQuality(90)// 裁剪压缩质量 默认100
                        .minimumCompressSize(100)// 小于100kb的图片不压缩
                        //.cropWH()// 裁剪宽高比，设置如果大于图片本身宽高则无效
                        //.rotateEnabled(true) // 裁剪是否可旋转图片
                        //.scaleEnabled(true)// 裁剪是否可放大缩小图片
                        //.videoQuality()// 视频录制质量 0 or 1
                        //.videoSecond()//显示多少秒以内的视频or音频也可适用
                        //.recordVideoSecond()//录制视频秒数 默认60s
                        .forResult(PictureConfig.CHOOSE_REQUEST);//结果回调onActivityResult code
            } else {
                // 单独拍照
                PictureSelector.create(DaemonActivity.this)
                        .openCamera(chooseMode)// 单独拍照，也可录像或也可音频 看你传入的类型是图片or视频
                        .theme(themeId)// 主题样式设置 具体参考 values/styles
                        .maxSelectNum(maxSelectNum)// 最大图片选择数量
                        .minSelectNum(1)// 最小选择数量
                        .selectionMode(cb_choose_mode.isChecked() ?
                                PictureConfig.MULTIPLE : PictureConfig.SINGLE)// 多选 or 单选
                        .previewImage(cb_preview_img.isChecked())// 是否可预览图片
                        .previewVideo(cb_preview_video.isChecked())// 是否可预览视频
                        .enablePreviewAudio(cb_preview_audio.isChecked()) // 是否可播放音频
                        .isCamera(cb_isCamera.isChecked())// 是否显示拍照按钮
                        .enableCrop(cb_crop.isChecked())// 是否裁剪
                        .compress(cb_compress.isChecked())// 是否压缩
                        .glideOverride(160, 160)// glide 加载宽高，越小图片列表越流畅，但会影响列表图片浏览的清晰度
                        .withAspectRatio(aspect_ratio_x, aspect_ratio_y)// 裁剪比例 如16:9 3:2 3:4 1:1 可自定义
                        .hideBottomControls(!cb_hide.isChecked())// 是否显示uCrop工具栏，默认不显示
                        .isGif(cb_isGif.isChecked())// 是否显示gif图片
                        .freeStyleCropEnabled(cb_styleCrop.isChecked())// 裁剪框是否可拖拽
                        .circleDimmedLayer(cb_crop_circular.isChecked())// 是否圆形裁剪
                        .showCropFrame(cb_showCropFrame.isChecked())// 是否显示裁剪矩形边框 圆形裁剪时建议设为false
                        .showCropGrid(cb_showCropGrid.isChecked())// 是否显示裁剪矩形网格 圆形裁剪时建议设为false
                        .openClickSound(cb_voice.isChecked())// 是否开启点击声音
                        .selectionMedia(selectList)// 是否传入已选图片
                        .previewEggs(false)//预览图片时 是否增强左右滑动图片体验(图片滑动一半即可看到上一张是否选中)
                        //.previewEggs(false)// 预览图片时 是否增强左右滑动图片体验(图片滑动一半即可看到上一张是否选中)
                        //.cropCompressQuality(90)// 裁剪压缩质量 默认为100
                        .minimumCompressSize(100)// 小于100kb的图片不压缩
                        //.cropWH()// 裁剪宽高比，设置如果大于图片本身宽高则无效
                        //.rotateEnabled() // 裁剪是否可旋转图片
                        //.scaleEnabled()// 裁剪是否可放大缩小图片
                        //.videoQuality()// 视频录制质量 0 or 1
                        //.videoSecond()////显示多少秒以内的视频or音频也可适用
                        .forResult(PictureConfig.CHOOSE_REQUEST);//结果回调onActivityResult code
            }
        }

    };
    private int x = 0, y = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daemon);
        themeId = R.style.picture_default_style;
        minus = findViewById(R.id.minus);
        plus = findViewById(R.id.plus);
        tv_select_num = findViewById(R.id.tv_select_num);
        rgb_crop = findViewById(R.id.rgb_crop);
        rgb_style = findViewById(R.id.rgb_style);
        rgb_photo_mode = findViewById(R.id.rgb_photo_mode);
        cb_voice = findViewById(R.id.cb_voice);
        cb_choose_mode = findViewById(R.id.cb_choose_mode);
        cb_isCamera = findViewById(R.id.cb_isCamera);
        cb_isGif = findViewById(R.id.cb_isGif);
        cb_preview_img = findViewById(R.id.cb_preview_img);
        cb_preview_video = findViewById(R.id.cb_preview_video);
        cb_crop = findViewById(R.id.cb_crop);
        cb_styleCrop = findViewById(R.id.cb_styleCrop);
        cb_compress = findViewById(R.id.cb_compress);
        cb_mode = findViewById(R.id.cb_mode);
        cb_showCropGrid = findViewById(R.id.cb_showCropGrid);
        cb_showCropFrame = findViewById(R.id.cb_showCropFrame);
        cb_preview_audio = findViewById(R.id.cb_preview_audio);
        cb_hide = findViewById(R.id.cb_hide);
        cb_crop_circular = findViewById(R.id.cb_crop_circular);
        rgb_crop.setOnCheckedChangeListener(this);
        rgb_style.setOnCheckedChangeListener(this);
        rgb_photo_mode.setOnCheckedChangeListener(this);
        RecyclerView recyclerView = findViewById(R.id.recycler);
        left_back = findViewById(R.id.left_back);
        left_back.setOnClickListener(this);
        minus.setOnClickListener(this);
        plus.setOnClickListener(this);
        cb_crop.setOnCheckedChangeListener(this);
        cb_crop_circular.setOnCheckedChangeListener(this);
        cb_compress.setOnCheckedChangeListener(this);
        FullyGridLayoutManager manager = new FullyGridLayoutManager(DaemonActivity.this, 4, GridLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(manager);
        adapter = new GridImageAdapter(DaemonActivity.this, onAddPicClickListener);
        adapter.setList(selectList);
        adapter.setSelectMax(maxSelectNum);
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(new GridImageAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position, View v) {
                if (selectList.size() > 0) {
                    LocalMedia media = selectList.get(position);
                    String pictureType = media.getPictureType();
                    int mediaType = PictureMimeType.pictureToVideo(pictureType);
                    switch (mediaType) {
                        case 1:
                            // 预览图片 可自定长按保存路径
                            //PictureSelector.create(MainActivity.this).themeStyle(themeId).externalPicturePreview(position, "/custom_file", selectList);
                            PictureSelector.create(DaemonActivity.this).themeStyle(themeId).openExternalPreview(position, selectList);
                            break;
                        case 2:
                            // 预览视频
                            PictureSelector.create(DaemonActivity.this).externalPictureVideo(media.getPath());
                            break;
                        case 3:
                            // 预览音频
                            PictureSelector.create(DaemonActivity.this).externalPictureAudio(media.getPath());
                            break;
                    }
                }
            }
        });

        // 清空图片缓存，包括裁剪、压缩后的图片 注意:必须要在上传完成后调用 必须要获取权限
        RxPermissions permissions = new RxPermissions(this);
        permissions.request(Manifest.permission.WRITE_EXTERNAL_STORAGE).subscribe(new Observer<Boolean>() {


            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(Boolean aBoolean) {
                if (aBoolean) {
                    PictureFileUtils.deleteCacheDirFile(DaemonActivity.this);
                } else {
                    Toast.makeText(DaemonActivity.this,
                            getString(R.string.picture_jurisdiction), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == PictureConfig.CHOOSE_REQUEST) {// 图片选择结果回调
                selectList = PictureSelector.obtainMultipleResult(data);
                // 例如 LocalMedia 里面返回三种path
                // 1.media.getPath(); 为原图path
                // 2.media.getCutPath();为裁剪后path，需判断media.isCut();是否为true
                // 3.media.getCompressPath();为压缩后path，需判断media.isCompressed();是否为true
                // 如果裁剪并压缩了，已取压缩路径为准，因为是先裁剪后压缩的
                for (LocalMedia media : selectList) {
                    Log.i(TAG, "压缩---->" + media.getCompressPath());
                    Log.i(TAG, "原图---->" + media.getPath());
                    Log.i(TAG, "裁剪---->" + media.getCutPath());
                }
                adapter.setList(selectList);
                adapter.notifyDataSetChanged();
            }
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.left_back) {
            finish();
        } else if (id == R.id.minus) {
            if (maxSelectNum > 1) {
                maxSelectNum--;
            }
            tv_select_num.setText(maxSelectNum + "");
            adapter.setSelectMax(maxSelectNum);
        } else if (id == R.id.plus) {
            maxSelectNum++;
            tv_select_num.setText(maxSelectNum + "");
            adapter.setSelectMax(maxSelectNum);
        }
    }

    @Override
    public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
        if (checkedId == R.id.rb_all) {
            chooseMode = PictureMimeType.ofAll();
            cb_preview_img.setChecked(true);
            cb_preview_video.setChecked(true);
            cb_isGif.setChecked(false);
            cb_preview_video.setChecked(true);
            cb_preview_img.setChecked(true);
            cb_preview_video.setVisibility(View.VISIBLE);
            cb_preview_img.setVisibility(View.VISIBLE);
            cb_compress.setVisibility(View.VISIBLE);
            cb_crop.setVisibility(View.VISIBLE);
            cb_isGif.setVisibility(View.VISIBLE);
            cb_preview_audio.setVisibility(View.GONE);
        } else if (checkedId == R.id.rb_image) {
            chooseMode = PictureMimeType.ofImage();
            cb_preview_img.setChecked(true);
            cb_preview_video.setChecked(false);
            cb_isGif.setChecked(false);
            cb_preview_video.setChecked(false);
            cb_preview_video.setVisibility(View.GONE);
            cb_preview_img.setChecked(true);
            cb_preview_audio.setVisibility(View.GONE);
            cb_preview_img.setVisibility(View.VISIBLE);
            cb_compress.setVisibility(View.VISIBLE);
            cb_crop.setVisibility(View.VISIBLE);
            cb_isGif.setVisibility(View.VISIBLE);
        } else if (checkedId == R.id.rb_video) {
            chooseMode = PictureMimeType.ofVideo();
            cb_preview_img.setChecked(false);
            cb_preview_video.setChecked(true);
            cb_isGif.setChecked(false);
            cb_isGif.setVisibility(View.GONE);
            cb_preview_video.setChecked(true);
            cb_preview_video.setVisibility(View.VISIBLE);
            cb_preview_img.setVisibility(View.GONE);
            cb_preview_img.setChecked(false);
            cb_compress.setVisibility(View.GONE);
            cb_preview_audio.setVisibility(View.GONE);
            cb_crop.setVisibility(View.GONE);
        } else if (checkedId == R.id.rb_audio) {
            chooseMode = PictureMimeType.ofAudio();
            cb_preview_audio.setVisibility(View.VISIBLE);
        } else if (checkedId == R.id.rb_crop_default) {
            aspect_ratio_x = 0;
            aspect_ratio_y = 0;
        } else if (checkedId == R.id.rb_crop_1to1) {
            aspect_ratio_x = 1;
            aspect_ratio_y = 1;
        } else if (checkedId == R.id.rb_crop_3to4) {
            aspect_ratio_x = 3;
            aspect_ratio_y = 4;
        } else if (checkedId == R.id.rb_crop_3to2) {
            aspect_ratio_x = 3;
            aspect_ratio_y = 2;
        } else if (checkedId == R.id.rb_crop_16to9) {
            aspect_ratio_x = 16;
            aspect_ratio_y = 9;
        } else if (checkedId == R.id.rb_default_style) {
            themeId = R.style.picture_default_style;
        } else if (checkedId == R.id.rb_white_style) {
            themeId = R.style.picture_white_style;
        } else if (checkedId == R.id.rb_num_style) {
            themeId = R.style.picture_QQ_style;
        } else if (checkedId == R.id.rb_sina_style) {
            themeId = R.style.picture_Sina_style;
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        int id = buttonView.getId();
        if (id == R.id.cb_crop) {
            rgb_crop.setVisibility(isChecked ? View.VISIBLE : View.GONE);
            cb_hide.setVisibility(isChecked ? View.VISIBLE : View.GONE);
            cb_crop_circular.setVisibility(isChecked ? View.VISIBLE : View.GONE);
            cb_styleCrop.setVisibility(isChecked ? View.VISIBLE : View.GONE);
            cb_showCropFrame.setVisibility(isChecked ? View.VISIBLE : View.GONE);
            cb_showCropGrid.setVisibility(isChecked ? View.VISIBLE : View.GONE);
        } else if (id == R.id.cb_crop_circular) {
            if (isChecked) {
                x = aspect_ratio_x;
                y = aspect_ratio_y;
                aspect_ratio_x = 1;
                aspect_ratio_y = 1;
            } else {
                aspect_ratio_x = x;
                aspect_ratio_y = y;
            }
            rgb_crop.setVisibility(isChecked ? View.GONE : View.VISIBLE);
            if (isChecked) {
                cb_showCropFrame.setChecked(false);
                cb_showCropGrid.setChecked(false);
            } else {
                cb_showCropFrame.setChecked(true);
                cb_showCropGrid.setChecked(true);
            }
        }
    }

    /**
     * 自定义压缩存储地址
     *
     * @return
     */
    private String getPath() {
        String path = Environment.getExternalStorageDirectory() + "/Luban/image/";
        File file = new File(path);
        if (file.mkdirs()) {
            return path;
        }
        return path;
    }
}
