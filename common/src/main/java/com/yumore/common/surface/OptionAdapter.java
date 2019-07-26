package com.yumore.common.surface;

import android.view.View;
import android.widget.LinearLayout;
import androidx.annotation.Nullable;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.yumore.common.R;

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
    protected void convert(BaseViewHolder helper, M m) {
        if (m == null) {
            return;
        }
        int itemCount = 4;
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(helper.getConvertView().getLayoutParams());
        if (helper.getAdapterPosition() % itemCount == itemCount - 1) {
            layoutParams.setMargins(0, 0, 0, 0);
        } else {
            layoutParams.setMargins(0, 0, (int) mContext.getResources().getDimension(R.dimen.common_padding_normal), 0);
        }
        if (showEnable) {
            helper.getConvertView().setLayoutParams(layoutParams);
        }
        View view = helper.getView(R.id.common_split_view);
        view.setVisibility(helper.getAdapterPosition() < mData.size() - 1 ? View.VISIBLE : View.GONE);
    }
}
