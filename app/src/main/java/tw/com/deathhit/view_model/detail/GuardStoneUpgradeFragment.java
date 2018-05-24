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
import tw.com.deathhit.adapter.recycler_view.DataAdapter;
import tw.com.deathhit.utils.NoScrollingLinearLayoutManager;

public final class GuardStoneUpgradeFragment extends BaseFragment {
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

        String text = getString(R.string.upgrade_information) + " : " + dataHandler.getKey(path);

        TextView textView = block.findViewById(R.id.textView);

        textView.setText(text);

        text = getString(R.string.price) + " : " + dataHandler.getValue(path + "/Cost") + "\n" +
                getString(R.string.rarity) + " : " + dataHandler.getValue(path + "/Rare");

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
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


    }

    @Override
    public void onBindView(@NonNull View view, @Nullable Bundle savedInstanceState) {
        Bundle args = getArguments();

        assert args != null;
        String path = args.getString(Constants.ARGUMENT_PATH, null);

        //Set up recycler view
        ArrayList<String> temp = dataHandler.getChildrenPaths(path + "/Assets");

        RecyclerView recyclerView = view.findViewById(ID_RECYCLER_VIEW);

        if(temp.size() > 0) {
            ArrayList<String> items = new ArrayList<>(temp.size() + 1);

            for (String s : temp) {
                String value = dataHandler.getValue(s);

                if(value != null) {
                    String extraText = " x " + value.replaceAll("[^\\d]", "");

                    items.add("/MonsterAssetsDetail/" + value.split("\\s*x")[0] + Constants.OPERATOR_EXTRA_TEXT + extraText);
                }
            }

            Collections.sort(items);

            items.add(0, getString(R.string.required_materials));

            recyclerView.setAdapter(new DataAdapter(dataHandler, items));
        }else
            recyclerView.setVisibility(View.GONE);
    }
}
