package com.yumore.common.utility;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.yumore.common.R;

/**
 * @author Nathaniel
 * @date 2019/4/1 - 17:14
 */
public class ToastUtils {
    public static void showNormalToast(@NonNull Context context, @NonNull CharSequence message) {
        Toast toast = new Toast(context);
        View view = LayoutInflater.from(context).inflate(R.layout.common_toast_layout, null);
        TextView textView = view.findViewById(R.id.common_toast_message);
        textView.setText(message);
        toast.setView(view);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.BOTTOM, 0, 100);
        toast.show();
    }

    public static void showCenterToast(@NonNull Context context, @NonNull CharSequence message) {
        Toast toast = new Toast(context);
        View view = LayoutInflater.from(context).inflate(R.layout.common_toast_layout, null);
        TextView textView = view.findViewById(R.id.common_toast_message);
        textView.setText(message);
        toast.setView(view);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }
}
