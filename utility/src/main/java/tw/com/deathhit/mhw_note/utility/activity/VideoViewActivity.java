package tw.com.deathhit.mhw_note.utility.activity;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.MediaController;
import android.widget.VideoView;

import tw.com.deathhit.mhw_note.core.BaseActivity;

/**Simple video view activity supports one video view with intent.getData() as the uri of the video.**/
public abstract class VideoViewActivity extends BaseActivity implements MediaPlayer.OnPreparedListener, MediaPlayer.OnCompletionListener{
    private static final String CURRENT_POSITION = VideoViewActivity.class.getName() + ":currentPosition";
    private static final String IS_PLAYING = VideoViewActivity.class.getName() + ":play";

    private MediaController mediaController;

    private int currentPosition = 0;
    private boolean isPlaying = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(savedInstanceState != null){
            currentPosition = savedInstanceState.getInt(CURRENT_POSITION);
            isPlaying = savedInstanceState.getBoolean(IS_PLAYING);
        }
    }

    @Override
    public void onStart(){
        super.onStart();

        VideoView videoView = getVideoView();

        videoView.setOnCompletionListener(this);

        videoView.setOnPreparedListener(this);

        videoView.setVideoURI(getIntent().getData());

        videoView.setZOrderOnTop(true);

        setMediaController();
    }

    /**Save the states of video view.**/
    @Override
    public void onPause(){
        super.onPause();

        VideoView videoView = getVideoView();

        if (videoView.getCurrentPosition() != 0)
            currentPosition = videoView.getCurrentPosition();

        if (isPlaying = videoView.isPlaying())
            videoView.pause();
    }

    /**Stop release media player when activity goes to background.**/
    @Override
    public void onStop(){
        super.onStop();

        getVideoView().stopPlayback();
    }

    /**If activity is going to be recreated, save states to bundle.**/
    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putInt(CURRENT_POSITION, currentPosition);
        savedInstanceState.putBoolean(IS_PLAYING, isPlaying);

        super.onSaveInstanceState(savedInstanceState);
    }

    /**request() is not used here.**/
    @Override
    public Object request(int requestType, @Nullable Object... args) {
        return null;
    }

    /**Start video view on prepared.**/
    @Override
    public void onPrepared(MediaPlayer mediaPlayer) {
        mediaPlayer.seekTo(currentPosition);

        VideoView videoView = getVideoView();

        videoView.requestFocus();

        if (isPlaying)
            mediaPlayer.start();
        else {
            //Get first frame by start() and pause() at the same time.
            mediaPlayer.start();

            mediaPlayer.pause();
        }

        if(mediaController != null)
            mediaController.show();

    }

    /**Return to position 0 to be played again.**/
    @Override
    public void onCompletion(MediaPlayer mediaPlayer) {
        getVideoView().seekTo(0);

        currentPosition = 0;

        if(mediaController != null)
           mediaController.show();
    }

    /**This method is used to set default MediaController widget for the videoView. It is invoked in onStart().
     * Override it to either disable mediaController or supply custom one.**/
    protected void setMediaController(){
        if(mediaController == null)
            mediaController = new MediaController(this);

        mediaController.setAnchorView(getVideoView());

        getVideoView().setMediaController(mediaController);

        getVideoView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mediaController.show();
            }
        });
    }

    /**Get VideoView of the activity. VideoView must be available before onStart().**/
    public abstract VideoView getVideoView();
}
