package com.example.cdio_kabale_gruppe17;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class BilledeAdapter extends ArrayAdapter<Bitmap>  {
    private List<Bitmap> pictures = new ArrayList<>();
    private Context context;

    public BilledeAdapter(@NonNull Context ctx, @NonNull List<Bitmap> pics){
        super(ctx, 0, pics);
        this.context = ctx;
        this.pictures = pics;
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

        return listItem;
    }

}
