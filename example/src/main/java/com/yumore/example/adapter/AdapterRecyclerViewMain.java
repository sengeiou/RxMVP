package com.yumore.example.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.bumptech.glide.Glide;
import com.yumore.example.R;
import com.yumore.example.R2;
import com.yumore.example.entity.ModelMainItem;
import com.yumore.utility.utility.RxActivityTool;

import java.util.List;

/**
 * @author yumore
 * @date 2016/11/13
 */

public class AdapterRecyclerViewMain extends RecyclerView.Adapter<AdapterRecyclerViewMain.ViewHolder> {

    private int mScreenWidth, mItemWidth, mItemHeight;
    private Context context;
    private List<ModelMainItem> mValues;

    public AdapterRecyclerViewMain(List<ModelMainItem> items) {
        mValues = items;
    }

    @NonNull
    @Override
    public AdapterRecyclerViewMain.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recyclerview_main, parent, false);
        context = view.getContext();
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.mItem = mValues.get(position);
        holder.tvName.setText(holder.mItem.getName());
        Glide.with(context)
                .load(holder.mItem.getImage()).
                thumbnail(0.5f).
                into(holder.imageView);

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RxActivityTool.skipActivity(context, holder.mItem.getActivity());
            }
        });
    }


    @Override
    public int getItemCount() {
        return mValues.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public ModelMainItem mItem;
        @BindView(R2.id.imageView)
        ImageView imageView;
        @BindView(R2.id.tv_name)
        TextView tvName;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            ButterKnife.bind(this, view);
        }
    }
}
