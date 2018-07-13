package tw.com.deathhit.mhw_note.utility.widget;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.content.res.TypedArray;
import android.graphics.Matrix;
import android.graphics.SurfaceTexture;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RawRes;
import android.util.AttributeSet;
import android.view.Surface;
import android.view.TextureView;
import android.widget.MediaController;

import java.io.FileDescriptor;
import java.io.IOException;
import java.util.Map;

import tw.com.deathhit.mhw_note.utility.ScaleFactory;
import tw.com.deathhit.utility.R;

public class TextureVideoView extends TextureView implements TextureView.SurfaceTextureListener,
        MediaPlayer.OnVideoSizeChangedListener, MediaController.MediaPlayerControl{
    protected MediaPlayer mMediaPlayer;

    protected MediaController mediaController;

    protected ScaleFactory.ScaleType mScalableType = ScaleFactory.ScaleType.NONE;

    protected boolean isMediaControllerEnabled = false;

    public TextureVideoView(Context context) {
        this(context, null);
    }

    public TextureVideoView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TextureVideoView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        if (attrs == null)
            return;

        TypedArray typedArray = getContext().getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.TextureVideoView,
                0, 0);

        int scaleType;

        try {
            scaleType = typedArray.getInt(R.styleable.TextureVideoView_scaleType, ScaleFactory.ScaleType.NONE.ordinal());
        } finally {
            typedArray.recycle();
        }

        mScalableType = ScaleFactory.ScaleType.values()[scaleType];
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (mMediaPlayer == null)
            return;

        if (isPlaying())
            stop();

        release();
    }

    /**TextureView.SurfaceTextureListener method**/
    @Override
    public void onSurfaceTextureAvailable(SurfaceTexture surfaceTexture, int width, int height) {
        Surface surface = new Surface(surfaceTexture);

        if (mMediaPlayer != null)
            mMediaPlayer.setSurface(surface);
    }

    /**TextureView.SurfaceTextureListener method**/
    @Override
    public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {

    }

    /**TextureView.SurfaceTextureListener method**/
    @Override
    public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
        return false;
    }

    /**TextureView.SurfaceTextureListener method**/
    @Override
    public void onSurfaceTextureUpdated(SurfaceTexture surface) {

    }

    /**MediaPlayer.OnVideoSizeChangedListener method**/
    @Override
    public void onVideoSizeChanged(MediaPlayer mp, int width, int height) {
        scaleVideoSize(width, height);
    }

    /**MediaController.MediaPlayerControl method**/
    @Override
    public void start() {
        mMediaPlayer.start();
    }

    /**MediaController.MediaPlayerControl method**/
    @Override
    public boolean isPlaying() {
        return mMediaPlayer.isPlaying();
    }

    /**MediaController.MediaPlayerControl method**/
    @Override
    public void pause() {
        mMediaPlayer.pause();
    }

    /**MediaController.MediaPlayerControl method**/
    @Override
    public void seekTo(int msec) {
        mMediaPlayer.seekTo(msec);
    }

    /**MediaController.MediaPlayerControl method**/
    @Override
    public int getCurrentPosition() {
        return mMediaPlayer.getCurrentPosition();
    }

    /**MediaController.MediaPlayerControl method**/
    @Override
    public int getDuration() {
        return mMediaPlayer.getDuration();
    }

    /**MediaController.MediaPlayerControl method**/
    @Override
    public int getBufferPercentage() {
        return  (mMediaPlayer.getCurrentPosition()*100)/mMediaPlayer.getDuration();
    }

    /**MediaController.MediaPlayerControl method**/
    @Override
    public boolean canPause() {
        return true;
    }

    /**MediaController.MediaPlayerControl method**/
    @Override
    public boolean canSeekBackward() {
        return true;
    }

    /**MediaController.MediaPlayerControl method**/
    @Override
    public boolean canSeekForward() {
        return true;
    }

    /**MediaController.MediaPlayerControl method**/
    @Override
    public int getAudioSessionId() {
        return getId();
    }

    private void initializeMediaPlayer() {
        if (mMediaPlayer == null) {
            mMediaPlayer = new MediaPlayer();
            mMediaPlayer.setOnVideoSizeChangedListener(this);

            setSurfaceTextureListener(this);

            if (getSurfaceTexture() != null)
                mMediaPlayer.setSurface(new Surface(getSurfaceTexture()));
        } else
            reset();
    }

    private void initializeMediaController(){
        if(!isMediaControllerEnabled || mediaController != null)
            return;

        mediaController = new MediaController(getContext());

        mediaController.setMediaPlayer(this);

        mediaController.setAnchorView(this);

        mediaController.setEnabled(true);
    }

    private void scaleVideoSize(int videoWidth, int videoHeight) {
        if (videoWidth == 0 || videoHeight == 0)
            return;

        Matrix matrix = ScaleFactory.getScaleMatrix(getWidth(), getHeight(), getVideoWidth(), getVideoHeight(), mScalableType);

        if (matrix != null)
            setTransform(matrix);
    }

    private void setDataSource(@NonNull AssetFileDescriptor afd) throws IOException {
        setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength());

        afd.close();
    }

    public void enableMediaController(){
        isMediaControllerEnabled = true;
    }

    public void setRawData(@RawRes int id) throws IOException {
        AssetFileDescriptor afd = getResources().openRawResourceFd(id);

        setDataSource(afd);
    }

    public void setAssetData(@NonNull String assetName) throws IOException {
        AssetManager manager = getContext().getAssets();

        AssetFileDescriptor afd = manager.openFd(assetName);

        setDataSource(afd);
    }

    public void setDataSource(@NonNull String path) throws IOException {
        initializeMediaPlayer();

        mMediaPlayer.setDataSource(path);
    }

    public void setDataSource(@NonNull Context context, @NonNull Uri uri,
                              @Nullable Map<String, String> headers) throws IOException {
        initializeMediaPlayer();

        mMediaPlayer.setDataSource(context, uri, headers);
    }

    public void setDataSource(@NonNull Context context, @NonNull Uri uri) throws IOException {
        initializeMediaPlayer();

        mMediaPlayer.setDataSource(context, uri);
    }

    public void setDataSource(@NonNull FileDescriptor fd, long offset, long length)
            throws IOException {
        initializeMediaPlayer();

        mMediaPlayer.setDataSource(fd, offset, length);
    }

    public void setDataSource(@NonNull FileDescriptor fd) throws IOException {
        initializeMediaPlayer();

        mMediaPlayer.setDataSource(fd);
    }


    public void setScaleType(ScaleFactory.ScaleType scalableType) {
        mScalableType = scalableType;

        if(mMediaPlayer != null)
            scaleVideoSize(getVideoWidth(), getVideoHeight());
    }

    public void prepare(@Nullable MediaPlayer.OnPreparedListener listener)
            throws IOException, IllegalStateException {
        mMediaPlayer.setOnPreparedListener(listener);
        mMediaPlayer.prepare();

        initializeMediaController();
    }

    public void prepareAsync(@Nullable MediaPlayer.OnPreparedListener listener)
            throws IllegalStateException {
        mMediaPlayer.setOnPreparedListener(listener);
        mMediaPlayer.prepareAsync();

        initializeMediaController();
    }

    public void prepare() throws IOException, IllegalStateException {
        prepare(null);
    }

    public void prepareAsync() throws IllegalStateException {
        prepareAsync(null);
    }

    public void setOnErrorListener(@Nullable MediaPlayer.OnErrorListener listener) {
        mMediaPlayer.setOnErrorListener(listener);
    }

    public void setOnCompletionListener(@Nullable MediaPlayer.OnCompletionListener listener) {
        mMediaPlayer.setOnCompletionListener(listener);
    }

    public void setOnInfoListener(@Nullable MediaPlayer.OnInfoListener listener) {
        mMediaPlayer.setOnInfoListener(listener);
    }

    public int getVideoHeight() {
        return mMediaPlayer.getVideoHeight();
    }

    public int getVideoWidth() {
        return mMediaPlayer.getVideoWidth();
    }

    public MediaController getMediaController(){
        return mediaController;
    }

    public boolean isLooping() {
        return mMediaPlayer.isLooping();
    }

    public void setLooping(boolean looping) {
        mMediaPlayer.setLooping(looping);
    }

    public void setVolume(float leftVolume, float rightVolume) {
        mMediaPlayer.setVolume(leftVolume, rightVolume);
    }

    public void stop() {
        mMediaPlayer.stop();
    }

    public void reset() {
        mMediaPlayer.reset();
    }

    public void release() {
        reset();

        mMediaPlayer.release();

        mMediaPlayer = null;
    }
}
