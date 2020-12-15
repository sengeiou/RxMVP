package com.yumore.example;

import android.content.Context;

import com.yumore.common.helper.InitializeHelper;
import com.yumore.provider.utility.EmptyUtils;
import com.yumore.utility.utility.RxTool;

/**
 * @author nathaniel
 * @version V1.0.0
 * @package com.yumore.example
 * @datetime 12/15/20 - 2:19 PM
 */
public class ExampleApplication implements InitializeHelper {
    private static ExampleApplication exampleApplication;

    private ExampleApplication() {
    }

    public synchronized static ExampleApplication getInstance() {
        if (EmptyUtils.isEmpty(exampleApplication)) {
            exampleApplication = new ExampleApplication();
        }
        return exampleApplication;
    }

    @Override
    public void initialize(Context context) {
        RxTool.init(context);

    }
}
