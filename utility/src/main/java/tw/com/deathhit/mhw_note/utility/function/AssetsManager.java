package tw.com.deathhit.mhw_note.utility.function;

import android.content.Context;
import android.graphics.drawable.Drawable;

import java.io.IOException;
import java.io.InputStream;

public final class AssetsManager {
    private AssetsManager(){

    }

    /**Get Drawable from assets.**/
    public static Drawable getDrawable(Context context, String url){
        Drawable drawable = null;
        InputStream inputStream = null;
        try {
            inputStream = context.getAssets().open(url);
            drawable = Drawable.createFromStream(inputStream, null);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if(inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return drawable;
    }
}
