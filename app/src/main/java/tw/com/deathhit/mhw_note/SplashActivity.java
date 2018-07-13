package tw.com.deathhit.mhw_note;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.Menu;
import android.widget.Toast;

import com.google.android.gms.ads.MobileAds;

import tw.com.deathhit.mhw_note.utils.NetworkChecker;

public final class SplashActivity extends BaseActivity implements DataHandler.OnDataRequestedListener{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setRestartApplicationOnNewProcess(false);

        setContentView(R.layout.activity_splash);

        if(!NetworkChecker.checkNetwork(this))
            toast(getString(R.string.no_internet), Toast.LENGTH_LONG);

        DataHandler.addOnDataRequestedListener(this);

        //Request data after view is created to make activity responsive
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                //Request data from remote data base
                DataHandler.requestData(SplashActivity.this, Constants.STORAGE_DATABASE);

                //Initialize advertisement
                MobileAds.initialize(SplashActivity.this, getString(R.string.banner_ad_unit_id));
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return false;   //No menu for a splash activity
    }

    @Override
    public void onBackPressed() {
        //Disable onBackPressed()
    }

    /**Start activity after data is acquired.**/
    @Override
    public void onDataRequested(boolean isNewData) {
        DataHandler.removeOnDataRequestedListener(this);

        Intent intent = new Intent(SplashActivity.this, MainActivity.class);

        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);

        finish();

        startActivity(intent);
    }
}