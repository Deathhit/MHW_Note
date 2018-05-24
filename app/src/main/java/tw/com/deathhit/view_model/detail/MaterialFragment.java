package tw.com.deathhit.view_model.detail;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;

import tw.com.deathhit.Constants;
import tw.com.deathhit.adapter.recycler_view.TextAdapter;
import tw.com.deathhit.R;
import tw.com.deathhit.utils.NoScrollingLinearLayoutManager;

public final class MaterialFragment extends BaseFragment {
    private static final int ID_INTRODUCTION_BLOCK = R.id.block;

    private static final int ID_RECYCLER_VIEW = R.id.recyclerView;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        if(getArguments() == null)
            return null;

        String path = getArguments().getString(Constants.ARGUMENT_PATH, null);

        View view = inflater.inflate(R.layout.fragment_detail, container, false);

        //Set up introduction
        View block = view.findViewById(ID_INTRODUCTION_BLOCK);

        ImageView imageView = block.findViewById(R.id.imageView);

        String url = dataHandler.getValue(path + "/Image");

        if(url != null && url.length() > 0)
            Glide.with(imageView).load(url).apply(new RequestOptions().placeholder(R.drawable.ic_unknown_item).error(R.drawable.ic_unknown_item)).into(imageView);
        else
            Glide.with(imageView).load(R.drawable.ic_unknown_item).into(imageView);

        String price = dataHandler.getValue(path + "/Prize");
        price = price != null ? price : "???";

        String rare = dataHandler.getValue(path + "/Rare");
        rare = rare != null ? rare : "???";

        String text = getResources().getString(R.string.price) + " : " + price + "\n" +
                getResources().getString(R.string.rarity) + " : " + rare;

        TextView textView = block.findViewById(R.id.textView2);

        textView.setText(text);

        text = getResources().getString(R.string.material) + " : " + dataHandler.getKey(path);

        textView = block.findViewById(R.id.textView);

        textView.setText(text);

        //Configure recycler view
        RecyclerView recyclerView = view.findViewById(ID_RECYCLER_VIEW);

        recyclerView.setLayoutManager(new NoScrollingLinearLayoutManager(getContext()));

        recyclerView.addItemDecoration(new DividerItemDecoration(inflater.getContext(), DividerItemDecoration.VERTICAL));

        recyclerView.setHasFixedSize(true);

        return view;
    }

    @Override
    public void onBindView(@NonNull View view, @Nullable Bundle savedInstanceState) {
        Bundle args = getArguments();

        assert args != null;
        String path = args.getString(Constants.ARGUMENT_PATH, null);

        //Set up recycler view
        ArrayList<String> items = dataHandler.getSiblingsPaths(path + "/Info");

        RecyclerView recyclerView = view.findViewById(ID_RECYCLER_VIEW);

        if(items.size() > 0) {
            for (int i = 0; i < items.size(); i++) {
                String item = items.get(i);

                String parentKey = dataHandler.getParentKey(item);

                assert parentKey != null;
                if (parentKey.equals("Info")) {
                    items.remove(i);
                    items.add(i, dataHandler.getKey(item));
                }
            }
            recyclerView.setAdapter(new TextAdapter(dataHandler, items));
        }else
            recyclerView.setVisibility(View.GONE);
    }
}
