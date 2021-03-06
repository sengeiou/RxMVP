package com.yumore.camera.widget;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Outline;
import android.graphics.Rect;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewOutlineProvider;
import android.widget.ImageView;

import androidx.annotation.RequiresApi;
import androidx.core.view.GestureDetectorCompat;

import com.yumore.camera.R;
import com.yumore.camera.engine.render.PreviewRenderer;
import com.yumore.filter.glfilter.stickers.StaticStickerNormalFilter;
import com.yumore.uitls.utils.DensityUtils;

/**
 * 预览问题
 *
 * @author CainHuang
 * @date 2019/7/6
 */
public class CainTextureView extends TextureView {

    private static final String TAG = "CainTextureView";
    private static final boolean VERBOSE = false;
    //测试贴纸的移动
    StaticStickerNormalFilter dragSticker = null;
    // 单击时点击的位置
    private float mTouchX = 0;
    private float mTouchY = 0;
    // 对焦动画
    private ValueAnimator mFocusAnimator;
    // 对焦图片
    private ImageView mFocusImageView;
    // 滑动事件监听
    private OnTouchScroller mScroller;
    // 单双击事件监听
    private OnMultiClickListener mMultiClickListener;
    /**
     * 双击监听器
     */
    private final GestureDetector.OnDoubleTapListener mDoubleTapListener = new GestureDetector.OnDoubleTapListener() {
        @Override
        public boolean onSingleTapConfirmed(MotionEvent e) {
            if (VERBOSE) {
                Log.d(TAG, "onSingleTapConfirmed: ");
            }
            mTouchX = e.getX();
            mTouchY = e.getY();
            if (mMultiClickListener != null) {
                mMultiClickListener.onSurfaceSingleClick(e.getX(), e.getY());
            }
            return true;
        }

        @Override
        public boolean onDoubleTap(MotionEvent e) {
            if (VERBOSE) {
                Log.d(TAG, "onDoubleTap: ");
            }
            if (mMultiClickListener != null) {
                mMultiClickListener.onSurfaceDoubleClick(e.getX(), e.getY());
            }
            return false;
        }

        @Override
        public boolean onDoubleTapEvent(MotionEvent e) {
            if (VERBOSE) {
                Log.d(TAG, "onDoubleTapEvent: ");
            }
            return true;
        }
    };
    // 手势监听器
    private GestureDetectorCompat mGestureDetector;

    public CainTextureView(Context context) {
        this(context, null);
    }

    public CainTextureView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CainTextureView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        setClickable(true);
        mGestureDetector = new GestureDetectorCompat(getContext(), new GestureDetector.OnGestureListener() {
            @Override
            public boolean onDown(MotionEvent e) {
                if (VERBOSE) {
                    Log.d(TAG, "onDown: ");
                }
                //测试贴纸触摸
                dragSticker = PreviewRenderer.getInstance().touchDown(e);
                return false;
            }

            @Override
            public void onShowPress(MotionEvent e) {
                if (VERBOSE) {
                    Log.d(TAG, "onShowPress: ");
                }
            }

            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                if (VERBOSE) {
                    Log.d(TAG, "onSingleTapUp: ");
                }
                dragSticker = null;
                return false;
            }

            @Override
            public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
                if (VERBOSE) {
                    Log.d(TAG, "onScroll: ");
                }

                if (dragSticker != null) {
                    dragSticker.onScroll(distanceX, distanceY);
                    return true;
                }

                // 上下滑动
                if (Math.abs(distanceX) < Math.abs(distanceY) * 1.5) {
                    // 是否从左边开始上下滑动
                    boolean leftScroll = e1.getX() < getWidth() / 2;
                    if (distanceY > 0) {
                        if (mScroller != null) {
                            mScroller.swipeUpper(leftScroll, Math.abs(distanceY));
                        }
                    } else {
                        if (mScroller != null) {
                            mScroller.swipeDown(leftScroll, Math.abs(distanceY));
                        }
                    }
                }

                return true;
            }

            @Override
            public void onLongPress(MotionEvent e) {
                if (VERBOSE) {
                    Log.d(TAG, "onLongPress: ");
                }
            }

            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                if (VERBOSE) {
                    Log.d(TAG, "onFling: ");
                }

                if (dragSticker != null) {
                    return false;
                }

                // 快速左右滑动
                if (Math.abs(velocityX) > Math.abs(velocityY) * 1.5) {
                    if (velocityX < 0) {
                        if (mScroller != null) {
                            mScroller.swipeBack();
                        }
                    } else {
                        if (mScroller != null) {
                            mScroller.swipeFrontal();
                        }
                    }
                }
                return false;
            }
        });
        mGestureDetector.setOnDoubleTapListener(mDoubleTapListener);

        // 添加圆角显示
        if (Build.VERSION.SDK_INT >= 21) {
            setOutlineProvider(new RoundOutlineProvider(DensityUtils.dp2px(getContext(), 7.5f)));
            setClipToOutline(true);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mGestureDetector.onTouchEvent(event);
        return super.onTouchEvent(event);
    }

    /**
     * 添加对焦动画
     */
    public void showFocusAnimation() {
        if (mFocusAnimator == null) {
            mFocusImageView = new ImageView(getContext());
            mFocusImageView.setImageResource(R.drawable.video_focus);
            mFocusImageView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            mFocusImageView.measure(0, 0);
            mFocusImageView.setX(mTouchX - mFocusImageView.getMeasuredWidth() / 2);
            mFocusImageView.setY(mTouchY - mFocusImageView.getMeasuredHeight() / 2);
            final ViewGroup parent = (ViewGroup) getParent();
            parent.addView(mFocusImageView);

            mFocusAnimator = ValueAnimator.ofFloat(0, 1).setDuration(500);
            mFocusAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    if (mFocusImageView != null) {
                        float value = (float) animation.getAnimatedValue();
                        mFocusImageView.setScaleX(2 - value);
                        mFocusImageView.setScaleY(2 - value);
                    }
                }
            });
            mFocusAnimator.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    if (mFocusImageView != null) {
                        parent.removeView(mFocusImageView);
                        mFocusAnimator = null;
                    }
                }
            });
            mFocusAnimator.start();
        }
    }

    /**
     * 添加滑动回调
     *
     * @param scroller
     */
    public void addOnTouchScroller(OnTouchScroller scroller) {
        mScroller = scroller;
    }

    /**
     * 添加点击事件回调
     *
     * @param listener
     */
    public void addMultiClickListener(OnMultiClickListener listener) {
        mMultiClickListener = listener;
    }

    /**
     * 滑动监听器
     */
    public interface OnTouchScroller {
        void swipeBack();

        void swipeFrontal();

        void swipeUpper(boolean startInLeft, float distance);

        void swipeDown(boolean startInLeft, float distance);
    }

    /**
     * 点击事件监听器
     */
    public interface OnMultiClickListener {

        // 单击
        void onSurfaceSingleClick(float x, float y);

        // 双击
        void onSurfaceDoubleClick(float x, float y);
    }

    /**
     * 添加视频圆角功能
     */
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private static class RoundOutlineProvider extends ViewOutlineProvider {
        private final float mRadius;

        RoundOutlineProvider(float radius) {
            mRadius = radius;
        }

        @Override
        public void getOutline(View view, Outline outline) {
            Rect rect = new Rect();
            view.getGlobalVisibleRect(rect);
            int leftMargin = 0;
            int topMargin = 0;
            Rect selfRect = new Rect(leftMargin, topMargin,
                    rect.right - rect.left - leftMargin, rect.bottom - rect.top - topMargin);
            outline.setRoundRect(selfRect, mRadius);
        }
    }
}
