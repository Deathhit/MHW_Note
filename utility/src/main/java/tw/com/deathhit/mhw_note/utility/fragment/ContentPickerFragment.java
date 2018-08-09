package tw.com.deathhit.mhw_note.utility.fragment;

import android.Manifest;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import tw.com.deathhit.mhw_note.core.ApplicationHelper;
import tw.com.deathhit.mhw_note.core.BaseFragment;
import tw.com.deathhit.utility.BuildConfig;

import static android.app.Activity.RESULT_OK;

/**Fragment designed to handle picking media content. You can pick either existing or fresh content.
 * android.permission.WRITE_EXTERNAL_STORAGE is required to work correctly.**/
public abstract class ContentPickerFragment extends BaseFragment {
    public enum Type{
        FILE,
        AUDIO_FILE,
        IMAGE_FILE,
        VIDEO_FILE,
        RECORD_SOUND,
        RECORD_VIDEO,
        TAKE_PHOTO
    }

    private static final String FILE_TYPE = "file/*";
    private static final String IMAGE_TYPE = "image/*";
    private static final String AUDIO_TYPE = "audio/*";
    private static final String VIDEO_TYPE = "video/*";

    //Properties used to create a file
    private static final String AUDIO_PREFIX = "AUD_";
    private static final String IMAGE_PREFIX = "IMG_";
    private static final String VIDEO_PREFIX = "VID_";

    private static final String AUDIO_SUFFIX = ".3ga";
    private static final String IMAGE_SUFFIX = ".jpg";
    private static final String VIDEO_SUFFIX = ".mp4";

    private static final String ARGS = ContentPickerFragment.class.getName() + ":args";
    private static final String TYPE = ContentPickerFragment.class.getName() + ":type";
    private static final String URI = ContentPickerFragment.class.getName() + ":uri";

    //Temporary references
    private Bundle args;
    private Type type;
    private Uri uri;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(savedInstanceState != null){
            args = savedInstanceState.getBundle(ARGS);
            type = (Type)savedInstanceState.getSerializable(TYPE);

            String uriString = savedInstanceState.getString(URI);

            if(uriString != null)
                uri = Uri.parse(savedInstanceState.getString(URI));
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        ArrayList<Uri> uriList = null;

        switch(resultCode) {
            case(RESULT_OK):
                switch (type){
                    case AUDIO_FILE:
                    case FILE:
                    case IMAGE_FILE:
                    case VIDEO_FILE:
                        if(data.getClipData() != null) {
                            ClipData clipData = data.getClipData();

                            uriList = new ArrayList<>(clipData.getItemCount());

                            for (int i = 0; i < clipData.getItemCount(); i++)
                                uriList.add(clipData.getItemAt(i).getUri());
                        }else{
                            uriList = new ArrayList<>(1);

                            uriList.add(data.getData());
                        }
                        break;
                    case RECORD_SOUND:
                    case RECORD_VIDEO:
                    case TAKE_PHOTO:
                        assert getContext() != null;
                        addMediaToGallery(getContext());

                        uriList = new ArrayList<>(1);

                        uriList.add(uri);
                        break;
                }

                onContentPicked(requestCode, uriList, args);
                break;
        }

        args = null;
        type = null;
        uri = null;
    }

    /**If activity is going to be recreated, save states to bundle.**/
    @Override
    public void onSaveInstanceState(@NonNull Bundle savedInstanceState) {
        savedInstanceState.putBundle(ARGS, args);
        savedInstanceState.putSerializable(TYPE, type);

        if(uri != null)
            savedInstanceState.putString(URI, uri.toString());

        super.onSaveInstanceState(savedInstanceState);
    }

    /**Start gallery activity to pick up desired contents. Triggers onContentPicked() after selection. This will launch picker activity according to the given request code.
     * You need to override onContentPicked() method to work with selected result. android.permission.WRITE_EXTERNAL_STORAGE is required before calling this method.**/
    public void startContentPicker(@NonNull Type type, int requestCode){
        startContentPicker(type, requestCode, null);
    }

    /**Start gallery activity to pick up desired contents. Triggers onContentPicked() after selection. This will launch picker activity according to the given request code.
     * You need to override onContentPicked() method to work with selected result. android.permission.WRITE_EXTERNAL_STORAGE is required before calling this method.**/
    public void startContentPicker(@NonNull Type type, int requestCode, @Nullable Bundle args) {
        assert getContext() != null;
        if(ContextCompat.checkSelfPermission(getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT < 23)
                throw new IllegalStateException("Permission WRITE_EXTERNAL_STORAGE is not granted. Please add permission to the manifest file.");
            else {
                throw new IllegalStateException("Permission WRITE_EXTERNAL_STORAGE is not granted. Please ask for permission before invoking this method.");
            }
        }

        this.args = args;
        this.type = type;

        Intent intent;

        switch (type){
            case RECORD_SOUND:
                intent = new Intent(MediaStore.Audio.Media.RECORD_SOUND_ACTION);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, createFile(getContext()));
                break;
            case RECORD_VIDEO:
                intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, createFile(getContext()));
                intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
                break;
            case TAKE_PHOTO:
                intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, createFile(getContext()));
                break;
            default:
                intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);

                switch (type){
                    case AUDIO_FILE:
                        intent.setType(AUDIO_TYPE);
                        break;
                    case FILE :
                        intent.setType(FILE_TYPE);
                        break;
                    case IMAGE_FILE:
                        intent.setType(IMAGE_TYPE);
                        break;
                    case VIDEO_FILE:
                        intent.setType(VIDEO_TYPE);
                        break;
                }

                break;
        }

        intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);

        assert getActivity() != null;
        if(intent.resolveActivity(getActivity().getPackageManager()) != null)
            startActivityForResult(intent, requestCode);
    }

    /**Create empty media file as storage.**/
    private Uri createFile(Context context){
        String prefix = null;
        String suffix = null;

        switch (type){
            case RECORD_SOUND:
                prefix = AUDIO_PREFIX;
                suffix = AUDIO_SUFFIX;
                break;
            case RECORD_VIDEO:
                prefix = VIDEO_PREFIX;
                suffix = VIDEO_SUFFIX;
                break;
            case TAKE_PHOTO:
                prefix = IMAGE_PREFIX;
                suffix = IMAGE_SUFFIX;
                break;
        }

        String appName = ApplicationHelper.getAppLabel(context);

        assert appName != null;
        File storageDirectory = new File(Environment.getExternalStorageDirectory(), appName);

        if(storageDirectory.exists() || storageDirectory.mkdir()) {
            File file = null;

            prefix = prefix + SimpleDateFormat.getDateTimeInstance().format(new Date()) + "_";

            try {
                file = File.createTempFile(
                        prefix,
                        suffix,
                        storageDirectory);
            }catch (IOException e){
                e.printStackTrace();
            }

            if(file != null)
                return uri = FileProvider.getUriForFile(context, context.getPackageName() + "." + BuildConfig.APPLICATION_ID + ".provider", file);
        }

        throw new IllegalStateException("Unable to create file.");
    }

    /**Add picked media content to gallery.**/
    private void addMediaToGallery(Context context) {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);

        mediaScanIntent.setData(uri);

        context.sendBroadcast(mediaScanIntent);
    }

    /**This method is called after the selection triggered by startContentPicker().
     * You need to override this method to define custom actions.**/
    protected abstract void onContentPicked(int requestCode, List<Uri> data, @Nullable Bundle bundle);
}
