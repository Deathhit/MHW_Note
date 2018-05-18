package tw.com.deathhit.core;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import java.lang.ref.WeakReference;
import java.util.Set;

/**Activity class that provides the basic functionality. Extend it to create your activity.**/
public abstract class BaseActivity extends AppCompatActivity {
    private static Toast toast;    //Global toast

    private static boolean restartOnNewProcess = true;

    private static boolean isNewProcess = true;

    private int containerId;    //This is the id used to represent container of fragment.

    @SuppressLint("ShowToast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        bindToPresenter();

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

        //Create and bind view
        onCreateView(savedInstanceState);

        if(savedInstanceState == null){
            new Handler(getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    onBindViewOnce();
                }
            });
        }
    }

    @Override
    protected void onStart(){
        super.onStart();

        bindToPresenter();
    }

    @Override
    protected void onResume(){
        super.onResume();

        bindToPresenter();
    }

    @Override
    public void onBackPressed() {
        if(getCurrentFragment() == null || !getCurrentFragment().onBackPressed())
            super.onBackPressed();
    }

    /**Bind activity to presenter. Do not do anything else in this method.**/
    private void bindToPresenter(){
        Presenter.activity = new WeakReference<>(this);
    }

    /**Clear fragment transaction history.**/
    protected final void clearTransactionHistory(){
        FragmentManager manager = getSupportFragmentManager();

        manager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
    }

    /**Restart application by launching launcher activity with flags Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK.**/
    protected final void restartApplication(){
        Intent intent = getBaseContext().getPackageManager()
                .getLaunchIntentForPackage(getBaseContext().getPackageName());

        if(intent == null)
            return;

        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP| Intent.FLAG_ACTIVITY_NEW_TASK);

        finish();

        startActivity(intent);
    }

    /**Get the container id assigned by setFragmentContainer().**/
    protected int getContainerId(){
        return containerId;
    }

    /**Get the current fragment shown on the container.**/
    protected BaseFragment getCurrentFragment(){
        return (BaseFragment)getSupportFragmentManager().findFragmentById(containerId);
    }

    /**Replace the content of the container set by setFragmentContainer(). Use fragment.getClass().getName() as fragment tag.**/
    protected void setFragment(@NonNull BaseFragment fragment, boolean addToBackStack){
        setFragment(fragment, getContainerId(), addToBackStack);
    }

    /**Replace the content of target container with another fragment. You can add side effects by overriding this method.
     * Use fragment.getClass().getName() as fragment tag.**/
    protected void setFragment(@NonNull BaseFragment fragment, int containerId, boolean addToBackStack){
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        transaction.replace(containerId, fragment, fragment.getClass().getName());
        if(addToBackStack)
            transaction.addToBackStack(null);

        transaction.commit();
    }

    /**Provide a container for setFragment() method.**/
    protected void setFragmentContainer(int id){
        this.containerId = id;
    }

    /**If restartOnNewProcess is true, application will restart from the launcher activity when it needs to get back from a dead process.
     * The default value of restartOnNewProcess is true. If the activity is already the launcher activity, this will do nothing.**/
    protected void setRestartApplicationOnNewProcess(boolean restartOnNewProcess){
        BaseActivity.restartOnNewProcess = restartOnNewProcess;
    }

    /**Display message with a short toast.**/
    protected void toast(CharSequence message){
        toast(message, Toast.LENGTH_SHORT);
    }

    /**Display message with toast. Override this method to make effects.**/
    protected void toast(CharSequence message, int duration){
        toast.setText(message);
        toast.setDuration(duration);
        toast.show();
    }

    /**Called in onCreate() to create view.**/
    protected abstract void onCreateView(Bundle savedInstanceState);

    /**Bind view when savedInstanceState is null. This method is called after onResume().**/
    protected abstract void onBindViewOnce();

    /**This generic method is used to provide unique functionality of the activity.
     * You can avoid coupling activity and fragment by implementing this method.**/
    protected abstract Object request(int requestType, @Nullable Object... args);

    /**The global activity presenter, declare the activity methods that you want to share with fragments and views here.**/
    public static final class Presenter{
        private static WeakReference<BaseActivity> activity;

        public static void clearTransactionHistory(){
            activity.get().clearTransactionHistory();
        }

        public static void finish(){
            activity.get().finish();
        }

        public static View findViewById(int id){
            return activity.get().findViewById(id);
        }

        public static Intent getIntent(){
            return activity.get().getIntent();
        }

        public static void onBackPressed(){
            activity.get().onBackPressed();
        }

        public static void setFragment(@NonNull BaseFragment fragment, boolean addToBackStack){
            activity.get().setFragment(fragment, addToBackStack);
        }

        public static void setFragment(@NonNull BaseFragment fragment, int containerId, boolean addToBackStack){
            activity.get().setFragment(fragment, containerId, addToBackStack);
        }

        public static void setResult(int resultCode, Intent intent){
            activity.get().setResult(resultCode, intent);
        }

        public static void startActivity(Intent intent){
            activity.get().startActivity(intent);
        }

        public static void startActivityForResult(Intent intent, int requestCode){
            activity.get().startActivityForResult(intent, requestCode);
        }

        public static void restartApplication(){
            activity.get().restartApplication();
        }

        public static Object request(int requestType, @Nullable Object... args){
            return activity.get().request(requestType, args);
        }

        public static void toast(CharSequence message){
            activity.get().toast(message);
        }

        public static void toast(CharSequence message, int duration){
            activity.get().toast(message, duration);
        }

        private Presenter(){

        }
    }
}
