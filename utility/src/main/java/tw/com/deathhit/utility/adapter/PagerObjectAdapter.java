package tw.com.deathhit.utility.adapter;

import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

public abstract class PagerObjectAdapter<T> extends PagerAdapter {
    protected List<T> items = new ArrayList<>();

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object)   {
        container.removeView((View)object);
    }

    @Override
    public int getCount() {
        return  items.size();
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        View view = onCreateView(items, position);

        container.addView(view);

        return view;
    }

    @Override
    public boolean isViewFromObject(@NonNull View arg0, @NonNull Object arg1) {
        return arg0==arg1;
    }

    public List<T> getItems(){
        return items;
    }

    public abstract View onCreateView(List<T> items, int position);
}
