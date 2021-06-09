package com.example.cdio_kabale_gruppe17;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
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

        if (goal.get(0).get(0).getOwnNumber() == Card.cardNumber.EMPTY){
            goalPoint4.setImageBitmap(BitmapFactory.decodeResource(this.getResources(), R.drawable.outline));
        } else goalPoint4.setImageBitmap(pics.get(goal.get(0).get(0).getPicPos()+1));

        if (goal.get(1).get(0).getOwnNumber() == Card.cardNumber.EMPTY){
            goalPoint3.setImageBitmap(BitmapFactory.decodeResource(this.getResources(), R.drawable.outline));
        } else goalPoint3.setImageBitmap(pics.get(goal.get(1).get(0).getPicPos()+1));

        if (goal.get(2).get(0).getOwnNumber() == Card.cardNumber.EMPTY){
            goalPoint2.setImageBitmap(BitmapFactory.decodeResource(this.getResources(), R.drawable.outline));
        } else goalPoint2.setImageBitmap(pics.get(goal.get(2).get(0).getPicPos()+1));

        if (goal.get(3).get(0).getOwnNumber() == Card.cardNumber.EMPTY){
            goalPoint1.setImageBitmap(BitmapFactory.decodeResource(this.getResources(), R.drawable.outline));
        } else goalPoint1.setImageBitmap(pics.get(goal.get(3).get(0).getPicPos()+1));




        if (board.get(0).get(0).getOwnNumber() == Card.cardNumber.EMPTY){
            boardPoint7.setImageBitmap(BitmapFactory.decodeResource(this.getResources(), R.drawable.outline));
        } else boardPoint7.setImageBitmap(pics.get(board.get(0).get(0).getPicPos()+1));

        if (board.get(1).get(0).getOwnNumber() == Card.cardNumber.EMPTY){
            boardPoint6.setImageBitmap(BitmapFactory.decodeResource(this.getResources(), R.drawable.outline));
        } else boardPoint6.setImageBitmap(pics.get(board.get(1).get(0).getPicPos()+1));

        if (board.get(2).get(0).getOwnNumber() == Card.cardNumber.EMPTY){
            boardPoint5.setImageBitmap(BitmapFactory.decodeResource(this.getResources(), R.drawable.outline));
        } else boardPoint5.setImageBitmap(pics.get(board.get(2).get(0).getPicPos()+1));

        if (board.get(3).get(0).getOwnNumber() == Card.cardNumber.EMPTY){
            boardPoint4.setImageBitmap(BitmapFactory.decodeResource(this.getResources(), R.drawable.outline));
        } else boardPoint4.setImageBitmap(pics.get(board.get(3).get(0).getPicPos()+1));

        if (board.get(4).get(0).getOwnNumber() == Card.cardNumber.EMPTY){
            boardPoint3.setImageBitmap(BitmapFactory.decodeResource(this.getResources(), R.drawable.outline));
        } else boardPoint3.setImageBitmap(pics.get(board.get(4).get(0).getPicPos()+1));

        if (board.get(5).get(0).getOwnNumber() == Card.cardNumber.EMPTY){
            boardPoint2.setImageBitmap(BitmapFactory.decodeResource(this.getResources(), R.drawable.outline));
        } else boardPoint2.setImageBitmap(pics.get(board.get(5).get(0).getPicPos()+1));

        if (board.get(6).get(0).getOwnNumber() == Card.cardNumber.EMPTY){
            boardPoint1.setImageBitmap(BitmapFactory.decodeResource(this.getResources(), R.drawable.outline));
        } else boardPoint1.setImageBitmap(pics.get(board.get(6).get(0).getPicPos()+1));


        if (hand.get(0).getOwnNumber() == Card.cardNumber.EMPTY){
            hand3.setImageBitmap(BitmapFactory.decodeResource(this.getResources(), R.drawable.outline));
        } else if (hand.get(0).getOwnNumber() == Card.cardNumber.TURNED){
            hand3.setImageBitmap(BitmapFactory.decodeResource(this.getResources(), R.drawable.question));
        } else hand3.setImageBitmap(pics.get(hand.get(0).getPicPos()));

        if (hand.get(1).getOwnNumber() == Card.cardNumber.EMPTY){
            hand2.setImageBitmap(BitmapFactory.decodeResource(this.getResources(), R.drawable.outline));
        } else if (hand.get(1).getOwnNumber() == Card.cardNumber.TURNED){
            hand2.setImageBitmap(BitmapFactory.decodeResource(this.getResources(), R.drawable.question));
        } else hand2.setImageBitmap(pics.get(hand.get(1).getPicPos()));

        if (hand.get(2).getOwnNumber() == Card.cardNumber.EMPTY){
            hand1.setImageBitmap(BitmapFactory.decodeResource(this.getResources(), R.drawable.outline));
        } else if (hand.get(2).getOwnNumber() == Card.cardNumber.TURNED){
            hand1.setImageBitmap(BitmapFactory.decodeResource(this.getResources(), R.drawable.question));
        } else hand1.setImageBitmap(pics.get(hand.get(2).getPicPos()));


        continueButton.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {
        if (v == continueButton){
            startActivity(new Intent(this, MainActivity.class));
        }
    }
}