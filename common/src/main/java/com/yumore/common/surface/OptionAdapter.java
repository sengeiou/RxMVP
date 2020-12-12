package com.yumore.common.surface;

import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.yumore.common.R;

import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * @author Nathaniel
 * @date 18-7-6-上午11:01
 */
public class OptionAdapter<M> extends BaseQuickAdapter<M, BaseViewHolder> {

    private boolean showEnable;

    public OptionAdapter(int layoutResId, @Nullable List<M> data) {
        super(layoutResId, data);
    }

    public void setShowEnable(boolean showEnable) {
        this.showEnable = showEnable;
    }

    @Override
    protected void convert(@NotNull BaseViewHolder helper, M m) {
        if (m == null) {
            return;
        }
        int itemCount = 4;
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(helper.itemView.getLayoutParams());
        if (helper.getAdapterPosition() % itemCount == itemCount - 1) {
            layoutParams.setMargins(0, 0, 0, 0);
        } else {
            layoutParams.setMargins(0, 0, (int) getContext().getResources().getDimension(R.dimen.common_padding_normal), 0);
        }
        if (showEnable) {
            helper.itemView.setLayoutParams(layoutParams);
        }
        View view = helper.getView(R.id.common_split_view);
        view.setVisibility(helper.getAdapterPosition() < getData().size() - 1 ? View.VISIBLE : View.GONE);
    }
}
