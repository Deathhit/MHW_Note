package tw.com.deathhit.core;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

public abstract class FragmentActivity extends BaseActivity{
    private int containerId;    //This is the id used to represent container of fragment

    @Override
    public void onBackPressed() {
        if(getCurrentFragment() == null || !getCurrentFragment().onBackPressed())
            super.onBackPressed();
    }

    /**Clear fragment transaction history.**/
    protected void clearTransactionHistory(){
        FragmentManager manager = getSupportFragmentManager();

        manager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
    }

    /**Get the container id assigned by setFragmentContainer().**/
    protected int getContainerId(){
        return containerId;
    }

    /**Get the current fragment shown on the container.**/
    @Nullable
    protected BaseFragment getCurrentFragment(){
        return (BaseFragment)getSupportFragmentManager().findFragmentById(containerId);
    }

    /**Replace the content of the container set by setFragmentContainer(). Throws exception if containerId is not set.
     * Use fragment.getClass().getName() as fragment tag.**/
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

    public static class Presenter extends BaseActivity.Presenter{
        public static void clearTransactionHistory(){
            ((FragmentActivity)activity.get()).clearTransactionHistory();
        }

        public static void setFragment(@NonNull BaseFragment fragment, boolean addToBackStack){
            ((FragmentActivity)activity.get()).setFragment(fragment, addToBackStack);
        }

        public static void setFragment(@NonNull BaseFragment fragment, int containerId, boolean addToBackStack){
            ((FragmentActivity)activity.get()).setFragment(fragment, containerId, addToBackStack);
        }

    }
}
