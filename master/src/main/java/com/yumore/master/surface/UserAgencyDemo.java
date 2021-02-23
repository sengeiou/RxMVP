package com.yumore.master.surface;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.launcher.ARouter;
import com.yumore.master.provider.MasterProvider;

/**
 * @author Nathaniel
 * @date 19-3-1 - 下午5:28
 */
public class UserAgencyDemo {

    @Autowired
    MasterProvider userAgency;

    private void useAgencyWithAnnotation() {
        // 使用注解
        userAgency.getUsername();
    }

    private void useAgencyWithoutAnnotation() {
        MasterProvider userAgency = ARouter.getInstance().navigation(MasterProvider.class);
        userAgency.getUsername();
    }
}
