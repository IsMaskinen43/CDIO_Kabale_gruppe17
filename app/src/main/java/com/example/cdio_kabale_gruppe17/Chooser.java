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
            if (p.second.first.getOwnNumber() == Card.cardNumber.HAND){
                texts.add("Get some new cards by flipping the hand stack");
            }
            else if (p.second.second < 7) {
                texts.add("Move " + p.second.first.getOwnNumber() + " of " + p.second.first.getOwnType() + " from column " + (7 - p.second.first.getColumn()) + " to column " + (7 - p.second.second) + " with a score of " + p.first);
            } else {
                texts.add("Move " + p.second.first.getOwnNumber() + " of " + p.second.first.getOwnType() + " from column " + (7 - p.second.first.getColumn()) + " to goal point " + (11 - p.second.second) + " with a score of " + p.first);
            }

            picPos.add(p.second.first.getPicPos());

        }

        ChooseAdapter adapter = new ChooseAdapter(this, texts, PictureHelperClass.getInstance().getPictureList(), picPos);
        view.setAdapter(adapter);

        view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (moves.get(position).second.second < 7) {
                    // TODO Fix movecard funktionen
                    Board.getInstance().moveCard(moves.get(position).second.first, Board.getInstance().getBoard().get(moves.get(position).second.first.getColumn()), Board.getInstance().getBoard().get(moves.get(position).second.second));

                } else {
                    Board.getInstance().moveCard(moves.get(position).second.first, Board.getInstance().getBoard().get(moves.get(position).second.first.getColumn()), Board.getInstance().getGoalPoints().get(10-moves.get(position).second.second));
                }

                Toast.makeText(getApplicationContext(), "Card moved", Toast.LENGTH_SHORT).show();
                Intent i = new Intent(getApplicationContext(), ShowBoard.class);
                //Intent i = new Intent(getApplicationContext(), Chooser.class);
                startActivity(i);

            }
        });

    }
}