package tw.com.deathhit.mhw_note.adapter;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import tw.com.deathhit.mhw_note.DataHandler;
import tw.com.deathhit.mhw_note.R;
import tw.com.deathhit.mhw_note.utility.adapter.ObjectAdapter;

public final class SpinnerAdapter extends ObjectAdapter<String> {
    private DataHandler dataHandler;

    public SpinnerAdapter(DataHandler dataHandler, ArrayList<String> items){
        this.dataHandler = dataHandler;
        this.items = items;
    }

    @SuppressLint("InflateParams")
    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if(view == null)
            view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_drop_down_text, null);

        TextView textView = view.findViewById(R.id.textView);

        String item = items.get(i);

        if(item != null)
            textView.setText(dataHandler.getKey(item));
        else
            textView.setText(R.string.not_equipped);

        return view;
    }
}
