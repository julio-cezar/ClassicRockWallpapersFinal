package br.com.maracujas.classicrockwallpapersfinal;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.AssetFileDescriptor;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Vibrator;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class WallpaperActivity extends AppCompatActivity implements View.OnClickListener {

    ImageView display, image1, image2, image3, image4, image5;
    Button setWall, bPreview;
    ImageButton ibshare, ibsave, ibsound;
    int toPhone;
    Uri uri;
    Bundle extras;
    String categoria;
    TextView tvCategoria;
    FirebaseStorage storage;
    StorageReference RootRef, WallpapersRef, folderRef, imageRef;
    final long ONE_MEGABYTE = 1024 * 1024;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_wallpaper);
        extras = getIntent().getExtras();
        categoria = "no";

        if (extras != null) {
            categoria = extras.getString("categoria");
        }

        iniciarViews();
        setListeners();

        toPhone = R.mipmap.ideia_icon_grande;
        uri = Uri.parse("android.resource://br.com.maracujas.classicrockwallpapersfinal/mipmap/ideia_icon_grande");
        display.setImageBitmap(decodeSampledBitmapFromResource(getResources(), R.mipmap.ideia_icon_grande, 150, 150));

        storage = FirebaseStorage.getInstance();
        RootRef = storage.getReferenceFromUrl("gs://classic-rock-wallpapers-final.appspot.com");
        WallpapersRef = RootRef.child("wallpapers");

        switch (categoria) {
            /*case "fire":
                tvCategoria.setText("Rock");

                folderRef = WallpapersRef.child("fire");
                //Toast.makeText(getApplicationContext(), folderRef.getPath(), Toast.LENGTH_LONG).show();
                StorageReference imageRef = folderRef.child("fire_1.jpg");
                final long ONE_MEGABYTE = 1024 * 1024;
                imageRef.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                    @Override
                    public void onSuccess(byte[] bytes) {
                        image1.setImageBitmap(decodeSampledBitmapFromResource(getResources(), R.drawable.back_guns_1, 75, 75));                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        // Handle any errors
                    }
                });

                break;*/

            case "rock":
                tvCategoria.setText("Rock");
                image1.setImageBitmap(decodeSampledBitmapFromResource(getResources(), R.drawable.back_guns_1, 75, 75));
                image2.setImageBitmap(decodeSampledBitmapFromResource(getResources(), R.drawable.back_guns_2, 75, 75));
                image3.setImageBitmap(decodeSampledBitmapFromResource(getResources(), R.drawable.back_guns_3, 75, 75));
                image4.setImageBitmap(decodeSampledBitmapFromResource(getResources(), R.drawable.back_guns_4, 75, 75));
                image5.setImageBitmap(decodeSampledBitmapFromResource(getResources(), R.drawable.back_guns_5, 75, 75));
                break;
            case "skull":
                tvCategoria.setText("Skull");
                image1.setImageBitmap(decodeSampledBitmapFromResource(getResources(), R.drawable.back_deeppurple_1, 75, 75));
                image2.setImageBitmap(decodeSampledBitmapFromResource(getResources(), R.drawable.back_deeppurple_2, 75, 75));
                image3.setImageBitmap(decodeSampledBitmapFromResource(getResources(), R.drawable.back_deeppurple_3, 75, 75));
                image4.setImageBitmap(decodeSampledBitmapFromResource(getResources(), R.drawable.back_deeppurple_4, 75, 75));
                image5.setImageBitmap(decodeSampledBitmapFromResource(getResources(), R.drawable.back_deeppurple_5, 75, 75));
                break;
            case "no":
                Toast.makeText(getApplicationContext(), "no Image!", Toast.LENGTH_LONG).show();
                break;
        }
       /* switch(banda) {
            case "no":
                tvBanda.setText("Caveiras");

                imageRef = folderRef.child("skull_1.jpg");

                File localFile = null;
                try {
                    localFile = File.createTempFile("images", "jpg");
                } catch (IOException e) {
                    e.printStackTrace();
                }

                imageRef.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                        // Local temp file has been created
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        // Handle any errors
                    }
                });

                image1.setImageBitmap(decodeSampledBitmapFromResource(getResources(), R.drawable.back_ff_1, 75, 75));
                image2.setImageBitmap(decodeSampledBitmapFromResource(getResources(), R.drawable.back_ff_2, 75, 75));
                image3.setImageBitmap(decodeSampledBitmapFromResource(getResources(), R.drawable.back_ff_3, 75, 75));
                image4.setImageBitmap(decodeSampledBitmapFromResource(getResources(), R.drawable.back_ff_4, 75, 75));
                image5.setImageBitmap(decodeSampledBitmapFromResource(getResources(), R.drawable.back_ff_5, 75, 75));
                break;
        }*/


    }

    private void setListeners() {
        image1.setOnClickListener(this);
        image2.setOnClickListener(this);
        image3.setOnClickListener(this);
        image4.setOnClickListener(this);
        image5.setOnClickListener(this);
        setWall.setOnClickListener(this);
        bPreview.setOnClickListener(this);
        ibshare.setOnClickListener(this);
        ibsave.setOnClickListener(this);
        ibsound.setOnClickListener(this);
    }

    private void iniciarViews() {
        display = (ImageView) findViewById(R.id.IVDisplay);
        image1 = (ImageView) findViewById(R.id.IVimage1);
        image2 = (ImageView) findViewById(R.id.IVimage2);
        image3 = (ImageView) findViewById(R.id.IVimage3);
        image4 = (ImageView) findViewById(R.id.IVimage4);
        image5 = (ImageView) findViewById(R.id.IVimage5);
        setWall = (Button) findViewById(R.id.BsetWallpaper);
        bPreview = (Button) findViewById(R.id.BPreview);
        ibshare = (ImageButton) findViewById(R.id.IBSave);
        ibsave = (ImageButton) findViewById(R.id.IBShare);
        ibsound = (ImageButton) findViewById(R.id.IBSound);
        tvCategoria = (TextView) findViewById(R.id.tvCategoria);
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
    public void onClick(View v) {
        switch (categoria) {
            case "rock":
                switch (v.getId()) {
                    case R.id.IVimage1:
                        display.setImageDrawable(null);
                        display.setImageBitmap(decodeSampledBitmapFromResource(getResources(), R.drawable.back_guns_1, 150, 150));
                        toPhone = R.drawable.back_guns_1;
                        uri = Uri.parse("android.resource://br.com.maracujas.classicrockwallpapersfinal/drawable/back_guns_1");
                        break;
                    case R.id.IVimage2:
                        display.setImageDrawable(null);
                        display.setImageBitmap(decodeSampledBitmapFromResource(getResources(), R.drawable.back_guns_2, 150, 150));
                        toPhone = R.drawable.back_guns_2;
                        uri = Uri.parse("android.resource://br.com.maracujas.classicrockwallpapersfinal/drawable/back_guns_2");
                        break;
                    case R.id.IVimage3:
                        display.setImageDrawable(null);
                        display.setImageBitmap(decodeSampledBitmapFromResource(getResources(), R.drawable.back_guns_3, 150, 150));
                        toPhone = R.drawable.back_guns_3;
                        uri = Uri.parse("android.resource://br.com.maracujas.classicrockwallpapersfinal/drawable/back_guns_3");
                        break;
                    case R.id.IVimage4:
                        display.setImageDrawable(null);
                        display.setImageBitmap(decodeSampledBitmapFromResource(getResources(), R.drawable.back_guns_4, 150, 150));
                        toPhone = R.drawable.back_guns_4;
                        uri = Uri.parse("android.resource://br.com.maracujas.classicrockwallpapersfinal/drawable/back_guns_4");
                        break;
                    case R.id.IVimage5:
                        display.setImageDrawable(null);
                        display.setImageBitmap(decodeSampledBitmapFromResource(getResources(), R.drawable.back_guns_5, 150, 150));
                        toPhone = R.drawable.back_guns_5;
                        uri = Uri.parse("android.resource://br.com.maracujas.classicrockwallpapersfinal/drawable/back_guns_5");
                        break;
                }
                break;

            case "skull":
                switch (v.getId()) {
                    case R.id.IVimage1:
                        display.setImageDrawable(null);
                        display.setImageBitmap(decodeSampledBitmapFromResource(getResources(), R.drawable.back_deeppurple_1, 150, 150));
                        toPhone = R.drawable.back_deeppurple_1;
                        uri = Uri.parse("android.resource://br.com.maracujas.classicrockwallpapersfinal/drawable/back_deeppurple_1");
                        break;
                    case R.id.IVimage2:
                        display.setImageDrawable(null);
                        display.setImageBitmap(decodeSampledBitmapFromResource(getResources(), R.drawable.back_deeppurple_2, 150, 150));
                        toPhone = R.drawable.back_deeppurple_2;
                        uri = Uri.parse("android.resource://br.com.maracujas.classicrockwallpapersfinal/drawable/back_deeppurple_2");
                        break;
                    case R.id.IVimage3:
                        display.setImageDrawable(null);
                        display.setImageBitmap(decodeSampledBitmapFromResource(getResources(), R.drawable.back_deeppurple_3, 150, 150));
                        toPhone = R.drawable.back_deeppurple_3;
                        uri = Uri.parse("android.resource://br.com.maracujas.classicrockwallpapersfinal/drawable/back_deeppurple_3");
                        break;
                    case R.id.IVimage4:
                        display.setImageDrawable(null);
                        display.setImageBitmap(decodeSampledBitmapFromResource(getResources(), R.drawable.back_deeppurple_4, 150, 150));
                        toPhone = R.drawable.back_deeppurple_4;
                        uri = Uri.parse("android.resource://br.com.maracujas.classicrockwallpapersfinal/drawable/back_deeppurple_4");
                        break;
                    case R.id.IVimage5:
                        display.setImageDrawable(null);
                        display.setImageBitmap(decodeSampledBitmapFromResource(getResources(), R.drawable.back_deeppurple_5, 150, 150));
                        toPhone = R.drawable.back_deeppurple_5;
                        uri = Uri.parse("android.resource://br.com.maracujas.classicrockwallpapersfinal/drawable/back_deeppurple_5");
                        break;
                }
                break;
        }

        switch (v.getId()) {
            case R.id.BsetWallpaper:
                setWallpaper();
                sound();
                break;
            case R.id.IBSave:
                saveImage();
                sound();
                break;
            case R.id.IBShare:
                shareImage();
                sound();
                break;
            case R.id.IBSound:
                sound();
                break;
            case R.id.BPreview:
                Intent intentPreview = new Intent(WallpaperActivity.this, PreviewActivity.class);
                intentPreview.putExtra("image", toPhone);
                startActivity(intentPreview);
                //	 moveTaskToBack(true);
                break;
        }
    }

    public void setWallpaper() {
        InputStream is = getResources().openRawResource(toPhone);
        Bitmap bm = BitmapFactory.decodeStream(is);
        try {
            Vibrator v = (Vibrator) this.getSystemService(Context.VIBRATOR_SERVICE);
            // Vibrate for 500 milliseconds
            v.vibrate(500);
            getApplicationContext().setWallpaper(bm);
            Toast.makeText(getApplicationContext(), "Image was set as Wallpaper Succesfully!", Toast.LENGTH_LONG).show();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void shareImage() {

        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("image/jpeg");
        shareIntent.putExtra(Intent.EXTRA_TEXT, "Best Classic Rock Wallpapers!");
        shareIntent.putExtra(Intent.EXTRA_TITLE, "SHARE");
        shareIntent.putExtra(Intent.EXTRA_STREAM, uri);
        shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        startActivity(Intent.createChooser(shareIntent, "Share image using"));
    }

    public void saveImage() {
        if (ContextCompat.checkSelfPermission(WallpaperActivity.this,
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(WallpaperActivity.this,
                    new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    14);
        } else {
            String commonPath = Environment.getExternalStorageDirectory().toString() + "/ClassicRock";
            File direct = new File(commonPath);

            if (!direct.exists()) {
                if (direct.mkdir()) {
                    Log.d("tag", "directory created");
                    //directory is created;
                }

            }
            Bitmap bm = BitmapFactory.decodeResource(getResources(), toPhone);
            String strtoPhone = "" + toPhone;
            String nameFile = strtoPhone + ".jpeg";
            OutputStream outStream = null;

            File savingFile = new File(commonPath, nameFile);
            if (!savingFile.exists()) {
                Log.d("tag", "file is created");

                try {

                    savingFile.createNewFile();
                    outStream = new FileOutputStream(savingFile);
                    bm.compress(Bitmap.CompressFormat.JPEG, 100, outStream);
                    outStream.flush();
                    outStream.close();

                    Log.d("tag", "Saved");
                    Toast.makeText(getApplicationContext(), "Image Saved Succesfully!", Toast.LENGTH_LONG).show();

                } catch (FileNotFoundException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();

                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();

                }

            } else {
                Toast.makeText(getApplicationContext(), "Image already Saved!", Toast.LENGTH_LONG).show();

            }
        }
    }

    public void sound() {
        final MediaPlayer mp = new MediaPlayer();
        if (mp.isPlaying()) {
            mp.stop();
            mp.reset();
        }
        try {

            AssetFileDescriptor afd;
            afd = getAssets().openFd("raw_back_in_black_2.mp3");
            mp.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength());
            mp.prepare();
            mp.start();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }




      /*  public  void setNotification(){


            File newSoundFile = new File("/sdcard/media/Ringtone", "myringtone.oog");
            Uri mUri = Uri.parse("android.resource://com.maracujas.whasappbestclassicrockwallpapers/R.raw.raw_back_in_black");
            ContentResolver mCr = getContentResolver();
            AssetFileDescriptor soundFile;
            try {
                soundFile= mCr.openAssetFileDescriptor(mUri, "r");
            } catch (FileNotFoundException e) {
                soundFile=null;
            }

            try {
                byte[] readData = new byte[1024];
                FileInputStream fis = soundFile.createInputStream();
                FileOutputStream fos = new FileOutputStream(newSoundFile);
                int i = fis.read(readData);

                while (i != -1) {
                    fos.write(readData, 0, i);
                    i = fis.read(readData);
                }

                fos.close();
            } catch (IOException io) {
            }
            Log.i("notification", "file created");

            ContentValues values = new ContentValues();
            values.put(MediaStore.MediaColumns.DATA, newSoundFile.getAbsolutePath());
            values.put(MediaStore.MediaColumns.TITLE, "1my ringtone");
            values.put(MediaStore.MediaColumns.MIME_TYPE, "audio/oog");
            values.put(MediaStore.MediaColumns.SIZE, newSoundFile.length());
            values.put(MediaStore.Audio.Media.ARTIST, R.string.app_name);
            values.put(MediaStore.Audio.Media.IS_RINGTONE, true);
            values.put(MediaStore.Audio.Media.IS_NOTIFICATION, true);
            values.put(MediaStore.Audio.Media.IS_ALARM, true);
            values.put(MediaStore.Audio.Media.IS_MUSIC, true);

            Uri uri = MediaStore.Audio.Media.getContentUriForPath(newSoundFile.getAbsolutePath());
            Uri newUri = mCr.insert(uri, values);


            try {
                RingtoneManager.setActualDefaultRingtoneUri(this, RingtoneManager.TYPE_RINGTONE, newUri);
                Toast.makeText(getApplicationContext(), "sound set as ringtone!", Toast.LENGTH_LONG).show();
            } catch (Throwable t) {
                Log.d("notification", "catch exception");
            }


        }*/

    public void onBackPressed() {

        Intent start = new Intent(WallpaperActivity.this, DashboardActivity.class);
        startActivity(start);
        finish();
    }

    @Override
    public void onDestroy() {
        Cleanup();
        super.onDestroy();
        // finish();
    }

    @Override
    protected void onPause() {
        super.onPause();
        System.gc();
    }

    private void Cleanup() {
        System.gc();
        Runtime.getRuntime().gc();
    }

}
