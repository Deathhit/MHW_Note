package tw.com.deathhit.mhw_note.view_model.detail;

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
import java.util.Collections;

import tw.com.deathhit.mhw_note.Constants;
import tw.com.deathhit.mhw_note.R;
import tw.com.deathhit.mhw_note.adapter.recycler_view.DataAdapter;
import tw.com.deathhit.mhw_note.adapter.recycler_view.ImageAdapter;
import tw.com.deathhit.mhw_note.utils.NoScrollingLinearLayoutManager;

public final class MonsterFragment extends BaseFragment {
    private static final int ID_INTRODUCTION_BLOCK = R.id.block;
    private static final int ID_AREAS_BLOCK = R.id.block2;
    private static final int ID_NOTE_BLOCK = R.id.block3;

    private static final int ID_MATERIAL_RECYCLER_VIEW = R.id.recyclerView;
    private static final int ID_DETAIL_RECYCLER_VIEW = R.id.recyclerView2;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        if(getArguments() == null)
            return null;

        String path = getArguments().getString(Constants.ARGUMENT_PATH, null);

        View view = inflater.inflate(R.layout.fragment_detail_monster, container, false);

        //Set up introduction
        View introduction = view.findViewById(ID_INTRODUCTION_BLOCK);

        ImageView imageView = introduction.findViewById(R.id.imageView);

        String url = dataHandler.getValue(path + "/Image");

        if(url != null && url.length() > 0)
            Glide.with(imageView).load(url).apply(new RequestOptions().placeholder(R.drawable.ic_unknown_monster).error(R.drawable.ic_unknown_monster)).into(imageView);
        else
            Glide.with(imageView).load(R.drawable.ic_unknown_monster).into(imageView);

        String text = dataHandler.getValue(path + "/Description");

        TextView textView = introduction.findViewById(R.id.textView2);

        textView.setText(text);

        text = getResources().getString(R.string.monster) + " : " + dataHandler.getKey(path);

        textView = view.findViewById(R.id.textView);

        textView.setText(text);

        //Set up areas
        long count = dataHandler.getChildrenCount(path + "/Map");

        View areas = view.findViewById(ID_AREAS_BLOCK);

        for(int i=0;i<count;i++){
            text = dataHandler.getValue(path + "/Map" + "/" + String.valueOf(i));

            if(text != null) {
                View map = null;

                switch (text) {
                    case "Forest":
                        map = areas.findViewById(R.id.imageView);
                        break;
                    case "Ant":
                        map = areas.findViewById(R.id.imageView2);
                        break;
                    case "Coral":
                        map = areas.findViewById(R.id.imageView3);
                        break;
                    case "Gas":
                        map = areas.findViewById(R.id.imageView4);
                        break;
                    case "Dragon":
                        map = areas.findViewById(R.id.imageView5);
                        break;
                }

                if (map != null)
                    map.setAlpha(1);
            }
        }

        //Configure material recycler view
        RecyclerView recyclerView = view.findViewById(ID_MATERIAL_RECYCLER_VIEW);

        recyclerView.setLayoutManager(new NoScrollingLinearLayoutManager(getContext()));

        recyclerView.addItemDecoration(new DividerItemDecoration(inflater.getContext(), DividerItemDecoration.VERTICAL));

        recyclerView.setHasFixedSize(true);

        //Set note
        View note = view.findViewById(ID_NOTE_BLOCK);

        text = dataHandler.getValue(path + "/Tip");

        if(text == null)
            note.setVisibility(View.GONE);
        else {
            textView = note.findViewById(R.id.textView2);

            textView.setText(text);

            textView = note.findViewById(R.id.textView);

            textView.setText(getResources().getString(R.string.ecological_notes));
        }

        //Configure detail recycler view
        recyclerView = view.findViewById(ID_DETAIL_RECYCLER_VIEW);

        recyclerView.setLayoutManager(new NoScrollingLinearLayoutManager(getContext()));

        recyclerView.setHasFixedSize(true);

        return view;
    }

    @Override
    public void onBindView(@NonNull View view, @Nullable Bundle savedInstanceState) {
        Bundle args = getArguments();

        assert args != null;
        String path = args.getString(Constants.ARGUMENT_PATH, null);

        ArrayList<String> items = dataHandler.getSiblingsPaths(path + "/Assets");

        //Set up recycler view for materials
        RecyclerView recyclerView = view.findViewById(ID_MATERIAL_RECYCLER_VIEW);

        if(items.size() > 0) {
            Collections.sort(items);

            for (int i = 0; i < items.size(); i++) {
                String item = items.get(i);

                items.remove(i);

                String parentKey = dataHandler.getParentKey(item);

                assert parentKey != null;
                if (parentKey.equals("Assets"))
                    items.add(i, dataHandler.getKey(item));
                else
                    items.add(i, "/MonsterAssetsDetail/" + dataHandler.getValue(item));
            }

            recyclerView.setAdapter(new DataAdapter(dataHandler, items));
        }else
            recyclerView.setVisibility(View.GONE);

        //Set up detail recycler view
        items = dataHandler.getChildrenPaths(path + "/SliderImages");

        recyclerView = view.findViewById(ID_DETAIL_RECYCLER_VIEW);

        if(items.size() > 0) {
            Collections.sort(items);

            recyclerView.setAdapter(new ImageAdapter(dataHandler, items));
        }else
            recyclerView.setVisibility(View.GONE);
    }

    @Override
    public void onDestroyView() {
        View view = getView();

        assert view != null;
        RecyclerView recyclerView = view.findViewById(ID_DETAIL_RECYCLER_VIEW);

        recyclerView.setAdapter(null);

        super.onDestroyView();
    }
}