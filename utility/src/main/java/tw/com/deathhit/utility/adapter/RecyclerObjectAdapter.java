package tw.com.deathhit.utility.adapter;

import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public abstract class RecyclerObjectAdapter<T> extends RecyclerView.Adapter{
    protected List<T> items = new ArrayList<>();

    @Override
    public int getItemCount() {
        return items.size();
    }

    public List<T> getItems(){
        return items;
    }
}
