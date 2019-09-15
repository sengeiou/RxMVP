package com.yumore.sample.test;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.launcher.ARouter;
import com.yumore.sample.agency.UserAgency;

/**
 * @author Nathaniel
 * @date 19-3-1 - 下午5:28
 */
public class UserAgencyDemo {

    @Autowired
    UserAgency userAgency;

    private void useAgencyWithAnnotation() {
        // 使用注解
        userAgency.getUsername();
    }

    private void useAgencyWithoutAnnotation() {
        UserAgency userAgency = ARouter.getInstance().navigation(UserAgency.class);
        userAgency.getUsername();
    }
}
