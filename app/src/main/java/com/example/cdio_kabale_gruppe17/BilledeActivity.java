package com.example.cdio_kabale_gruppe17;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Pair;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class BilledeActivity extends AppCompatActivity implements View.OnClickListener{
    private LinearLayout layout;
    private TextView seekbarText;
    private SeekBar seekbarBar;
    private Context ctx;
    private Board currBoard;
    private Button continueButton;
    private List<Bitmap> bitmapList = new ArrayList<>();
    private List<Bitmap> bitmapListSliced = new ArrayList<>();
    private SharedPreferences prefs;
    private int offset = 0;
    private int prevPos;
    private List<String> cardInfoGlobal = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_billeder);

        ctx = this;
        currBoard = Board.getInstance();
        prefs = PreferenceManager.getDefaultSharedPreferences(this);

        layout = findViewById(R.id.linearLayoutBillede);
        seekbarBar = findViewById(R.id.resizerBar);
        seekbarText = findViewById(R.id.resizerText);
        seekbarBar.setProgress(Integer.parseInt(prefs.getString("resizeRatio","4")));
        seekbarText.setText("Downscale size: " + seekbarBar.getProgress());
        continueButton = findViewById(R.id.continueButton);

        setViews();

        continueButton.setOnClickListener(this);

        seekbarBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (ctx != null) {
                    prefs.edit().putString("resizeRatio", progress + "").apply();
                    seekbarText.setText("Downscale size: " + progress);
                    CardDetector.getCard(null, progress);
                    setViews();
                }
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
        bitmapList.clear();
        bitmapList.add(CardDetector.grayScale);
        cardInfoGlobal.clear();
        for (int i = 0; i < CardDetector.pixels.size(); i++) {
            // Create bitmap from the pixels of the card
            Bitmap nytbitmap = Bitmap.createBitmap(CardDetector.width.get(i), CardDetector.height.get(i), Bitmap.Config.ARGB_8888);

            // Set the pixels into the bitmap
            nytbitmap.setPixels(CardDetector.pixels.get(i), 0, CardDetector.width.get(i), 0, 0, CardDetector.width.get(i), CardDetector.height.get(i));

            // override the old image with the new
            bitmapList.add(nytbitmap);
        }

        bitmapListSliced.clear();
        bitmapListSliced.add(CardDetector.grayScale);
        for (int i = 0; i < CardDetector.pixels.size(); i++) {
            // Create bitmap from the pixels of the card
            Bitmap nytbitmap = Bitmap.createBitmap(CardDetector.width.get(i)/4, CardDetector.height.get(i)/4, Bitmap.Config.ARGB_8888);

            // Set the pixels into the bitmap
            nytbitmap.setPixels(CardDetector.pixels.get(i), 0, CardDetector.width.get(i), 0, 0, CardDetector.width.get(i)/4, CardDetector.height.get(i)/4);

            // override the old image with the new
            bitmapListSliced.add(nytbitmap);
        }

        tensorController tc = new tensorController(this);

        for (int i = 1; i < bitmapList.size(); i++) {
            cardInfoGlobal.add(tc.runmodel(bitmapList.get(i)));
        }

        layout.removeAllViews();
        BilledeAdapter adapter = new BilledeAdapter(ctx, bitmapList, cardInfoGlobal);
        for (int i = 0; i < bitmapList.size(); i++) {
            View v = adapter.getView(i, null, null);

            layout.addView(v);

        }


    }

    public void removeBitmap(int position){
        boolean found = false;
        CardDetector.xCoords.remove(position-1);
        CardDetector.yCoords.remove(position-1);
        CardDetector.pixels.remove(position-1);
        CardDetector.width.remove(position-1);
        CardDetector.height.remove(position-1);
        cardInfoGlobal.remove(position-1);


        // add the removed bitmap's card to a banned pool when getting all moves
        for (List<Card> l: currBoard.getCards()) {
            for (Card c: l) {
                if (c.getPicPos() == position-1){
                    currBoard.addBannedCard(new Pair<>(c.getxCoord(), c.getyCoord()));
                    found = true;
                    break;
                }
            }
            if (found) break;
        }

        // move all the picpositions higher than the current one down with 1
        for (List<Card> l: currBoard.getCards()) {
            for (Card c: l) {
                if (c.getPicPos() >= position-1){
                    c.setPicPos(c.getPicPos()-1);
                }
            }
        }

        // remove the bitmap and reinstantiate the adapter view
        bitmapList.remove(position);
        bitmapListSliced.remove(position);
        layout.removeAllViews();

        BilledeAdapter adapter = new BilledeAdapter(ctx, bitmapList, cardInfoGlobal);
        for (int i = 0; i < bitmapList.size(); i++) {
            View v = adapter.getView(i, null, null);

            layout.addView(v);

        }

    }


    @Override
    public void onClick(View v) {
        if (v == continueButton) {
            ArrayList<String> cardInfo = new ArrayList<>(cardInfoGlobal);

            currBoard.instantiate(cardInfo);
            currBoard.printBoard();
            PictureHelperClass.getInstance().setPictureList(bitmapList);
            Intent i = new Intent(this, Chooser.class);
            startActivity(i);
        }
    }

    public void changeSpinnerItem(String newString, int position){
        cardInfoGlobal.set(position, newString);
    }

}















