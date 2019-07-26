package com.yumore.common.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.yumore.common.R;
import com.yumore.common.utility.EmptyUtils;
import com.yumore.common.utility.SpaceItemDecoration;

/**
 * @author Nathaniel
 * @date 18-7-13-下午4:58
 */
public class CustomPopupWindow extends PopupWindow {
    public CustomPopupWindow(Context context) {
        super(context);
    }

    public CustomPopupWindow(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomPopupWindow(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public CustomPopupWindow(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public CustomPopupWindow(View contentView, int width, int height) {
        super(contentView, width, height);
    }

    public CustomPopupWindow(View contentView, int width, int height, boolean focusable) {
        super(contentView, width, height, focusable);
    }

    @Override
    public void showAsDropDown(View anchor) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            Rect rect = new Rect();
            anchor.getGlobalVisibleRect(rect);
            int height = anchor.getResources().getDisplayMetrics().heightPixels - rect.bottom;
            setHeight(height);
        } else {
            super.showAsDropDown(anchor);
        }
    }

    public static class Builder implements View.OnClickListener, BaseQuickAdapter.OnItemClickListener {

        private BaseQuickAdapter.OnItemClickListener onItemClickListener;
        private int width, height;
        private RecyclerView.LayoutManager layoutManager;
        private View.OnClickListener onClickListener;
        private CustomPopupWindow customPopupWindow;
        private Context context;
        private boolean widthFull, heightFull;
        private View customView;
        private BaseQuickAdapter baseQuickAdapter;
        private RecyclerView recyclerView;
        private boolean scrollEnable;
        private View headerView, footerView;

        public Builder(Context context) {
            this.context = context;
        }

        public Builder setLayoutManager(RecyclerView.LayoutManager layoutManager) {
            this.layoutManager = layoutManager;
            return this;
        }

        public Builder setOnItemClickListener(BaseQuickAdapter.OnItemClickListener onItemClickListener) {
            this.onItemClickListener = onItemClickListener;
            return this;
        }

        public Builder setOnClickListener(View.OnClickListener onClickListener) {
            this.onClickListener = onClickListener;
            return this;
        }

        public Builder setWidth(int width) {
            this.width = width;
            return this;
        }

        public Builder setHeight(int height) {
            this.height = height;
            return this;
        }

        public Builder setWidthFull(boolean widthFull) {
            this.widthFull = widthFull;
            return this;
        }

        public Builder setHeaderView(View headerView) {
            this.headerView = headerView;
            return this;
        }

        public Builder setFooterView(View footerView) {
            this.footerView = footerView;
            return this;
        }

        public Builder setHeightFull(boolean heightFull) {
            this.heightFull = heightFull;
            return this;
        }

        public Builder setCustomView(View customView) {
            this.customView = customView;
            return this;
        }

        public Builder setScrollEnable(boolean scrollEnable) {
            this.scrollEnable = scrollEnable;
            return this;
        }

        public Builder setBaseQuickAdapter(BaseQuickAdapter baseQuickAdapter) {
            this.baseQuickAdapter = baseQuickAdapter;
            return this;
        }

        public CustomPopupWindow create() {
            width = width > 0 ? width : widthFull ? ViewGroup.LayoutParams.MATCH_PARENT : ViewGroup.LayoutParams.WRAP_CONTENT;
            height = height > 0 ? height : heightFull ? ViewGroup.LayoutParams.MATCH_PARENT : ViewGroup.LayoutParams.WRAP_CONTENT;
            LayoutInflater inflater = LayoutInflater.from(context);
            View contentView = inflater.inflate(R.layout.custom_popup_window, null);
            final LinearLayout linearLayout = contentView.findViewById(R.id.popup_window_root);
            LinearLayout headerLayout = contentView.findViewById(R.id.popup_header_layout);
            LinearLayout footerLayout = contentView.findViewById(R.id.popup_footer_layout);
            int space = (int) context.getResources().getDimension(R.dimen.common_item_space_minimum);
            if (!EmptyUtils.isObjectEmpty(headerView)) {
                headerLayout.removeAllViews();
                headerLayout.addView(headerView);
            } else {
                headerLayout.setMinimumHeight(space);
            }
            if (EmptyUtils.isObjectEmpty(customView)) {
                contentView.findViewById(R.id.popup_window_cancel).setOnClickListener(this);
                recyclerView = contentView.findViewById(R.id.recyclerView);
                if (layoutManager == null) {
                    layoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
                }
                SpaceItemDecoration spaceItemDecoration = new SpaceItemDecoration(space, false, SpaceItemDecoration.LINEAR_LAYOUT);
                recyclerView.addItemDecoration(spaceItemDecoration);
                recyclerView.setNestedScrollingEnabled(scrollEnable);
                recyclerView.setOverScrollMode(scrollEnable ? RecyclerView.OVER_SCROLL_ALWAYS : RecyclerView.OVER_SCROLL_NEVER);
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setAdapter(baseQuickAdapter);
                baseQuickAdapter.setOnItemClickListener(this);
            } else {
                linearLayout.removeAllViews();
                linearLayout.addView(customView);
            }
            if (!EmptyUtils.isObjectEmpty(footerView)) {
                footerLayout.removeAllViews();
                footerLayout.addView(footerView);
            } else {
                headerLayout.setMinimumHeight(space);
            }
            customPopupWindow = new CustomPopupWindow(contentView, width, height, true);
            customPopupWindow.setContentView(contentView);
            customPopupWindow.setFocusable(true);
            customPopupWindow.setBackgroundDrawable(new ColorDrawable(0xb0808080));
            customPopupWindow.setAnimationStyle(R.style.PopupWindowStyle);
            contentView.setOnTouchListener(new View.OnTouchListener() {

                @SuppressLint("ClickableViewAccessibility")
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    int y = (int) event.getY();
                    if (event.getAction() == MotionEvent.ACTION_UP) {
                        if (y < linearLayout.getTop()) {
                            customPopupWindow.dismiss();
                        }
                    }
                    return true;
                }
            });
            customPopupWindow.update();
            return customPopupWindow;
        }


        @Override
        public void onClick(View view) {
            if (view.getId() == R.id.popup_window_cancel) {
                if (onClickListener != null) {
                    onClickListener.onClick(view);
                }
            }
        }

        @Override
        public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
            if (null != onItemClickListener) {
                onItemClickListener.onItemClick(adapter, view, position);
            }
        }
    }
}
