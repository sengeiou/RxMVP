package com.yumore.easymvp.example;

import com.yumore.easymvp.R;
import com.yumore.easymvp.base.BaseMvpActivity;

/**
 * 例子4：不使用 mvp 的情况
 */
public class ExampleActivity4 extends BaseMvpActivity {
    @Override
    protected int getContentView() {
        return R.layout.activity_mainmvp;
    }

    @Override
    public void init() {

    }
}
