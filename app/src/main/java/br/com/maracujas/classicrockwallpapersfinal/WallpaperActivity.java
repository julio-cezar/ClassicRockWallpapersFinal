package br.com.maracujas.classicrockwallpapersfinal;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.AssetFileDescriptor;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Vibrator;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ShareCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Locale;

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
    StorageReference RootRef, WallpapersRef, folderRef;
    private static final String KEY_FILE_URI = "key_file_uri";
    private static final String KEY_DOWNLOAD_URL = "key_download_url";
  //  private Uri mDownloadUrl = null;
   // private Uri mFileUri = null;
    //private ProgressDialog mProgressDialog;
    private static final String TAG = "Storage#Wallpaper";
   // private BroadcastReceiver mDownloadReceiver;
    //StorageReference gsReference;

    InterstitialAd mInterstitialAd;

    final long ONE_MEGABYTE = 1024 * 1024;
    final long FOUR_MEGABYTE = 4096 * 4096;
    File f1 = null;
    File f2 = null;
    File f3 = null;
    File f4 = null;
    File f5 = null;
    private File fSave;

   /// Uri fUri;

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

        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId("ca-app-pub-7040951679419231/5264850301");
        mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                requestNewInterstitial();
            }
        });
        requestNewInterstitial();

        storage = FirebaseStorage.getInstance();
        RootRef = storage.getReferenceFromUrl("gs://classic-rock-wallpapers-final.appspot.com");
       WallpapersRef = RootRef.child("wallpapers");

        // Restore instance state
        /*if (savedInstanceState != null) {
            mFileUri = savedInstanceState.getParcelable(KEY_FILE_URI);
            mDownloadUrl = savedInstanceState.getParcelable(KEY_DOWNLOAD_URL);
        }*/
        // gsReference = storage.getReferenceFromUrl("gs://classic-rock-wallpapers-final.appspot.com/wallpapers/fire/fire_1.jpg");

       /* // Download receiver
        mDownloadReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Log.d(TAG, "downloadReceiver:onReceive:" + intent);
                hideProgressDialog();

                if (MyDownloadService.ACTION_COMPLETED.equals(intent.getAction())) {
                    String path = intent.getStringExtra(MyDownloadService.EXTRA_DOWNLOAD_PATH);
                    long numBytes = intent.getLongExtra(MyDownloadService.EXTRA_BYTES_DOWNLOADED, 0);

                    // Alert success
                    showMessageDialog("Success", String.format(Locale.getDefault(),
                            "%d bytes downloaded from %s", numBytes, path));
                }

                if (MyDownloadService.ACTION_ERROR.equals(intent.getAction())) {
                    String path = intent.getStringExtra(MyDownloadService.EXTRA_DOWNLOAD_PATH);

                    // Alert failure
                    showMessageDialog("Error", String.format(Locale.getDefault(),
                            "Failed to download from %s", path));
                }
            }
        };*/
        try {
            f1 = File.createTempFile("images1", "jpg");
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            f2 = File.createTempFile("images2", "jpg");
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            f3 = File.createTempFile("images3", "jpg");
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            f4 = File.createTempFile("images4", "jpg");
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            f5 = File.createTempFile("images5", "jpg");
        } catch (IOException e) {
            e.printStackTrace();
        }


        switch (categoria) {
            case "fire":
                tvCategoria.setText("Fire");

                folderRef = WallpapersRef.child("fire");
                //StorageReference imageRef = folderRef.child("fire_1.jpg");

              //  StorageReference gsReference = storage.getReferenceFromUrl("gs://classic-rock-wallpapers-final.appspot.com/wallpapers/fire/fire_1.jpg");
               /* final long ONE_MEGABYTE = 1024 * 1024;
                gsReference.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                    @Override
                    public void onSuccess(byte[] bytes) {
                        Toast.makeText(getApplicationContext(), "sucesso", Toast.LENGTH_LONG).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        Toast.makeText(getApplicationContext(), "falhou", Toast.LENGTH_LONG).show();
                    }
                });*/
             //   Log.d(TAG, "beforeCreateFiles");




             // Uri  f2uri =  Uri.parse("https://firebasestorage.googleapis.com/v0/b/classic-rock-wallpapers-final.appspot.com/o/wallpapers%2Ffire%2Ffire_1.jpg?alt=media&token=cbafc722-92b6-4488-8814-db1c4a4bd5da");


                //Picasso.with(WallpaperActivity.this).load(f2uri.toString()).networkPolicy(NetworkPolicy.OFFLINE).into(image1);


                folderRef.child("fire_1.jpg").getFile(f1).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                       // Log.d(TAG, "onSuccessaddOnSuccessListener = "+ fUri);
                        //Toast.makeText(getApplicationContext(), "Success", Toast.LENGTH_LONG).show();


                        // Picasso.with(WallpaperActivity.this).load(uri.toString()).networkPolicy(NetworkPolicy.OFFLINE).into(image1);
                    // fUri = fUri;
                       /* Picasso.with(WallpaperActivity.this)
                                .load(fUri.toString())
                                .networkPolicy(NetworkPolicy.OFFLINE)
                                .into(image1, new Callback() {
                                    @Override
                                    public void onSuccess() {

                                    }

                                    @Override
                                    public void onError() {
                                        //Try again online if cache failed
                                        Picasso.with(WallpaperActivity.this)
                                                .load(fUri.toString())
                                                //.error(R.drawable.header)
                                                .into(image1, new Callback() {
                                                    @Override
                                                    public void onSuccess() {

                                                    }

                                                    @Override
                                                    public void onError() {
                                                        Log.v("Picasso","Could not fetch image");
                                                    }
                                                });
                                    }
                                });*/


                        // Local temp file has been created
                         Bitmap bitmap = BitmapFactory.decodeFile(f1.getAbsolutePath());

                        //image1.setImageBitmap(Bitmap.createScaledBitmap(bitmap, 75, 75, false));
                        image1.setImageBitmap(getResizedBitmap(bitmap, 75, 75));

                        // myImageView.setImageBitmap(myBitmap);
                       // image1.setImageBitmap(bitmap);

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        // Handle any errors
                        //Toast.makeText(getApplicationContext(), "Error! Try again later.", Toast.LENGTH_LONG).show();
                        Log.e(TAG, "FailureListener= "+exception.toString());
                    }
                });

                folderRef.child("fire_2.jpg").getFile(f2).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                            // Local temp file has been created
                            Bitmap bitmap = BitmapFactory.decodeFile(f2.getAbsolutePath());
                            // myImageView.setImageBitmap(myBitmap);
                            // image3.setImageBitmap(bitmap);
                            // image3.setImageBitmap(Bitmap.createScaledBitmap(bitmap, 75, 75, false));
                            image2.setImageBitmap(getResizedBitmap(bitmap, 75, 75));

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        // Handle any errors
                        Toast.makeText(getApplicationContext(), "Error! Try again later.", Toast.LENGTH_LONG).show();
                        Log.e(TAG, exception.toString());
                    }
                });

                folderRef.child("fire_3.jpg").getFile(f3).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                        // Local temp file has been created
                        Bitmap bitmap = BitmapFactory.decodeFile(f3.getAbsolutePath());
                        // myImageView.setImageBitmap(myBitmap);
                       // image3.setImageBitmap(bitmap);
                       // image3.setImageBitmap(Bitmap.createScaledBitmap(bitmap, 75, 75, false));
                        image3.setImageBitmap(getResizedBitmap(bitmap, 75, 75));


                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        // Handle any errors
                        Toast.makeText(getApplicationContext(), "Error! Try again later.", Toast.LENGTH_LONG).show();
                        Log.e(TAG, exception.toString());
                    }
                });

                folderRef.child("fire_4.jpg").getFile(f4).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                        // Local temp file has been created
                        Bitmap bitmap = BitmapFactory.decodeFile(f4.getAbsolutePath());
                        // myImageView.setImageBitmap(myBitmap);
                        //image4.setImageBitmap(Bitmap.createScaledBitmap(bitmap, 75, 75, false));
                        image4.setImageBitmap(getResizedBitmap(bitmap, 75, 75));

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        // Handle any errors
                        Toast.makeText(getApplicationContext(), "Error! Try again later.", Toast.LENGTH_LONG).show();
                        Log.e(TAG, exception.toString());
                    }
                });

                folderRef.child("fire_5.jpg").getFile(f5).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                        // Local temp file has been created
                        Bitmap bitmap = BitmapFactory.decodeFile(f5.getAbsolutePath());
                        // myImageView.setImageBitmap(myBitmap);
                       // image5.setImageBitmap(Bitmap.createScaledBitmap(bitmap, 75, 75, false));
                        image5.setImageBitmap(getResizedBitmap(bitmap, 75, 75));

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        // Handle any errors
                        Toast.makeText(getApplicationContext(), "Error! Try again later.", Toast.LENGTH_LONG).show();
                        Log.e(TAG, exception.toString());
                    }
                });

                /*folderRef.child("fire_1.jpg").getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                    @Override
                    public void onSuccess(byte[] bytes) {
                        Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                       image1.setImageBitmap(bitmap);
                        //image1.setImageBitmap(decodeSampledBitmapFromResource(getResources(), R.drawable.fire_1, 75, 75));
                       // Toast.makeText(getApplicationContext(), folderRef.getPath(), Toast.LENGTH_LONG).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        Toast.makeText(getApplicationContext(), exception.toString(), Toast.LENGTH_LONG).show();
                        Log.e(TAG, "img1 ="+exception.toString());
                        //image1.setImageBitmap(decodeSampledBitmapFromResource(getResources(), R.drawable.fire_1, 75, 75));
                    }
                });*/

              /*  folderRef.child("fire_2.jpg").getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                    @Override
                    public void onSuccess(byte[] bytes) {
                        Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                        image2.setImageBitmap(bitmap);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        Toast.makeText(getApplicationContext(), "Error! Try again later.", Toast.LENGTH_LONG).show();
                        Log.e(TAG, "img2 ="+exception.toString());
                    }
                });
                folderRef.child("fire_3.jpg").getBytes(FOUR_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                    @Override
                    public void onSuccess(byte[] bytes) {
                        Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                        image3.setImageBitmap(bitmap);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        Toast.makeText(getApplicationContext(), "Error! Try again later.", Toast.LENGTH_LONG).show();
                        Log.e(TAG, "img3 ="+exception.toString());
                    }
                });

                folderRef.child("fire_4.jpg").getBytes(FOUR_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                    @Override
                    public void onSuccess(byte[] bytes) {
                        Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                        image4.setImageBitmap(bitmap);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        Toast.makeText(getApplicationContext(), "Error! Try again later.", Toast.LENGTH_LONG).show();
                        Log.e(TAG, "img3 ="+exception.toString());
                    }
                });

                folderRef.child("fire_5.jpg").getBytes(FOUR_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                    @Override
                    public void onSuccess(byte[] bytes) {
                        Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                        image5.setImageBitmap(bitmap);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        Toast.makeText(getApplicationContext(), "Error! Try again later.", Toast.LENGTH_LONG).show();
                        Log.e(TAG, "img3 ="+exception.toString());
                    }
                });*/

               // image1.setImageBitmap(decodeSampledBitmapFromResource(getResources(), R.drawable.fire_1, 75, 75));
               // image2.setImageBitmap(decodeSampledBitmapFromResource(getResources(), R.drawable.fire_2, 75, 75));
               // image3.setImageBitmap(decodeSampledBitmapFromResource(getResources(), R.drawable.fire_3, 75, 75));
               // image4.setImageBitmap(decodeSampledBitmapFromResource(getResources(), R.drawable.fire_4, 75, 75));
                //image5.setImageBitmap(decodeSampledBitmapFromResource(getResources(), R.drawable.fire_5, 75, 75));
                break;

            case "rock":
                tvCategoria.setText("Rock");
                folderRef = WallpapersRef.child("rock");

                folderRef.child("rock_1.jpg").getFile(f1).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                        // Local temp file has been created
                        Bitmap bitmap = BitmapFactory.decodeFile(f1.getAbsolutePath());
                        // myImageView.setImageBitmap(myBitmap);
                        // image5.setImageBitmap(Bitmap.createScaledBitmap(bitmap, 75, 75, false));
                        image1.setImageBitmap(getResizedBitmap(bitmap, 75, 75));

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        // Handle any errors
                        Toast.makeText(getApplicationContext(), "Error! Try again later.", Toast.LENGTH_LONG).show();
                        Log.e(TAG, exception.toString());
                    }
                });
                folderRef.child("rock_2.jpg").getFile(f2).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                        // Local temp file has been created
                        Bitmap bitmap = BitmapFactory.decodeFile(f2.getAbsolutePath());
                        // myImageView.setImageBitmap(myBitmap);
                        // image5.setImageBitmap(Bitmap.createScaledBitmap(bitmap, 75, 75, false));
                        image2.setImageBitmap(getResizedBitmap(bitmap, 75, 75));

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        // Handle any errors
                        Toast.makeText(getApplicationContext(), "Error! Try again later.", Toast.LENGTH_LONG).show();
                        Log.e(TAG, exception.toString());
                    }
                });
                folderRef.child("rock_3.jpg").getFile(f3).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                        // Local temp file has been created
                        Bitmap bitmap = BitmapFactory.decodeFile(f3.getAbsolutePath());
                        // myImageView.setImageBitmap(myBitmap);
                        // image5.setImageBitmap(Bitmap.createScaledBitmap(bitmap, 75, 75, false));
                        image3.setImageBitmap(getResizedBitmap(bitmap, 75, 75));

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        // Handle any errors
                        Toast.makeText(getApplicationContext(), "Error! Try again later.", Toast.LENGTH_LONG).show();
                        Log.e(TAG, exception.toString());
                    }
                });
                folderRef.child("rock_4.jpg").getFile(f4).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                        // Local temp file has been created
                        Bitmap bitmap = BitmapFactory.decodeFile(f4.getAbsolutePath());
                        // myImageView.setImageBitmap(myBitmap);
                        // image5.setImageBitmap(Bitmap.createScaledBitmap(bitmap, 75, 75, false));
                        image4.setImageBitmap(getResizedBitmap(bitmap, 75, 75));

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        // Handle any errors
                        Toast.makeText(getApplicationContext(), "Error! Try again later.", Toast.LENGTH_LONG).show();
                        Log.e(TAG, exception.toString());
                    }
                });
                folderRef.child("rock_5.jpg").getFile(f5).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                        // Local temp file has been created
                        Bitmap bitmap = BitmapFactory.decodeFile(f5.getAbsolutePath());
                        // myImageView.setImageBitmap(myBitmap);
                        // image5.setImageBitmap(Bitmap.createScaledBitmap(bitmap, 75, 75, false));
                        image5.setImageBitmap(getResizedBitmap(bitmap, 75, 75));

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        // Handle any errors
                        Toast.makeText(getApplicationContext(), "Error! Try again later.", Toast.LENGTH_LONG).show();
                        Log.e(TAG, exception.toString());
                    }
                });
                /*image1.setImageBitmap(decodeSampledBitmapFromResource(getResources(), R.drawable.rock_1, 75, 75));
                image2.setImageBitmap(decodeSampledBitmapFromResource(getResources(), R.drawable.rock_2, 75, 75));
                image3.setImageBitmap(decodeSampledBitmapFromResource(getResources(), R.drawable.rock_3, 75, 75));
                image4.setImageBitmap(decodeSampledBitmapFromResource(getResources(), R.drawable.rock_4, 75, 75));
                image5.setImageBitmap(decodeSampledBitmapFromResource(getResources(), R.drawable.rock_5, 75, 75));*/
                break;
            case "skull":
                tvCategoria.setText("Skull");
                folderRef = WallpapersRef.child("caveira");

                folderRef.child("skull_1.jpg").getFile(f1).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                        // Local temp file has been created
                        Bitmap bitmap = BitmapFactory.decodeFile(f1.getAbsolutePath());
                        // myImageView.setImageBitmap(myBitmap);
                        // image5.setImageBitmap(Bitmap.createScaledBitmap(bitmap, 75, 75, false));
                        image1.setImageBitmap(getResizedBitmap(bitmap, 75, 75));

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        // Handle any errors
                        Toast.makeText(getApplicationContext(), "Error! Try again later.", Toast.LENGTH_LONG).show();
                        Log.e(TAG, exception.toString());
                    }
                });

                folderRef.child("skull_2.jpg").getFile(f2).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                        // Local temp file has been created
                        Bitmap bitmap = BitmapFactory.decodeFile(f2.getAbsolutePath());
                        // myImageView.setImageBitmap(myBitmap);
                        // image5.setImageBitmap(Bitmap.createScaledBitmap(bitmap, 75, 75, false));
                        image2.setImageBitmap(getResizedBitmap(bitmap, 75, 75));

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        // Handle any errors
                        Toast.makeText(getApplicationContext(), "Error! Try again later.", Toast.LENGTH_LONG).show();
                        Log.e(TAG, exception.toString());
                    }
                });

                folderRef.child("skull_3.jpg").getFile(f3).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                        // Local temp file has been created
                        Bitmap bitmap = BitmapFactory.decodeFile(f3.getAbsolutePath());
                        // myImageView.setImageBitmap(myBitmap);
                        // image5.setImageBitmap(Bitmap.createScaledBitmap(bitmap, 75, 75, false));
                        image3.setImageBitmap(getResizedBitmap(bitmap, 75, 75));

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        // Handle any errors
                        Toast.makeText(getApplicationContext(), "Error! Try again later.", Toast.LENGTH_LONG).show();
                        Log.e(TAG, exception.toString());
                    }
                });

                folderRef.child("skull_4.jpg").getFile(f4).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                        // Local temp file has been created
                        Bitmap bitmap = BitmapFactory.decodeFile(f4.getAbsolutePath());
                        // myImageView.setImageBitmap(myBitmap);
                        // image5.setImageBitmap(Bitmap.createScaledBitmap(bitmap, 75, 75, false));
                        image4.setImageBitmap(getResizedBitmap(bitmap, 75, 75));

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        // Handle any errors
                        Toast.makeText(getApplicationContext(), "Error! Try again later.", Toast.LENGTH_LONG).show();
                        Log.e(TAG, exception.toString());
                    }
                });

                folderRef.child("skull_5.jpg").getFile(f5).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                        // Local temp file has been created
                        Bitmap bitmap = BitmapFactory.decodeFile(f5.getAbsolutePath());
                        // myImageView.setImageBitmap(myBitmap);
                        // image5.setImageBitmap(Bitmap.createScaledBitmap(bitmap, 75, 75, false));
                        image5.setImageBitmap(getResizedBitmap(bitmap, 75, 75));

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        // Handle any errors
                        Toast.makeText(getApplicationContext(), "Error! Try again later.", Toast.LENGTH_LONG).show();
                        Log.e(TAG, exception.toString());
                    }
                });

                /*image1.setImageBitmap(decodeSampledBitmapFromResource(getResources(), R.drawable.skull_1, 75, 75));
                image2.setImageBitmap(decodeSampledBitmapFromResource(getResources(), R.drawable.skull_2, 75, 75));
                image3.setImageBitmap(decodeSampledBitmapFromResource(getResources(), R.drawable.skull_3, 75, 75));
                image4.setImageBitmap(decodeSampledBitmapFromResource(getResources(), R.drawable.skull_4, 75, 75));
                image5.setImageBitmap(decodeSampledBitmapFromResource(getResources(), R.drawable.skull_5, 75, 75));*/
                break;
            case "guitar":
                tvCategoria.setText("Guitar");
                folderRef = WallpapersRef.child("guitarra");

                folderRef.child("guitar_1.jpg").getFile(f1).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                        // Local temp file has been created
                        Bitmap bitmap = BitmapFactory.decodeFile(f1.getAbsolutePath());
                        // myImageView.setImageBitmap(myBitmap);
                        // image5.setImageBitmap(Bitmap.createScaledBitmap(bitmap, 75, 75, false));
                        image1.setImageBitmap(getResizedBitmap(bitmap, 75, 75));

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        // Handle any errors
                        Toast.makeText(getApplicationContext(), "Error! Try again later.", Toast.LENGTH_LONG).show();
                        Log.e(TAG, exception.toString());
                    }
                });

                folderRef.child("guitar_2.jpg").getFile(f2).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                        // Local temp file has been created
                        Bitmap bitmap = BitmapFactory.decodeFile(f2.getAbsolutePath());
                        // myImageView.setImageBitmap(myBitmap);
                        // image5.setImageBitmap(Bitmap.createScaledBitmap(bitmap, 75, 75, false));
                        image2.setImageBitmap(getResizedBitmap(bitmap, 75, 75));

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        // Handle any errors
                        Toast.makeText(getApplicationContext(), "Error! Try again later.", Toast.LENGTH_LONG).show();
                        Log.e(TAG, exception.toString());
                    }
                });

                folderRef.child("guitar_3.jpg").getFile(f3).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                        // Local temp file has been created
                        Bitmap bitmap = BitmapFactory.decodeFile(f3.getAbsolutePath());
                        // myImageView.setImageBitmap(myBitmap);
                        // image5.setImageBitmap(Bitmap.createScaledBitmap(bitmap, 75, 75, false));
                        image3.setImageBitmap(getResizedBitmap(bitmap, 75, 75));

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        // Handle any errors
                        Toast.makeText(getApplicationContext(), "Error! Try again later.", Toast.LENGTH_LONG).show();
                        Log.e(TAG, exception.toString());
                    }
                });

                folderRef.child("guitar_4.jpg").getFile(f4).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                        // Local temp file has been created
                        Bitmap bitmap = BitmapFactory.decodeFile(f4.getAbsolutePath());
                        // myImageView.setImageBitmap(myBitmap);
                        // image5.setImageBitmap(Bitmap.createScaledBitmap(bitmap, 75, 75, false));
                        image4.setImageBitmap(getResizedBitmap(bitmap, 75, 75));

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        // Handle any errors
                        Toast.makeText(getApplicationContext(), "Error! Try again later.", Toast.LENGTH_LONG).show();
                        Log.e(TAG, exception.toString());
                    }
                });

                folderRef.child("guitar_5.jpg").getFile(f5).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                        // Local temp file has been created
                        Bitmap bitmap = BitmapFactory.decodeFile(f5.getAbsolutePath());
                        // myImageView.setImageBitmap(myBitmap);
                        // image5.setImageBitmap(Bitmap.createScaledBitmap(bitmap, 75, 75, false));
                        image5.setImageBitmap(getResizedBitmap(bitmap, 75, 75));

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        // Handle any errors
                        Toast.makeText(getApplicationContext(), "Error! Try again later.", Toast.LENGTH_LONG).show();
                        Log.e(TAG, exception.toString());
                    }
                });

             //  beginDownload();
                /*image1.setImageBitmap(decodeSampledBitmapFromResource(getResources(), R.drawable.gitarra_1, 75, 75));
                image2.setImageBitmap(decodeSampledBitmapFromResource(getResources(), R.drawable.gitarra_2, 75, 75));
                image3.setImageBitmap(decodeSampledBitmapFromResource(getResources(), R.drawable.gitarra_3, 75, 75));
                image4.setImageBitmap(decodeSampledBitmapFromResource(getResources(), R.drawable.gitarra_4, 75, 75));
                image5.setImageBitmap(decodeSampledBitmapFromResource(getResources(), R.drawable.gitarra_5, 75, 75));*/
                break;
            case "no":
                Toast.makeText(getApplicationContext(), "no Image!", Toast.LENGTH_LONG).show();
                break;
        }

    }

    private void requestNewInterstitial() {
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice("F0777154C5F794B0B7A1EF4120502169")
                .build();

        mInterstitialAd.loadAd(adRequest);
    }


    /*private void showMessageDialog(String title, String message) {
        AlertDialog ad = new AlertDialog.Builder(this)
                .setTitle(title)
                .setMessage(message)
                .create();
        ad.show();
    }

    @Override
    public void onSaveInstanceState(Bundle out) {
        super.onSaveInstanceState(out);
        out.putParcelable(KEY_FILE_URI, mFileUri);
        out.putParcelable(KEY_DOWNLOAD_URL, mDownloadUrl);
    }*/

   /* private void beginDownload() {
        // Get path
       // String path = "photos/" + mFileUri.getLastPathSegment();
        //String path = ""+gsReference.getDownloadUrl();
        folderRef = WallpapersRef.child("fire");
        imageRef = folderRef.child("fire_1.jpg");
        String path = imageRef.getPath();
        Log.d(TAG, path);

        // Kick off download service
        Intent intent = new Intent(this, MyDownloadService.class);
        intent.setAction(MyDownloadService.ACTION_DOWNLOAD);
        intent.putExtra(MyDownloadService.EXTRA_DOWNLOAD_PATH, path);
        startService(intent);

        // Show loading spinner
        showProgressDialog();
    }
    private void showProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setMessage("Loading...");
            mProgressDialog.setIndeterminate(true);
        }

        mProgressDialog.show();
    }
    private void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }*/

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

    public Bitmap getResizedBitmap(Bitmap bm, int newWidth, int newHeight) {
        int width = bm.getWidth();
        int height = bm.getHeight();
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        // CREATE A MATRIX FOR THE MANIPULATION
        Matrix matrix = new Matrix();
        // RESIZE THE BIT MAP
        matrix.postScale(scaleWidth, scaleHeight);

        // "RECREATE" THE NEW BITMAP
        Bitmap resizedBitmap = Bitmap.createBitmap(
                bm, 0, 0, width, height, matrix, false);
        bm.recycle();
        return resizedBitmap;
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
       /* if (mInterstitialAd.isLoaded()) {
            mInterstitialAd.show();
        }*/

        Bitmap bitmap;

                switch (v.getId()) {
                    case R.id.IVimage1:
                        if(f1!=null) {
                            display.setImageDrawable(null);
                            bitmap = BitmapFactory.decodeFile(f1.getAbsolutePath());
                            display.setImageBitmap(bitmap);
                            //.setImageBitmap(decodeSampledBitmapFromResource(getResources(), R.drawable.fire_3, 150, 150));
                            fSave = f1;//toPhone = R.drawable.fire_3;
                            //uri = Uri.parse("android.resource://br.com.maracujas.classicrockwallpapersfinal/drawable/fire_3");
                        }

                        /*display.setImageDrawable(null);
                        display.setImageBitmap(decodeSampledBitmapFromResource(getResources(), R.drawable.rock_1, 150, 150));
                        toPhone = R.drawable.rock_1;
                        uri = Uri.parse("android.resource://br.com.maracujas.classicrockwallpapersfinal/drawable/rock_1");*/
                        break;
                    case R.id.IVimage2:
                        if(f2!=null) {
                            display.setImageDrawable(null);
                            bitmap = BitmapFactory.decodeFile(f2.getAbsolutePath());
                            display.setImageBitmap(bitmap);
                            //.setImageBitmap(decodeSampledBitmapFromResource(getResources(), R.drawable.fire_3, 150, 150));
                            fSave = f2;//toPhone = R.drawable.fire_3;
                            //uri = Uri.parse("android.resource://br.com.maracujas.classicrockwallpapersfinal/drawable/fire_3");
                        }
                        break;
                    case R.id.IVimage3:
                        if(f3!=null) {
                            display.setImageDrawable(null);
                            bitmap = BitmapFactory.decodeFile(f3.getAbsolutePath());
                            display.setImageBitmap(bitmap);
                            //.setImageBitmap(decodeSampledBitmapFromResource(getResources(), R.drawable.fire_3, 150, 150));
                            fSave = f3;//toPhone = R.drawable.fire_3;
                            //uri = Uri.parse("android.resource://br.com.maracujas.classicrockwallpapersfinal/drawable/fire_3");
                        }
                        break;
                    case R.id.IVimage4:
                        if(f4!=null) {
                            display.setImageDrawable(null);
                            bitmap = BitmapFactory.decodeFile(f4.getAbsolutePath());
                            display.setImageBitmap(bitmap);
                            //.setImageBitmap(decodeSampledBitmapFromResource(getResources(), R.drawable.fire_3, 150, 150));
                            fSave = f4;//toPhone = R.drawable.fire_3;
                            //uri = Uri.parse("android.resource://br.com.maracujas.classicrockwallpapersfinal/drawable/fire_3");
                        }
                        break;
                    case R.id.IVimage5:
                        if(f5!=null) {
                            display.setImageDrawable(null);
                            bitmap = BitmapFactory.decodeFile(f5.getAbsolutePath());
                            display.setImageBitmap(bitmap);
                            //.setImageBitmap(decodeSampledBitmapFromResource(getResources(), R.drawable.fire_3, 150, 150));
                            fSave = f5;//toPhone = R.drawable.fire_3;
                            //uri = Uri.parse("android.resource://br.com.maracujas.classicrockwallpapersfinal/drawable/fire_3");
                        }
                        break;
                }


        switch (v.getId()) {
            case R.id.BsetWallpaper:
                setWallpaper();
                sound();
                if (mInterstitialAd.isLoaded()) {
                    mInterstitialAd.show();
                }
                break;
            case R.id.IBSave:
                saveImage();
                sound();
                if (mInterstitialAd.isLoaded()) {
                    mInterstitialAd.show();
                }
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
                if(fSave!=null) {
                    intentPreview.putExtra("image", fSave.getAbsolutePath());
                }
                startActivity(intentPreview);
                //	 moveTaskToBack(true);
                break;
        }
    }

    public void setWallpaper() {
        Bitmap bm;
        if(fSave!=null){
             bm = BitmapFactory.decodeFile(fSave.getAbsolutePath());
        } else {
            InputStream is = getResources().openRawResource(toPhone);
             bm = BitmapFactory.decodeStream(is);
        }


        try {
            Vibrator v = (Vibrator) this.getSystemService(Context.VIBRATOR_SERVICE);
            // Vibrate for 500 milliseconds
            v.vibrate(500);
            getApplicationContext().setWallpaper(bm);
            Toast.makeText(getApplicationContext(), R.string.wallpaper_set_sucess, Toast.LENGTH_LONG).show();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void shareImage() {
        Uri contentUri = null;
        if(fSave!=null) {
            Log.d(TAG, "PathImage: " + fSave);
            contentUri = FileProvider.getUriForFile(
                    WallpaperActivity.this, "br.com.maracujas.classicrockwallpapersfinal.fileprovider", fSave);
        }
      /*  Intent shareIntent = ShareCompat.IntentBuilder.from(WallpaperActivity.this)
                .setStream(uriToImage)
                .getIntent();
        // Provide read access
        shareIntent.setData(uriToImage);
        shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        startActivity(Intent.createChooser(shareIntent, "Share image using"));*/

        if (contentUri != null) {
            //Toast.makeText(getApplicationContext(), "" + contentUri, Toast.LENGTH_LONG).show();
            Log.d(TAG, contentUri + "");

            Intent shareIntent = ShareCompat.IntentBuilder.from(WallpaperActivity.this)
                    .setStream(contentUri)
                    .getIntent();
            // Provide read access
            shareIntent.setData(contentUri);
            shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            shareIntent.setType("image/jpg");
            startActivity(Intent.createChooser(shareIntent, "Share image using"));
        } else {
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("image/jpg");
            shareIntent.putExtra(Intent.EXTRA_TEXT, "Best Classic Rock Wallpapers Final!");
            shareIntent.putExtra(Intent.EXTRA_TITLE, "SHARE");

            shareIntent.putExtra(Intent.EXTRA_STREAM, uri);
            startActivity(Intent.createChooser(shareIntent, "Share image using"));
        }

            /*Intent shareIntent = new Intent();
            shareIntent.setAction(Intent.ACTION_SEND);
            shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION); // temp permission for receiving app to read this file
            //shareIntent.setDataAndType(contentUri, getContentResolver().getType(contentUri));
            shareIntent.putExtra(Intent.EXTRA_STREAM, contentUri);
            startActivity(Intent.createChooser(shareIntent, "Share image using"));
            shareIntent.setType("image/jpg");
            shareIntent.setData(contentUri);*




        //Bitmap bitmap = BitmapFactory.decodeFile(f1.getAbsolutePath());

     //   Uri uriToImage =  Uri.fromFile(fSave);
       // Uri.fromFile(fSave)
       // Toast.makeText(getApplicationContext(),""+uriToImage, Toast.LENGTH_LONG).show();
      /*  Uri uriToImage = Uri.parse(f1.getAbsolutePath())*/

    }

    public void saveImage() {
        if (ContextCompat.checkSelfPermission(WallpaperActivity.this,
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(WallpaperActivity.this,
                    new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    14);
        } else {
            String commonPath = Environment.getExternalStorageDirectory().toString() + "/ClassicRockFinal";
            File direct = new File(commonPath);

            if (!direct.exists()) {
                if (direct.mkdir()) {
                    Log.d("tag", "directory created");
                    //directory is created;
                }

            }

            String strtoPhone;
            Bitmap bm;
            if (fSave != null){
                 bm = BitmapFactory.decodeFile(fSave.getAbsolutePath());
                 strtoPhone = "" + fSave.getName();

            }else{
                 bm = BitmapFactory.decodeResource(getResources(), toPhone);
                 strtoPhone = "" + toPhone;
            }
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
                    Toast.makeText(getApplicationContext(), R.string.saved_success, Toast.LENGTH_LONG).show();

                } catch (FileNotFoundException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();

                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();

                }

            } else {
                Toast.makeText(getApplicationContext(), R.string.already_saved, Toast.LENGTH_LONG).show();

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
