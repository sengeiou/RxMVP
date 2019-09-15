package com.yumore.example.common;

import android.content.Context;
import com.yumore.common.helper.InitializeHelper;
import com.yumore.utility.utility.RxTool;

/**
 * @author vonde
 * @date 2016/12/23
 */

public class ExampleApplication implements InitializeHelper {

    private static ExampleApplication exampleApplication;


    public static ExampleApplication getInstance() {
        if (exampleApplication == null) {
            exampleApplication = new ExampleApplication();
        }
        return exampleApplication;
    }

    @Override
    public void initialize(Context context) {
        RxTool.init(context);
    }
}
