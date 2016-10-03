package br.com.maracujas.classicrockwallpapersfinal;

import android.app.Application;



/**
 * Created by julio on 30/09/2016.
 */
public class Global extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        /* Picasso.Builder builder = new Picasso.Builder(this);
        builder.downloader(new OkHttpDownloader(this,Integer.MAX_VALUE));
        Picasso built = builder.build();
        built.setIndicatorsEnabled(true);
        built.setLoggingEnabled(true);
        Picasso.setSingletonInstance(built); */

    }
}
