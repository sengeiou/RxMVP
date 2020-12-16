package com.yumore.common.manager;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import com.yumore.common.widget.CustomSnackBar;

import java.lang.ref.WeakReference;

/**
 * Manages {@link CustomSnackBar}s.
 */
public class SnackBarManager {

    private static final int MSG_TIMEOUT = 0;

    private static final int SHORT_DURATION_MS = 1500;
    private static final int LONG_DURATION_MS = 2750;

    private static SnackBarManager snackBarManager;
    private final Object object;
    private final Handler handler;
    private SnackBarRecord currentSnackBar;
    private SnackBarRecord nextSnackBar;

    private SnackBarManager() {
        object = new Object();
        handler = new Handler(Looper.getMainLooper(), new Handler.Callback() {
            @Override
            public boolean handleMessage(Message message) {
                switch (message.what) {
                    case MSG_TIMEOUT:
                        handleTimeout((SnackBarRecord) message.obj);
                        return true;

                    default:
                        break;
                }
                return false;
            }
        });
    }

    public static SnackBarManager getInstance() {
        if (snackBarManager == null) {
            snackBarManager = new SnackBarManager();
        }
        return snackBarManager;
    }

    public void show(int duration, Callback callback) {
        synchronized (object) {
            if (isCurrentSnackBar(callback)) {
                // Means that the callback is already in the queue. We'll just update the duration
                currentSnackBar.duration = duration;
                // If this is the TSnackbar currently being shown, call re-schedule it's
                // timeout
                handler.removeCallbacksAndMessages(currentSnackBar);
                scheduleTimeoutLocked(currentSnackBar);
                return;
            } else if (isNextSnackBar(callback)) {
                // We'll just update the duration
                nextSnackBar.duration = duration;
            } else {
                // Else, we need to create a new record and queue it
                nextSnackBar = new SnackBarRecord(duration, callback);
            }

            if (currentSnackBar != null && cancelSnackBarLocked(currentSnackBar, CustomSnackBar.Callback.DISMISS_EVENT_CONSECUTIVE)) {
                // If we currently have a TSnackbar, try and cancel it and wait in line
                return;
            } else {
                // Clear out the current snackbar
                currentSnackBar = null;
                // Otherwise, just show it now
                showNextSnackBarLocked();
            }
        }
    }

    public void dismiss(Callback callback, int event) {
        synchronized (object) {
            if (isCurrentSnackBar(callback)) {
                cancelSnackBarLocked(currentSnackBar, event);
            } else if (isNextSnackBar(callback)) {
                cancelSnackBarLocked(nextSnackBar, event);
            }
        }
    }

    /**
     * Should be called when a TSnackbar is no longer displayed. This is after any exit
     * animation has finished.
     */
    public void onDismissed(Callback callback) {
        synchronized (object) {
            if (isCurrentSnackBar(callback)) {
                // If the callback is from a TSnackbar currently show, remove it and show a new one
                currentSnackBar = null;
                if (nextSnackBar != null) {
                    showNextSnackBarLocked();
                }
            }
        }
    }

    /**
     * Should be called when a TSnackbar is being shown. This is after any entrance animation has
     * finished.
     */
    public void onShown(Callback callback) {
        synchronized (object) {
            if (isCurrentSnackBar(callback)) {
                scheduleTimeoutLocked(currentSnackBar);
            }
        }
    }

    public void cancelTimeout(Callback callback) {
        synchronized (object) {
            if (isCurrentSnackBar(callback)) {
                handler.removeCallbacksAndMessages(currentSnackBar);
            }
        }
    }

    public void restoreTimeout(Callback callback) {
        synchronized (object) {
            if (isCurrentSnackBar(callback)) {
                scheduleTimeoutLocked(currentSnackBar);
            }
        }
    }

    public boolean isCurrent(Callback callback) {
        synchronized (object) {
            return isCurrentSnackBar(callback);
        }
    }

    public boolean isCurrentOrNext(Callback callback) {
        synchronized (object) {
            return isCurrentSnackBar(callback) || isNextSnackBar(callback);
        }
    }

    private void showNextSnackBarLocked() {
        if (nextSnackBar != null) {
            currentSnackBar = nextSnackBar;
            nextSnackBar = null;

            final Callback callback = currentSnackBar.callback.get();
            if (callback != null) {
                callback.show();
            } else {
                // The callback doesn't exist any more, clear out the TSnackbar
                currentSnackBar = null;
            }
        }
    }

    private boolean cancelSnackBarLocked(SnackBarRecord record, int event) {
        final Callback callback = record.callback.get();
        if (callback != null) {
            callback.dismiss(event);
            return true;
        }
        return false;
    }

    private boolean isCurrentSnackBar(Callback callback) {
        return currentSnackBar != null && currentSnackBar.isSnackBar(callback);
    }

    private boolean isNextSnackBar(Callback callback) {
        return nextSnackBar != null && nextSnackBar.isSnackBar(callback);
    }

    private void scheduleTimeoutLocked(SnackBarRecord snackBarRecord) {
        if (snackBarRecord.duration == CustomSnackBar.LENGTH_INDEFINITE) {
            // If we're set to indefinite, we don't want to set a timeout
            return;
        }

        int durationMs = LONG_DURATION_MS;
        if (snackBarRecord.duration > 0) {
            durationMs = snackBarRecord.duration;
        } else if (snackBarRecord.duration == CustomSnackBar.LENGTH_SHORT) {
            durationMs = SHORT_DURATION_MS;
        }
        handler.removeCallbacksAndMessages(snackBarRecord);
        handler.sendMessageDelayed(Message.obtain(handler, MSG_TIMEOUT, snackBarRecord), durationMs);
    }

    private void handleTimeout(SnackBarRecord record) {
        synchronized (object) {
            if (currentSnackBar == record || nextSnackBar == record) {
                cancelSnackBarLocked(record, CustomSnackBar.Callback.DISMISS_EVENT_TIMEOUT);
            }
        }
    }

    public interface Callback {
        void show();

        void dismiss(int event);
    }

    private class SnackBarRecord {
        private final WeakReference<Callback> callback;
        private int duration;

        SnackBarRecord(int duration, Callback callback) {
            this.callback = new WeakReference<>(callback);
            this.duration = duration;
        }

        boolean isSnackBar(Callback callback) {
            return callback != null && this.callback.get() == callback;
        }
    }

}
