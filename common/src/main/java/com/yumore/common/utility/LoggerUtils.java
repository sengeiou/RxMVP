package com.yumore.common.utility;

import android.util.Log;

/**
 * @author Nathaniel
 * @date 2018/5/29-10:17
 */
public class LoggerUtils {

    public static void e(String tag, String message) {
        int strLength = message.length();
        int start = 0;
        int maxLength = 2000;
        int end = maxLength;
        int time = (message.length() + maxLength) / maxLength;
        for (int i = 0; i < time; i++) {
            if (strLength > end) {
                Log.e(tag + i, message.substring(start, end));
                start = end;
                end = end + maxLength;
            } else {
                Log.e(tag, message.substring(start, strLength));
                break;
            }
        }
    }
}
