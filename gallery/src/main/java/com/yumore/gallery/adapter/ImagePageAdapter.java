package com.yumore.gallery.adapter;

import android.app.Activity;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;
import com.github.chrisbanes.photoview.OnPhotoTapListener;
import com.github.chrisbanes.photoview.PhotoView;
import com.yumore.gallery.entity.ImageItem;
import com.yumore.gallery.helper.ImagePicker;
import com.yumore.gallery.utility.ScreenUtils;

import java.util.ArrayList;


/**
 * @author Nathaniel
 */
public class ImagePageAdapter extends PagerAdapter {

    public PhotoViewClickListener listener;
    private int screenWidth;
    private int screenHeight;
    private ImagePicker imagePicker;
    private ArrayList<ImageItem> images;
    private Activity mActivity;

    public ImagePageAdapter(Activity activity, ArrayList<ImageItem> images) {
        this.mActivity = activity;
        this.images = images;

        DisplayMetrics dm = ScreenUtils.getScreenPix(activity);
        screenWidth = dm.widthPixels;
        screenHeight = dm.heightPixels;
        imagePicker = ImagePicker.getInstance();
    }

    public void setData(ArrayList<ImageItem> images) {
        this.images = images;
    }

    public void setPhotoViewClickListener(PhotoViewClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        PhotoView photoView = new PhotoView(mActivity);
        ImageItem imageItem = images.get(position);
        imagePicker.getImageLoader().displayImagePreview(mActivity, imageItem.getPath(), photoView, screenWidth, screenHeight);
        photoView.setOnPhotoTapListener(new OnPhotoTapListener() {
            @Override
            public void onPhotoTap(ImageView view, float x, float y) {
                if (listener != null) {
                    listener.OnPhotoTapListener(view, x, y);
                }
            }
        });
        container.addView(photoView);
        return photoView;
    }

    @Override
    public int getCount() {
        return images.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }

    @Override
    public int getItemPosition(@NonNull Object object) {
        return POSITION_NONE;
    }

    public interface PhotoViewClickListener {
        void OnPhotoTapListener(View view, float v, float v1);
    }
}
