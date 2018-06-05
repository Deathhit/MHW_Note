package tw.com.deathhit.mhw_note.utility.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

/**Adapter class that supports both View and regular object types.**/
public abstract class RecyclerMultiAdapter<T> extends RecyclerObjectAdapter<Object> {
    @Override
    public final int getItemViewType(int position) {
        if(View.class.isInstance(items.get(position)))
            return -(++position);   //Negative integer represents View type
        else
            return position;    //Positive integer represents the generic type
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        int viewType = getItemViewType(position);

        if(viewType >= 0)
            onBindViewHolderForType(holder, position);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(viewType < 0)    //This index holds a View object
            return new RecyclerView.ViewHolder((View)items.get(-(++viewType))) {
                @Override
                public String toString() {
                    return super.toString();
                }
            };
        else    //This index holds an object of the generic type
            return onCreateViewHolderForType(parent, viewType);
    }

    protected T getItem(int position){
        return (T)items.get(position);
    }

    /**Implement this method instead of onBindViewHolder() to bind view holder for the generic type.**/
    public abstract void onBindViewHolderForType(RecyclerView.ViewHolder holder, int position);

    /**Implement this method instead of onCreateViewHolder() to create ViewHolder for the generic type.
     * Be noted that negative viewType means the object in adapter is a View.**/
    public abstract RecyclerView.ViewHolder onCreateViewHolderForType(ViewGroup parent, int viewType);
}
