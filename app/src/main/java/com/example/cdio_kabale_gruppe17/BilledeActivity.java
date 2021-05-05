package com.example.cdio_kabale_gruppe17;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class BilledeActivity extends AppCompatActivity {
    private LinearLayout layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.billeder);

        layout = findViewById(R.id.linearLayoutBillede);

        List<Bitmap> bitmapList = new ArrayList<>();

        for (int i = 0; i < CardDetector.pixels.size(); i++) {
            // Create bitmap from the pixels of the card
            Bitmap nytbitmap = Bitmap.createBitmap(CardDetector.width.get(i), CardDetector.height.get(i), Bitmap.Config.ARGB_8888);

            // Set the pixels into the bitmap
            nytbitmap.setPixels(CardDetector.pixels.get(i), 0, CardDetector.width.get(i), 0, 0, CardDetector.width.get(i), CardDetector.height.get(i));

            // override the old image with the new
            bitmapList.add(nytbitmap);
        }

        BilledeAdapter adapter = new BilledeAdapter(this, bitmapList);
        for (int i = 0; i < bitmapList.size(); i++) {
            View v = adapter.getView(i, null, null);
            layout.addView(v);
        }
    }
}
