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

public final class GuardStoneFragment extends BaseFragment {
    private static final int ID_DESCRIPTION_BLOCK = R.id.block;

    private static final int ID_RECYCLER_VIEW = R.id.recyclerView;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        if(getArguments() == null)
            return null;

        String path = getArguments().getString(Constants.ARGUMENT_PATH, null);

        View view = inflater.inflate(R.layout.fragment_detail, container, false);

        //Set up description
        View block = view.findViewById(ID_DESCRIPTION_BLOCK);

        ImageView imageView = block.findViewById(R.id.imageView);

        Glide.with(imageView).load(getResources().getDrawable(R.drawable.ic_unknown_item)).into(imageView);

        String text = getString(R.string.guard_stone) + " : " + dataHandler.getKey(path);

        TextView textView = block.findViewById(R.id.textView);

        textView.setText(text);

        textView = block.findViewById(R.id.textView2);

        text = dataHandler.getValue(path + "/Description");

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

        ArrayList<String> items = dataHandler.getChildrenPaths(path + "/Skill");

        if(items.size() > 0) {
            for (int i = 0; i < items.size(); i++) {
                String key = dataHandler.getValue(items.get(i));

                items.remove(i);

                items.add(i, "/Skill/" + key);
            }

            Collections.sort(items);

            items.add(0, getString(R.string.skill));
        }

        ArrayList<String> temp = dataHandler.getChildrenPaths(path + "/Lv");

        if(temp.size() > 0) {
            Collections.sort(temp);

            temp.add(0, getString(R.string.upgrade_information));

            items.addAll(temp);
        }

        //Set up recycler view
        RecyclerView recyclerView = view.findViewById(ID_RECYCLER_VIEW);

        if(items.size() > 0)
            recyclerView.setAdapter(new DataAdapter(dataHandler, items));
        else
            recyclerView.setVisibility(View.GONE);
    }
}
