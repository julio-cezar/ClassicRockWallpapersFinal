package br.com.maracujas.classicrockwallpapersfinal;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public class WallpaperActivity extends AppCompatActivity {

    ImageView display,image1,image2,image3,image4,image5;
    Button setWall,bPreview;
    ImageButton ibshare, ibsave, ibsound ;
    int toPhone;
    Uri uri;
    Bundle extras;
    String banda;
    TextView tvBanda;
    FirebaseStorage storage;
    StorageReference RootRef,WallpapersRef,folderRef,imageRef;
    final long ONE_MEGABYTE = 1024 * 1024;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_wallpaper);
        extras = getIntent().getExtras();
        banda="no";

        if (extras != null) {
            banda = extras.getString("banda");
        }
        iniciar();

        toPhone = R.mipmap.ideia_icon_grande;
        uri = Uri.parse("android.resource://br.com.maracujas.classicrockwallpapersfinal/mipmap/ideia_icon_grande");
        display.setImageBitmap(decodeSampledBitmapFromResource(getResources(), R.mipmap.ideia_icon_grande, 150, 150));
        //Bitmap bMap = BitmapFactory.decodeResource(getResources(), R.drawable.ideia_icon_grande);
        //display.setImageBitmap(bMap);

        storage = FirebaseStorage.getInstance();
        RootRef = storage.getReferenceFromUrl("gs://classic-rock-wallpapers-final.appspot.com");
        WallpapersRef = RootRef.child("wallpapers");
        folderRef = WallpapersRef.child("caveira");

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

    private void iniciar () {
        display  = (ImageView) findViewById(R.id.IVDisplay);
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
         tvBanda = (TextView) findViewById(R.id.tvBanda);
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




}
