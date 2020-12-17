package com.yumore.camera.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.yumore.camera.R;
import com.yumore.camera.loader.MediaLoader;
import com.yumore.camera.loader.impl.GlideMediaLoader;
import com.yumore.filter.glfilter.resource.bean.ResourceData;
import com.yumore.uitls.utils.BitmapUtils;

import java.util.List;

/**
 * 贴纸资源适配器
 */
public class PreviewResourceAdapter extends RecyclerView.Adapter<PreviewResourceAdapter.ResourceHolder> {

    private final Drawable mPlaceHolder;
    private final List<ResourceData> mResourceList;
    private final Context mContext;
    private final MediaLoader mMediaLoader;
    private int mSelected;
    private OnResourceChangeListener mListener;

    public PreviewResourceAdapter(Context context, List<ResourceData> itemList) {
        mContext = context;
        mResourceList = itemList;
        mSelected = 0;
        mPlaceHolder = context.getDrawable(R.drawable.ic_camera_thumbnail_placeholder);
        mMediaLoader = new GlideMediaLoader();
    }

    @NonNull
    @Override
    public ResourceHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_preview_resource_view,
                parent, false);
        ResourceHolder holder = new ResourceHolder(view);
        holder.resourceRoot = view.findViewById(R.id.resource_root);
        holder.resourcePanel = view.findViewById(R.id.resource_panel);
        holder.resourceThumb = view.findViewById(R.id.resource_thumb);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ResourceHolder holder, int position) {
        ResourceData resource = mResourceList.get(position);
        // 如果是asset下面的，则直接解码
        if (!TextUtils.isEmpty(resource.thumbPath) && resource.thumbPath.startsWith("assets://")) {
            holder.resourceThumb.setImageBitmap(BitmapUtils.getImageFromAssetsFile(mContext,
                    resource.thumbPath.substring("assets://".length())));
        } else {
            if (mMediaLoader != null) {
                mMediaLoader.loadThumbnail(mContext, mPlaceHolder, holder.resourceThumb,
                        Uri.parse(resource.thumbPath));
            }
        }

        if (position == mSelected) {
            holder.resourcePanel.setBackgroundResource(R.drawable.ic_camera_effect_selected);
        } else {
            holder.resourcePanel.setBackgroundResource(0);
        }

        final int currentPosition = position;
        holder.resourceRoot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mSelected == currentPosition) {
                    return;
                }
                int last = mSelected;
                mSelected = currentPosition;
                notifyItemChanged(last);
                notifyItemChanged(currentPosition);
                if (mListener != null) {
                    mListener.onResourceChanged(mResourceList.get(currentPosition));
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return mResourceList == null ? 0 : mResourceList.size();
    }

    public void setOnResourceChangeListener(OnResourceChangeListener listener) {
        mListener = listener;
    }


    public interface OnResourceChangeListener {
        void onResourceChanged(ResourceData resourceData);
    }

    public class ResourceHolder extends RecyclerView.ViewHolder {

        public LinearLayout resourceRoot;
        public FrameLayout resourcePanel;
        public ImageView resourceThumb;

        public ResourceHolder(View itemView) {
            super(itemView);
        }
    }

}
