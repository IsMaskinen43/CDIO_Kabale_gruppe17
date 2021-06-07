package com.example.cdio_kabale_gruppe17;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class Board {
    private static Board instance = null;
    private static List<LinkedList<Card>> board = new ArrayList<>();
    private static List<LinkedList<Card>> goalPoints = new ArrayList<>();


    private Board(){
    }

    public static Board getInstance(){
        if (instance == null){
            instance = new Board();
        }
        return instance;
    }


    // instantiate the board with some linked lists based on the columns
    public void instantiate(){
        // set the margin that the distance between cards in same column can be
        float margin = 0.5f;

        // instantiate the seven columns
        board.clear();
        goalPoints.clear();
        for (int i = 0; i < 7; i++) {
            board.add(new LinkedList<>());
        }

        // instantiate the four goal points
        for (int i = 0; i < 4; i++) {
            goalPoints.add(new LinkedList<>());
        }

        // find the max and min points in the x axis
        int maxX=Integer.MIN_VALUE, minX=Integer.MAX_VALUE;
        for (int i = 0; i < CardDetector.xCoords.size(); i++) {
            maxX = Math.max(maxX, CardDetector.xCoords.get(i));
            minX = Math.min(minX, CardDetector.xCoords.get(i));
        }

        // get the estimated distance between each column
        float dx = (maxX - minX) / 7.0f;
        System.out.println("DX ER " + dx);

        // make a list with the estimated position of the cards in the columns (this will be used together with a margin to estimate better which cards are in the same column)
        List<Float> kolonneList = new ArrayList<>();
        for (int i = 0; i < CardDetector.xCoords.size(); i++) {
            kolonneList.add((CardDetector.xCoords.get(i)-minX)/dx);
        }

        // set up the board based on the average x coord and margin
        List<Float> averages = new ArrayList<>();
        List<Integer> numberOfAverages = new ArrayList<>();
        // calculate how many columns there are on the board
        for (int i = 0; i < kolonneList.size(); i++) {
            boolean isInMargin = false;
            // få kort fra ML af
            // TODO lav om til machine learning i stedet for predefined
            Card currentCard = new Card(Card.cardColor.BLACK, Card.cardNumber.ACE, CardDetector.yCoords.get(i), CardDetector.xCoords.get(i));

            // if there is no other averages in the list add this one
            if (averages.isEmpty()){
                averages.add(kolonneList.get(i));
                numberOfAverages.add(1);
                board.get(averages.size()-1).addLast(currentCard);
            }
            // else we check if this point is in the margin of the average
            else{
                for (int j = 0; j < averages.size(); j++) {
                    // if it is in the margin add it to that list and calculate average
                    if (averages.get(j)+margin > kolonneList.get(i) && averages.get(j)-margin < kolonneList.get(i)){
                        numberOfAverages.set(j, numberOfAverages.get(j)+1);
                        averages.set(j, (averages.get(j)+kolonneList.get(i))/(numberOfAverages.get(j)));
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
                        board.get(j).addLast(currentCard);
                        // we add the removed cards back
                        for (int k = tempList.size()-1; k > 0; k--) {
                            board.get(j).addLast(tempList.get(k));
                        }
                        break;
                    }
                }
                if (!isInMargin) {
                    // if not in average margin we add a new average
                    averages.add(kolonneList.get(i));
                    numberOfAverages.add(1);
                    board.get(averages.size()-1).addLast(currentCard);
                }
            }
        }
    }

    // move a card from one linked list to another (no logic in function)
    public void moveCard(Card target, LinkedList<Card> originBunke, LinkedList<Card> endBunke){
        List<Card> tempList = new ArrayList<>();

        // first iterate from the bottom of the column until we find the target card
        // meanwhile we save all cards that are beneath the target so we can move it together with the target
        for (int i = 0; i < originBunke.size(); i++) {
            if (originBunke.getLast() != target){
                tempList.add(originBunke.getLast());
                originBunke.removeLast();
            }
            else{
                tempList.add(originBunke.getLast());
                originBunke.removeLast();
                break;
            }
        }

        // insert templist to end list
        for (int i = tempList.size(); i > 0; i--) {
            endBunke.addLast(tempList.get(i-1));
        }
    }

    public Boolean checkMove(Card from, Card to){
        // check if you want to move to empty space with king
        if (from.getOwnNumber() == Card.cardNumber.KING){
            return to.getOwnNumber() == Card.cardNumber.EMPTY;
        }
        // return true if the to card has a number 1 higher than from and is not the same color
        return (from.getOwnNumber().getNumber()+1 == to.getOwnNumber().getNumber()) && (from.getOwnColor() != to.getOwnColor());
    }

    // debugging the board
    public void printBoard(){
        if (!board.isEmpty()){
            int number = 0;
            for (LinkedList<Card> b: board) {
                for (int i = 0; i < b.size(); i++) {
                    System.out.print("Kolonne " + number + " " +  b.get(i).getOwnNumber() + " " + b.get(i).getOwnColor() + " " + "Y COORD " + b.get(i).getyCoord() + " X COORD " + b.get(i).getxCoord() +"\t");
                }
                System.out.println();
                number++;
            }
        }
    }
}
