package com.example.cdio_kabale_gruppe17;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
    private List<Integer> picPos = new ArrayList<>(), otherPicPos = new ArrayList<>();

    public ChooseAdapter(@NonNull Context ctx, @NonNull List<String> texts, @NonNull List<Bitmap> pictures, List<Integer> picPos, List<Integer> otherPicPos){
        super(ctx, 0, texts);
        this.context = ctx;
        this.texts = texts;
        this.pictures = pictures;
        this.picPos = picPos;
        this.otherPicPos = otherPicPos;
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
        ImageView billede2 = listItem.findViewById(R.id.chooserBillede2);
        ImageView arrow = listItem.findViewById(R.id.chooserBilledeArrow);
        // first picture is the grayscale so we don't want to show that again
        if (texts.get(position).equals("Get some new cards by flipping the hand stack")){
            billede.setImageBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.bunke));
            billede2.setVisibility(View.INVISIBLE);
            arrow.setVisibility(View.INVISIBLE);
        } else {
            billede.setImageBitmap(pictures.get(picPos.get(position) + 1));
            billede2.setVisibility(View.VISIBLE);
            arrow.setVisibility(View.VISIBLE);
            if (otherPicPos.get(position) != 0) {
                billede2.setImageBitmap(pictures.get(otherPicPos.get(position) + 1));
            } else billede2.setImageBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.outline));
        }



        return listItem;
    }


}
