package com.yumore.master.provider;

import android.content.Context;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.yumore.common.utility.PreferencesUtils;
import com.yumore.provider.ISampleProvider;
import com.yumore.provider.RouterConstants;

/**
 * @author Nathaniel
 * @date 19-3-1 - 下午5:23
 */
@Route(path = RouterConstants.SAMPLE_PROVIDER)
public class MasterProvider implements ISampleProvider {
    private Context context;

    @Override
    public void init(Context context) {
        this.context = context;
    }


    @Override
    public String getUsername() {
        return "";
    }

    @Override
    public boolean getTractionEnable() {
        return PreferencesUtils.getInstance(context).getTractionEnable();
    }

    @Override
    public void setTractionEnable(boolean tractionEnable) {
        PreferencesUtils.getInstance(context).setTractionEnable(tractionEnable);
    }

    @Override
    public boolean getIntroduceEnable() {
        return PreferencesUtils.getInstance(context).getIntroduceEnable();
    }

    @Override
    public void setIntroduceEnable(boolean introduceEnable) {
        PreferencesUtils.getInstance(context).setTractionEnable(introduceEnable);
    }
}
