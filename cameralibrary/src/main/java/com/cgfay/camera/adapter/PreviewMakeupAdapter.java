package com.yumore.camera.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.widget.RecyclerView;

import com.yumore.cameralibrary.R;

import java.util.Arrays;
import java.util.List;

public class PreviewMakeupAdapter extends RecyclerView.Adapter<PreviewMakeupAdapter.ImageHolder> {

    // 滤镜名称
    private final List<String> mItemNames;
    private final Context mContext;
    private int mSelected = 0;
    private OnMakeupSelectedListener mSelectedListener;

    public PreviewMakeupAdapter(Context context) {
        mContext = context;
        String[] beautyLists = mContext.getResources().getStringArray(R.array.preview_makeup);
        mItemNames = Arrays.asList(beautyLists);
    }

    @NonNull
    @Override
    public ImageHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_preview_beauty_view, parent, false);
        return new ImageHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ImageHolder holder, final int position) {
        holder.itemName.setText(mItemNames.get(position));
        if (position == mSelected) {
            holder.itemPanel.setBackgroundResource(R.drawable.ic_camera_effect_selected);
        } else {
            holder.itemPanel.setBackgroundResource(0);
        }
        holder.itemRoot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mSelected == position) {
                    return;
                }
                int lastSelected = mSelected;
                mSelected = position;
                notifyItemChanged(lastSelected, 0);
                notifyItemChanged(position, 0);
                if (mSelectedListener != null) {
                    mSelectedListener.onMakeupSelected(position, mItemNames.get(position));
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return (mItemNames == null) ? 0 : mItemNames.size();
    }

    public void addOnMakeupSelectedListener(OnMakeupSelectedListener listener) {
        mSelectedListener = listener;
    }

    /**
     * 滚动到当前选中位置
     *
     * @param selected
     */
    public void scrollToCurrentSelected(int selected) {
        int lastSelected = mSelected;
        mSelected = selected;
        notifyItemChanged(lastSelected, 0);
        notifyItemChanged(mSelected, 0);
    }

    /**
     * 获取选中索引
     *
     * @return
     */
    public int getSelectedPosition() {
        return mSelected;
    }

    public interface OnMakeupSelectedListener {
        void onMakeupSelected(int position, String makeupName);
    }

    class ImageHolder extends RecyclerView.ViewHolder {
        // 根布局
        public LinearLayout itemRoot;
        // 背景框
        public FrameLayout itemPanel;
        // 预览文字
        public TextView itemName;

        public ImageHolder(View itemView) {
            super(itemView);
            itemRoot = itemView.findViewById(R.id.item_beauty_root);
            itemPanel = itemView.findViewById(R.id.item_beauty_panel);
            itemName = itemView.findViewById(R.id.item_beauty_name);
        }
    }
}
