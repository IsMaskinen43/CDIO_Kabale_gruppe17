package com.example.cdio_kabale_gruppe17;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import org.opencv.android.OpenCVLoader;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private static String TAG = "opencv";
    private static final int PICK_FROM_GALLERY = 1;
    private static final int MY_CAMERA_REQUEST_CODE = 100;
    private static final int CAMERA_REQUEST = 1888;
    private Uri photoUri;
    public ImageView img;
    private SharedPreferences prefs;
    private Button fromCamera, fromGallery;

    static {
        if (!OpenCVLoader.initDebug())
            Log.d(TAG, "Unable to load OpenCV");
        else {
            Log.d(TAG, "OpesnCV loaded");
            Log.d(TAG, "OpesnCV loaded");
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fromCamera = findViewById(R.id.loadbtn);
        fromGallery = findViewById(R.id.req_cam_btn);
        img = findViewById(R.id.test);

        fromCamera.setOnClickListener(this);
        fromGallery.setOnClickListener(this);

        prefs = PreferenceManager.getDefaultSharedPreferences(this);

        tensorController tc = new tensorController(this);
        Log.d(TAG,"output: " +tc.runmodel(BitmapFactory.decodeResource(this.getResources(), R.drawable.h6)));

    }


    @Override
    public void onClick(View v) {

        if (v == fromCamera) {

           /* try {
                if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    requestPermissions(new String[]{Manifest.permission.CAMERA}, MY_CAMERA_REQUEST_CODE);
                } else {
                    showCamera();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }*/
            Intent liveFeed = new Intent(this, com.example.cdio_kabale_gruppe17.liveFeed.class);
            startActivity(liveFeed);


        } else if (v == fromGallery) {
            try {
                if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, PICK_FROM_GALLERY);
                } else {
                    Intent galleryIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(galleryIntent, PICK_FROM_GALLERY);


                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }


    private void startIntent (Bitmap photo){

        CardDetector.getCard(photo, Integer.parseInt(prefs.getString("resizeRatio","4")));

        Intent i = new Intent(this, BilledeActivity.class);
        startActivity(i);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        switch (requestCode) {
            case PICK_FROM_GALLERY:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                        //Intent galleryIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        //startActivityForResult(galleryIntent, PICK_FROM_GALLERY);

                    Intent intent = new Intent();
                    intent.setType("image/*");
                    intent.setAction(Intent.ACTION_GET_CONTENT);//
                    startActivityForResult(Intent.createChooser(intent, "Select File"), PICK_FROM_GALLERY);
                } else {
                    //do something like displaying a message that he didn`t allow the app to access gallery and you wont be able to let him select from gallery
                }
                break;
            case MY_CAMERA_REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    showCamera();

                } else {
                    Toast.makeText(this, "camera permission denied", Toast.LENGTH_LONG).show();
                }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (requestCode == CAMERA_REQUEST ){
            if (resultCode == RESULT_OK) {
                if (photoUri != null) {
                    System.out.println(photoUri);
                    try {
                        Bitmap bm = MediaStore.Images.Media.getBitmap(this.getContentResolver(), photoUri);
                        img.setImageBitmap(bm);
                        startIntent(bm);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

        } else if (requestCode == PICK_FROM_GALLERY && resultCode == MainActivity.RESULT_OK) {
            try {
                Bitmap bm = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), data.getData());
                img.setImageBitmap(bm);
                startIntent(bm);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }



    private void showCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (intent.resolveActivity(this.getPackageManager()) != null) {
            File file = null;
            try {
                file = createImageFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
            photoUri = null;
            if (file != null) {
                photoUri =  FileProvider.getUriForFile(this, BuildConfig.APPLICATION_ID + ".provider",file);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
                startActivityForResult(intent, CAMERA_REQUEST);
            }
        }
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File storageDir = this.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        // File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        return File.createTempFile(timeStamp, ".jpg", storageDir);
    }





}










