package tw.com.deathhit.mhw_note.utility.adapter;

import android.widget.BaseAdapter;

import java.util.ArrayList;
import java.util.List;

public abstract class ObjectAdapter<T> extends BaseAdapter {
    protected List<T> items = new ArrayList<>();

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public List<T> getItems(){
        return items;
    }
}
