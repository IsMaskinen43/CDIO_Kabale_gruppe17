package com.example.cdio_kabale_gruppe17;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class BilledeActivity extends AppCompatActivity {
    private LinearLayout layout;
    private TextView seekbarText;
    private SeekBar seekbarBar;
    private Context ctx;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.billeder);

        ctx = this;

        layout = findViewById(R.id.linearLayoutBillede);
        seekbarBar = findViewById(R.id.resizerBar);
        seekbarText = findViewById(R.id.resizerText);
        seekbarText.setText("Downscale size: " + seekbarBar.getProgress());

        setViews();

        seekbarBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                seekbarText.setText("Downscale size: " + progress);
                CardDetector.getCard(null, progress);
                setViews();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });


    }

    public void setViews(){
        List<Bitmap> bitmapList = new ArrayList<>();
        for (int i = 0; i < CardDetector.pixels.size(); i++) {
            // Create bitmap from the pixels of the card
            Bitmap nytbitmap = Bitmap.createBitmap(CardDetector.width.get(i), CardDetector.height.get(i), Bitmap.Config.ARGB_8888);

            // Set the pixels into the bitmap
            nytbitmap.setPixels(CardDetector.pixels.get(i), 0, CardDetector.width.get(i), 0, 0, CardDetector.width.get(i), CardDetector.height.get(i));

            // override the old image with the new
            bitmapList.add(nytbitmap);
        }
        bitmapList.add(CardDetector.grayScale);
        bitmapList.add(CardDetector.scaleTest);
        layout.removeAllViews();
        BilledeAdapter adapter = new BilledeAdapter(ctx, bitmapList);
        for (int i = 0; i < bitmapList.size(); i++) {
            View v = adapter.getView(i, null, null);
            layout.addView(v);
        }
    }

}
