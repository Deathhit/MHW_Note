package tw.com.deathhit.adapter.recycler_view;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

import tw.com.deathhit.Constants;
import tw.com.deathhit.DataHandler;
import tw.com.deathhit.R;
import tw.com.deathhit.core.BaseActivity;

public final class CalculatorAdapter extends BaseAdapter implements View.OnClickListener{
    public CalculatorAdapter(DataHandler dataHandler, List<String> items) {
        super(dataHandler, items);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        String path = getPath(position);

        View view = holder.itemView;

        view.setTag(position);

        TextView textView = view.findViewById(R.id.textView);

        switch (getItemViewType(position)) {
            case TYPE_DATA :
                ImageView imageView = view.findViewById(R.id.imageView);

                Glide.with(imageView).load(R.drawable.ic_ticket).into(imageView);

                textView.setText(dataHandler.getKey(path));

                view.setOnClickListener(this);
                break;
            case TYPE_CATEGORY :
                textView.setText(getPath(position));
        }
    }

    @Override
    public void onClick(View view) {
        int position = (int)view.getTag();

        BaseActivity.Presenter.request(Constants.REQUEST_CALCULATOR_DETAIL, getPath(position));
    }
}
