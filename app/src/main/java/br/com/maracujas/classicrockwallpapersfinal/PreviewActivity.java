package br.com.maracujas.classicrockwallpapersfinal;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ImageView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

import java.io.File;

public class PreviewActivity extends AppCompatActivity {
    Bundle extras;
    File imageFile;
    AdView adView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_preview);

        ImageView image1 = (ImageView) findViewById(R.id.imageViewPreview);

        extras = getIntent().getExtras();
        //imageId=1;

        if (extras != null) {
            //String auximage = extras.getString("image");
          //  imageId = ;

             imageFile = new File(extras.getString("image"));
            Bitmap bitmap = BitmapFactory.decodeFile(imageFile.getAbsolutePath());
            image1.setImageBitmap(bitmap);
        }else{
            image1.setImageBitmap(decodeSampledBitmapFromResource(getResources(), R.mipmap.ideia_icon_grande, 380, 600));
        }

        MobileAds.initialize(getApplicationContext(), "ca-app-pub-7040951679419231~6905557505");

        adView = (AdView)this.findViewById(R.id.adViewP);
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                .addTestDevice("F0777154C5F794B0B7A1EF4120502169")
                .build();
        adView.loadAd(adRequest);
    }

    @Override
    public void onDestroy()
    {
        Cleanup();
        super.onDestroy();
        finish();
        adView.destroy();
    }

    @Override
    protected void onStop(){
        super.onStop();
        adView.pause();
    }

    @Override
    protected void onPause() {
        super.onPause();
        unbindDrawables(getWindow().getDecorView().findViewById(android.R.id.content));
        System.gc();
    }

    private void Cleanup()
    {
        unbindDrawables(getWindow().getDecorView().findViewById(android.R.id.content));
        System.gc();
        Runtime.getRuntime().gc();
    }

    private void unbindDrawables(View view)
    {
        if (view.getBackground() != null)
        {
            view.getBackground().setCallback(null);
        }
        if (view instanceof ViewGroup && !(view instanceof AdapterView))
        {
            for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++)
            {
                unbindDrawables(((ViewGroup) view).getChildAt(i));
            }
            ((ViewGroup) view).removeAllViews();
        }
    }

    public static Bitmap decodeSampledBitmapFromResource(
            Resources res, int resId, int reqWidth,
            int reqHeight) {

        // Primeiro faz a decodificação com
        // inJustDecodeBounds = true para verificar as dimensões
        final BitmapFactory.Options options =
                new BitmapFactory.Options();

        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(res, resId, options);

        // Calcula o inSampleSize
        options.inSampleSize =
                calculateInSampleSize(options, reqWidth, reqHeight);

        // Decodifica o bitmap com o inSampleSize configurado
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeResource(res, resId, options);
    }
    public static int calculateInSampleSize(
            BitmapFactory.Options options, int reqWidth,
            int reqHeight) {
        // Altura e largura da imagem
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            // Calcula as proporções de altura e largura
            // com a altura e largura solicitada
            final int heightRatio =
                    Math.round((float) height / (float) reqHeight);

            final int widthRatio =
                    Math.round((float) width / (float) reqWidth);

            // Escolhe qual a melhor proporção para inSampleSize
            inSampleSize =
                    heightRatio < widthRatio ? heightRatio : widthRatio;
        }

        return inSampleSize;
    }

    @Override
    protected void onResume(){
        super.onResume();
        adView.resume();
    }


}
