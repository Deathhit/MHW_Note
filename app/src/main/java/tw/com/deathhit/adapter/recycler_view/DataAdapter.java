package tw.com.deathhit.adapter.recycler_view;

import android.annotation.SuppressLint;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;

import tw.com.deathhit.Constants;
import tw.com.deathhit.DataHandler;
import tw.com.deathhit.R;
import tw.com.deathhit.core.BaseActivity;

public final class DataAdapter extends BaseAdapter implements View.OnClickListener{
    public DataAdapter(DataHandler dataHandler, List<String> items){
        super(dataHandler, items);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = null;

        switch (viewType){
            case TYPE_DATA :
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_data, parent, false);
                break;
            case TYPE_CATEGORY :
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_category, parent, false);
                break;
        }

        assert view != null;
        return new RecyclerView.ViewHolder(view) {
            @Override
            public String toString() {
                return super.toString();
            }
        };
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
                        BaseActivity.Presenter.request(Constants.REQUEST_GUARD_STONE_DETAIL, path);
                        break;
                    case "Lv":
                        BaseActivity.Presenter.request(Constants.REQUEST_GUARD_STONE_UPGRADE_DETAIL, path);
                        break;
                    case "Jewelry":
                        BaseActivity.Presenter.request(Constants.REQUEST_JEWEL_DETAIL, path);
                        break;
                    case "Pos":
                        BaseActivity.Presenter.request(Constants.REQUEST_EQUIPMENT_POSITION_DETAIL, path);
                        break;
                    default:
                        BaseActivity.Presenter.request(Constants.REQUEST_EQUIPMENT_SERIES_DETAIL, path);
                        break;
                }
                break;
            case "MonsterAssetsDetail" :
                BaseActivity.Presenter.request(Constants.REQUEST_MATERIAL_DETAIL, path);
                break;
            case "MonsterDetail" :
                BaseActivity.Presenter.request(Constants.REQUEST_MONSTER_DETAIL, path);
                break;
            case "Skill" :
                BaseActivity.Presenter.request(Constants.REQUEST_SKILL_DETAIL, path);
                break;
        }
    }
}
