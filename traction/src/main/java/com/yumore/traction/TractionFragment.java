package com.yumore.traction;


import android.media.MediaPlayer;
import android.widget.VideoView;

/**
 * @author Nathaniel
 */
public class GuidePagerFragment extends LazyLoadFragment implements MediaPlayer.OnPreparedListener, MediaPlayer.OnCompletionListener {

    private VideoView videoView;
    private int curPage;
    private boolean mHasPaused;

    @Override
    protected int setContentView() {
        return R.layout.fragment_traction;
    }

    @Override
    protected void stopLoad() {
        super.stopLoad();
        if (videoView != null) {
            videoView.stopPlayback();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    protected void lazyLoad() {
        if (getArguments() == null) {
            return;
        }
        videoView = findViewById(R.id.videoview_guide);
        int videoRes = getArguments().getInt("res");
        curPage = getArguments().getInt("page");
        videoView.setOnPreparedListener(this);
        videoView.setVideoPath("android.resource://" + getActivity().getPackageName() + "/" + videoRes);

    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        if (videoView != null) {
            videoView.requestFocus();
            videoView.seekTo(0);
            videoView.start();
            videoView.setOnCompletionListener(this);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mHasPaused) {
            if (videoView != null) {
                videoView.seekTo(curPage);
                videoView.resume();
            }
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (videoView != null) {
            curPage = videoView.getCurrentPosition();
        }
        mHasPaused = true;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (videoView != null) {
            videoView.stopPlayback();
        }
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        ((TractionActivity) getActivity()).next(curPage);
    }
}
