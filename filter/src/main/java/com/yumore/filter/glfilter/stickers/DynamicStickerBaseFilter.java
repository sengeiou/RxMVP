package com.yumore.filter.glfilter.stickers;

import android.content.Context;

import com.yumore.filter.glfilter.base.GLImageAudioFilter;
import com.yumore.filter.glfilter.stickers.bean.DynamicSticker;

import java.util.ArrayList;
import java.util.List;

/**
 * 贴纸滤镜基类
 */
public class DynamicStickerBaseFilter extends GLImageAudioFilter {

    // 贴纸数据
    protected DynamicSticker mDynamicSticker;

    // 贴纸加载器列表
    protected List<DynamicStickerLoader> mStickerLoaderList;

    public DynamicStickerBaseFilter(Context context, DynamicSticker sticker, String vertexShader, String fragmentShader) {
        super(context, vertexShader, fragmentShader);
        mDynamicSticker = sticker;
        mStickerLoaderList = new ArrayList<>();
    }

}
