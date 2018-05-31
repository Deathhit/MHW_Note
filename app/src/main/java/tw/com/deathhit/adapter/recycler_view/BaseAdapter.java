package tw.com.deathhit.adapter.recycler_view;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import tw.com.deathhit.Constants;
import tw.com.deathhit.R;
import tw.com.deathhit.DataHandler;
import tw.com.deathhit.utility.adapter.RecyclerObjectAdapter;

import static tw.com.deathhit.Constants.OPERATOR_PATH;

abstract class BaseAdapter extends RecyclerObjectAdapter<String>{
    static final int TYPE_DATA = 0;
    static final int TYPE_CATEGORY = 1;

    private static final String OPERATOR_EXTRA_TEXT = Constants.OPERATOR_EXTRA_TEXT;
    
    protected DataHandler dataHandler;

    BaseAdapter(DataHandler dataHandler, List<String> items){
        this.dataHandler = dataHandler;

        this.items = items;
    }

    @Override
    public int getItemViewType(int position) {
        if(getPath(position).contains(OPERATOR_PATH))
            return TYPE_DATA;
        else
            return TYPE_CATEGORY;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = null;

        switch (viewType){
            case TYPE_DATA :
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_data, parent, false);
                break;
            case TYPE_CATEGORY :
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_category, parent, false);
                break;
        }

        assert view != null;
        return new RecyclerView.ViewHolder(view) {
            @Override
            public String toString() {
                return super.toString();
            }
        };
    }

    String getPath(int position){
        if(items.get(position) == null)
            return null;
        else
            return items.get(position).split(OPERATOR_EXTRA_TEXT)[0];
    }

    String getExtraText(int position){
        String[] temp = items.get(position).split(OPERATOR_EXTRA_TEXT);

        if(temp.length > 1)
            return items.get(position).split(OPERATOR_EXTRA_TEXT)[1];
        else
            return "";
    }
}
