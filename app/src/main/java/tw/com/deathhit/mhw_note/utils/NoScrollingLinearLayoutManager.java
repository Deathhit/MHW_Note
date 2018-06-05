package tw.com.deathhit.mhw_note.utils;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.util.AttributeSet;

public final class NoScrollingLinearLayoutManager extends LinearLayoutManager{
    public NoScrollingLinearLayoutManager(Context context) {
        super(context);
    }

    public NoScrollingLinearLayoutManager(Context context, int orientation, boolean reverseLayout) {
        super(context, orientation, reverseLayout);
    }

    public NoScrollingLinearLayoutManager(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    public boolean canScrollVertically(){
        return false;
    }

    @Override
    public boolean canScrollHorizontally(){
        return false;
    }
}
