package com.yumore.example.provider;

import android.app.Application;
import android.content.Context;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.yumore.provider.IExampleProvider;
import com.yumore.provider.RouterConstants;

/**
 * @author nathaniel
 * @version V1.0.0
 * @package com.yumore.example.provider
 * @datetime 12/15/20 - 11:10 AM
 */
@Route(path = RouterConstants.EXAMPLE_PROVIDER)
public class ExampleProvider implements IExampleProvider {
    @Override
    public void initPlugins(Application application) {

    }

    @Override
    public void init(Context context) {

    }
}
