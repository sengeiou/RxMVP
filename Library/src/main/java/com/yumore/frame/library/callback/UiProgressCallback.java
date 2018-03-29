package com.yumore.frame.library.callback;

import android.os.Handler;
import android.os.Message;

import com.yumore.frame.library.entity.Progress;
import com.yumore.frame.library.handler.ProgressHandler;
import com.yumore.frame.library.listener.OnProgressListener;

/**
 * @author Nathaniel
 * @date 2018/3/29-17:43
 */

public abstract class UiProgressCallback implements OnProgressListener {
    private final Handler mainHandler = new UiHandler(this);
    private boolean first = false;

    @Override
    public void onProgress(long bytesWrite, long contentLength, boolean done) {
        if (!first) {
            first = true;
            Message start = Message.obtain();
            start.obj = new Progress(bytesWrite, contentLength, done);
            start.what = ProgressHandler.START;
            mainHandler.sendMessage(start);
        }

        Message message = Message.obtain();
        message.obj = new Progress(bytesWrite, contentLength, done);
        message.what = ProgressHandler.UPDATE;
        mainHandler.sendMessage(message);

        if (done) {
            Message finish = Message.obtain();
            finish.obj = new Progress(bytesWrite, contentLength, done);
            finish.what = ProgressHandler.FINISH;
            mainHandler.sendMessage(finish);
        }
    }

    protected abstract void onUiProgress(long currentBytes, long contentLength, boolean done);

    protected abstract void onUiStart(long currentBytes, long contentLength, boolean done);

    protected abstract void onUiPause(long currentBytes, long contentLength, boolean done);

    protected abstract void onUiFinish(long currentBytes, long contentLength, boolean done);

    private static class UiHandler extends ProgressHandler {
        public UiHandler(UiProgressCallback uiProgressListener) {
            super(uiProgressListener);
        }

        @Override
        public void onStart(UiProgressCallback uiProgressCallback, long currentBytes, long contentLength, boolean done) {
            if (null != uiProgressCallback) {
                uiProgressCallback.onUiStart(currentBytes, contentLength, done);
            }
        }

        @Override
        public void onProgress(UiProgressCallback uiProgressCallback, long currentBytes, long contentLength, boolean done) {
            if (null != uiProgressCallback) {
                uiProgressCallback.onUiProgress(currentBytes, contentLength, done);
            }
        }

        @Override
        protected void onPause(UiProgressCallback uiProgressCallback, long currentBytes, long contentLength, boolean done) {
            if (null != uiProgressCallback) {
                uiProgressCallback.onUiPause(currentBytes, contentLength, done);
            }
        }

        @Override
        public void onFinish(UiProgressCallback uiProgressCallback, long currentBytes, long contentLength, boolean done) {
            if (null != uiProgressCallback) {
                uiProgressCallback.onUiFinish(currentBytes, contentLength, done);
            }
        }
    }
}
