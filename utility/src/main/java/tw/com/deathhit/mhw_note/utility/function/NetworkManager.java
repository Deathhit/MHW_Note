package tw.com.deathhit.mhw_note.utility.function;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public final class NetworkManager {
    public static final int TYPE_NOT_CONNECTED = -1;
    public static final int TYPE_MOBILE = ConnectivityManager.TYPE_MOBILE;
    public static final int TYPE_WIFI = ConnectivityManager.TYPE_WIFI;

    public static int getConnectivityStatus(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);

        assert cm != null;
        @SuppressLint("MissingPermission") NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if (null != activeNetwork) {
            if(activeNetwork.getType() == ConnectivityManager.TYPE_WIFI)
                return TYPE_WIFI;

            if(activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE)
                return TYPE_MOBILE;
        }
        return TYPE_NOT_CONNECTED;
    }

    private NetworkManager(){

    }
}
