package com.cgfay.camera.fragment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import androidx.appcompat.widget.GridLayoutManager;
import androidx.appcompat.widget.RecyclerView;
import androidx.core.app.Fragment;
import com.cgfay.camera.adapter.PreviewResourceAdapter;
import com.cgfay.cameralibrary.R;
import com.cgfay.filter.glfilter.resource.ResourceHelper;
import com.cgfay.filter.glfilter.resource.bean.ResourceData;

/**
 * 贴纸资源页面
 */
public class PreviewResourceFragment extends Fragment {

    private static final String TAG = "PreviewResourceFragment";

    // 内容显示列表
    private View mContentView;

    // 标题
    private LinearLayout mLayoutStickerTitle;

    // 容器
    private LinearLayout mLayoutStickerContent;

    // 贴纸列表 TODO 后续可以改成ViewPager的形式，用于支持多种贴纸类型
    private LinearLayout mResourceLayout;
    private RecyclerView mResourceView;

    // 布局管理器
    private LayoutInflater mInflater;
    private Activity mActivity;
    private OnResourceChangeListener mOnResourceChangeListener;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mActivity = getActivity();
        mInflater = LayoutInflater.from(mActivity);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mContentView = inflater.inflate(R.layout.fragment_preview_resource, container, false);
        return mContentView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initView(mContentView);
    }

    private void initView(View view) {
        mLayoutStickerTitle = view.findViewById(R.id.layout_sticker_title);
        mLayoutStickerContent = view.findViewById(R.id.layout_sticker_content);
        if (mResourceLayout == null) {
            mResourceLayout = (LinearLayout) mInflater.inflate(R.layout.view_preview_resource, null);
        }
        mResourceView = mResourceLayout.findViewById(R.id.preview_resource_list);
        GridLayoutManager manager = new GridLayoutManager(mActivity, 5);
        mResourceView.setLayoutManager(manager);
        PreviewResourceAdapter adapter = new PreviewResourceAdapter(mActivity, ResourceHelper.getResourceList());
        mResourceView.setAdapter(adapter);
        adapter.setOnResourceChangeListener(resourceData -> {
            if (mOnResourceChangeListener != null) {
                mOnResourceChangeListener.onResourceChange(resourceData);
            }
        });
        mLayoutStickerContent.addView(mResourceLayout);
    }

    @Override
    public void onDestroyView() {
        mContentView = null;
        mOnResourceChangeListener = null;
        super.onDestroyView();
    }

    @Override
    public void onDetach() {
        mActivity = null;
        super.onDetach();
    }

    /**
     * 添加资源切换监听器
     *
     * @param listener
     */
    public void addOnChangeResourceListener(OnResourceChangeListener listener) {
        mOnResourceChangeListener = listener;
    }

    /**
     * 资源切换监听器
     */
    public interface OnResourceChangeListener {

        void onResourceChange(ResourceData data);
    }

}
