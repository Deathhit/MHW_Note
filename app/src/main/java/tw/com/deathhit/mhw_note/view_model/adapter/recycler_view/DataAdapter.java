package tw.com.deathhit.mhw_note.view_model.adapter.recycler_view;

import android.annotation.SuppressLint;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;

import tw.com.deathhit.mhw_note.Constants;
import tw.com.deathhit.mhw_note.DataHandler;
import tw.com.deathhit.mhw_note.R;
import tw.com.deathhit.mhw_note.core.BaseActivity;

public final class DataAdapter extends BaseAdapter implements View.OnClickListener{
    public DataAdapter(DataHandler dataHandler, List<String> items){
        super(dataHandler, items);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        String path = getPath(position);

        View view = holder.itemView;

        view.setTag(position);

        TextView textView = view.findViewById(R.id.textView);

        switch (getItemViewType(position)){
            case TYPE_DATA :
                textView.setText(dataHandler.getKey(path));

                ImageView imageView = view.findViewById(R.id.imageView);

                String dataType = path.split(Constants.OPERATOR_PATH)[1];

                String url;

                switch (dataType){
                    case "Equipment" :
                        dataType = dataHandler.getParentKey(path);

                        assert dataType != null;
                        switch (dataType) {
                            case "GuardStone":
                            case "Lv":
                                Glide.with(imageView).load(R.drawable.ic_unknown_item).into(imageView);
                                break;
                            case "Jewelry":
                                Glide.with(imageView).load(R.drawable.ic_jewel).into(imageView);
                                break;
                            case "Pos":
                                String equipmentPos = dataHandler.getValue(path + "/Position");

                                assert equipmentPos != null;
                                switch (equipmentPos){
                                    case "Hand" :
                                        Glide.with(imageView).load(R.drawable.ic_equipment_arms).into(imageView);
                                        break;
                                    case "Pants" :
                                        Glide.with(imageView).load(R.drawable.ic_equipment_waist).into(imageView);
                                        break;
                                    case "Shoes" :
                                        Glide.with(imageView).load(R.drawable.ic_equipment_legs).into(imageView);
                                        break;
                                    case "Body" :
                                        Glide.with(imageView).load(R.drawable.ic_equipment_body).into(imageView);
                                        break;
                                    case "Head" :
                                        Glide.with(imageView).load(R.drawable.ic_equipment_head).into(imageView);
                                        break;
                                }
                                break;
                            default:
                                Glide.with(imageView).load(R.drawable.ic_equipment_head).into(imageView);
                                break;
                        }
                        break;
                    case "MonsterAssetsDetail" :
                        url = dataHandler.getValue(path + "/Image");

                        if (url != null && url.length() > 0) {
                            Uri uri = Uri.parse(url);

                            Glide.with(imageView).load(uri).apply(new RequestOptions().placeholder(R.drawable.ic_unknown_item).error(R.drawable.ic_unknown_item)).into(imageView);
                        } else
                            imageView.setImageDrawable(imageView.getContext().getResources().getDrawable(R.drawable.ic_unknown_item));
                        break;
                    case "MonsterDetail" :
                        url = dataHandler.getValue(path + "/Image");

                        if(url != null && url.length() > 0) {
                            Uri uri = Uri.parse(url);

                            Glide.with(imageView).load(uri).apply(new RequestOptions().placeholder(R.drawable.ic_unknown_monster).error(R.drawable.ic_unknown_monster)).into(imageView);
                        }else
                            imageView.setImageDrawable(imageView.getContext().getResources().getDrawable(R.drawable.ic_unknown_monster));
                        break;
                    case "Skill" :
                        Glide.with(imageView).load(R.drawable.ic_book).into(imageView);
                        break;
                }

                textView.setText(dataHandler.getKey(path) + getExtraText(position));

                view.setOnClickListener(this);
                break;
            case TYPE_CATEGORY :
                textView.setText(path);
                break;
        }
    }

    @Override
    public void onClick(View view) {
        int position = (int)view.getTag();

        String path = getPath(position);

        String dataType = path.split(Constants.OPERATOR_PATH)[1];

        switch (dataType){
            case "Equipment" :
                dataType = dataHandler.getParentKey(path);

                assert dataType != null;
                switch (dataType) {
                    case "GuardStone":
                        BaseActivity.get().request(Constants.REQUEST_GUARD_STONE_DETAIL, path);
                        break;
                    case "Lv":
                        BaseActivity.get().request(Constants.REQUEST_GUARD_STONE_UPGRADE_DETAIL, path);
                        break;
                    case "Jewelry":
                        BaseActivity.get().request(Constants.REQUEST_JEWEL_DETAIL, path);
                        break;
                    case "Pos":
                        BaseActivity.get().request(Constants.REQUEST_EQUIPMENT_POSITION_DETAIL, path);
                        break;
                    default:
                        BaseActivity.get().request(Constants.REQUEST_EQUIPMENT_SERIES_DETAIL, path);
                        break;
                }
                break;
            case "MonsterAssetsDetail" :
                BaseActivity.get().request(Constants.REQUEST_MATERIAL_DETAIL, path);
                break;
            case "MonsterDetail" :
                BaseActivity.get().request(Constants.REQUEST_MONSTER_DETAIL, path);
                break;
            case "Skill" :
                BaseActivity.get().request(Constants.REQUEST_SKILL_DETAIL, path);
                break;
        }
    }
}
