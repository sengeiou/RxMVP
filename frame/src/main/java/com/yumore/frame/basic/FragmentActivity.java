package com.yumore.frame.basic;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Window;

import com.yumore.frame.R;


/**
 * @author Nathaniel
 * @date 2018/4/19-16:38
 */
public class FragmentActivity extends AppCompatActivity {
    protected BaseFragment baseFragment;
    private String fragmentTag;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.common_frgment_activity);
        initialize(savedInstanceState);
    }

    @Override
    protected void onSaveInstanceState(Bundle bundle) {
        bundle.putString("fragment", fragmentTag);
        super.onSaveInstanceState(bundle);
    }

    protected void initialize(Bundle savedInstanceState) {
        String fragmentClass = getIntent().getStringExtra("fragment");
        if (null != savedInstanceState) {
            String currentFragment = savedInstanceState.getString("fragment");
            if (null != currentFragment) {
                if (currentFragment.equals(fragmentClass)) {
                    showFragment(currentFragment);
                } else {
                    removeFragment(currentFragment);
                    createFragment(fragmentClass);
                }
            } else {
                createFragment(fragmentClass);
            }
        } else {
            createFragment(fragmentClass);
        }
        fragmentTag = fragmentClass;
    }

    private void createFragment(String fragmentClass) {
        if (TextUtils.isEmpty(fragmentClass)) {
            return;
        }
        baseFragment = BaseFragment.createFragment(fragmentClass);
        if (null != baseFragment) {
            getSupportFragmentManager().beginTransaction().add(R.id.fragment_activity_content, baseFragment, fragmentClass).commitAllowingStateLoss();
        }

    }

    private void removeFragment(String fragmentClass) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment fragment = fragmentManager.findFragmentByTag(fragmentClass);
        if (fragment != null) {
            fragmentManager.beginTransaction().remove(fragment).commitAllowingStateLoss();
        }

    }

    private void showFragment(String fragmentClass) {
        Fragment fragment = getSupportFragmentManager().findFragmentByTag(fragmentClass);
        FragmentManager fragmentManager = getSupportFragmentManager();
        if (null != fragmentManager) {
            fragmentManager.beginTransaction().show(fragment).commitAllowingStateLoss();
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (null != baseFragment) {
            baseFragment.onActivityResult(requestCode, resultCode, data);
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
}
