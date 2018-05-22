package tw.com.deathhit;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import tw.com.deathhit.utility.function.NetworkManager;

public final class SplashActivity extends tw.com.deathhit.core.BaseActivity implements DataHandler.OnDataRequestedListener{
    private DataHandler dataHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_splash);

        setRestartApplicationOnNewProcess(false);

        dataHandler = new DataHandler(this, Constants.STORAGE_DATABASE);

        if(NetworkManager.getConnectivityStatus(this) == NetworkManager.TYPE_NOT_CONNECTED)
            toast(getString(R.string.no_internet), Toast.LENGTH_LONG);

        dataHandler.addOnDataRequestedListener(this);

        dataHandler.requestData();
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
