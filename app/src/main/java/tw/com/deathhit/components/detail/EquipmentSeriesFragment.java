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
import tw.com.deathhit.adapters.recycler_view.DataAdapter;
import tw.com.deathhit.R;
import tw.com.deathhit.comparators.PositionComparator;
import tw.com.deathhit.utils.NoScrollingLinearLayoutManager;

public final class EquipmentSeriesFragment extends BaseFragment {
    private static final int ID_INTRODUCTION_BLOCK = R.id.block;

    private static final int ID_RECYCLER_VIEW = R.id.recyclerView;

    @Override
    public View onCreateViewOnce(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if(getArguments() == null)
            return null;

        String path = getArguments().getString(Constants.ARGUMENT_PATH, null);

        View view = inflater.inflate(R.layout.fragment_detail, container, false);

        //Set up introduction
        View block = view.findViewById(ID_INTRODUCTION_BLOCK);

        ImageView imageView = block.findViewById(R.id.imageView);

        Glide.with(imageView).load(getResources().getDrawable(R.drawable.ic_equipment_head)).into(imageView);

        TextView textView = block.findViewById(R.id.textView);

        String text = getResources().getString(R.string.series) + " : " + dataHandler.getKey(path);

        textView.setText(text);

        textView = block.findViewById(R.id.textView2);

        String cost = dataHandler.getValue(path + "/Cost");
        cost = cost != null ? cost : "???";

        String rare = dataHandler.getValue(path + "/Rare");
        rare = rare != null ? rare : "???";

        text = getResources().getString(R.string.price) + " : " + cost + "\n" +
                getResources().getString(R.string.rarity) + " : " + rare;

        textView.setText(text);

        ArrayList<String> items = new ArrayList<>();

        ArrayList<String> temp = dataHandler.getChildrenPaths(path + "/SerieSkill/Skill");

        if(temp.size() > 0) {
            items = new ArrayList<>(temp.size() + 1);

            for (String s : temp) {
                String value = dataHandler.getValue(s);

                if(value == null)
                    break;

                String extraText = "  " + getString(R.string.condition) + " : " + value.replaceAll("[^\\d]", "") + getString(R.string.piece);

                items.add("/Skill/" + value.split("\\s*[0-9]+")[0] + Constants.OPERATOR_EXTRA_TEXT + extraText);
            }

            Collections.sort(items);

            items.add(0, getString(R.string.series_skill) + " : " + dataHandler.getValue(path + "/SerieSkill/Name"));
        }

        temp = dataHandler.getChildrenPaths(path + "/Pos");

        if(temp.size() > 0) {
            Collections.sort(temp, new PositionComparator(dataHandler));

            temp.add(0, getString(R.string.position_information));

            items.addAll(temp);
        }

        //Set up recycler view
        RecyclerView recyclerView = view.findViewById(ID_RECYCLER_VIEW);

        if(items.size() > 0) {
            recyclerView.setLayoutManager(new NoScrollingLinearLayoutManager(getContext()));

            recyclerView.addItemDecoration(new DividerItemDecoration(inflater.getContext(), DividerItemDecoration.VERTICAL));

            recyclerView.setHasFixedSize(true);

            recyclerView.setAdapter(new DataAdapter(dataHandler, items));
        }else
            recyclerView.setVisibility(View.GONE);

        return view;
    }
}
