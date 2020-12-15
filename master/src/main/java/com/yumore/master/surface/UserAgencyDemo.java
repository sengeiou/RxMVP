package com.yumore.master.surface;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.launcher.ARouter;
import com.yumore.master.provider.SampleProvider;

/**
 * @author Nathaniel
 * @date 19-3-1 - 下午5:28
 */
public class UserAgencyDemo {

    @Autowired
    SampleProvider userAgency;

    private void useAgencyWithAnnotation() {
        // 使用注解
        userAgency.getUsername();
    }

    private void useAgencyWithoutAnnotation() {
        SampleProvider userAgency = ARouter.getInstance().navigation(SampleProvider.class);
        userAgency.getUsername();
    }
}
