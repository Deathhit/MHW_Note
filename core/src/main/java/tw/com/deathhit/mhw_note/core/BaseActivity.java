package tw.com.deathhit.mhw_note.core;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import java.lang.ref.WeakReference;

/**Activity class that provides the basic functionality. Extend it to create your activity.**/
public abstract class BaseActivity extends AppCompatActivity {
    private static WeakReference<BaseActivity> activity;

    private static Toast toast;    //Global toast

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
