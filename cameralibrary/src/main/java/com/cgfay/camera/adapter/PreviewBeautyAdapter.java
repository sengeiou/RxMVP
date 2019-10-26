package com.cgfay.camera.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.widget.RecyclerView;
import com.cgfay.cameralibrary.R;

import java.util.Arrays;
import java.util.List;

/**
 * 美颜列表适配器
 */
public class PreviewBeautyAdapter extends RecyclerView.Adapter<PreviewBeautyAdapter.ImageHolder> {

    // 滤镜名称
    private final List<String> mItemNames;
    private Context mContext;
    private int mSelected = 0;
    private OnBeautySelectedListener mBeautySelectedListener;

    public PreviewBeautyAdapter(Context context) {
        mContext = context;
        String[] beautyLists = mContext.getResources().getStringArray(R.array.preview_beauty);
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
        holder.beautyName.setText(mItemNames.get(position));
        if (position == mSelected) {
            holder.beautyPanel.setBackgroundResource(R.drawable.ic_camera_effect_selected);
        } else {
            holder.beautyPanel.setBackgroundResource(0);
        }
        holder.beautyRoot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mSelected == position) {
                    return;
                }
                int lastSelected = mSelected;
                mSelected = position;
                notifyItemChanged(lastSelected, 0);
                notifyItemChanged(position, 0);
                if (mBeautySelectedListener != null) {
                    mBeautySelectedListener.onBeautySelected(position, mItemNames.get(position));
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return (mItemNames == null) ? 0 : mItemNames.size();
    }

    public void addOnBeautySelectedListener(OnBeautySelectedListener listener) {
        mBeautySelectedListener = listener;
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

    public interface OnBeautySelectedListener {
        void onBeautySelected(int position, String beautyName);
    }

    class ImageHolder extends RecyclerView.ViewHolder {
        // 根布局
        public LinearLayout beautyRoot;
        // 背景框
        public FrameLayout beautyPanel;
        // 预览文字
        public TextView beautyName;

        public ImageHolder(View itemView) {
            super(itemView);
            beautyRoot = itemView.findViewById(R.id.item_beauty_root);
            beautyPanel = itemView.findViewById(R.id.item_beauty_panel);
            beautyName = itemView.findViewById(R.id.item_beauty_name);
        }
    }
}
