package com.yumore.common.utility;

import android.app.Activity;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.yumore.common.R;
import com.yumore.gallery.loader.ImageLoader;

/**
 * @author Nathaniel
 * @date 18-8-4-下午12:26
 */
public class GlideImageLoader implements ImageLoader {

    @Override
    public void displayImage(Activity context, String path, ImageView imageView, int width, int height) {
        Glide.with(context)
                .load(path)
                .apply(getRequestOptions())
                .into(imageView);
    }

    @Override
    public void displayImagePreview(Activity context, String path, ImageView imageView, int width, int height) {
        Glide.with(context)
                .load(path)
                .apply(getRequestOptions())
                .into(imageView);
    }

    @Override
    public void clearMemoryCache() {

    }

    private RequestOptions getRequestOptions() {
        return RequestOptions.centerInsideTransform()
                .error(R.mipmap.icon_image_failed)
                .placeholder(R.mipmap.icon_image_holder)
                .diskCacheStrategy(DiskCacheStrategy.NONE);
    }
}
