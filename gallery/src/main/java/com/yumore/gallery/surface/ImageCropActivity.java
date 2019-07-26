package com.yumore.gallery.surface;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.yumore.gallery.R;
import com.yumore.gallery.entity.ImageItem;
import com.yumore.gallery.helper.ImagePicker;
import com.yumore.gallery.utility.BitmapUtils;
import com.yumore.gallery.widget.CropImageView;

import java.io.File;
import java.util.ArrayList;

/**
 * @author Nathaniel
 */
public class ImageCropActivity extends BaseImageActivity implements View.OnClickListener, CropImageView.OnBitmapSaveCompleteListener {

    private CropImageView cropImageView;
    private Bitmap bitmap;
    private boolean saveRectangle;
    private int outputX;
    private int outputY;
    private ArrayList<ImageItem> imageItems;
    private ImagePicker imagePicker;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_crop);

        imagePicker = ImagePicker.getInstance();

        //初始化View
        findViewById(R.id.btn_back).setOnClickListener(this);
        Button certainly = findViewById(R.id.btn_ok);
        certainly.setText(getString(R.string.ip_complete));
        certainly.setOnClickListener(this);
        TextView tvDes = findViewById(R.id.tv_des);
        tvDes.setText(getString(R.string.ip_photo_crop));
        cropImageView = findViewById(R.id.cv_crop_image);
        cropImageView.setOnBitmapSaveCompleteListener(this);

        //获取需要的参数
        outputX = imagePicker.getOutPutX();
        outputY = imagePicker.getOutPutY();
        saveRectangle = imagePicker.isSaveRectangle();
        imageItems = imagePicker.getSelectedImages();
        String imagePath = imageItems.get(0).getPath();

        cropImageView.setFocusStyle(imagePicker.getStyle());
        cropImageView.setFocusWidth(imagePicker.getFocusWidth());
        cropImageView.setFocusHeight(imagePicker.getFocusHeight());

        //缩放图片
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(imagePath, options);
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        options.inSampleSize = calculateInSampleSize(options, displayMetrics.widthPixels, displayMetrics.heightPixels);
        options.inJustDecodeBounds = false;
        bitmap = BitmapFactory.decodeFile(imagePath, options);
        //设置默认旋转角度
        cropImageView.setImageBitmap(cropImageView.rotate(bitmap, BitmapUtils.getBitmapDegree(imagePath)));
    }

    public int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        int width = options.outWidth;
        int height = options.outHeight;
        int inSampleSize = 1;
        if (height > reqHeight || width > reqWidth) {
            if (width > height) {
                inSampleSize = width / reqWidth;
            } else {
                inSampleSize = height / reqHeight;
            }
        }
        return inSampleSize;
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.btn_back) {
            setResult(RESULT_CANCELED);
            finish();
        } else if (id == R.id.btn_ok) {
            cropImageView.saveBitmapToFile(imagePicker.getCropCacheFolder(this), outputX, outputY, saveRectangle);
        }
    }

    @Override
    public void onBitmapSaveSuccess(File file) {
        //裁剪后替换掉返回数据的内容，但是不要改变全局中的选中数据
        imageItems.remove(0);
        ImageItem imageItem = new ImageItem();
        imageItem.setPath(file.getAbsolutePath());
        imageItems.add(imageItem);
        Intent intent = new Intent();
        intent.putExtra(ImagePicker.EXTRA_RESULT_ITEMS, imageItems);
        //单选不需要裁剪，返回数据
        setResult(ImagePicker.RESULT_CODE_ITEMS, intent);
        finish();
    }

    @Override
    public void onBitmapSaveError(File file) {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        cropImageView.setOnBitmapSaveCompleteListener(null);
        if (null != bitmap && !bitmap.isRecycled()) {
            bitmap.recycle();
            bitmap = null;
        }
    }
}
