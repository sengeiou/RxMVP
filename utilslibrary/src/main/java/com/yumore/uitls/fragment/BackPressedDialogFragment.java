package com.yumore.uitls.fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import com.yumore.utilslibrary.R;


/**
 * 预览页面返回对话框
 */
public class BackPressedDialogFragment extends DialogFragment {

    public static final String MESSAGE = "message";

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        final Fragment parent = getParentFragment();
        Bundle bundle = getArguments();
        int resId = -1;
        if (bundle != null) {
            resId = bundle.getInt(MESSAGE, -1);
        }
        return new AlertDialog.Builder(getActivity())
                .setMessage(resId == -1 ? R.string.back_pressed_message : resId)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Activity activity = parent.getActivity();
                        if (activity != null) {
                            activity.finish();
                        }
                    }
                })
                .setNegativeButton(android.R.string.cancel, null)
                .create();
    }
}
