package com.yumore.common.utility;

import android.content.Context;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.Priority;
import com.bumptech.glide.Registry;
import com.bumptech.glide.integration.okhttp3.OkHttpUrlLoader;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.cache.DiskLruCacheFactory;
import com.bumptech.glide.load.engine.cache.LruResourceCache;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.module.AppGlideModule;
import com.bumptech.glide.request.RequestOptions;
import com.yumore.common.R;

import java.io.File;
import java.io.InputStream;

/**
 * @author Nathaniel
 * @date 2018/11/27-1:27
 */
// @GlideModule
public class CustomGlideModule extends AppGlideModule {

    @SuppressWarnings("ResultOfMethodCallIgnored")
    @Override
    public void applyOptions(@NonNull Context context, @NonNull GlideBuilder builder) {
        RequestOptions options = new RequestOptions()
                .centerCrop()
                .placeholder(R.mipmap.icon_image_holder)
                .error(R.mipmap.icon_image_failed)
                .priority(Priority.HIGH)
                .format(DecodeFormat.PREFER_ARGB_8888)
                .diskCacheStrategy(DiskCacheStrategy.ALL);
        int maxMemory = (int) Runtime.getRuntime().maxMemory();
        int memoryCacheSize = maxMemory / 8;
        //设置内存缓存大小
        File cacheDir = new File(FileUtils.getCachePath());
        if (!cacheDir.exists()) {
            cacheDir.mkdirs();
        }
        int diskCacheSize = 1024 * 1024 * 512;
        //设置磁盘缓存大小
        builder.setMemoryCache(new LruResourceCache(memoryCacheSize));
        builder.setDiskCache(new DiskLruCacheFactory(cacheDir.getPath(), "image", diskCacheSize));
        builder.setDefaultRequestOptions(options);
    }


    @Override
    public void registerComponents(@NonNull Context context, @NonNull Glide glide, @NonNull Registry registry) {
        registry.replace(GlideUrl.class, InputStream.class, new OkHttpUrlLoader.Factory());
    }
}
