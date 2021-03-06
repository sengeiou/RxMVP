package com.yumore.caincamera.activity;

import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;

import com.yumore.caincamera.R;
import com.yumore.caincamera.fragment.MusicMergeFragment;
import com.yumore.uitls.fragment.MusicSelectFragment;

/**
 * 视频音乐合成
 */
public class MusicMergeActivity extends AppCompatActivity {

    public static final String PATH = "video_path";

    private static final String FRAGMENT_MUSIC_MERGE = "fragment_music_merge";

    private String mVideoPath;
    private final MusicSelectFragment.OnMusicSelectedListener listener = (music) -> {
        MusicMergeFragment fragment = MusicMergeFragment.newInstance();
        fragment.setVideoPath(mVideoPath);
        fragment.setMusicPath(music.getSongUrl(), music.getDuration());
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, fragment, FRAGMENT_MUSIC_MERGE)
                .commit();
    };

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
        hideNavigationBar();
        setContentView(R.layout.activity_music_merge);
        if (null == savedInstanceState) {
            mVideoPath = getIntent().getStringExtra(PATH);
            MusicSelectFragment fragment = new MusicSelectFragment();
            fragment.addOnMusicSelectedListener(listener);
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.fragment_container, fragment)
                    .commit();
        }
    }

}
