package com.yumore.common.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.VectorDrawable;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.ColorInt;
import androidx.annotation.DrawableRes;
import androidx.annotation.IntDef;
import androidx.annotation.NonNull;
import androidx.annotation.StringRes;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.content.ContextCompat;
import androidx.core.view.ViewCompat;
import androidx.core.view.ViewPropertyAnimatorListenerAdapter;

import com.google.android.material.behavior.SwipeDismissBehavior;
import com.yumore.common.R;
import com.yumore.common.manager.SnackBarManager;
import com.yumore.common.utility.CustomAnimationUtils;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * CustomSnackBar
 *
 * @author Nathaniel
 * nathanwriting@126.com
 * @version v1.0.0
 * @date 18-7-19 - 下午2:53
 */
public final class CustomSnackBar {
    public static final int LENGTH_INDEFINITE = -2;
    public static final int LENGTH_SHORT = -1;
    public static final int LENGTH_LONG = 0;
    private static final int ANIMATION_DURATION = 250;
    private static final int ANIMATION_FADE_DURATION = 180;
    private static final Handler HANDLER;
    private static final int MSG_SHOW = 0;
    private static final int MSG_DISMISS = 1;

    static {
        HANDLER = new Handler(Looper.getMainLooper(), new Handler.Callback() {
            @Override
            public boolean handleMessage(Message message) {
                switch (message.what) {
                    case MSG_SHOW:
                        ((CustomSnackBar) message.obj).showView();
                        return true;
                    case MSG_DISMISS:
                        ((CustomSnackBar) message.obj).hideView(message.arg1);
                        return true;

                    default:
                        break;
                }
                return false;
            }
        });
    }

    private final ViewGroup viewGroup;
    private final Context context;
    private final SnackBarLayout snackbarLayout;
    private final SnackBarManager.Callback mManagerCallback = new SnackBarManager.Callback() {
        @Override
        public void show() {
            HANDLER.sendMessage(HANDLER.obtainMessage(MSG_SHOW, CustomSnackBar.this));
        }

        @Override
        public void dismiss(int event) {
            HANDLER.sendMessage(HANDLER.obtainMessage(MSG_DISMISS, event, 0, CustomSnackBar.this));
        }
    };
    private int duration;
    private Callback callback;

    private CustomSnackBar(ViewGroup parent) {
        viewGroup = parent;
        context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        snackbarLayout = (SnackBarLayout) inflater.inflate(R.layout.snackbar_content_layout, viewGroup, false);
    }

    @NonNull
    public static CustomSnackBar make(@NonNull View view, @NonNull CharSequence text, @Duration int duration) {
        CustomSnackBar snackbar = new CustomSnackBar(findSuitableParent(view));
        snackbar.setText(text);
        snackbar.setDuration(duration);
        return snackbar;
    }

    @NonNull
    public static CustomSnackBar make(@NonNull View view, @StringRes int resId, @Duration int duration) {
        return make(view, view.getResources().getText(resId), duration);
    }

    private static ViewGroup findSuitableParent(View view) {
        ViewGroup fallback = null;
        do {
            if (view instanceof CoordinatorLayout) {
                return (ViewGroup) view;
            } else if (view instanceof FrameLayout) {
                if (view.getId() == android.R.id.content) {
                    return (ViewGroup) view;
                } else {

                    fallback = (ViewGroup) view;
                }
            }
            if (view != null) {

                final ViewParent parent = view.getParent();
                view = parent instanceof View ? (View) parent : null;
            }
        } while (view != null);
        return fallback;
    }

    private static float convertDpToPixel(float dp, Context context) {
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        return dp * ((float) metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private static Bitmap getBitmap(VectorDrawable vectorDrawable) {
        Bitmap bitmap = Bitmap.createBitmap(vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        vectorDrawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        vectorDrawable.draw(canvas);
        return bitmap;
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private static Bitmap getBitmap(Drawable drawable) {
        if (drawable instanceof BitmapDrawable) {
            return ((BitmapDrawable) drawable).getBitmap();
        } else if (drawable instanceof VectorDrawable) {
            return getBitmap((VectorDrawable) drawable);
        } else {
            throw new IllegalArgumentException("unsupported drawable CourseType");
        }
    }

    @Deprecated
    public CustomSnackBar addIcon(int resource_id, int size) {
        final TextView textView = snackbarLayout.getMessageView();
        textView.setCompoundDrawablesWithIntrinsicBounds(new BitmapDrawable(Bitmap.createScaledBitmap(((BitmapDrawable) (context.getResources()
                .getDrawable(resource_id))).getBitmap(), size, size, true)), null, null, null);
        return this;
    }

    public CustomSnackBar setIconPadding(int padding) {
        final TextView textView = snackbarLayout.getMessageView();
        textView.setCompoundDrawablePadding(padding);
        return this;
    }

    public CustomSnackBar setIconLeft(@DrawableRes int drawableRes, float sizeDp) {
        final TextView textView = snackbarLayout.getMessageView();
        Drawable drawable = ContextCompat.getDrawable(context, drawableRes);
        if (drawable != null) {
            drawable = fitDrawable(drawable, (int) convertDpToPixel(sizeDp, context));
        } else {
            throw new IllegalArgumentException("resource_id is not a valid drawable!");
        }
        final Drawable[] compoundDrawables = textView.getCompoundDrawables();
        textView.setCompoundDrawables(drawable, compoundDrawables[1], compoundDrawables[2], compoundDrawables[3]);
        return this;
    }

    public CustomSnackBar setIconRight(@DrawableRes int drawableRes, float sizeDp) {
        final TextView textView = snackbarLayout.getMessageView();
        Drawable drawable = ContextCompat.getDrawable(context, drawableRes);
        if (drawable != null) {
            drawable = fitDrawable(drawable, (int) convertDpToPixel(sizeDp, context));
        } else {
            throw new IllegalArgumentException("resource_id is not a valid drawable!");
        }
        final Drawable[] compoundDrawables = textView.getCompoundDrawables();
        textView.setCompoundDrawables(compoundDrawables[0], compoundDrawables[1], drawable, compoundDrawables[3]);
        return this;
    }

    /**
     * Overrides the max width of this snackbar's layout. This is typically not necessary; the snackbar
     * width will be according to Google's Material guidelines. Specifically, the max width will be
     * <p>
     * To allow the snackbar to have a width equal to the parent view, set a value <= 0.
     *
     * @param maxWidth the max width in pixels
     * @return this TSnackbar
     */
    public CustomSnackBar setMaxWidth(int maxWidth) {
        snackbarLayout.maxWidth = maxWidth;
        return this;
    }

    private Drawable fitDrawable(Drawable drawable, int sizePx) {
        if (drawable.getIntrinsicWidth() != sizePx || drawable.getIntrinsicHeight() != sizePx) {
            if (drawable instanceof BitmapDrawable) {
                drawable = new BitmapDrawable(context.getResources(), Bitmap.createScaledBitmap(getBitmap(drawable), sizePx, sizePx, true));
            }
        }
        drawable.setBounds(0, 0, sizePx, sizePx);

        return drawable;
    }

    @NonNull
    public CustomSnackBar setAction(@StringRes int resId, View.OnClickListener listener) {
        return setAction(context.getText(resId), listener);
    }

    @NonNull
    private CustomSnackBar setAction(@NonNull CharSequence text, final View.OnClickListener listener) {
        final TextView textView = snackbarLayout.getActionView();

        if (TextUtils.isEmpty(text) || listener == null) {
            textView.setVisibility(View.GONE);
            textView.setOnClickListener(null);
        } else {
            textView.setVisibility(View.VISIBLE);
            textView.setText(text);
            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onClick(view);

                    dispatchDismiss(Callback.DISMISS_EVENT_ACTION);
                }
            });
        }
        return this;
    }

    @NonNull
    public CustomSnackBar setActionTextColor(ColorStateList colors) {
        final TextView textView = snackbarLayout.getActionView();
        textView.setTextColor(colors);
        return this;
    }

    @NonNull
    public CustomSnackBar setActionTextColor(@ColorInt int color) {
        final TextView textView = snackbarLayout.getActionView();
        textView.setTextColor(color);
        return this;
    }

    @NonNull
    public CustomSnackBar setText(@NonNull CharSequence message) {
        final TextView textView = snackbarLayout.getMessageView();
        textView.setText(message);
        return this;
    }

    @NonNull
    public CustomSnackBar setText(@StringRes int resId) {
        return setText(context.getText(resId));
    }

    @Duration
    public int getDuration() {
        return duration;
    }

    @NonNull
    public CustomSnackBar setDuration(@Duration int duration) {
        this.duration = duration;
        return this;
    }

    @NonNull
    public View getView() {
        return snackbarLayout;
    }

    public void show() {
        SnackBarManager.getInstance().show(duration, mManagerCallback);
    }

    public void dismiss() {
        dispatchDismiss(Callback.DISMISS_EVENT_MANUAL);
    }

    private void dispatchDismiss(@Callback.DismissEvent int event) {
        SnackBarManager.getInstance().dismiss(mManagerCallback, event);
    }

    @NonNull
    public CustomSnackBar setCallback(Callback callback) {
        this.callback = callback;
        return this;
    }

    public boolean isShown() {
        return SnackBarManager.getInstance().isCurrent(mManagerCallback);
    }

    public boolean isShownOrQueued() {
        return SnackBarManager.getInstance().isCurrentOrNext(mManagerCallback);
    }

    final void showView() {
        if (snackbarLayout.getParent() == null) {
            final ViewGroup.LayoutParams lp = snackbarLayout.getLayoutParams();

            if (lp instanceof CoordinatorLayout.LayoutParams) {

                final Behavior behavior = new Behavior();
                behavior.setStartAlphaSwipeDistance(0.1f);
                behavior.setEndAlphaSwipeDistance(0.6f);
                behavior.setSwipeDirection(SwipeDismissBehavior.SWIPE_DIRECTION_START_TO_END);
                behavior.setListener(new SwipeDismissBehavior.OnDismissListener() {
                    @Override
                    public void onDismiss(View view) {
                        dispatchDismiss(Callback.DISMISS_EVENT_SWIPE);
                    }

                    @Override
                    public void onDragStateChanged(int state) {
                        switch (state) {
                            case SwipeDismissBehavior.STATE_DRAGGING:
                            case SwipeDismissBehavior.STATE_SETTLING:

                                SnackBarManager.getInstance().cancelTimeout(mManagerCallback);
                                break;
                            case SwipeDismissBehavior.STATE_IDLE:

                                SnackBarManager.getInstance().restoreTimeout(mManagerCallback);
                                break;

                            default:
                                break;
                        }
                    }
                });
                ((CoordinatorLayout.LayoutParams) lp).setBehavior(behavior);
            }
            viewGroup.addView(snackbarLayout);
        }

        snackbarLayout.setOnAttachStateChangeListener(new SnackBarLayout.OnAttachStateChangeListener() {
            @Override
            public void onViewAttachedToWindow(View v) {
            }

            @Override
            public void onViewDetachedFromWindow(View v) {
                if (isShownOrQueued()) {
                    HANDLER.post(new Runnable() {
                        @Override
                        public void run() {
                            onViewHidden(Callback.DISMISS_EVENT_MANUAL);
                        }
                    });
                }
            }
        });

        if (ViewCompat.isLaidOut(snackbarLayout)) {
            animateViewIn();
        } else {
            snackbarLayout.setOnLayoutChangeListener(new SnackBarLayout.OnLayoutChangeListener() {
                @Override
                public void onLayoutChange(View view, int left, int top, int right, int bottom) {
                    animateViewIn();
                    snackbarLayout.setOnLayoutChangeListener(null);
                }
            });
        }
    }

    private void animateViewIn() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            ViewCompat.setTranslationY(snackbarLayout, -snackbarLayout.getHeight());
            ViewCompat.animate(snackbarLayout)
                    .translationY(0f)
                    .setInterpolator(CustomAnimationUtils.FAST_OUT_SLOW_IN_INTERPOLATOR)
                    .setDuration(ANIMATION_DURATION)
                    .setListener(new ViewPropertyAnimatorListenerAdapter() {
                        @Override
                        public void onAnimationStart(View view) {
                            snackbarLayout.animateChildrenIn(ANIMATION_DURATION - ANIMATION_FADE_DURATION, ANIMATION_FADE_DURATION);
                        }

                        @Override
                        public void onAnimationEnd(View view) {
                            if (callback != null) {
                                callback.onShown(CustomSnackBar.this);
                            }
                            SnackBarManager.getInstance().onShown(mManagerCallback);
                        }
                    })
                    .start();
        } else {
            Animation anim = AnimationUtils.loadAnimation(snackbarLayout.getContext(), R.anim.slide_top_in);
            anim.setInterpolator(CustomAnimationUtils.FAST_OUT_SLOW_IN_INTERPOLATOR);
            anim.setDuration(ANIMATION_DURATION);
            anim.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationEnd(Animation animation) {
                    if (callback != null) {
                        callback.onShown(CustomSnackBar.this);
                    }
                    SnackBarManager.getInstance()
                            .onShown(mManagerCallback);
                }

                @Override
                public void onAnimationStart(Animation animation) {
                }

                @Override
                public void onAnimationRepeat(Animation animation) {
                }
            });
            snackbarLayout.startAnimation(anim);
        }
    }

    private void animateViewOut(final int event) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            ViewCompat.animate(snackbarLayout)
                    .translationY(-snackbarLayout.getHeight())
                    .setInterpolator(CustomAnimationUtils.FAST_OUT_SLOW_IN_INTERPOLATOR)
                    .setDuration(ANIMATION_DURATION)
                    .setListener(new ViewPropertyAnimatorListenerAdapter() {
                        @Override
                        public void onAnimationStart(View view) {
                            snackbarLayout.animateChildrenOut(0, ANIMATION_FADE_DURATION);
                        }

                        @Override
                        public void onAnimationEnd(View view) {
                            onViewHidden(event);
                        }
                    })
                    .start();
        } else {
            Animation animation = AnimationUtils.loadAnimation(snackbarLayout.getContext(), R.anim.slide_top_out);
            animation.setInterpolator(CustomAnimationUtils.FAST_OUT_SLOW_IN_INTERPOLATOR);
            animation.setDuration(ANIMATION_DURATION);
            animation.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationEnd(Animation animation) {
                    onViewHidden(event);
                }

                @Override
                public void onAnimationStart(Animation animation) {
                }

                @Override
                public void onAnimationRepeat(Animation animation) {
                }
            });
            snackbarLayout.startAnimation(animation);
        }
    }

    private void hideView(int event) {
        if (snackbarLayout.getVisibility() != View.VISIBLE || isBeingDragged()) {
            onViewHidden(event);
        } else {
            animateViewOut(event);
        }
    }

    private void onViewHidden(int event) {
        SnackBarManager.getInstance().onDismissed(mManagerCallback);
        if (callback != null) {
            callback.onDismissed(this, event);
        }
        final ViewParent parent = snackbarLayout.getParent();
        if (parent instanceof ViewGroup) {
            ((ViewGroup) parent).removeView(snackbarLayout);
        }
    }

    private boolean isBeingDragged() {
        final ViewGroup.LayoutParams layoutParams = snackbarLayout.getLayoutParams();
        if (layoutParams instanceof CoordinatorLayout.LayoutParams) {
            final CoordinatorLayout.LayoutParams cllp = (CoordinatorLayout.LayoutParams) layoutParams;
            final CoordinatorLayout.Behavior behavior = cllp.getBehavior();

            if (behavior instanceof SwipeDismissBehavior) {
                return ((SwipeDismissBehavior) behavior).getDragState() != SwipeDismissBehavior.STATE_IDLE;
            }
        }
        return false;
    }

    @IntDef({LENGTH_INDEFINITE, LENGTH_SHORT, LENGTH_LONG})
    @Retention(RetentionPolicy.SOURCE)
    public @interface Duration {
    }

    public static abstract class Callback {
        public static final int DISMISS_EVENT_SWIPE = 0;
        public static final int DISMISS_EVENT_ACTION = 1;
        public static final int DISMISS_EVENT_TIMEOUT = 2;
        public static final int DISMISS_EVENT_MANUAL = 3;
        public static final int DISMISS_EVENT_CONSECUTIVE = 4;

        public void onDismissed(CustomSnackBar snackbar, @DismissEvent int event) {

        }

        public void onShown(CustomSnackBar snackbar) {

        }


        @IntDef({
                DISMISS_EVENT_SWIPE, DISMISS_EVENT_ACTION, DISMISS_EVENT_TIMEOUT,
                DISMISS_EVENT_MANUAL, DISMISS_EVENT_CONSECUTIVE
        })
        @Retention(RetentionPolicy.SOURCE)
        public @interface DismissEvent {
        }
    }

    public static class SnackBarLayout extends LinearLayout {
        private TextView textView;
        private Button button;
        private int maxWidth;
        private final int maxInlineActionWidth;
        private OnLayoutChangeListener onLayoutChangeListener;
        private OnAttachStateChangeListener onAttachStateChangeListener;

        public SnackBarLayout(Context context) {
            this(context, null);
        }

        public SnackBarLayout(Context context, AttributeSet attrs) {
            super(context, attrs);
            TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.SnackbarLayout);
            maxWidth = typedArray.getDimensionPixelSize(R.styleable.SnackbarLayout_android_maxWidth, -1);
            maxInlineActionWidth = typedArray.getDimensionPixelSize(R.styleable.SnackbarLayout_maxActionInlineWidth, -1);
            if (typedArray.hasValue(R.styleable.SnackbarLayout_elevation)) {
                ViewCompat.setElevation(this, typedArray.getDimensionPixelSize(R.styleable.SnackbarLayout_elevation, 0));
            }
            typedArray.recycle();
            setClickable(true);
            LayoutInflater.from(context).inflate(R.layout.include_snackbar_layout, this);
            ViewCompat.setAccessibilityLiveRegion(this, ViewCompat.ACCESSIBILITY_LIVE_REGION_POLITE);
        }

        private static void updateTopBottomPadding(View view, int topPadding, int bottomPadding) {
            if (ViewCompat.isPaddingRelative(view)) {
                ViewCompat.setPaddingRelative(view, ViewCompat.getPaddingStart(view), topPadding, ViewCompat.getPaddingEnd(view), bottomPadding);
            } else {
                view.setPadding(view.getPaddingLeft(), topPadding, view.getPaddingRight(), bottomPadding);
            }
        }

        @Override
        protected void onFinishInflate() {
            super.onFinishInflate();
            textView = findViewById(R.id.snackbar_text);
            button = findViewById(R.id.snackbar_action);
        }

        TextView getMessageView() {
            return textView;
        }

        Button getActionView() {
            return button;
        }

        @Override
        protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
            if (maxWidth > 0 && getMeasuredWidth() > maxWidth) {
                widthMeasureSpec = MeasureSpec.makeMeasureSpec(maxWidth, MeasureSpec.EXACTLY);
                super.onMeasure(widthMeasureSpec, heightMeasureSpec);
            }
            final int multiLineVPadding = getResources().getDimensionPixelSize(
                    R.dimen.design_snackbar_padding_vertical_2lines);
            final int singleLineVPadding = getResources().getDimensionPixelSize(
                    R.dimen.design_snackbar_padding_vertical);
            final boolean isMultiLine = textView.getLayout().getLineCount() > 1;
            boolean remeasure = false;
            if (isMultiLine && maxInlineActionWidth > 0 && button.getMeasuredWidth() > maxInlineActionWidth) {
                if (updateViewsWithinLayout(VERTICAL, multiLineVPadding, multiLineVPadding - singleLineVPadding)) {
                    remeasure = true;
                }
            } else {
                final int messagePadding = isMultiLine ? multiLineVPadding : singleLineVPadding;
                if (updateViewsWithinLayout(HORIZONTAL, messagePadding, messagePadding)) {
                    remeasure = true;
                }
            }
            if (remeasure) {
                super.onMeasure(widthMeasureSpec, heightMeasureSpec);
            }
        }

        void animateChildrenIn(int delay, int duration) {
            ViewCompat.setAlpha(textView, 0f);
            ViewCompat.animate(textView)
                    .alpha(1f)
                    .setDuration(duration)
                    .setStartDelay(delay)
                    .start();

            if (button.getVisibility() == VISIBLE) {
                ViewCompat.setAlpha(button, 0f);
                ViewCompat.animate(button)
                        .alpha(1f)
                        .setDuration(duration)
                        .setStartDelay(delay)
                        .start();
            }
        }

        void animateChildrenOut(int delay, int duration) {
            ViewCompat.setAlpha(textView, 1f);
            ViewCompat.animate(textView)
                    .alpha(0f)
                    .setDuration(duration)
                    .setStartDelay(delay)
                    .start();

            if (button.getVisibility() == VISIBLE) {
                ViewCompat.setAlpha(button, 1f);
                ViewCompat.animate(button)
                        .alpha(0f)
                        .setDuration(duration)
                        .setStartDelay(delay)
                        .start();
            }
        }

        @Override
        protected void onLayout(boolean changed, int l, int t, int r, int b) {
            super.onLayout(changed, l, t, r, b);
            if (changed && onLayoutChangeListener != null) {
                onLayoutChangeListener.onLayoutChange(this, l, t, r, b);
            }
        }

        @Override
        protected void onAttachedToWindow() {
            super.onAttachedToWindow();
            if (onAttachStateChangeListener != null) {
                onAttachStateChangeListener.onViewAttachedToWindow(this);
            }
        }

        @Override
        protected void onDetachedFromWindow() {
            super.onDetachedFromWindow();
            if (onAttachStateChangeListener != null) {
                onAttachStateChangeListener.onViewDetachedFromWindow(this);
            }
        }

        void setOnLayoutChangeListener(OnLayoutChangeListener onLayoutChangeListener) {
            this.onLayoutChangeListener = onLayoutChangeListener;
        }

        void setOnAttachStateChangeListener(OnAttachStateChangeListener listener) {
            onAttachStateChangeListener = listener;
        }

        boolean updateViewsWithinLayout(final int orientation, final int messagePadTop, final int messagePadBottom) {
            boolean changed = false;
            if (orientation != getOrientation()) {
                setOrientation(orientation);
                changed = true;
            }
            if (textView.getPaddingTop() != messagePadTop || textView.getPaddingBottom() != messagePadBottom) {
                updateTopBottomPadding(textView, messagePadTop, messagePadBottom);
                changed = true;
            }
            return changed;
        }

        interface OnLayoutChangeListener {
            void onLayoutChange(View view, int left, int top, int right, int bottom);
        }

        interface OnAttachStateChangeListener {
            void onViewAttachedToWindow(View v);

            void onViewDetachedFromWindow(View v);
        }
    }

    private final class Behavior extends SwipeDismissBehavior<SnackBarLayout> {
        @Override
        public boolean canSwipeDismissView(@NonNull View child) {
            return child instanceof SnackBarLayout;
        }

        @Override
        public boolean onInterceptTouchEvent(CoordinatorLayout parent, SnackBarLayout child, MotionEvent event) {
            if (parent.isPointInChildBounds(child, (int) event.getX(), (int) event.getY())) {
                switch (event.getActionMasked()) {
                    case MotionEvent.ACTION_DOWN:
                        SnackBarManager.getInstance().cancelTimeout(mManagerCallback);
                        break;
                    case MotionEvent.ACTION_UP:
                    case MotionEvent.ACTION_CANCEL:
                        SnackBarManager.getInstance().restoreTimeout(mManagerCallback);
                        break;
                    default:
                        break;
                }
            }
            return super.onInterceptTouchEvent(parent, child, event);
        }
    }
}