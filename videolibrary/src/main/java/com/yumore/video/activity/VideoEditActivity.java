package com.yumore.video.activity;

import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;

import com.yumore.uitls.bean.Music;
import com.yumore.uitls.fragment.MusicSelectFragment;
import com.yumore.video.R;
import com.yumore.video.fragment.VideoEditFragment;

public class VideoEditActivity extends AppCompatActivity implements VideoEditFragment.OnSelectMusicListener,
        MusicSelectFragment.OnMusicSelectedListener {

    public static final String VIDEO_PATH = "videoPath";

    private static final String FRAGMENT_VIDEO_EDIT = "fragment_video_edit";
    private static final String FRAGMENT_MUSIC_SELECT = "fragment_video_music_select";

    protected void hideNavigationBar() {
        if (Build.VERSION.SDK_INT > 11 && Build.VERSION.SDK_INT < 19) {
            View v = getWindow().getDecorView();
            v.setSystemUiVisibility(View.GONE);
        } else if (Build.VERSION.SDK_INT >= 19) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
            View decorView = getWindow().getDecorView();
            int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY | View.SYSTEM_UI_FLAG_FULLSCREEN;
            decorView.setSystemUiVisibility(uiOptions);
            decorView.setOnSystemUiVisibilityChangeListener(new View.OnSystemUiVisibilityChangeListener() {
                @Override
                public void onSystemUiVisibilityChange(int visibility) {
                    hideNavigationBar();
                }
            });
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        hideNavigationBar();
        setContentView(R.layout.activity_video_edit);
        if (null == savedInstanceState) {
            String videoPath = getIntent().getStringExtra(VIDEO_PATH);
            VideoEditFragment fragment = VideoEditFragment.newInstance();
            fragment.setOnSelectMusicListener(this);
            fragment.setVideoPath(videoPath);
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_content, fragment, FRAGMENT_VIDEO_EDIT)
                    .addToBackStack(FRAGMENT_VIDEO_EDIT)
                    .commit();
        }
    }

    @Override
    public void onBackPressed() {
        // ??????fragment????????????????????????????????????????????????????????????????????????????????????????????????
        int backStackEntryCount = getSupportFragmentManager().getBackStackEntryCount();
        if (backStackEntryCount == 1) {
            VideoEditFragment fragment = (VideoEditFragment) getSupportFragmentManager()
                    .findFragmentByTag(FRAGMENT_VIDEO_EDIT);
            if (fragment != null) {
                fragment.onBackPressed();
            }
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onDestroy() {
        int backStackEntryCount = getSupportFragmentManager().getBackStackEntryCount();
        if (backStackEntryCount == 1) {
            VideoEditFragment fragment = (VideoEditFragment) getSupportFragmentManager()
                    .findFragmentByTag(FRAGMENT_VIDEO_EDIT);
            if (fragment != null) {
                fragment.setOnSelectMusicListener(null);
            }
        }
        super.onDestroy();
    }

    @Override
    public void onOpenMusicSelectPage() {
        MusicSelectFragment fragment = new MusicSelectFragment();
        fragment.addOnMusicSelectedListener(this);
        getSupportFragmentManager()
                .beginTransaction()
                .setCustomAnimations(R.anim.anim_slide_up, 0)
                .add(R.id.fragment_content, fragment)
                .addToBackStack(FRAGMENT_MUSIC_SELECT)
                .commit();
    }

    @Override
    public void onMusicSelected(Music music) {
        getSupportFragmentManager().popBackStack(FRAGMENT_VIDEO_EDIT, 0);
        VideoEditFragment fragment = (VideoEditFragment) getSupportFragmentManager()
                .findFragmentByTag(FRAGMENT_VIDEO_EDIT);
        if (fragment != null) {
            fragment.setSelectedMusic(music.getSongUrl(), music.getDuration());
        }
    }
}
