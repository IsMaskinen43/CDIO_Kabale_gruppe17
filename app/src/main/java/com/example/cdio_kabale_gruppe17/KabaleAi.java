package com.example.cdio_kabale_gruppe17;

import android.util.Pair;

import java.util.ArrayList;
import java.util.List;

public class KabaleAi {
    Board Current;
    Algoritme alg = new Algoritme();
    int iteration;
    int maxDepth;

    public KabaleAi( int iteration, int maxDepth){
        this.iteration = iteration;
        this.maxDepth = maxDepth;
    }

    public void run(Board board){
        int[] list = new int[10];
        int count = 0;
        for(Pair<Integer,Pair<Card,Integer>> pair : alg.getBestMoves(board)){
            for (int i =0; i < iteration; i++) {

             //   list[count] += turn(newBoard.moveCard(pair.second.first, newBoard.getBoard().get(pair.second.first.getColumn()), newBoard.getGoalPoints().get(pair.second.second)),0);

            }
            list[count] = list[count]/iteration;
        }
        for(int i : list){
            System.out.println(i);
        }
    }


    private int turn(Board board, int depth){

            if(alg.getBestMoves(board).isEmpty()|| depth == maxDepth)
                return evalBoard(board);
            Pair<Integer, Pair<Card,Integer>> pair = alg.getBestMoves(board).get((int) (Math.random() * 9));
            board.moveCard(pair.second.first, board.getBoard().get(pair.second.first.getColumn()), board.getGoalPoints().get(pair.second.second));
            return turn(board, depth+1);

    }

    private int evalBoard(Board board){
        List<List<Card>> currBoard = board.getBoard();
        int point = 0;
       for(int l =0; l <7; l++){

           // Check for king on an empty spot

           if (l < 7) {
               if (currBoard.get(l).get(currBoard.get(l).size() - 1).getOwnNumber() == Card.cardNumber.EMPTY )
                   point += 5;
           }

           // get the amount of turned cards in every column
           List<Integer> turnedAmount = new ArrayList<>();
           int index = 0;
           for (List<Card> col: currBoard) {
             index += alg.getTurnedAmount(col);
           }
           // sort it by number of turned cards

           // get the index of the current cards column
                      // if there are more turned cards than half the columns then we want to expose it
           if (index >= Math.floor(turnedAmount.size()/2.0)) point += 4;
       }
       for(List<Card> list : board.getGoalPoints()){
           if(list.get(list.size()-1).getOwnNumber() == Card.cardNumber.KING){
               point+=10;
           }
           point += list.size()*2;
       }

       return point;
    }


}
