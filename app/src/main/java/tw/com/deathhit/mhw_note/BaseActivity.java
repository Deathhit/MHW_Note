package tw.com.deathhit.mhw_note;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.text.method.LinkMovementMethod;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import tw.com.deathhit.mhw_note.core.FragmentActivity;

abstract class BaseActivity extends FragmentActivity {
    private static final int ID_ABOUT_APPLICATION = R.id.item;
    private static final int ID_REPORT_MISSING_DATA = R.id.item2;
    private static final int ID_CLOSE_AD = R.id.item3;

    /**Load ads to ad views.**/
    public static void loadAdViews(AdView... adViews){
        new Handler(Looper.getMainLooper()).post(new LoadAdViews(adViews));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case ID_ABOUT_APPLICATION :
                AlertDialog.Builder builder = new AlertDialog.Builder(this);

                builder.setTitle(R.string.about_application);
                builder.setMessage(R.string.about_application_content);

                AlertDialog dialog = builder.show();

                //Enable html link
                ((TextView)dialog.findViewById(android.R.id.message)).setMovementMethod(LinkMovementMethod.getInstance());
                break;
            case ID_REPORT_MISSING_DATA :
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/html");

                String[] mailAddresses = new String[1];

                mailAddresses[0] = getString(R.string.service_mail_address);

                intent.putExtra(Intent.EXTRA_EMAIL, mailAddresses);
                intent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.app_name) + " : " + getString(R.string.report_missing_data));
                intent.putExtra(Intent.EXTRA_TEXT, getString(R.string.report_missing_data_content));

                startActivity(Intent.createChooser(intent, getString(R.string.report_missing_data)));
                break;
            case ID_CLOSE_AD :
                //TODO implement
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    private static class LoadAdViews implements Runnable{
        private AdView[] adViews;

        LoadAdViews(AdView... adViews){
            this.adViews = adViews;
        }

        @Override
        public void run() {
            for(AdView adView : adViews)
                adView.loadAd(new AdRequest.Builder().build());
        }
    }
}