package tw.com.deathhit.core;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.lang.ref.SoftReference;

/**Fragment class that provides the basic functionality. Extend it to create your fragment. Do not
 * keep any strong reference to view objects as property. This will kill the effect of view recycling.**/
public abstract class BaseFragment extends Fragment {
    private SoftReference<View> view;
    
    private boolean isViewCreated = false; //The value is true if onCreateView() returns the result of onCreateViewOnce() instead of view

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if(view != null && view.get() != null) {
            isViewCreated = false;

            return view.get();
        } else {
            isViewCreated = true;

            view = new SoftReference<>(onCreateViewOnce(inflater, container, savedInstanceState));

            return view.get();
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if(isViewCreated)
            onViewCreated(savedInstanceState);
    }

    /**This is a life cycle method called within onActivityCreated(), and it is triggered only when onCreateViewOnce() is called to create view for fragment.**/
    protected void onViewCreated(Bundle savedInstanceStates){

    }

    /**Tell fragment to recreate view.**/
    public void notifyToRefresh(){
        view = null;
    }

    /**BaseActivity will invoke this method from its current BaseFragment. Return true if you want to consume the event.**/
    public boolean onBackPressed(){
        return false;
    }

    /**Create view only one time to reuse created view.**/
    public abstract View onCreateViewOnce(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState);
}
