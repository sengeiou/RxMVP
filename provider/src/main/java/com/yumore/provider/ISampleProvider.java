package com.yumore.provider;

import com.alibaba.android.arouter.facade.template.IProvider;

/**
 * @author nathaniel
 * @version V1.0.0
 * @package com.yumore.provider
 * @datetime 12/12/20 - 3:01 PM
 */
interface ISampleProvider extends IProvider {
    boolean getTractionEnable();

    void setTractionEnable(boolean tractionEnable);
}
