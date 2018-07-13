package tw.com.deathhit.mhw_note.view_model.adapter.recycler_view;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;

import tw.com.deathhit.mhw_note.DataHandler;
import tw.com.deathhit.mhw_note.R;
import tw.com.deathhit.mhw_note.core.BaseActivity;
import tw.com.deathhit.mhw_note.utility.adapter.RecyclerObjectAdapter;
import tw.com.deathhit.mhw_note.utils.ImageActivity;

public final class ImageAdapter extends RecyclerObjectAdapter<String> implements View.OnClickListener{
    private DataHandler dataHandler;

    public ImageAdapter(DataHandler dataHandler, ArrayList<String> items){
        this.dataHandler = dataHandler;

        this.items = items;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_image, parent, false);

        view.setOnClickListener(this);

        return new RecyclerView.ViewHolder(view) {
            @Override
            public String toString() {
                return super.toString();
            }
        };
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        View view = holder.itemView;

        String path = dataHandler.getValue(items.get(position));

        view.setTag(path);

        ImageView imageView = holder.itemView.findViewById(R.id.imageView);

        Glide.with(imageView).load(path).apply(new RequestOptions().placeholder(R.drawable.ic_image_not_available).error(R.drawable.ic_image_not_available)).into(imageView);
    }

    @Override
    public void onClick(View view) {
        Intent intent = new Intent(view.getContext(), ImageActivity.class);

        intent.setData(Uri.parse((String)view.getTag()));

        BaseActivity.get().startActivity(intent);
    }
}
