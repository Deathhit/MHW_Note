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
import tw.com.deathhit.adapters.recycler_view.DataAdapter;
import tw.com.deathhit.utils.NoScrollingLinearLayoutManager;

public final class EquipmentPositionFragment extends BaseFragment {
    private static final int ID_INTRODUCTION_BLOCK = R.id.block;
    private static final int ID_ATTRIBUTES_BLOCK = R.id.block2;

    private static final int ID_SKILL_RECYCLER_VIEW = R.id.recyclerView;
    private static final int ID_MATERIALS_RECYCLER_VIEW = R.id.recyclerView2;
    @Override
    public View onCreateViewOnce(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if(getArguments() == null)
            return null;

        String path = getArguments().getString(Constants.ARGUMENT_PATH, null);

        View view = inflater.inflate(R.layout.fragment_detail_position, container, false);

        //Set up description
        View block = view.findViewById(ID_INTRODUCTION_BLOCK);

        ImageView imageView = block.findViewById(R.id.imageView);

        String value = dataHandler.getValue(path + "/Position");

        if(value != null) {
            switch (value) {
                case "Head":
                    Glide.with(imageView).load(getResources().getDrawable(R.drawable.ic_equipment_head)).into(imageView);
                    break;
                case "Body":
                    Glide.with(imageView).load(getResources().getDrawable(R.drawable.ic_equipment_body)).into(imageView);
                    break;
                case "Hand":
                    Glide.with(imageView).load(getResources().getDrawable(R.drawable.ic_equipment_arms)).into(imageView);
                    break;
                case "Pants":
                    Glide.with(imageView).load(getResources().getDrawable(R.drawable.ic_equipment_waist)).into(imageView);
                    break;
                case "Shoes":
                    Glide.with(imageView).load(getResources().getDrawable(R.drawable.ic_equipment_legs)).into(imageView);
                    break;
            }
        }

        String text = getString(R.string.position_information) + " : " + dataHandler.getKey(path);

        TextView textView = block.findViewById(R.id.textView);

        textView.setText(text);

        textView = block.findViewById(R.id.textView2);

        text = getString(R.string.slot) + " : " + dataHandler.getValue(path + "/Slot") + "\n" +
                getString(R.string.defence) + " : " + dataHandler.getValue(path + "/Defence");

        textView.setText(text);

        //Set up attributes
        View block2 = view.findViewById(ID_ATTRIBUTES_BLOCK);

        textView = block2.findViewById(R.id.textView);

        text = textView.getText() + " : " + dataHandler.getValue(path + "/Attributes/火");

        textView.setText(text);

        textView = block2.findViewById(R.id.textView2);

        text = textView.getText() + " : " + dataHandler.getValue(path + "/Attributes/水");

        textView.setText(text);

        textView = block2.findViewById(R.id.textView3);

        text = textView.getText() + " : " + dataHandler.getValue(path + "/Attributes/雷");

        textView.setText(text);

        textView = block2.findViewById(R.id.textView4);

        text = textView.getText() + " : " + dataHandler.getValue(path + "/Attributes/冰");

        textView.setText(text);

        textView = block2.findViewById(R.id.textView5);

        text = textView.getText() + " : " + dataHandler.getValue(path + "/Attributes/龍");

        textView.setText(text);

        //Set up recycler view
        ArrayList<String> items;

        RecyclerView recyclerView = view.findViewById(ID_SKILL_RECYCLER_VIEW);

        ArrayList<String> temp = dataHandler.getChildrenPaths(path + "/Skill");

        if(temp.size() > 0){
            items = new ArrayList<>(temp.size() + 1);

            for (String s : temp) {
                value = dataHandler.getValue(s);

                if(value != null) {
                    String extraText = "  " + getString(R.string.level) + " : " + value.replaceAll("[^\\d]", "");

                    items.add("/Skill/" + value.split("\\s*[0-9]+")[0] + Constants.OPERATOR_EXTRA_TEXT + extraText);
                }
            }

            Collections.sort(items);

            items.add(0, getString(R.string.skill));

            recyclerView.setLayoutManager(new NoScrollingLinearLayoutManager(getContext()));

            recyclerView.addItemDecoration(new DividerItemDecoration(container.getContext(), DividerItemDecoration.VERTICAL));

            recyclerView.setHasFixedSize(true);

            recyclerView.setAdapter(new DataAdapter(dataHandler, items));

        }else
            recyclerView.setVisibility(View.GONE);

        temp = dataHandler.getChildrenPaths(path + "/Assets");

        recyclerView = view.findViewById(ID_MATERIALS_RECYCLER_VIEW);

        if(temp.size() > 0) {
            items = new ArrayList<>(temp.size() + 1);

            for (String s : temp) {
                value = dataHandler.getValue(s);

                if(value != null) {
                    String extraText = " x " + value.replaceAll("[^\\d]", "");

                    items.add("/MonsterAssetsDetail/" + value.split("\\s*x")[0] + Constants.OPERATOR_EXTRA_TEXT + extraText);
                }
            }

            Collections.sort(items);

            items.add(0, getString(R.string.required_materials));

            recyclerView.setLayoutManager(new NoScrollingLinearLayoutManager(getContext()));

            recyclerView.addItemDecoration(new DividerItemDecoration(container.getContext(), DividerItemDecoration.VERTICAL));

            recyclerView.setHasFixedSize(true);

            recyclerView.setAdapter(new DataAdapter(dataHandler, items));
        }else
            recyclerView.setVisibility(View.GONE);

        return view;
    }

    @Override
    public void onDestroyView() {
        View view = getView();

        assert view != null;
        RecyclerView recyclerView = view.findViewById(ID_MATERIALS_RECYCLER_VIEW);

        recyclerView.setAdapter(null);

        super.onDestroyView();
    }
}
