package tw.com.deathhit.mhw_note.core;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import java.lang.ref.WeakReference;
import java.util.Set;

/**Activity class that provides the basic functionality. Extend it to create your activity.**/
public abstract class BaseActivity extends AppCompatActivity {
    private static WeakReference<BaseActivity> activity;

    private static Toast toast;    //Global toast

    private static boolean restartOnNewProcess = true;

    private static boolean isNewProcess = true;

    public static BaseActivity get(){
        return activity.get();
    }

    @SuppressLint("ShowToast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Bind current activity to presenter
        activity = new WeakReference<>(this);

        if(toast == null)
            toast = Toast.makeText(getApplicationContext(), "", Toast.LENGTH_SHORT);

        //Check if OS launched a new process for the application
        if(restartOnNewProcess && isNewProcess) {
            Intent intent = getIntent();

            String action = intent.getAction();

            Set<String> categories = intent.getCategories();

            //Check if current activity is the launcher activity
            if(action != null && categories != null && action.equals(Intent.ACTION_MAIN) && categories.contains(Intent.CATEGORY_LAUNCHER))
                isNewProcess = false;

            //If process was killed, restart the application
            if(isNewProcess)
                restartApplication();
        }
    }

    @Override
    protected void onStart(){
        super.onStart();

        //Bind current activity to presenter
        activity = new WeakReference<>(this);
    }

    @Override
    protected void onResume(){
        super.onResume();

        //Bind current activity to presenter
        activity = new WeakReference<>(this);
    }

    /**Restart application by launching launcher activity with flags Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK.**/
    public void restartApplication(){
        Intent intent = getBaseContext().getPackageManager()
                .getLaunchIntentForPackage(getBaseContext().getPackageName());

        if(intent == null)
            return;

        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP| Intent.FLAG_ACTIVITY_NEW_TASK);

        finish();

        startActivity(intent);
    }

    /**This generic method is used to provide unique functionality of the activity.
     * You can avoid coupling activity and fragment by overriding this method.**/
    public Object request(int requestCode, @Nullable Object... args){
        return null;
    }

    /**If restartOnNewProcess is true, application will restart from the launcher activity when it needs to get back from a dead process.
     * The default value of restartOnNewProcess is true. If the activity is already the launcher activity, this will do nothing.**/
    public void setRestartApplicationOnNewProcess(boolean restartOnNewProcess){
        BaseActivity.restartOnNewProcess = restartOnNewProcess;
    }

    /**Display message with a short toast.**/
    public void toast(CharSequence message){
        toast(message, Toast.LENGTH_SHORT);
    }

    /**Display message with toast. Override this method to make effects.**/
    public void toast(CharSequence message, int duration){
        toast.setText(message);
        toast.setDuration(duration);
        toast.show();
    }
}
