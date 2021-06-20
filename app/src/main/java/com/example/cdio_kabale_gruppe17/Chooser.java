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
        List<Integer> otherPicPos = new ArrayList<>();
        for (Pair<Integer, Pair<Card, Integer>> p: moves) {
            if (p.second.first.getOwnNumber() == Card.cardNumber.HAND){
                texts.add("Get some new cards by flipping the hand stack");
                otherPicPos.add(0);
            }
            else if (p.second.second < 7) {
                Card otherCard = Board.getInstance().getBoard().get(p.second.second).get(Board.getInstance().getBoard().get(p.second.second).size()-1);
                if (otherCard.getOwnNumber() != Card.cardNumber.EMPTY) {
                    texts.add("Move " + p.second.first.getOwnNumber() + " of " + p.second.first.getOwnType() + " from column " + (7 - p.second.first.getColumn()) + " to " + otherCard.getOwnNumber() + " of " + otherCard.getOwnType() + " at column " + (7 - p.second.second) + " with a score of " + p.first);
                } else texts.add("Move " + p.second.first.getOwnNumber() + " of " + p.second.first.getOwnType() + " from column " + (7 - p.second.first.getColumn()) + " to column " + (7 - p.second.second) + " with a score of " + p.first);
                otherPicPos.add(otherCard.getPicPos());
            } else {
                texts.add("Move " + p.second.first.getOwnNumber() + " of " + p.second.first.getOwnType() + " from column " + (7 - p.second.first.getColumn()) + " to goal point " + Math.abs(11 - p.second.second-4) + " with a score of " + p.first);
                otherPicPos.add(Board.getInstance().getGoalPoints().get(Math.abs(11 - p.second.second-4)).get(Board.getInstance().getGoalPoints().get(Math.abs(11 - p.second.second-4)).size()-1).getPicPos());
            }

            picPos.add(p.second.first.getPicPos());


        }

        ChooseAdapter adapter = new ChooseAdapter(this, texts, PictureHelperClass.getInstance().getPictureList(), picPos, otherPicPos);
        view.setAdapter(adapter);

        view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // set bool according to if we got new cards from hand or not
                Board.getInstance().setTurnedHand(moves.get(position).second.first.getOwnNumber() == Card.cardNumber.HAND);
                if (moves.get(position).second.second < 7) {
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