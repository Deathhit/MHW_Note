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

import java.util.ArrayList;
import java.util.Collections;

import tw.com.deathhit.Constants;
import tw.com.deathhit.R;
import tw.com.deathhit.adapters.recycler_view.DataAdapter;
import tw.com.deathhit.utils.NoScrollingLinearLayoutManager;

public final class JewelFragment extends BaseFragment {
    private static final int ID_DESCRIPTION_BLOCK = R.id.block;

    private static final int ID_RECYCLER_VIEW = R.id.recyclerView;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        if (getArguments() == null)
            return null;

        String path = getArguments().getString(Constants.ARGUMENT_PATH, null);

        View view = inflater.inflate(R.layout.fragment_detail, container, false);

        //Set up description
        View block = view.findViewById(ID_DESCRIPTION_BLOCK);

        ImageView imageView = block.findViewById(R.id.imageView);

        Glide.with(imageView).load(getResources().getDrawable(R.drawable.ic_jewel)).into(imageView);

        String text = getString(R.string.jewel) + " : " + dataHandler.getKey(path);

        TextView textView = block.findViewById(R.id.textView);

        textView.setText(text);

        String value = dataHandler.getValue(path + "/AlchemyOnly");

        if(value != null) {
            text = getString(R.string.alchemy_only) + " : ";

            if (value.equals("false"))
                text = text + getString(R.string.no);
            else
                text = text + getString(R.string.yes);
        }

        text = text + "\n" + getString(R.string.slot) + " : " + dataHandler.getValue(path + "/Hole");

        textView = block.findViewById(R.id.textView2);

        textView.setText(text);

        //Configure recycler view
        RecyclerView recyclerView = view.findViewById(ID_RECYCLER_VIEW);

        recyclerView.setLayoutManager(new NoScrollingLinearLayoutManager(getContext()));

        recyclerView.addItemDecoration(new DividerItemDecoration(container.getContext(), DividerItemDecoration.VERTICAL));

        recyclerView.setHasFixedSize(true);

        return view;
    }

    @Override
    public void onBindView(@NonNull View view, @Nullable Bundle savedInstanceState) {
        Bundle args = getArguments();

        assert args != null;
        String path = args.getString(Constants.ARGUMENT_PATH, null);

        //Set up recycler view
        ArrayList<String> temp = dataHandler.getChildrenPaths(path + "/Skill");

        RecyclerView recyclerView = view.findViewById(ID_RECYCLER_VIEW);

        if(temp.size() > 0) {
            ArrayList<String> items = new ArrayList<>(2);

            for (String s : temp)
                items.add("/Skill/" + dataHandler.getValue(s));

            Collections.sort(items);

            items.add(0, getString(R.string.skill));

            recyclerView.setAdapter(new DataAdapter(dataHandler, items));
        }else
            recyclerView.setVisibility(View.GONE);
    }
}
