package tw.com.deathhit.utility.widget;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

public class TabBar extends LinearLayout implements View.OnClickListener{
    protected OnItemClickListener onItemClickListener = null;

    protected int currentIndex;

    public TabBar(Context context) {
        super(context);
    }

    public TabBar(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public TabBar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void addView(View child, int index, ViewGroup.LayoutParams params ) {
        params.width = 0;
        params.height = ViewGroup.LayoutParams.MATCH_PARENT;
        ((LayoutParams)params).weight = 1;

        super.addView(child, index, params);

        select(0);

        child.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int index = indexOfChild(v);

        select(index);

        if(onItemClickListener != null)
            onItemClickListener.onClick(this, index);
    }

    public int getSelectedIndex(){
        return currentIndex;
    }

    public void select(int index){
        for(int i=0;i<getChildCount();i++)
            getChildAt(i).setSelected(false);

        getChildAt(index).setSelected(true);

        currentIndex = index;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener){
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener{
        void onClick(View v, int index);
    }
}
