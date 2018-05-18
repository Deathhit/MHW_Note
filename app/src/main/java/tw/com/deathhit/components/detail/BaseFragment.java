package tw.com.deathhit.components.detail;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import tw.com.deathhit.Constants;
import tw.com.deathhit.DataHandler;
import tw.com.deathhit.R;

abstract class BaseFragment extends tw.com.deathhit.core.BaseFragment{
    private static final int ID_RECYCLER_VIEW = R.id.recyclerView;

    protected DataHandler dataHandler;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        dataHandler = new DataHandler(inflater.getContext(), Constants.STORAGE_DATABASE);

        return super.onCreateView(inflater, container, savedInstanceState);
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
