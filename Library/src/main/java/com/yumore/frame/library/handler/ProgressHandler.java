package com.yumore.frame.library.handler;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import com.yumore.frame.library.callback.UiProgressCallback;
import com.yumore.frame.library.entity.Progress;

import java.lang.ref.WeakReference;

/**
 * @author Nathaniel
 * @date 2018/3/29-17:45
 */

public abstract class ProgressHandler extends Handler {
    public static final int UPDATE = 0x01;
    public static final int START = 0x02;
    public static final int PAUSE = 0x03;
    public static final int FINISH = 0x04;

    private final WeakReference<UiProgressCallback> uiProgressCallbackWeakReference;

    public ProgressHandler(UiProgressCallback uiProgressCallback) {
        super(Looper.getMainLooper());
        uiProgressCallbackWeakReference = new WeakReference<>(uiProgressCallback);
    }

    protected abstract void onStart(UiProgressCallback uiProgressCallback, long currentBytes, long contentLength, boolean done);

    protected abstract void onProgress(UiProgressCallback uiProgressCallback, long currentBytes, long contentLength, boolean done);

    protected abstract void onPause(UiProgressCallback uiProgressCallback, long currentBytes, long contentLength, boolean done);

    protected abstract void onFinish(UiProgressCallback uiProgressCallback, long currentBytes, long contentLength, boolean done);

    @Override
    public void handleMessage(Message msg) {
        UiProgressCallback uiProgressCallback;
        switch (msg.what) {
            case UPDATE:
                uiProgressCallback = uiProgressCallbackWeakReference.get();
                if (null != uiProgressCallback) {
                    Progress progress = (Progress) msg.obj;
                    onProgress(uiProgressCallback, progress.getCurrentBytes(), progress.getContentLength(), progress.isDone());
                }
                break;

            case START:
                uiProgressCallback = uiProgressCallbackWeakReference.get();
                if (null != uiProgressCallback) {
                    Progress progress = (Progress) msg.obj;
                    onStart(uiProgressCallback, progress.getCurrentBytes(), progress.getContentLength(), progress.isDone());
                }
                break;

            case PAUSE:
                uiProgressCallback = uiProgressCallbackWeakReference.get();
                if (null != uiProgressCallback) {
                    Progress progress = (Progress) msg.obj;
                    onPause(uiProgressCallback, progress.getCurrentBytes(), progress.getContentLength(), progress.isDone());
                }
                break;

            case FINISH:
                uiProgressCallback = uiProgressCallbackWeakReference.get();
                if (null != uiProgressCallback) {
                    Progress progress = (Progress) msg.obj;
                    onFinish(uiProgressCallback, progress.getCurrentBytes(), progress.getContentLength(), progress.isDone());
                }
                break;

            default:
                super.handleMessage(msg);
                break;
        }

    }
}
