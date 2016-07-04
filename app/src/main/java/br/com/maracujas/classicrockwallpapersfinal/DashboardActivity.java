package br.com.maracujas.classicrockwallpapersfinal;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

/**
 * Created by julio on 02/07/2016.
 */
public class DashboardActivity extends AppCompatActivity implements View.OnClickListener {
    Button btGuns, btDeep;
    Context ctx = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        InitViews();



        MobileAds.initialize(getApplicationContext(), "ca-app-pub-9694259300655137~7036976403");

        AdView mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
              .addTestDevice("F0777154C5F794B0B7A1EF4120502169").build();

        mAdView.loadAd(adRequest);

    }

    private void InitViews() {
         btGuns = (Button) findViewById(R.id.bt_guns);
         btDeep = (Button) findViewById(R.id.bt_deeppurple);
    }

    private void setListeners() {
        btGuns.setOnClickListener(this);
        btDeep.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent i=new Intent(this, WallpaperActivity.class);
        switch(v.getId()){
            case R.id.bt_guns:
                i.putExtra("categoria", "guns");
                this.startActivity(i);
                finish();
                break;
            case R.id.bt_deeppurple:
                i.putExtra("categoria", "deep");
                this.startActivity(i);
                finish();
                break;
        }
    }

    public void btGuns(View v){
        Intent i=new Intent(this, WallpaperActivity.class);
        i.putExtra("categoria", "guns");
        this.startActivity(i);
        finish();
    }

    public void btDeep(View v){
        Intent i=new Intent(this, WallpaperActivity.class);
        i.putExtra("categoria", "deep");
        this.startActivity(i);
        finish();
    }
}
