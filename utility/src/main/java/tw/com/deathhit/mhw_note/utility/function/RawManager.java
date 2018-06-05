package tw.com.deathhit.mhw_note.utility.function;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public final class RawManager {
    private RawManager(){

    }

    public static JSONArray getJSONArray(Context context, int resourceId) {
        StringBuilder sb = new StringBuilder();

        BufferedReader br =  new BufferedReader(new InputStreamReader(context.getResources().openRawResource(resourceId)));

        String temp;

        try {
            while ((temp = br.readLine()) != null)
                sb.append(temp);
        } catch (IOException e) {
            e.printStackTrace();
            try {
                br.close(); // stop reading
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }

        try {
            return new JSONArray(sb.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }
}
