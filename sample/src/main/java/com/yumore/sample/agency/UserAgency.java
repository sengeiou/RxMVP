package com.yumore.sample.agency;

import android.content.Context;
import com.yumore.provider.IUserProvider;

/**
 * @author Nathaniel
 * @date 19-3-1 - 下午5:23
 */
public class UserAgency implements IUserProvider {

    @Override
    public void init(Context context) {

    }

    @Override
    public String getUsername() {
        return "";
    }
}
