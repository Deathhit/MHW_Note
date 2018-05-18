package tw.com.deathhit.core;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Environment;

/**Provides application info needed for the framework.**/
public class ApplicationInfo extends android.content.pm.ApplicationInfo{
    private static final String SHARED_PREFERENCES_NAME = ApplicationInfo.class.getName();

    //Preference keys
    private static final String KEY_PREVIOUS_VERSION = "previousVersion";

    /**Get label of the application.**/
    public static String getAppLabel(Context context) {
        PackageManager packageManager = context.getPackageManager();
        android.content.pm.ApplicationInfo applicationInfo = null;

        try {
            applicationInfo = packageManager.getApplicationInfo(context.getApplicationInfo().packageName, 0);
        } catch (final PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        return (String) (applicationInfo != null ? packageManager.getApplicationLabel(applicationInfo) : null);
    }

    /**Get SharedPreferences named by getAppLabel().**/
    public static SharedPreferences getSharedPreferences(Context context){
        return context.getSharedPreferences(getAppLabel(context), Context.MODE_PRIVATE);
    }

    /**Check if external storage is writable.**/
    public static boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();

        return Environment.MEDIA_MOUNTED.equals(state);
    }

    /**Check if external storage is readable.**/
    public static boolean isExternalStorageReadable() {
        String state = Environment.getExternalStorageState();

        return  (Environment.MEDIA_MOUNTED.equals(state) ||
                Environment.MEDIA_MOUNTED_READ_ONLY.equals(state));
    }

    /**Check if current build is new to local device from the last time the method was invoked.**/
    public static boolean isNewVersion(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);

        int currentVersion = -1;

        try {
            PackageInfo packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);

            currentVersion = packageInfo.versionCode;

        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        int savedVersion = preferences.getInt(KEY_PREVIOUS_VERSION, -1);

        boolean isNewVersion = currentVersion != savedVersion;

        if (isNewVersion) {
            SharedPreferences.Editor editor = context.getSharedPreferences(SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE).edit();

            //Application is no longer on its first run
            editor.putInt(KEY_PREVIOUS_VERSION, currentVersion);

            editor.commit();
        }

        return isNewVersion;
    }
}
