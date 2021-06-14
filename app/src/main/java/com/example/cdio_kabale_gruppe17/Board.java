package com.example.cdio_kabale_gruppe17;

import android.util.Pair;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class Board {

    // TODO lav en liste der indeholder kort som ikke skal køres getmove på
    private static Board instance = null;
    private static List<List<Card>> board = new ArrayList<>();
    private static List<List<Card>> goalPoints = new ArrayList<>();
    private static List<Card> hand = new ArrayList<>();
    private static List<Pair<Integer,Integer>> bannedCards = new ArrayList<>();



    private Board(){
    }

    public static Board getInstance(){
        if (instance == null){
            instance = new Board();
        }
        return instance;
    }


    // instantiate the board with some lists based on the columns
    // TODO fix så den sorterer board efter x koordinater
    public void instantiate(ArrayList<String> cardInfo){
        // set the margin that the distance between cards in same column can be
        float margin = 0.5f;

        // instantiate the seven columns
        board.clear();
        goalPoints.clear();
        hand.clear();
        bannedCards.clear();
        for (int i = 0; i < 7; i++) {
            board.add(new ArrayList<>());
        }

        // instantiate the four goal points
        for (int i = 0; i < 4; i++) {
            goalPoints.add(new ArrayList<>());
            goalPoints.get(i).add(new Card(Card.cardColor.EMPTY, Card.cardNumber.EMPTY, Card.cardType.EMPTY, 7+i, 0));
        }

        for (int i = 0; i < 3; i++) {
            hand.add(new Card(Card.cardColor.EMPTY, Card.cardNumber.EMPTY, Card.cardType.EMPTY, i, 0));
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
            System.out.println(cardInfo.get(i));
            boolean isInMargin = false;
            // få kort fra ML af
            // TODO lav om til machine learning i stedet for predefined

            Card currentCard = new Card(getColor(cardInfo.get(i)), getNumber(cardInfo.get(i)),getType(cardInfo.get(i)), 0, i, CardDetector.yCoords.get(i), CardDetector.xCoords.get(i));

            // if there is no other averages in the list add this one
            if (averages.isEmpty()){
                averages.add(columnList.get(i));
                numberOfAverages.add(1);
                board.get(averages.size()-1).add(currentCard);
                currentCard.setColumn(averages.size()-1);
            }
            // else we check if this point is in the margin of the average
            else{
                for (int j = 0; j < averages.size(); j++) {
                    // if it is in the margin add it to that list and calculate average
                    if (averages.get(j)+margin > columnList.get(i) && averages.get(j)-margin < columnList.get(i)){
                        numberOfAverages.set(j, numberOfAverages.get(j)+1);
                        averages.set(j, (averages.get(j)+columnList.get(i))/(numberOfAverages.get(j)));
                        isInMargin = true;
                        // TODO change to a collections sort
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
                        currentCard.setColumn(j);
                        break;
                    }
                }
                if (!isInMargin) {
                    // if not in average margin we add a new average
                    if (averages.size() != 7) {
                        averages.add(columnList.get(i));
                        numberOfAverages.add(1);
                        board.get(averages.size() - 1).add(currentCard);
                        currentCard.setColumn(averages.size() - 1);
                    }
                }
            }
        }

        // sort the board list according to the x coordinates of the cards
        board.sort(new Comparator<List<Card>>() {
            @Override
            public int compare(List<Card> o1, List<Card> o2) {
                if (o2.size() == 0) return -1;
                if (o1.size() == 0) return 1;
                if (o1.get(0).getxCoord() > o2.get(0).getxCoord()) return -1;
                else if (o1.get(0).getxCoord() == o2.get(0).getxCoord()) return 0;
                else return 1;
            }
        });



        // create empty lists for the rest
        for (int i = averages.size(); i < 7; i++) {
            List<Card> temp = new ArrayList<>();
            temp.add(new Card(Card.cardColor.EMPTY, Card.cardNumber.EMPTY, Card.cardType.EMPTY, i, 0));
            board.set(i, temp);
        }
    }

    private Card.cardType getType(String card) {
        String c = String.valueOf(card.charAt(0));
        switch (c){
            case "K":
                return  Card.cardType.CLUB;
            case "S":
                return  Card.cardType.SPADE;
            case "R":
                return  Card.cardType.DIAMOND;
            case "H":
                return  Card.cardType.HEART;
            default:
                return Card.cardType.EMPTY;
        }
    }

    private Card.cardNumber getNumber(String card) {
        String c = String.valueOf(card.charAt(1));
        switch (c){
            case "1":
                return  Card.cardNumber.ONE;
            case "2":
                return  Card.cardNumber.TWO;
            case "3":
                return  Card.cardNumber.THREE;
            case "4":
                return  Card.cardNumber.FOUR;
            case "5":
                return  Card.cardNumber.FIVE;
            case "6":
                return  Card.cardNumber.SIX;
            case "7":
                return  Card.cardNumber.SEVEN;
            case "8":
                return  Card.cardNumber.EIGHT;
            case "9":
                return  Card.cardNumber.NINE;
            case "10":
                return  Card.cardNumber.TEN;
            case "B":
                return  Card.cardNumber.JACK;
            case "D":
                return  Card.cardNumber.QUEEN;
            case "K":
                return  Card.cardNumber.KING;
            case "A":
                return  Card.cardNumber.ACE;
            default:
                return  Card.cardNumber.EMPTY;


        }
    }

    private Card.cardColor getColor(String card) {
        String c = String.valueOf(card.charAt(0));

        switch (c){

            case "K":
            case "S":
                return  Card.cardColor.BLACK;
            case "R":
            case "H":
                return  Card.cardColor.RED;
            default:
                return  Card.cardColor.EMPTY;

        }


    }

    // move a card from one list to another (no logic in function)
    public void moveCard(Card target, List<Card> origin, List<Card> end) {
        // if the card is on the board
        if (target.getOwnNumber() != Card.cardNumber.HAND) {
            List<Card> tempList = new ArrayList<>();
            // first iterate from the bottom of the column until we find the target card
            // meanwhile we save all cards that are beneath the target so we can move it together with the target
            for (int i = 0; i < origin.size(); i++) {
                tempList.add(origin.get(i));
                origin.remove(origin.get(i));
                if (tempList.get(tempList.size() - 1) == target) {
                    break;
                }
            }

            int endCol = end.get(end.size() - 1).getColumn();
            if (end.get(end.size() - 1).getOwnNumber() == Card.cardNumber.EMPTY) {
                end.remove(end.size() - 1);
            }
            for (int i = tempList.size(); i > 0; i--) {
                end.add(tempList.get(i - 1));
            }
            if (end.isEmpty()) {
                end.add(new Card(Card.cardColor.EMPTY, Card.cardNumber.EMPTY, Card.cardType.EMPTY, endCol, 0));
            }
            if (origin.isEmpty()) {
                origin.add(new Card(Card.cardColor.EMPTY, Card.cardNumber.EMPTY, Card.cardType.EMPTY, endCol, 0));
            }
        }
        // check if it is in the hand but not find new cards
        // TODO fix det her shit ved at rykke kort fra hand til board
        /*else if (target.getOwnNumber() != Card.cardNumber.TURNED && target.getOwnNumber() != Card.cardNumber.EMPTY){
            origin.remove(target);
            target.setColumn(end.get(end.size()-1).getColumn());
            end.add(target);
            origin.add(new Card(Card.cardColor.EMPTY, Card.cardNumber.EMPTY, Card.cardType.EMPTY, target.getColumn(), 0));
        } */else{
            // find new cards
            for (int i = 0; i < hand.size(); i++) {
                hand.set(i, new Card(Card.cardColor.EMPTY, Card.cardNumber.TURNED, Card.cardType.TURNED, i, 0));
            }
        }
    }

    // check if a move for a card is legal
    public Boolean checkMove(Card from, Card to){
        if (from.getOwnNumber() == Card.cardNumber.EMPTY) return false;
        // if moving to an empty space but not in end goal
        if (to.getOwnNumber() == Card.cardNumber.EMPTY && to.getColumn() < 7){
            return true;
        }
        // check if you want to move to empty space with king
        if (from.getOwnNumber() == Card.cardNumber.KING && to.getColumn() < 7){
            return to.getOwnNumber() == Card.cardNumber.EMPTY;
        }

        // check if you want to move card to goal point
        if (to.getColumn() >= 7){
            // move an ace onto the goal points
            if (to.getOwnNumber() == Card.cardNumber.EMPTY && from.getOwnNumber() == Card.cardNumber.ACE) return true;
            // move another card onto the goal points
            if (to.getOwnNumber().getNumber() == from.getOwnNumber().getNumber()-1 && to.getOwnType() == from.getOwnType()) return true;
        }

        // return true if the to card has a number 1 higher than from and is not the same color
        return (from.getOwnNumber().getNumber()+1 == to.getOwnNumber().getNumber()) && (from.getOwnColor() != to.getOwnColor() && to.getColumn() < 7);
    }


    // will find all applicable moves for a card in any other column
    public Pair<Card, List<Integer>> getMovesForCard(Card start, int column){
        List<Integer> moveList = new ArrayList<>();
        // get each column on the board
        for (int i = 0; i < board.size(); i++) {
            // if not own column
            if (i != column) {
                // check if you can place something on the bottom card
                if (checkMove(start, board.get(i).get(Math.max(0,board.get(i).size()-1)))){
                    moveList.add(i);
                }
            }
        }
        // check the point goals
        for (int i = 0; i < goalPoints.size(); i++) {
            if (checkMove(start, goalPoints.get(i).get(Math.max(0, goalPoints.get(i).size()-1)))){
                moveList.add(7+i);
            }
        }


        return new Pair<>(start, moveList);
    }

    // will find all applicable moves for all cards in all columns
    public List<Pair<Card,List<Integer>>> getAllMoves(){
        List<Pair<Card,List<Integer>>> moves = new ArrayList<>();
        for (int i = 0; i < board.size(); i++) {
            for (Card c: board.get(i)) {
                if (c.getOwnNumber() != Card.cardNumber.EMPTY) {
                    Pair<Integer, Integer> temp = new Pair<>(c.getxCoord(), c.getyCoord());
                    if (!bannedCards.contains(temp)) {
                        moves.add(getMovesForCard(c, i));
                    }
                }
            }
        }
        // check the cards in the hand
        for (int i = 0; i < hand.size(); i++) {
            if (hand.get(i).getOwnNumber() != Card.cardNumber.EMPTY) {
                moves.add(getMovesForCard(hand.get(i), i));
            }
        }

        // add a get new cards move
        List<Integer> temp =  new ArrayList<>();
        temp.add(0);
        moves.add(new Pair<>(new Card(Card.cardColor.EMPTY, Card.cardNumber.HAND, Card.cardType.TURNED, 0, 0),temp));

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

    public List<List<Card>> getBoard() {
        return board;
    }

    public List<List<Card>> getGoalPoints() {
        return goalPoints;
    }

    public List<Card> getHand() {
        return hand;
    }

    public void addBannedCard(Pair<Integer, Integer> xyCoords) {
        // add the cards to a banned list
        bannedCards.add(xyCoords);

        // remove them from the board or hand list
        for (int i = 0; i < hand.size(); i++) {
            if (hand.get(i).getxCoord() == xyCoords.first && hand.get(i).getyCoord() == xyCoords.second) {
                int column = hand.get(i).getColumn();
                hand.set(i, new Card(Card.cardColor.EMPTY, Card.cardNumber.EMPTY, Card.cardType.EMPTY, column, 0));
                break;
            }
        }
        for (List<Card> l: board) {
            for (int i = 0; i < l.size(); i++) {
                if (l.get(i).getxCoord() == xyCoords.first && l.get(i).getyCoord() == xyCoords.second) {
                    int column = l.get(i).getColumn();
                    l.set(i, new Card(Card.cardColor.EMPTY, Card.cardNumber.EMPTY, Card.cardType.EMPTY, column, 0));
                    removeRedundantCards(column-1);
                    break;
                }
            }
        }
    }

    public List<Pair<Integer, Integer>> getBannedCards() {
        return bannedCards;
    }

    private void removeRedundantCards(int column) {
        // if there is more than 1 card then remove all empty cards
        if (board.get(column).size() > 1) {
            for (int i = 0; i < board.get(column).size(); i++) {
                if (board.get(column).get(i).getOwnColor() == Card.cardColor.EMPTY ||  board.get(column).get(i).getOwnNumber() == Card.cardNumber.EMPTY){
                    board.get(column).remove(board.get(column).get(i));
                    i--;
                }
            }
        }
    }

}
