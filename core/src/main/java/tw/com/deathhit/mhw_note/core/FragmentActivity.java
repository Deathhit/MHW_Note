package tw.com.deathhit.mhw_note.core;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

public abstract class FragmentActivity extends BaseActivity{
    public static FragmentActivity get() {
        return (FragmentActivity)BaseActivity.get();
    }

    private int containerId;    //This is the id used to represent container of fragment

    @Override
    public void onBackPressed() {
        if(getCurrentFragment() == null || !getCurrentFragment().onBackPressed())
            super.onBackPressed();
    }

    /**Clear fragment transaction history.**/
    public void clearTransactionHistory(){
        FragmentManager manager = getSupportFragmentManager();

        manager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
    }

    /**Get the container id assigned by setFragmentContainer().**/
    public int getContainerId(){
        return containerId;
    }

    /**Get the current fragment shown on the container.**/
    @Nullable
    public BaseFragment getCurrentFragment(){
        return (BaseFragment)getSupportFragmentManager().findFragmentById(containerId);
    }

    /**Replace the content of the container set by setFragmentContainer(). Throws exception if containerId is not set.
     * Use fragment.getClass().getName() as fragment tag.**/
    public FragmentTransaction setFragment(@NonNull BaseFragment fragment, boolean addToBackStack){
        return setFragment(fragment, getContainerId(), addToBackStack);
    }

    /**Replace the content of target container with another fragment. You can add side effects by overriding this method.
     * Use fragment.getClass().getName() as fragment tag.**/
    public FragmentTransaction setFragment(@NonNull BaseFragment fragment, int containerId, boolean addToBackStack){
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        transaction.replace(containerId, fragment, fragment.getClass().getName());
        if(addToBackStack)
            transaction.addToBackStack(null);

        return transaction;
    }

    /**Provide a container for setFragment() method.**/
    protected void setFragmentContainer(int id){
        this.containerId = id;
    }
}
