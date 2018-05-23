package tw.com.deathhit;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.widget.Toast;

import com.google.android.gms.ads.MobileAds;

import tw.com.deathhit.utility.function.NetworkManager;

public final class SplashActivity extends BaseActivity implements DataHandler.OnDataRequestedListener{
    private DataHandler dataHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setRestartApplicationOnNewProcess(false);

        setContentView(R.layout.activity_splash);

        //Request data from remote data base
        dataHandler = new DataHandler(this, Constants.STORAGE_DATABASE);

        if(NetworkManager.getConnectivityStatus(this) == NetworkManager.TYPE_NOT_CONNECTED)
            toast(getString(R.string.no_internet), Toast.LENGTH_LONG);

        dataHandler.addOnDataRequestedListener(this);

        dataHandler.requestData();

        //Initialize advertisement
        MobileAds.initialize(this, getString(R.string.banner_ad_unit_id));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return false;   //No menu for a splash activity
    }

    @Override
    public void onBackPressed() {
        //Disable onBackPressed()
    }

    @Override
    public void onDataRequested(boolean isNewData) {
        dataHandler.removeOnDataRequestedListener(this);

        Intent intent = new Intent(SplashActivity.this, MainActivity.class);

        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);

        finish();

        startActivity(intent);
    }
}