package tw.com.deathhit.mhw_note.view_model.adapter.recycler_view;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import tw.com.deathhit.mhw_note.DataHandler;
import tw.com.deathhit.mhw_note.R;

public final class TextAdapter extends BaseAdapter {
    public TextAdapter(DataHandler dataHandler, List<String> items) {
        super(dataHandler, items);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = null;

        switch (viewType){
            case TYPE_DATA :
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_text, parent, false);
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

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        String path = getPath(position);
        
        View view = holder.itemView;

        view.setTag(position);

        TextView textView = view.findViewById(R.id.textView);

        switch (getItemViewType(position)) {
            case TYPE_DATA :
                textView.setText(dataHandler.getValue(path));
                break;
            case TYPE_CATEGORY :
                textView.setText(path);
        }
    }
}
