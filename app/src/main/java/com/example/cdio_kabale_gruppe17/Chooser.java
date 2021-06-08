package com.example.cdio_kabale_gruppe17;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class Chooser extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chooser);
        ListView view = findViewById(R.id.chooser_listview);

        List<Pair<Integer, Pair<Card, Integer>>> moves = Algoritme.getInstance().getBestMoves(Board.getInstance());

        List<String> texts = new ArrayList<>();
        List<Integer> picPos = new ArrayList<>();
        for (Pair<Integer, Pair<Card, Integer>> p: moves) {
            texts.add("Move " + p.second.first.getOwnNumber() + " of " + p.second.first.getOwnType() + " from column " + p.second.first.getColumn() + " to column " + p.second.second + " with a score of " + p.first);
            picPos.add(p.second.first.getPicPos());
        }

        ChooseAdapter adapter = new ChooseAdapter(this, texts, PictureHelperClass.getInstance().getPictureList(), picPos);
        view.setAdapter(adapter);

        view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // TODO Fix movecard funktionen
                //Board.getInstance().moveCard(moves.get(position).second.first, Board.getInstance().getBoard().get(moves.get(position).second.first.getColumn()), Board.getInstance().getBoard().get(moves.get(position).second.second));
                Toast.makeText(getApplicationContext(),"Card moved", Toast.LENGTH_SHORT).show();
                Intent i = new Intent(getApplicationContext(), MainActivity.class);
                //Intent i = new Intent(getApplicationContext(), Chooser.class);
                startActivity(i);
            }
        });

    }
}