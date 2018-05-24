package tw.com.deathhit.utility.fragment;

import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import tw.com.deathhit.core.ApplicationInfo;
import tw.com.deathhit.core.BaseFragment;

import static android.app.Activity.RESULT_OK;

/**Fragment designed to handle picking media content. You can pick either existing or fresh content.**/
public abstract class ContentPickerFragment extends BaseFragment {
    public static final int AUDIO_REQUEST_CODE = 0;
    public static final int FILE_REQUEST_CODE = 1;
    public static final int IMAGE_REQUEST_CODE = 2;
    public static final int VIDEO_REQUEST_CODE = 3;
    public static final int RECORD_SOUND_REQUEST_CODE = 4;
    public static final int RECORD_VIDEO_REQUEST_CODE = 5;
    public static final int TAKE_PHOTO_REQUEST_CODE = 6;

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

    private static Object[] args;   //Temporary reference for args

    private static Uri uri; //Temporary reference for file uri

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        ArrayList<Uri> uriList = null;

        switch(resultCode) {
            case(RESULT_OK):
                switch (requestCode){
                    case(AUDIO_REQUEST_CODE):
                    case(FILE_REQUEST_CODE):
                    case(IMAGE_REQUEST_CODE):
                    case(VIDEO_REQUEST_CODE):
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
                    case(RECORD_SOUND_REQUEST_CODE):
                    case(RECORD_VIDEO_REQUEST_CODE):
                    case(TAKE_PHOTO_REQUEST_CODE):
                        addMediaToGallery(getContext());

                        uriList = new ArrayList<>(1);

                        uriList.add(uri);
                        break;
                }

                onContentPicked(requestCode, uriList, args);
                break;
        }

        args = null;

        uri = null;
    }

    /**Create empty media file as storage.**/
    private static Uri createFile(Context context, int requestCode){
        String prefix = null;
        String suffix = null;

        switch (requestCode){
            case(RECORD_SOUND_REQUEST_CODE):
                prefix = AUDIO_PREFIX;
                suffix = AUDIO_SUFFIX;
                break;
            case(RECORD_VIDEO_REQUEST_CODE):
                prefix = VIDEO_PREFIX;
                suffix = VIDEO_SUFFIX;
                break;
            case(TAKE_PHOTO_REQUEST_CODE):
                prefix = IMAGE_PREFIX;
                suffix = IMAGE_SUFFIX;
                break;
        }

        File storageDirectory = new File(Environment.getExternalStorageDirectory(), ApplicationInfo.getAppLabel(context));

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
                return uri = Uri.fromFile(file);
        }

        return null;
    }

    /**Add picked media content to gallery.**/
    private static void addMediaToGallery(Context context) {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);

        mediaScanIntent.setData(uri);

        context.sendBroadcast(mediaScanIntent);
    }

    /**Start gallery activity to pick up desired contents. Triggers onContentPicked() after selection. This will launch picker activity according to the given request code.
     * You need to override onContentPicked() method to act on selection result.**/
    public void startContentPicker(int requestCode, @Nullable Object... args){
        Intent intent;

        ContentPickerFragment.args = args;

        switch (requestCode){
            case(AUDIO_REQUEST_CODE):
                intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType(AUDIO_TYPE);
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                break;
            case(FILE_REQUEST_CODE):
                intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType(FILE_TYPE);
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                break;
            case(IMAGE_REQUEST_CODE):
                intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType(IMAGE_TYPE);
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                break;
            case(VIDEO_REQUEST_CODE):
                intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType(VIDEO_TYPE);
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                break;
            case(RECORD_SOUND_REQUEST_CODE):
                intent = new Intent(MediaStore.Audio.Media.RECORD_SOUND_ACTION);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, createFile(getContext(), requestCode));
                break;
            case(RECORD_VIDEO_REQUEST_CODE):
                intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, createFile(getContext(), requestCode));
                intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
                break;
            case(TAKE_PHOTO_REQUEST_CODE):
                intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, createFile(getContext(), requestCode));
                break;
            default:
                return;
        }

        if(intent.resolveActivity(getActivity().getPackageManager()) != null)
            startActivityForResult(intent, requestCode);
    }

    /**This method is called after the selection triggered by startContentPicker().
     * You need to override this method to define custom actions.**/
    protected abstract void onContentPicked(int requestCode, List<Uri> data, @Nullable Object... args);
}
