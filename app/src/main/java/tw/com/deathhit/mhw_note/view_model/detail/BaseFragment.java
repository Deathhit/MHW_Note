package tw.com.deathhit.mhw_note.view_model.detail;

import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import tw.com.deathhit.mhw_note.Constants;
import tw.com.deathhit.mhw_note.DataHandler;
import tw.com.deathhit.mhw_note.R;

abstract class BaseFragment extends tw.com.deathhit.mhw_note.core.BaseFragment {
    private static final int ID_RECYCLER_VIEW = R.id.recyclerView;

    protected DataHandler dataHandler;

    @CallSuper
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        dataHandler = new DataHandler(inflater.getContext(), Constants.STORAGE_DATABASE);

        return null;
    }

    @Override
    public void onDestroyView() {
        View view = getView();

        assert view != null;
        RecyclerView recyclerView = view.findViewById(ID_RECYCLER_VIEW);

        recyclerView.setAdapter(null);

        super.onDestroyView();
    }
}
