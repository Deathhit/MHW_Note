package tw.com.deathhit.mhw_note.core;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**Fragment class that provides the basic functionality.**/
public abstract class BaseFragment extends Fragment {
    /**Force to implement onCreateView() since it is the common case.**/
    @Override
    public abstract View onCreateView(@NonNull LayoutInflater inflater,@Nullable ViewGroup container, @Nullable Bundle savedInstanceState);

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        View view = getView();

        if(view != null)
            onBindView(view, savedInstanceState);
    }

    /**FragmentActivity will invoke this method from its current BaseFragment. Return true if you want to consume the event.**/
    public boolean onBackPressed(){
        return false;
    }

    /**Bind view with data after it is created.**/
    public abstract void onBindView(@NonNull View view, @Nullable Bundle savedInstanceState);
}
