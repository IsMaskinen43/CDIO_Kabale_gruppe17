package com.example.cdio_kabale_gruppe17;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

import org.opencv.android.OpenCVLoader;

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

        kortBillede = findViewById(R.id.kortBillede);
        Bitmap bitmap = BitmapFactory.decodeResource(this.getResources(), R.drawable.kort2);
        kortBillede.setImageBitmap(bitmap);

        CardDetector.getCard(bitmap, 4);

        Intent i = new Intent(this, BilledeActivity.class);
        startActivity(i);

    }

}