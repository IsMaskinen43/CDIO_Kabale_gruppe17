package com.example.cdio_kabale_gruppe17;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import java.util.List;

public class ShowBoard extends AppCompatActivity implements View.OnClickListener{
    private ImageView goalPoint1, goalPoint2, goalPoint3, goalPoint4, boardPoint1, boardPoint2, boardPoint3, boardPoint4, boardPoint5, boardPoint6, boardPoint7, hand1, hand2, hand3;
    private Button continueButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_board);

        continueButton = findViewById(R.id.showBoardContinue);

        goalPoint1 = findViewById(R.id.goalPoint1);
        goalPoint2 = findViewById(R.id.goalPoint2);
        goalPoint3 = findViewById(R.id.goalPoint3);
        goalPoint4 = findViewById(R.id.goalPoint4);

        boardPoint1 = findViewById(R.id.boardPoint1);
        boardPoint2 = findViewById(R.id.boardPoint2);
        boardPoint3 = findViewById(R.id.boardPoint3);
        boardPoint4 = findViewById(R.id.boardPoint4);
        boardPoint5 = findViewById(R.id.boardPoint5);
        boardPoint6 = findViewById(R.id.boardPoint6);
        boardPoint7 = findViewById(R.id.boardPoint7);

        hand1 = findViewById(R.id.hand1);
        hand2 = findViewById(R.id.hand2);
        hand3 = findViewById(R.id.hand3);


        List<List<Card>> board = Board.getInstance().getBoard();
        List<List<Card>> goal = Board.getInstance().getGoalPoints();
        List<Card> hand = Board.getInstance().getHand();
        List<Bitmap> pics = PictureHelperClass.getInstance().getPictureList();
        List<Pair<Integer, Integer>> bannedPositions = Board.getInstance().getBannedCards();

        // set the correct pic on the goal positions
        for (int i = 0; i < goal.size(); i++) {
            ImageView currentView = null;
            switch (i){
                case 0:
                    currentView = goalPoint4;
                    break;
                case 1:
                    currentView = goalPoint3;
                    break;
                case 2:
                    currentView = goalPoint2;
                    break;
                case 3:
                    currentView = goalPoint1;
                    break;
            }
            if (goal.get(i).get(0).getOwnNumber() == Card.cardNumber.EMPTY || bannedPositions.contains(new Pair<>(goal.get(i).get(0).getxCoord(), goal.get(i).get(0).getyCoord()))) {
                currentView.setImageBitmap(BitmapFactory.decodeResource(this.getResources(), R.drawable.outline));

            } else{
                currentView.setImageBitmap(pics.get(goal.get(i).get(0).getPicPos()+1));
            }
        }


        // set the correct pic on the board positions
        for (int i = 0; i < board.size(); i++) {
            ImageView currentView = null;
            switch (i){
                case 0:
                    currentView = boardPoint7;
                    break;
                case 1:
                    currentView = boardPoint6;
                    break;
                case 2:
                    currentView = boardPoint5;
                    break;
                case 3:
                    currentView = boardPoint4;
                    break;
                case 4:
                    currentView = boardPoint3;
                    break;
                case 5:
                    currentView = boardPoint2;
                    break;
                case 6:
                    currentView = boardPoint1;
                    break;
            }

            if (board.get(i).get(0).getOwnNumber() == Card.cardNumber.EMPTY || bannedPositions.contains(new Pair<>(board.get(i).get(0).getxCoord(), board.get(i).get(0).getyCoord()))){
                currentView.setImageBitmap(BitmapFactory.decodeResource(this.getResources(), R.drawable.outline));
            } else{
                currentView.setImageBitmap(pics.get(board.get(i).get(0).getPicPos()+1));
            }
        }

        // set the correct pic on the hand positions
        for (int i = 0; i < hand.size(); i++) {
            ImageView currentView = null;
            switch (i){
                case 0:
                    currentView = hand3;
                    break;
                case 1:
                    currentView = hand2;
                    break;
                case 2:
                    currentView = hand1;
                    break;
            }
            if (hand.get(i).getOwnNumber() == Card.cardNumber.EMPTY || bannedPositions.contains(new Pair<>(hand.get(i).getxCoord(), hand.get(i).getyCoord()))){
                currentView.setImageBitmap(BitmapFactory.decodeResource(this.getResources(), R.drawable.outline));
            } else if (hand.get(i).getOwnNumber() == Card.cardNumber.TURNED){
                currentView.setImageBitmap(BitmapFactory.decodeResource(this.getResources(), R.drawable.question));
            } else {
                currentView.setImageBitmap(pics.get(hand.get(i).getPicPos()+1));
            }
        }

        continueButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v == continueButton){
            startActivity(new Intent(this, MainActivity.class));
        }
    }
}