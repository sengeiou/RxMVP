package com.yumore.common.utility;

import android.content.Context;
import android.content.res.TypedArray;
import androidx.annotation.NonNull;
import com.yumore.common.R;
import com.yumore.common.entity.Option;

import java.util.ArrayList;
import java.util.List;

public class DataUtils {
    public static List<Option> getFileOptionList(@NonNull Context context) {
        List<Option> itemList = new ArrayList<>();
        String[] categoryNames = context.getResources().getStringArray(R.array.file_option_names);
        TypedArray typedArray = context.getResources().obtainTypedArray(R.array.file_option_resources);
        for (int index = 0; index < categoryNames.length; index++) {
            Option item = new Option();
            item.setDescribe(categoryNames[index]);
            item.setResourceId(typedArray.getResourceId(index, R.mipmap.icon_share_black));
            itemList.add(item);
        }
        typedArray.recycle();
        return itemList;
    }
}
