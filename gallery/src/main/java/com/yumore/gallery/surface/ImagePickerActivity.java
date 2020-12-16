package com.yumore.gallery.surface;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.yumore.gallery.R;
import com.yumore.gallery.adapter.ImageFolderAdapter;
import com.yumore.gallery.adapter.ImageRecyclerAdapter;
import com.yumore.gallery.adapter.ImageRecyclerAdapter.OnImageItemClickListener;
import com.yumore.gallery.entity.ImageFolder;
import com.yumore.gallery.entity.ImageItem;
import com.yumore.gallery.helper.DataHolder;
import com.yumore.gallery.helper.ImageDataSource;
import com.yumore.gallery.helper.ImagePicker;
import com.yumore.gallery.utility.ScreenUtils;
import com.yumore.gallery.widget.FolderPopUpWindow;
import com.yumore.gallery.widget.GridSpacingItemDecoration;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Nathaniel
 */
public class ImagePickerActivity extends BaseImageActivity implements ImageDataSource.OnImagesLoadedListener, OnImageItemClickListener, ImagePicker.OnImageSelectedListener, View.OnClickListener {
    public static final int REQUEST_PERMISSION_STORAGE = 0x01;
    public static final int REQUEST_PERMISSION_CAMERA = 0x02;
    public static final String INTENT_EXTRAS_CAPTURE_ENABLE = "captureEnable";
    public static final String INTENT_EXTRAS_SELECT_IMAGES = "imageItems";
    public static final String INTENT_EXTRAS_CROPPER_ENABLE = "cropperEnable";
    private static final String TAG = ImagePickerActivity.class.getSimpleName();
    private ImagePicker imagePicker;
    private boolean originEnable = false;
    private View footerView;
    private Button positive;
    private TextView mtvDir;
    private TextView toPreview;
    private ImageFolderAdapter imageFolderAdapter;
    private FolderPopUpWindow folderPopUpWindow;
    private List<ImageFolder> imageFolderList;
    private boolean captureEnable = false;
    private boolean cropperEnable = false;
    private RecyclerView recyclerView;
    private ImageRecyclerAdapter imageRecyclerAdapter;

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        captureEnable = savedInstanceState.getBoolean(INTENT_EXTRAS_CAPTURE_ENABLE, false);
        cropperEnable = savedInstanceState.getBoolean(INTENT_EXTRAS_CROPPER_ENABLE, false);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(INTENT_EXTRAS_CAPTURE_ENABLE, captureEnable);
        outState.putBoolean(INTENT_EXTRAS_CROPPER_ENABLE, cropperEnable);
    }

    @SuppressWarnings("unchecked")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_picker);

        imagePicker = ImagePicker.getInstance();
        imagePicker.clear();
        imagePicker.addOnImageSelectedListener(this);
        // 如果限制选择一张 则表示是单选
        imagePicker.setMultiMode(imagePicker.getSelectLimit() != 1);

        Intent intent = getIntent();
        if (intent != null && intent.getExtras() != null) {
            captureEnable = intent.getBooleanExtra(INTENT_EXTRAS_CAPTURE_ENABLE, false);
            cropperEnable = intent.getBooleanExtra(INTENT_EXTRAS_CROPPER_ENABLE, false);
            if (captureEnable) {
                if (!(checkPermission(Manifest.permission.CAMERA))) {
                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, ImagePickerActivity.REQUEST_PERMISSION_CAMERA);
                } else {
                    imagePicker.takePicture(this, ImagePicker.REQUEST_CODE_TAKE);
                }
            }
            ArrayList<ImageItem> imageItems = (ArrayList<ImageItem>) intent.getSerializableExtra(INTENT_EXTRAS_SELECT_IMAGES);
            imagePicker.setSelectedImages(imageItems);
        }

        recyclerView = findViewById(R.id.recyclerView);

        findViewById(R.id.btn_back).setOnClickListener(this);
        positive = findViewById(R.id.btn_ok);
        positive.setOnClickListener(this);
        toPreview = findViewById(R.id.btn_preview);
        toPreview.setOnClickListener(this);
        footerView = findViewById(R.id.footer_bar);
        View allDirectory = findViewById(R.id.ll_dir);
        allDirectory.setOnClickListener(this);
        mtvDir = findViewById(R.id.tv_dir);
        if (imagePicker.isMultiMode()) {
            positive.setVisibility(View.VISIBLE);
            toPreview.setVisibility(View.VISIBLE);
        } else {
            positive.setVisibility(View.GONE);
            toPreview.setVisibility(View.GONE);
        }

        imageFolderAdapter = new ImageFolderAdapter(this, null);
        imageRecyclerAdapter = new ImageRecyclerAdapter(this, null);

        onImageSelected(0, null, false);

        if (checkPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            new ImageDataSource(this, null, this);
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_PERMISSION_STORAGE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_PERMISSION_STORAGE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                new ImageDataSource(this, null, this);
            } else {
                showToast("权限被禁止，无法选择本地图片");
            }
        } else if (requestCode == REQUEST_PERMISSION_CAMERA) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                imagePicker.takePicture(this, ImagePicker.REQUEST_CODE_TAKE);
            } else {
                showToast("权限被禁止，无法打开相机");
            }
        }
    }

    @Override
    protected void onDestroy() {
        imagePicker.removeOnImageSelectedListener(this);
        super.onDestroy();
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btn_ok) {
            Intent intent = new Intent();
            if (cropperEnable) {
                intent = new Intent(ImagePickerActivity.this, ImageCropActivity.class);
                startActivityForResult(intent, ImagePicker.REQUEST_CODE_CROP);
            } else {
                intent.putExtra(ImagePicker.EXTRA_RESULT_ITEMS, imagePicker.getSelectedImages());
                //多选不允许裁剪裁剪，返回数据
                setResult(ImagePicker.RESULT_CODE_ITEMS, intent);
                finish();
            }
        } else if (view.getId() == R.id.ll_dir) {
            if (imageFolderList == null) {
                Log.i(TAG, "您的手机没有图片");
                return;
            }
            // 点击文件夹按钮
            createPopupFolderList();
            // 刷新数据
            imageFolderAdapter.refreshData(imageFolderList);
            if (folderPopUpWindow.isShowing()) {
                folderPopUpWindow.dismiss();
            } else {
                folderPopUpWindow.showAtLocation(footerView, Gravity.NO_GRAVITY, 0, 0);
                //默认选择当前选择的上一个，当目录很多时，直接定位到已选中的条目
                int index = imageFolderAdapter.getSelectIndex();
                index = index == 0 ? index : index - 1;
                folderPopUpWindow.setSelection(index);
            }
        } else if (view.getId() == R.id.btn_preview) {
            Intent intent = new Intent(ImagePickerActivity.this, ImagePreviewActivity.class);
            intent.putExtra(ImagePicker.EXTRA_SELECTED_IMAGE_POSITION, 0);
            intent.putExtra(ImagePicker.EXTRA_IMAGE_ITEMS, imagePicker.getSelectedImages());
            intent.putExtra(ImagePreviewActivity.ORIGIN_ENABLE, originEnable);
            intent.putExtra(ImagePicker.EXTRA_FROM_ITEMS, true);
            startActivityForResult(intent, ImagePicker.REQUEST_CODE_PREVIEW);
        } else if (view.getId() == R.id.btn_back) {
            //点击返回按钮
            finish();
        }
    }

    /**
     * 创建弹出的ListView
     */
    private void createPopupFolderList() {
        folderPopUpWindow = new FolderPopUpWindow(this, imageFolderAdapter);
        folderPopUpWindow.setOnItemClickListener(new FolderPopUpWindow.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                imageFolderAdapter.setSelectIndex(position);
                imagePicker.setCurrentImageFolderPosition(position);
                folderPopUpWindow.dismiss();
                ImageFolder imageFolder = (ImageFolder) adapterView.getAdapter().getItem(position);
                if (null != imageFolder) {
                    imageRecyclerAdapter.refreshData(imageFolder.getImages());
                    mtvDir.setText(imageFolder.getName());
                }
            }
        });
        folderPopUpWindow.setMargin(footerView.getHeight());
    }

    @Override
    public void onImagesLoaded(List<ImageFolder> imageFolders) {
        this.imageFolderList = imageFolders;
        imagePicker.setImageFolders(imageFolders);
        if (imageFolders.size() == 0) {
            imageRecyclerAdapter.refreshData(null);
        } else {
            imageRecyclerAdapter.refreshData(imageFolders.get(0).getImages());
        }
        imageRecyclerAdapter.setOnImageItemClickListener(this);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(3, ScreenUtils.dip2px(this, 2), false));
        recyclerView.setAdapter(imageRecyclerAdapter);
        imageFolderAdapter.refreshData(imageFolders);
    }

    @Override
    public void onImageItemClick(View view, ImageItem imageItem, int position) {
        //根据是否有相机按钮确定位置
        position = imagePicker.isShowCamera() ? position - 1 : position;
        if (imagePicker.isMultiMode()) {
            Intent intent = new Intent(ImagePickerActivity.this, ImagePreviewActivity.class);
            intent.putExtra(ImagePicker.EXTRA_SELECTED_IMAGE_POSITION, position);
            // 但采用弱引用会导致预览弱引用直接返回空指针
            DataHolder.getInstance().save(DataHolder.DH_CURRENT_IMAGE_FOLDER_ITEMS, imagePicker.getCurrentImageFolderItems());
            intent.putExtra(ImagePreviewActivity.ORIGIN_ENABLE, originEnable);
            ImagePicker.getInstance().setCrop(false);
            // 如果是多选，点击图片进入预览界面
            startActivityForResult(intent, ImagePicker.REQUEST_CODE_PREVIEW);
        } else {
            imagePicker.clearSelectedImages();
            imagePicker.addSelectedImageItem(position, imagePicker.getCurrentImageFolderItems().get(position), true);
            if (cropperEnable) {
                Intent intent = new Intent(ImagePickerActivity.this, ImageCropActivity.class);
                // 单选需要裁剪，进入裁剪界面
                startActivityForResult(intent, ImagePicker.REQUEST_CODE_CROP);
            } else {
                Intent intent = new Intent();
                intent.putExtra(ImagePicker.EXTRA_RESULT_ITEMS, imagePicker.getSelectedImages());
                // 单选不需要裁剪，返回数据
                setResult(ImagePicker.RESULT_CODE_ITEMS, intent);
                finish();
            }
        }
    }

    @SuppressLint("StringFormatMatches")
    @Override
    public void onImageSelected(int position, ImageItem item, boolean isAdd) {
        if (imagePicker.getSelectImageCount() > 0) {
            positive.setText(getString(R.string.ip_select_complete, imagePicker.getSelectImageCount(), imagePicker.getSelectLimit()));
            positive.setEnabled(true);
            toPreview.setEnabled(true);
            toPreview.setText(getResources().getString(R.string.ip_preview_count, imagePicker.getSelectImageCount()));
            toPreview.setTextColor(ContextCompat.getColor(this, R.color.ip_text_primary_inverted));
            positive.setTextColor(ContextCompat.getColor(this, R.color.ip_text_primary_inverted));
        } else {
            positive.setText(getString(R.string.ip_complete));
            positive.setEnabled(false);
            toPreview.setEnabled(false);
            toPreview.setText(getResources().getString(R.string.ip_preview));
            toPreview.setTextColor(ContextCompat.getColor(this, R.color.ip_text_secondary_inverted));
            positive.setTextColor(ContextCompat.getColor(this, R.color.ip_text_secondary_inverted));
        }
        for (int i = imagePicker.isShowCamera() ? 1 : 0; i < imageRecyclerAdapter.getItemCount(); i++) {
            if (imageRecyclerAdapter.getItem(i).getPath() != null && imageRecyclerAdapter.getItem(i).getPath().equals(item.getPath())) {
                imageRecyclerAdapter.notifyItemChanged(i);
                return;
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null && data.getExtras() != null) {
            if (resultCode == ImagePicker.RESULT_CODE_BACK) {
                originEnable = data.getBooleanExtra(ImagePreviewActivity.ORIGIN_ENABLE, false);
            } else {
                //从拍照界面返回 点击 X , 没有选择照片
                if (data.getSerializableExtra(ImagePicker.EXTRA_RESULT_ITEMS) == null) {
                    //什么都不做 直接调起相机
                } else {
                    //说明是从裁剪页面过来的数据，直接返回就可以
                    setResult(ImagePicker.RESULT_CODE_ITEMS, data);
                }
                finish();
            }
        } else {
            //如果是裁剪，因为裁剪指定了存储的Uri，所以返回的data一定为null
            if (resultCode == RESULT_OK && requestCode == ImagePicker.REQUEST_CODE_TAKE) {
                ImagePicker.galleryAddPic(this, imagePicker.getTakeImageFile());
                String path = imagePicker.getTakeImageFile().getAbsolutePath();
                ImageItem imageItem = new ImageItem();
                imageItem.setPath(path);
                imagePicker.clearSelectedImages();
                imagePicker.addSelectedImageItem(0, imageItem, true);
                if (imagePicker.isCrop()) {
                    Intent intent = new Intent(ImagePickerActivity.this, ImageCropActivity.class);
                    startActivityForResult(intent, ImagePicker.REQUEST_CODE_CROP);
                } else {
                    Intent intent = new Intent();
                    intent.putExtra(ImagePicker.EXTRA_RESULT_ITEMS, imagePicker.getSelectedImages());
                    setResult(ImagePicker.RESULT_CODE_ITEMS, intent);
                    finish();
                }
            } else if (captureEnable) {
                finish();
            }
        }
    }
}