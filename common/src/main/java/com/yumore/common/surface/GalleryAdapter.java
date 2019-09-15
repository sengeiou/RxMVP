package com.yumore.common.surface;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;
import com.github.chrisbanes.photoview.PhotoView;
import com.yumore.common.R;
import com.yumore.common.utility.EmptyUtils;
import com.yumore.common.utility.ImageFillUtils;

import java.util.List;


/**
 * @author Nathaniel
 * @date 18-8-6-上午10:34
 */
public class GalleryAdapter extends PagerAdapter {
    private List<String> imageUrlList;

    public void setImageUrlList(List<String> imageUrlList) {
        this.imageUrlList = imageUrlList;
    }

    @Override
    public int getCount() {
        return EmptyUtils.isObjectEmpty(imageUrlList) ? 0 : imageUrlList.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        Context context = container.getContext();
        View view = LayoutInflater.from(container.getContext()).inflate(R.layout.common_item_gallery_list, container, false);
        PhotoView photoView = view.findViewById(R.id.photoView);
        String imageUrl = imageUrlList.get(position);
        ImageFillUtils.displayImage(context, imageUrl, photoView);
        photoView.setEnabled(true);
        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }
}
