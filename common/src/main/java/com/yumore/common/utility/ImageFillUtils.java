package com.yumore.common.utility;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.text.TextUtils;
import android.view.ViewGroup;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.request.transition.Transition;
import com.yumore.common.R;
import com.yumore.common.callback.OnImageCompressListener;
import com.yumore.common.callback.OnMultiImageCompressListener;
import top.zibin.luban.CompressionPredicate;
import top.zibin.luban.Luban;
import top.zibin.luban.OnCompressListener;

import java.io.File;
import java.io.IOException;


/**
 * File sourceFile = Glide.with(context).asFile().load(url).submit().get();
 *
 * @author Nathaniel
 * @date 18-7-17-上午10:49
 */
public final class ImageFillUtils {
    private static final String TAG = ImageFillUtils.class.getSimpleName();
    private static final int CROSS_FADE_TIME = 200;

    public static void displayImage(final Context context, String imageUrl, final ImageView imageView) {
        Glide.with(context)
                .load(imageUrl)
                .apply(getRequestOptions(context, ImageType.DEFAULT, imageView))
                .transition(DrawableTransitionOptions.withCrossFade(CROSS_FADE_TIME))
                .into(imageView);
    }

    public static void displayImage(final Context context, int resourceId, final ImageView imageView) {
        Glide.with(context)
                .load(resourceId)
                .apply(getRequestOptions(context, ImageType.DEFAULT, imageView))
                .transition(DrawableTransitionOptions.withCrossFade(CROSS_FADE_TIME))
                .into(imageView);
    }

    public static void displayImage(final Context context, String imageUrl, final ImageView imageView, ImageType imageType) {
        Glide.with(context)
                .load(imageUrl)
                .apply(getRequestOptions(context, imageType, imageView))
                .transition(DrawableTransitionOptions.withCrossFade(CROSS_FADE_TIME))
                .into(imageView);
    }


    public static void displayImage(final Context context, int resourceId, final ImageView imageView, ImageType imageType) {
        Glide.with(context)
                .load(resourceId)
                .apply(getRequestOptions(context, imageType, imageView))
                .transition(DrawableTransitionOptions.withCrossFade(CROSS_FADE_TIME))
                .into(imageView);
    }

    @SuppressWarnings("unchecked")
    public static void displayImage(final Context context, String imageUrl, final ImageView imageView, ImageFrom imageFrom) {
        SimpleTarget<Bitmap> simpleTarget = new SimpleTarget<Bitmap>(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL) {

            @Override
            public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                int imageWidth = resource.getWidth();
                int imageHeight = resource.getHeight();
                int height = imageView.getWidth() * imageHeight / imageWidth;
                ViewGroup.LayoutParams layoutParams = imageView.getLayoutParams();
                layoutParams.height = height;
                layoutParams.width = imageView.getWidth();
                imageView.setImageBitmap(resource);
            }
        };
        RequestManager requestManager = Glide.with(context);
        RequestBuilder requestBuilder;
        RequestOptions requestOptions = getRequestOptions(context, ImageType.DEFAULT, imageView);
        if (imageFrom == ImageFrom.BITMAP) {
            requestBuilder = requestManager.asBitmap();
        } else if (imageFrom == ImageFrom.GIF) {
            requestBuilder = requestManager.asGif();
        } else if (imageFrom == ImageFrom.DRAWABLE) {
            requestBuilder = requestManager.asDrawable();
        } else if (imageFrom == ImageFrom.FILE) {
            requestBuilder = requestManager.asFile();
        } else {
            requestBuilder = requestManager.asDrawable();
        }
        requestBuilder.load(imageUrl)
                .apply(requestOptions)
                .transition(DrawableTransitionOptions.withCrossFade(300))
                .into(simpleTarget);
    }

    @SuppressLint("CheckResult")
    @NonNull
    private static RequestOptions getRequestOptions(Context context, ImageType imageType, ImageView imageView) {
        RequestOptions requestOptions = new RequestOptions()
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC);
        if (imageType == ImageType.ROUNDED) {
            requestOptions.transform(new GlideRoundTransform(context, context.getResources().getDimension(R.dimen.common_radius_normal)));
        } else if (imageType == ImageType.CIRCLE) {
            requestOptions.transform(new GlideCircleTransform());
        } else if (imageType == ImageType.DEFAULT) {
            requestOptions.placeholder(R.mipmap.icon_image_holder)
                    .error(R.mipmap.icon_image_failed);
        } else {
            requestOptions.placeholder(R.mipmap.icon_image_holder)
                    .error(R.mipmap.icon_image_failed);
        }
        if (!requestOptions.isTransformationSet() && requestOptions.isTransformationAllowed() && imageView.getScaleType() != null) {
            switch (imageView.getScaleType()) {
                case CENTER_CROP:
                    requestOptions = requestOptions.clone().optionalCenterCrop();
                    break;
                case CENTER_INSIDE:
                    requestOptions = requestOptions.clone().optionalCenterInside();
                    break;
                case FIT_CENTER:
                case FIT_START:
                case FIT_END:
                    requestOptions = requestOptions.clone().optionalFitCenter();
                    break;
                case FIT_XY:
                    requestOptions = requestOptions.clone().optionalCenterInside();
                    break;
                case CENTER:
                case MATRIX:
                default:
                    // Do nothing.
                    break;
            }
        }
        return requestOptions;
    }

    public static void compressImage(Context context, String originPath, final OnImageCompressListener onImageCompressListener) {
      LoggerUtils.e(TAG, "origin file size is " + new File(originPath).length());
      LoggerUtils.e(TAG, "origin file path is " + originPath);
        Luban.with(context)
                .load(originPath)
                .setTargetDir(FileUtils.getCachePath())
                .setFocusAlpha(true)
                .ignoreBy(100)
                .filter(new CompressionPredicate() {
                    @Override
                    public boolean apply(String path) {
                        return !(TextUtils.isEmpty(path) || path.toLowerCase().endsWith(".gif"));
                    }
                })
                .setCompressListener(new OnCompressListener() {
                    @Override
                    public void onStart() {
                        if (!EmptyUtils.isObjectEmpty(onImageCompressListener)) {
                            onImageCompressListener.onPrepared();
                        }
                    }

                    @SuppressWarnings("ResultOfMethodCallIgnored")
                    @Override
                    public void onSuccess(File file) {
                      LoggerUtils.e(TAG, "compressed file name = " + file.getName());
                      LoggerUtils.e(TAG, "compressed file path = " + file.getAbsolutePath());
                      LoggerUtils.e(TAG, "compressed file size = " + file.length());
                        File newFile;
                        if (file.getName().toLowerCase().contains(".jpeg")) {
                            newFile = new File(file.getParent(), file.getName().toLowerCase().replace(".jpeg", ".jpg"));
                          LoggerUtils.e(TAG, "new file path = " + newFile.getAbsolutePath());
                            if (!newFile.exists()) {
                                try {
                                    file.createNewFile();
                                    boolean flag = file.renameTo(newFile);
                                    if (!flag) {
                                      LoggerUtils.e(TAG, "rename *.jpeg to *.jpg failed");
                                    }
                                    file.delete();
                                    if (!EmptyUtils.isObjectEmpty(onImageCompressListener)) {
                                        onImageCompressListener.onSuccess(newFile);
                                    }
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        } else {
                            if (!EmptyUtils.isObjectEmpty(onImageCompressListener)) {
                                onImageCompressListener.onSuccess(file);
                            }
                        }
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        if (!EmptyUtils.isObjectEmpty(onImageCompressListener)) {
                            onImageCompressListener.onFailure(throwable);
                        }
                    }
                })
                .launch();
    }

    public static void compressImage(Context context, String originPath, final int position, final OnMultiImageCompressListener onMultiImageCompressListener) {
        Luban.with(context)
                .load(originPath)
                .setFocusAlpha(true)
                .ignoreBy(100)
                .setTargetDir(FileUtils.getCachePath())
                .filter(new CompressionPredicate() {
                    @Override
                    public boolean apply(String path) {
                        return !(TextUtils.isEmpty(path) || path.toLowerCase().endsWith(".gif"));
                    }
                })
                .setCompressListener(new OnCompressListener() {
                    @Override
                    public void onStart() {
                        if (!EmptyUtils.isObjectEmpty(onMultiImageCompressListener)) {
                            onMultiImageCompressListener.onPrepared();
                        }
                    }

                    @SuppressWarnings("ResultOfMethodCallIgnored")
                    @Override
                    public void onSuccess(File file) {
                      LoggerUtils.e(TAG, "compressed file name = " + file.getName());
                      LoggerUtils.e(TAG, "compressed file path = " + file.getAbsolutePath());
                      LoggerUtils.e(TAG, "compressed file size = " + file.length());
                        File newFile;
                        if (file.getName().toLowerCase().contains(".jpeg")) {
                            newFile = new File(file.getParent(), file.getName().toLowerCase().replace(".jpeg", ".jpg"));
                          LoggerUtils.e(TAG, "new file path = " + newFile.getAbsolutePath());
                            if (!newFile.exists()) {
                                try {
                                    file.createNewFile();
                                    boolean flag = file.renameTo(newFile);
                                    if (!flag) {
                                        LoggerUtils.e(TAG, "rename *.jpeg to *.jpg failed");
                                    }
                                    file.delete();
                                    if (!EmptyUtils.isObjectEmpty(onMultiImageCompressListener)) {
                                        onMultiImageCompressListener.onSuccess(position, newFile);
                                    }
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        } else {
                            if (!EmptyUtils.isObjectEmpty(onMultiImageCompressListener)) {
                                onMultiImageCompressListener.onSuccess(position, file);
                            }
                        }
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        if (!EmptyUtils.isObjectEmpty(onMultiImageCompressListener)) {
                            onMultiImageCompressListener.onFailure(throwable);
                        }
                    }
                })
                .launch();
    }

    public enum ImageFrom {
        /**
         * 图片加载类型
         */
        DEFAULT(0),
        BITMAP(1),
        GIF(2),
        DRAWABLE(3),
        FILE(4);

        private int value;

        ImageFrom(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }

    public enum ImageType {
        /**
         * 图片展示类型
         */
        DEFAULT(0),
        ROUNDED(1),
        CIRCLE(2);

        private int value;

        ImageType(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }

    public enum ScaleType {
        /**
         * 缩放类型
         */
        CENTER_CROP(0),
        CENTER_INSIDE(1),
        FIT_CENTER(2),
        FIT_START(3),
        FIT_END(4),
        FIT_XY(5),
        CENTER(6),
        MATRIX(7);
        private int value;

        ScaleType(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }
}
