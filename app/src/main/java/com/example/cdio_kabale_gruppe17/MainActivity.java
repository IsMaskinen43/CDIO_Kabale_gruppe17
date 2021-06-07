package com.example.cdio_kabale_gruppe17;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import org.opencv.android.OpenCVLoader;

import java.io.FileNotFoundException;
import java.io.InputStream;

public class MainActivity extends AppCompatActivity {
    private static String TAG = "opencv";
    private ImageView kortBillede;

    static {
        if (!OpenCVLoader.initDebug())
            Log.d(TAG, "Unable to load OpenCV");
        else {
            Log.d(TAG, "OpenCV loaded");
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //PictureMaker.getInstance().picBool(this,this);
        //while(checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED){

        //}
        //PictureMaker.getInstance().uploadPic(this);

        kortBillede = findViewById(R.id.kortBillede);
        Bitmap bitmap = BitmapFactory.decodeResource(this.getResources(), R.drawable.kort4);
        kortBillede.setImageBitmap(bitmap);

        CardDetector.getCard(bitmap, 4);

        Intent i = new Intent(this, BilledeActivity.class);
        startActivity(i);

    }

    @Override
    protected void onActivityResult(int reqCode, int resultCode, Intent data) {
        super.onActivityResult(reqCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            try {
                // Først konverterer vi URI'en om til et bitmap som vi kan sende til firestore
                final Uri imageUri = data.getData();
                final InputStream imageStream = getContentResolver().openInputStream(imageUri);
                final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);

                kortBillede = findViewById(R.id.kortBillede);
                Bitmap bitmap = BitmapFactory.decodeResource(this.getResources(), R.drawable.kort2);
                kortBillede.setImageBitmap(selectedImage);

                CardDetector.getCard(selectedImage, 4);

                Intent i = new Intent(this, BilledeActivity.class);
                startActivity(i);


            } catch (FileNotFoundException e) {
                e.printStackTrace();
                Toast.makeText(this, "Der skete en fejl ved indlæsning af billedet. Prøv venligst igen", Toast.LENGTH_LONG).show();
            }
        }else {
            Toast.makeText(this, "Der blev ikke valgt noget billede",Toast.LENGTH_LONG).show();
        }
    }

}