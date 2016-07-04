package br.com.maracujas.classicrockwallpapersfinal;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

/**
 * Created by julio on 02/07/2016.
 */
public class DashboardActivity extends AppCompatActivity {
    Button btSkull, btRock, btGuitar, btFire;
    AdView mAdView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        InitViews();

        if (ContextCompat.checkSelfPermission(DashboardActivity.this,
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(DashboardActivity.this,
                    new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    14);

        }

        MobileAds.initialize(getApplicationContext(), "ca-app-pub-9694259300655137~7036976403");

        mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                .addTestDevice("F0777154C5F794B0B7A1EF4120502169").build();

        mAdView.loadAd(adRequest);

    }

    private void InitViews() {
        btSkull = (Button) findViewById(R.id.bt_skull);
        btRock = (Button) findViewById(R.id.bt_rock);
        btGuitar = (Button) findViewById(R.id.bt_guitar);
        btFire = (Button) findViewById(R.id.bt_fire);
    }

    public void selecionarOpcao(View view) {
        Intent i;
        switch (view.getId()) {
            case R.id.bt_rock:
                i = new Intent(DashboardActivity.this, WallpaperActivity.class);
                i.putExtra("categoria", "rock");
                startActivity(i);
                finish();
                break;
            case R.id.bt_skull:
                i = new Intent(DashboardActivity.this, WallpaperActivity.class);
                i.putExtra("categoria", "skull");
                startActivity(i);
                finish();
                break;
            case R.id.bt_fire:
                i = new Intent(DashboardActivity.this, WallpaperActivity.class);
                i.putExtra("categoria", "fire");
                startActivity(i);
                finish();
                break;
            case R.id.bt_guitar:
                i = new Intent(DashboardActivity.this, WallpaperActivity.class);
                i.putExtra("categoria", "guitar");
                startActivity(i);
                finish();
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        mAdView.resume();
    }


    @Override
    protected void onStop() {
        super.onStop();
        mAdView.pause();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        mAdView.destroy();
    }
}
