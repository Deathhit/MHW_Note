package tw.com.deathhit.components.detail;

import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.Collections;

import tw.com.deathhit.Constants;
import tw.com.deathhit.R;
import tw.com.deathhit.adapters.recycler_view.TextAdapter;
import tw.com.deathhit.utils.NoScrollingLinearLayoutManager;

public final class SkillFragment extends BaseFragment {
    private static final int ID_DESCRIPTION_BLOCK = R.id.block;

    private static final int ID_RECYCLER_VIEW = R.id.recyclerView;

    @Override
    public View onCreateViewOnce(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if(getArguments() == null)
            return null;

        String path = getArguments().getString(Constants.ARGUMENT_PATH, null);

        View view = inflater.inflate(R.layout.fragment_detail, container, false);

        //Set up description
        View block = view.findViewById(ID_DESCRIPTION_BLOCK);

        ImageView imageView = block.findViewById(R.id.imageView);

        Glide.with(imageView).load(getResources().getDrawable(R.drawable.ic_book)).into(imageView);

        String text = getString(R.string.skill) + " : " + dataHandler.getKey(path);

        TextView textView = block.findViewById(R.id.textView);

        textView.setText(text);

        text = dataHandler.getValue(path + "/Description");

        textView = block.findViewById(R.id.textView2);

        textView.setText(text);

        //Set up recycler view
        int count = (int) dataHandler.getChildrenCount(path + "/Level");

        if(count <= 0)
            return view;

        ArrayList<String> items = dataHandler.getChildrenPaths(path + "/Level");

        Collections.sort(items);

        items.add(0, getString(R.string.level_effect));

        RecyclerView recyclerView = view.findViewById(ID_RECYCLER_VIEW);

        recyclerView.setLayoutManager(new NoScrollingLinearLayoutManager(getContext()));

        recyclerView.addItemDecoration(new DividerItemDecoration(container.getContext(), DividerItemDecoration.VERTICAL));

        recyclerView.setHasFixedSize(true);

        recyclerView.setAdapter(new TextAdapter(dataHandler, items));

        return view;
    }
}
