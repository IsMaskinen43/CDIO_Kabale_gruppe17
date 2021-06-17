package com.example.cdio_kabale_gruppe17;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class BilledeAdapter extends ArrayAdapter<Bitmap>  {
    private List<Bitmap> pictures = new ArrayList<>();
    private List<String> cards = new ArrayList<>();
    private Context context;
    private BilledeActivity activity;

    public BilledeAdapter(@NonNull Context ctx, @NonNull List<Bitmap> pics, @NonNull List<String> cards){
        super(ctx, 0, pics);
        this.context = ctx;
        this.pictures = pics;
        this.cards = cards;
        this.activity = (BilledeActivity) ctx;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItem = convertView;
        if(listItem == null)
            // Vi inflater det relative layout som vi har lavet i xml
            listItem = LayoutInflater.from(context).inflate(R.layout.billede_element,parent,false);

        ImageView billede = listItem.findViewById(R.id.billede);
        Button delete = listItem.findViewById(R.id.del_btn);
        if(position != 0) {

            delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((BilledeActivity) context).removeBitmap(position);
                }
            });
        } else delete.setVisibility(View.INVISIBLE);
        billede.setImageBitmap(pictures.get(position));

        Spinner dropdown = listItem.findViewById(R.id.dropdown_cards);
        if(position != 0) {
            ArrayAdapter<CharSequence> dropDownAdapter = ArrayAdapter.createFromResource(getContext(), R.array.createDropDown, R.layout.spinner_item);
            dropdown.setAdapter(dropDownAdapter);
            dropdown.setSelection(getIndex(dropdown, cards.get(position-1)));
            int currentPos = position;
            dropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    activity.changeSpinnerItem(dropdown.getItemAtPosition(position).toString(),currentPos-1);
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });


        } else dropdown.setVisibility(View.INVISIBLE);

        return listItem;
    }

    private int getIndex(Spinner spinner, String myString){

        int index = 0;

        for (int i=0;i<spinner.getCount();i++){
            if (spinner.getItemAtPosition(i).equals(myString)){
                index = i;
            }
        }
        return index;
    }
}
