package com.yumore.gallery.helper;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build.VERSION;
import android.os.Build.VERSION_CODES;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.util.Log;
import com.yumore.gallery.BuildConfig;
import com.yumore.gallery.entity.ImageFolder;
import com.yumore.gallery.entity.ImageItem;
import com.yumore.gallery.loader.ImageLoader;
import com.yumore.gallery.utility.ScreenUtils;
import com.yumore.gallery.widget.CropImageView;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * @author Nathaniel
 */
public class ImagePicker {

    public static final String TAG = ImagePicker.class.getSimpleName();
    public static final int REQUEST_CODE_TAKE = 0x9101;
    public static final int REQUEST_CODE_CROP = 0x9102;
    public static final int REQUEST_CODE_PREVIEW = 0x9103;
    public static final int RESULT_CODE_ITEMS = 0x9104;
    public static final int RESULT_CODE_BACK = 0x9105;

    public static final String EXTRA_RESULT_ITEMS = "extraResultItems";
    public static final String EXTRA_SELECTED_IMAGE_POSITION = "selectedImagePosition";
    public static final String EXTRA_IMAGE_ITEMS = "extraImageItems";
    public static final String EXTRA_FROM_ITEMS = "extraFromItems";
    private static ImagePicker mInstance;
    public Bitmap cropBitmap;
    /**
     * 图片选择模式
     */
    private boolean multiMode = true;
    /**
     * 最大选择图片数量
     */
    private int selectLimit = 9;
    /**
     * 裁剪
     */
    private boolean crop = true;
    /**
     * 显示相机
     */
    private boolean showCamera = true;
    /**
     * 裁剪后的图片是否是矩形，否者跟随裁剪框的形状
     */
    private boolean isSaveRectangle = false;
    /**
     * 裁剪保存宽度
     */
    private int outPutX = 800;
    /**
     * 裁剪保存高度
     */
    private int outPutY = 800;
    /**
     * 焦点框的宽度
     */
    private int focusWidth = 280;
    /**
     * 焦点框的高度
     */
    private int focusHeight = 280;
    /**
     * 图片加载器
     */
    private ImageLoader imageLoader;
    /**
     * 裁剪框的形状
     */
    private CropImageView.Style style = CropImageView.Style.RECTANGLE;
    private File cropCacheFolder;
    private File takeImageFile;
    /**
     * 选中的图片集合
     */
    private ArrayList<ImageItem> imageItems = new ArrayList<>();
    /**
     * 所有的图片文件夹
     */
    private List<ImageFolder> imageFolderList;
    /**
     * 当前选中的文件夹位置 0表示所有图片
     */
    private int currentImageFolderPosition = 0;
    /**
     * 图片选中的监听回调
     */
    private List<OnImageSelectedListener> onImageSelectedListenerList;

    private ImagePicker() {
    }

    public static ImagePicker getInstance() {
        if (mInstance == null) {
            synchronized (ImagePicker.class) {
                if (mInstance == null) {
                    mInstance = new ImagePicker();
                }
            }
        }
        return mInstance;
    }

    /**
     * 根据系统时间、前缀、后缀产生一个文件
     */
    @SuppressWarnings("ResultOfMethodCallIgnored")
    public static File createFile(File folder, String prefix, String suffix) {
        if (!folder.exists() || !folder.isDirectory()) {
            folder.mkdirs();
        }
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.CHINA);
        String filename = prefix + dateFormat.format(new Date(System.currentTimeMillis())) + suffix;
        return new File(folder, filename);
    }

    /**
     * 扫描图片
     */
    public static void galleryAddPic(Context context, File file) {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        Uri contentUri = Uri.fromFile(file);
        mediaScanIntent.setData(contentUri);
        context.sendBroadcast(mediaScanIntent);
    }

    public boolean isMultiMode() {
        return multiMode;
    }

    public void setMultiMode(boolean multiMode) {
        this.multiMode = multiMode;
    }

    public int getSelectLimit() {
        return selectLimit;
    }

    public void setSelectLimit(int selectLimit) {
        this.selectLimit = selectLimit;
    }

    public boolean isCrop() {
        return crop;
    }

    public void setCrop(boolean crop) {
        this.crop = crop;
    }

    public boolean isShowCamera() {
        return showCamera;
    }

    public void setShowCamera(boolean showCamera) {
        this.showCamera = showCamera;
    }

    public boolean isSaveRectangle() {
        return isSaveRectangle;
    }

    public void setSaveRectangle(boolean isSaveRectangle) {
        this.isSaveRectangle = isSaveRectangle;
    }

    public int getOutPutX() {
        return outPutX;
    }

    public void setOutPutX(int outPutX) {
        this.outPutX = outPutX;
    }

    public int getOutPutY() {
        return outPutY;
    }

    public void setOutPutY(int outPutY) {
        this.outPutY = outPutY;
    }

    public int getFocusWidth() {
        return focusWidth;
    }

    public void setFocusWidth(int focusWidth) {
        this.focusWidth = focusWidth;
    }

    public int getFocusHeight() {
        return focusHeight;
    }

    public void setFocusHeight(int focusHeight) {
        this.focusHeight = focusHeight;
    }

    public File getTakeImageFile() {
        return takeImageFile;
    }

    public File getCropCacheFolder(Context context) {
        if (cropCacheFolder == null) {
            cropCacheFolder = new File(context.getCacheDir() + "/ImagePicker/cropTemp/");
        }
        return cropCacheFolder;
    }

    public void setCropCacheFolder(File cropCacheFolder) {
        this.cropCacheFolder = cropCacheFolder;
    }

    public ImageLoader getImageLoader() {
        return imageLoader;
    }

    public void setImageLoader(ImageLoader imageLoader) {
        this.imageLoader = imageLoader;
    }

    public CropImageView.Style getStyle() {
        return style;
    }

    public void setStyle(CropImageView.Style style) {
        this.style = style;
    }

    public List<ImageFolder> getImageFolders() {
        return imageFolderList;
    }

    public void setImageFolders(List<ImageFolder> imageFolders) {
        imageFolderList = imageFolders;
    }

    public int getCurrentImageFolderPosition() {
        return currentImageFolderPosition;
    }

    public void setCurrentImageFolderPosition(int mCurrentSelectedImageSetPosition) {
        currentImageFolderPosition = mCurrentSelectedImageSetPosition;
    }

    public ArrayList<ImageItem> getCurrentImageFolderItems() {
        return imageFolderList.get(currentImageFolderPosition).getImages();
    }

    public boolean isSelect(ImageItem item) {
        return imageItems.contains(item);
    }

    public int getSelectImageCount() {
        if (imageItems == null) {
            return 0;
        }
        return imageItems.size();
    }

    public ArrayList<ImageItem> getSelectedImages() {
        return imageItems;
    }

    public void setSelectedImages(ArrayList<ImageItem> selectedImages) {
        if (selectedImages == null) {
            return;
        }
        this.imageItems = selectedImages;
    }

    public void clearSelectedImages() {
        if (imageItems != null) {
            imageItems.clear();
        }
    }

    public void clear() {
        if (onImageSelectedListenerList != null) {
            onImageSelectedListenerList.clear();
            onImageSelectedListenerList = null;
        }
        if (imageFolderList != null) {
            imageFolderList.clear();
            imageFolderList = null;
        }
        if (imageItems != null) {
            imageItems.clear();
        }
        currentImageFolderPosition = 0;
    }

    /**
     * 拍照的方法
     * *默认情况下，即不需要指定intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
     * *照相机有自己默认的存储路径，拍摄的照片将返回一个缩略图。如果想访问原始图片，
     * *可以通过dat extra能够得到原始图片位置。即，如果指定了目标uri，data就没有数据，
     * *如果没有指定uri，则data就返回有数据！
     */
    public void takePicture(Activity activity, int requestCode) {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        takePictureIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        if (takePictureIntent.resolveActivity(activity.getPackageManager()) != null) {
            if (ScreenUtils.existSDCard()) {
                takeImageFile = new File(Environment.getExternalStorageDirectory(), "/DCIM/camera/");
            } else {
                takeImageFile = Environment.getDataDirectory();
            }
            takeImageFile = createFile(takeImageFile, "IMG_", ".png");
            Uri uri;
            if (VERSION.SDK_INT <= VERSION_CODES.M) {
                uri = Uri.fromFile(takeImageFile);
            } else {
                /*
                 * 7.0 调用系统相机拍照不再允许使用Uri方式，应该替换为FileProvider
                 * 并且这样可以解决MIUI系统上拍照返回size为0的情况
                 */
                Log.e("Nathaniel", BuildConfig.fileProviderName);
                uri = FileProvider.getUriForFile(activity, activity.getPackageName() + ".provider", takeImageFile);
                //加入uri权限 要不三星手机不能拍照
                List<ResolveInfo> resInfoList = activity.getPackageManager().queryIntentActivities(takePictureIntent, PackageManager.MATCH_DEFAULT_ONLY);
                for (ResolveInfo resolveInfo : resInfoList) {
                    String packageName = resolveInfo.activityInfo.packageName;
                    activity.grantUriPermission(packageName, uri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
                }
            }
            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        }
        activity.startActivityForResult(takePictureIntent, requestCode);
    }

    public void addOnImageSelectedListener(OnImageSelectedListener onImageSelectedListener) {
        if (onImageSelectedListenerList == null) {
            onImageSelectedListenerList = new ArrayList<>();
        }
        onImageSelectedListenerList.add(onImageSelectedListener);
    }

    public void removeOnImageSelectedListener(OnImageSelectedListener onImageSelectedListener) {
        if (onImageSelectedListenerList == null) {
            return;
        }
        onImageSelectedListenerList.remove(onImageSelectedListener);
    }

    public void addSelectedImageItem(int position, ImageItem item, boolean addEnable) {
        if (addEnable) {
            imageItems.add(item);
        } else {
            imageItems.remove(item);
        }
        notifyImageSelectedChanged(position, item, addEnable);
    }

    private void notifyImageSelectedChanged(int position, ImageItem item, boolean addEnable) {
        if (onImageSelectedListenerList == null) {
            return;
        }
        for (OnImageSelectedListener l : onImageSelectedListenerList) {
            l.onImageSelected(position, item, addEnable);
        }
    }

    /**
     * 用于手机内存不足，进程被系统回收，重启时的状态恢复
     */
    public void restoreInstanceState(Bundle savedInstanceState) {
        cropCacheFolder = (File) savedInstanceState.getSerializable("cropCacheFolder");
        takeImageFile = (File) savedInstanceState.getSerializable("takeImageFile");
        imageLoader = (ImageLoader) savedInstanceState.getSerializable("imageLoader");
        style = (CropImageView.Style) savedInstanceState.getSerializable("style");
        multiMode = savedInstanceState.getBoolean("multiMode");
        crop = savedInstanceState.getBoolean("crop");
        showCamera = savedInstanceState.getBoolean("showCamera");
        isSaveRectangle = savedInstanceState.getBoolean("isSaveRectangle");
        selectLimit = savedInstanceState.getInt("selectLimit");
        outPutX = savedInstanceState.getInt("outPutX");
        outPutY = savedInstanceState.getInt("outPutY");
        focusWidth = savedInstanceState.getInt("focusWidth");
        focusHeight = savedInstanceState.getInt("focusHeight");
    }

    /**
     * 用于手机内存不足，进程被系统回收时的状态保存
     */
    public void saveInstanceState(Bundle outState) {
        outState.putSerializable("cropCacheFolder", cropCacheFolder);
        outState.putSerializable("takeImageFile", takeImageFile);
        outState.putSerializable("imageLoader", imageLoader);
        outState.putSerializable("style", style);
        outState.putBoolean("multiMode", multiMode);
        outState.putBoolean("crop", crop);
        outState.putBoolean("showCamera", showCamera);
        outState.putBoolean("isSaveRectangle", isSaveRectangle);
        outState.putInt("selectLimit", selectLimit);
        outState.putInt("outPutX", outPutX);
        outState.putInt("outPutY", outPutY);
        outState.putInt("focusWidth", focusWidth);
        outState.putInt("focusHeight", focusHeight);
    }

    /**
     * 图片选中的监听
     */
    public interface OnImageSelectedListener {
        void onImageSelected(int position, ImageItem item, boolean isAdd);
    }
}