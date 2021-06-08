package com.example.cdio_kabale_gruppe17;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class ChooseAdapter extends ArrayAdapter<String> {
    private List<String> texts = new ArrayList<>();
    private Context context = null;
    private List<Bitmap> pictures = new ArrayList<>();
    private List<Integer> picPos = new ArrayList<>();

    public ChooseAdapter(@NonNull Context ctx, @NonNull List<String> texts, @NonNull List<Bitmap> pictures, List<Integer> picPos){
        super(ctx, 0, texts);
        this.context = ctx;
        this.texts = texts;
        this.pictures = pictures;
        this.picPos = picPos;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItem = convertView;
        if(listItem == null)
            // Vi inflater det relative layout som vi har lavet i xml
            listItem = LayoutInflater.from(context).inflate(R.layout.choose_element,parent,false);

        TextView textView = listItem.findViewById(R.id.chooseText);
        textView.setText(texts.get(position));

        ImageView billede = listItem.findViewById(R.id.chooserBillede);
        // first picture is the grayscale so we don't want to show that again
        billede.setImageBitmap(pictures.get(picPos.get(position)+1));

        return listItem;
    }


}
