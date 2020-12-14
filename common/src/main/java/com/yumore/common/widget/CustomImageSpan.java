package com.yumore.common.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.text.style.ImageSpan;

import androidx.annotation.NonNull;

import com.yumore.common.utility.LoggerUtils;

/**
 * @author Nathaniel
 * @date 2019/4/10 - 10:26
 */
public class CustomImageSpan extends ImageSpan {
    public static final int ALIGN_FONT_CENTER = 2;
    private int distW, distH;

    public CustomImageSpan(Context context, Bitmap bitmap) {
        super(context, bitmap);
    }

    public CustomImageSpan(Context context, Bitmap bitmap, int verticalAlignment) {
        super(context, bitmap, verticalAlignment);
    }

    public CustomImageSpan(Drawable drawable) {
        super(drawable);
    }

    public CustomImageSpan(Drawable drawable, int verticalAlignment) {
        super(drawable, verticalAlignment);
    }

    public CustomImageSpan(Drawable drawable, String source) {
        super(drawable, source);
    }

    public CustomImageSpan(Drawable drawable, String source, int verticalAlignment) {
        super(drawable, source, verticalAlignment);
    }

    public CustomImageSpan(Context context, Uri uri) {
        super(context, uri);
    }

    public CustomImageSpan(Context context, Uri uri, int verticalAlignment) {
        super(context, uri, verticalAlignment);
    }

    public CustomImageSpan(Context context, int resourceId) {
        super(context, resourceId);
    }

    public CustomImageSpan(Context context, int resourceId, int verticalAlignment) {
        super(context, resourceId, verticalAlignment);
    }

    public void setDistWAndH(int distW, int distH) {
        this.distW = distW;
        this.distH = distH;
    }

    @Override
    public int getSize(@NonNull Paint paint, CharSequence text, int start, int end, Paint.FontMetricsInt fontMetricsInt) {
        Drawable drawable = getDrawable();
        Rect rect = drawable.getBounds();
        if (fontMetricsInt != null) {
            Paint.FontMetricsInt metricsInt = paint.getFontMetricsInt();
            int fontHeight = metricsInt.bottom - metricsInt.top;
            int drHeight = rect.bottom - rect.top;

            int top = drHeight / 2 - fontHeight / 4;
            int bottom = drHeight / 2 + fontHeight / 4;

            fontMetricsInt.ascent = -bottom;
            fontMetricsInt.top = -bottom;
            fontMetricsInt.bottom = top;
            fontMetricsInt.descent = top;
        }
        return rect.right;
    }

    @Override
    public void draw(@NonNull Canvas canvas, CharSequence text, int start, int end, float x, int top, int y, int bottom, @NonNull Paint paint) {
        Drawable drawable = getDrawable();
        canvas.save();
        Paint.FontMetricsInt fontMetricsInt = paint.getFontMetricsInt();
        int transY = 0;
        if (mVerticalAlignment == ALIGN_BASELINE) {
            transY -= fontMetricsInt.descent;
        } else if (mVerticalAlignment == ALIGN_FONT_CENTER) {
            // transY = ((bottom - top) - drawable.getBounds().bottom) / 2 + top; // 不能垂直居中
            transY = ((y + fontMetricsInt.descent) + (y + fontMetricsInt.ascent)) / 2 - drawable.getBounds().bottom / 2;
        }
        canvas.translate(x, transY);
        if (distW > 0 && distH > 0) {
            int width = drawable.getIntrinsicWidth();
            int height = drawable.getIntrinsicHeight();
            Bitmap oldBitmap = drawableToBitmap(drawable);
            Matrix matrix = new Matrix();
            float scaleWidth = ((float) distW / width);
            LoggerUtils.logger("move", "width:" + width + ";;w:" + distW + ";;scaleWidth:" + scaleWidth);
            float scaleHeight = ((float) distH / height);
            matrix.postScale(scaleWidth, scaleHeight);
            Bitmap newBitmap = Bitmap.createBitmap(oldBitmap, 0, 0, width, height, matrix, true);
            newBitmap.setDensity(120);
            new BitmapDrawable(newBitmap).draw(canvas);
        } else {
            drawable.draw(canvas);
        }
        canvas.restore();
    }

    private Bitmap drawableToBitmap(Drawable drawable) {
        int width = drawable.getIntrinsicWidth();
        int height = drawable.getIntrinsicHeight();
        Bitmap.Config config = drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888 : Bitmap.Config.RGB_565;
        Bitmap bitmap = Bitmap.createBitmap(width, height, config);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, width, height);
        drawable.draw(canvas);
        return bitmap;
    }
}
