package com.example.cdio_kabale_gruppe17;

import androidx.appcompat.app.AppCompatActivity;

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
        Bitmap bitmap = BitmapFactory.decodeResource(this.getResources(), R.drawable.kort);

        // get pixels of the card detected
        int[] pixels = CardDetector.getCard(bitmap);

        // Create bitmap from the pixels of the card
        Bitmap nytbitmap = Bitmap.createBitmap(CardDetector.width, CardDetector.height, Bitmap.Config.ARGB_8888);

        // Set the pixels into the bitmap
        nytbitmap.setPixels(pixels, 0, CardDetector.width, 0, 0, CardDetector.width, CardDetector.height);

        // override the old image with the new
        kortBillede.setImageBitmap(nytbitmap);

    }

}