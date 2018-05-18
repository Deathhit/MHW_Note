package tw.com.deathhit.utility.function;

import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.support.v4.graphics.drawable.DrawableCompat;

public final class DrawableManager {
    private DrawableManager(){

    }

    public static Drawable copy(Drawable drawable){
        if(drawable == null)
            return null;

        return drawable.getConstantState().newDrawable().mutate();
    }

    public static Drawable setTint(Drawable drawable, int color){
        if(drawable == null)
            return null;

        DrawableCompat.setTint(drawable, color);

        return drawable;
    }

    public static Drawable setTintList(Drawable drawable, ColorStateList colorStateList){
        if(drawable == null)
            return null;

        DrawableCompat.setTintList(drawable,colorStateList);

        return drawable;
    }

    public static Drawable setIntrinsicBounds(Drawable drawable){
        if(drawable == null)
            return null;

        int h = drawable.getIntrinsicHeight();
        int w = drawable.getIntrinsicWidth();
        drawable.setBounds( 0, 0, w, h );

        return drawable;
    }
}
