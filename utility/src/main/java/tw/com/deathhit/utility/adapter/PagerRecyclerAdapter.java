package tw.com.deathhit.utility.adapter;

import android.support.annotation.NonNull;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;
import java.util.Stack;

public abstract class PagerRecyclerAdapter<T> extends PagerObjectAdapter<T> {
    private SparseArray<Stack<View>> recycledStacks = new SparseArray<>(1);

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        View view;

        int type = getItemViewType(position);

        Stack<View> viewStack;

        if(recycledStacks.get(type) == null)
            recycledStacks.put(type, viewStack = new Stack<>());
        else
            viewStack = recycledStacks.get(type);

        if (viewStack.isEmpty()) {
            view = onCreateView(items, position);

            onViewCreated(view);
        } else
            view = viewStack.pop();

        onBindView(items, view, position);

        onViewBound(view, position);

        container.addView(view);

        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object view) {
        container.removeView((View)view);
        recycledStacks.get(getItemViewType(position)).push((View)view);
    }

    /**Called after onCreateView(). Override to implement custom actions.**/
    public void onViewCreated(View view){

    }

    /**Called after onBindView(). Override to implement custom actions.**/
    public void onViewBound(View view, int position){

    }

    public abstract int getItemViewType(int position);
    public abstract void onBindView(List<T> items, View view, int position);
}
