package com.example.cdio_kabale_gruppe17;

import android.util.Pair;
import java.util.ArrayList;
import java.util.List;

public class Board {
    private static Board instance = null;
    private static List<List<Card>> board = new ArrayList<>();
    private static List<List<Card>> goalPoints = new ArrayList<>();


    private Board(){
    }

    public static Board getInstance(){
        if (instance == null){
            instance = new Board();
        }
        return instance;
    }


    // instantiate the board with some lists based on the columns
    public void instantiate(){
        // set the margin that the distance between cards in same column can be
        float margin = 0.5f;

        // instantiate the seven columns
        board.clear();
        goalPoints.clear();
        for (int i = 0; i < 7; i++) {
            board.add(new ArrayList<>());
        }

        // instantiate the four goal points
        for (int i = 0; i < 4; i++) {
            goalPoints.add(new ArrayList<>());
        }

        // find the max and min points in the x axis
        int maxX=Integer.MIN_VALUE, minX=Integer.MAX_VALUE;
        for (int i = 0; i < CardDetector.xCoords.size(); i++) {
            maxX = Math.max(maxX, CardDetector.xCoords.get(i));
            minX = Math.min(minX, CardDetector.xCoords.get(i));
        }

        // get the estimated distance between each column
        float dx = (maxX - minX) / 7.0f;

        // make a list with the estimated position of the cards in the columns (this will be used together with a margin to estimate better which cards are in the same column)
        List<Float> columnList = new ArrayList<>();
        for (int i = 0; i < CardDetector.xCoords.size(); i++) {
            columnList.add((CardDetector.xCoords.get(i)-minX)/dx);
        }

        // set up the board based on the average x coord and margin
        List<Float> averages = new ArrayList<>();
        List<Integer> numberOfAverages = new ArrayList<>();
        // calculate how many columns there are on the board
        for (int i = 0; i < columnList.size(); i++) {
            boolean isInMargin = false;
            // få kort fra ML af
            // TODO lav om til machine learning i stedet for predefined
            Card currentCard = new Card(Card.cardColor.BLACK, Card.cardNumber.ACE, CardDetector.yCoords.get(i), CardDetector.xCoords.get(i));

            // if there is no other averages in the list add this one
            if (averages.isEmpty()){
                averages.add(columnList.get(i));
                numberOfAverages.add(1);
                board.get(averages.size()-1).add(currentCard);
            }
            // else we check if this point is in the margin of the average
            else{
                for (int j = 0; j < averages.size(); j++) {
                    // if it is in the margin add it to that list and calculate average
                    if (averages.get(j)+margin > columnList.get(i) && averages.get(j)-margin < columnList.get(i)){
                        numberOfAverages.set(j, numberOfAverages.get(j)+1);
                        averages.set(j, (averages.get(j)+columnList.get(i))/(numberOfAverages.get(j)));
                        isInMargin = true;
                        // save the cards in a list if the current card is in the not at the bottom of the column
                        List<Card> tempList = new ArrayList<>();
                        // check the y coordinate to determine where in the column the card should be placed
                        for (int k = board.get(j).size(); k > 0; k--) {
                            // we remove all cards that are beneath the current card
                            if (board.get(j).get(k-1).getyCoord() < currentCard.getyCoord()){
                                tempList.add(board.get(j).get(k-1));
                                board.get(j).remove(k-1);
                            }
                            else break;
                        }
                        // we add the current card to the column
                        board.get(j).add(currentCard);
                        // we add the removed cards back
                        for (int k = tempList.size()-1; k > 0; k--) {
                            board.get(j).add(tempList.get(k));
                        }
                        break;
                    }
                }
                if (!isInMargin) {
                    // if not in average margin we add a new average
                    averages.add(columnList.get(i));
                    numberOfAverages.add(1);
                    board.get(averages.size()-1).add(currentCard);
                }
            }
        }

        // create empty lists for the rest
        for (int i = averages.size(); i < 7; i++) {
            List<Card> temp = new ArrayList<>();
            temp.add(new Card(Card.cardColor.EMPTY, Card.cardNumber.EMPTY));
            board.set(i, temp);
        }
    }

    // move a card from one list to another (no logic in function)
    public void moveCard(Card target, List<Card> origin, List<Card> end){
        List<Card> tempList = new ArrayList<>();
        // first iterate from the bottom of the column until we find the target card
        // meanwhile we save all cards that are beneath the target so we can move it together with the target
        for (int i = 0; i < origin.size(); i++) {
            tempList.add(origin.get(i));
            origin.remove(origin.get(i));
            if (tempList.get(tempList.size()-1) == target){
                break;
            }
        }

        if (end.get(end.size()-1).getOwnNumber() == Card.cardNumber.EMPTY){
            end.remove(end.size()-1);
        }
        for (int i = tempList.size(); i > 0; i--) {
            end.add(tempList.get(i-1));
        }
    }

    // check if a move for a card is legal
    public Boolean checkMove(Card from, Card to){
        if (from.getOwnNumber() == Card.cardNumber.EMPTY) return false;
        // if moving to an empty space
        if (to.getOwnNumber() == Card.cardNumber.EMPTY){
            return true;
        }
        // check if you want to move to empty space with king
        if (from.getOwnNumber() == Card.cardNumber.KING){
            return to.getOwnNumber() == Card.cardNumber.EMPTY;
        }
        // return true if the to card has a number 1 higher than from and is not the same color
        return (from.getOwnNumber().getNumber()+1 == to.getOwnNumber().getNumber()) && (from.getOwnColor() != to.getOwnColor());
    }


    // will find all applicable moves for a card in any other column
    public Pair<Card, List<Pair<Integer, Integer>>> getMovesForCard(Card start, int column){
        List<Pair<Integer, Integer>> moveList = new ArrayList<>();
        // get each column on the board
        for (int i = 0; i < board.size(); i++) {
            // if not own column
            if (i != column) {
                // check if you can place something on the bottom card
                if (checkMove(start, board.get(i).get(Math.max(0,board.get(i).size()-1)))){
                    moveList.add(new Pair<>(i,board.get(i).size()-1));
                }
            }
        }
        return new Pair<>(start, moveList);
    }

    // will find all applicable moves for all cards in all columns
    public List<Pair<Card,List<Pair<Integer,Integer>>>> getAllMoves(){
        List<Pair<Card,List<Pair<Integer,Integer>>>> moves = new ArrayList<>();
        for (int i = 0; i < board.size(); i++) {
            for (Card c: board.get(i)) {
                if (c.getOwnNumber() != Card.cardNumber.EMPTY) {
                    moves.add(getMovesForCard(c, i));
                }
            }
        }

        return moves;
    }


    // debugging the board
    public void printBoard(){
        if (!board.isEmpty()){
            int number = 0;
            for (List<Card> b: board) {
                for (int i = 0; i < b.size(); i++) {
                    System.out.print("Column " + number + " " +  b.get(i).getOwnNumber() + " " + b.get(i).getOwnColor() + " " + "Y coord " + b.get(i).getyCoord() + " X coord " + b.get(i).getxCoord() +"\t");
                }
                System.out.println();
                number++;
            }
        }
    }

    public List<List<Card>> getCards(){
        return board;
    }

}
