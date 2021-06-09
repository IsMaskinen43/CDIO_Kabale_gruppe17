package com.example.cdio_kabale_gruppe17;

import android.util.Pair;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class Algoritme {
    private static Algoritme instance = null;

    private Algoritme(){

    }

    public static Algoritme getInstance(){
        if (instance == null) instance = new Algoritme();
        return instance;
    }

    // get the (5 or 10) best possible move according to some factors
    /**
     * @param board (state of the current board)
     * @return a list of pair with a card and the column number
     * The 6 categories to determine the best move are
     * 1. Hand cards should always be flipped (6 pts)
     * 2. Don't play non-King cards on an empty column (5 pts)
     * 3. Expose large stacks of turned cards (4 pts)
     * 4. Play aces (3 pts)
     * 5. Keep color in mind when playing kings (2 pts)
     * 6. Keep twos away from board as long as possible until ace has been found (1 pts)
     */
    public List<Pair<Integer,Pair<Card,Integer>>> getBestMoves(Board board){
        List<Pair<Integer,Pair<Card,Integer>>> bestMoves = new ArrayList<>();
        // get all moves possible on the board
        List<Pair<Card,List<Integer>>> legalMoves = board.getAllMoves();
        // get the board state and the goal state
        List<List<Card>> currBoard = board.getBoard();
        List<List<Card>> currGoals = board.getGoalPoints();

        // iterate all possible moves, calculate the point and insert into bestmoves if good enough
        for (Pair<Card,List<Integer>> p: legalMoves) {
            for (Integer l: p.second) {
                System.out.print(p.first.toString() + " har legal move: " + l +"\t");
                int point = 0;

                // Check for king on an empty spot
                if (l < 7) {
                    if (currBoard.get(l).get(currBoard.get(l).size() - 1).getOwnNumber() == Card.cardNumber.EMPTY && p.first.getOwnNumber() == Card.cardNumber.KING)
                        point += 5;
                }

                // get the amount of turned cards in every column
                List<Integer> turnedAmount = new ArrayList<>();
                for (List<Card> col: currBoard) {
                    turnedAmount.add(getTurnedAmount(col));
                }
                // sort it by number of turned cards
                Collections.sort(turnedAmount);
                // get the index of the current cards column
                System.out.println(getTurnedAmount(currBoard.get(p.first.getColumn())));
                int index = turnedAmount.indexOf(getTurnedAmount(currBoard.get(p.first.getColumn())));
                // if there are more turned cards than half the columns then we want to expose it
                if (index >= Math.floor(turnedAmount.size()/2.0)) point += 4;


                // if the current card is an ace and the placement is in the goal column then we want to play it
                if (p.first.getOwnNumber() == Card.cardNumber.ACE && l >= 7) point += 3;

                // if you play a king that a queen of opposite color is on the board
                if (p.first.getOwnNumber() == Card.cardNumber.KING && isOtherColorQueen(currBoard, p.first.getColumn(), p.first)) point += 2;

                // if you can play a two and ace has been found then play it on the stack else deduct points cause you dont want to play the two
                if (p.first.getOwnNumber() == Card.cardNumber.TWO && aceIsFound(currGoals,p.first) && l >= 7) {
                    point += 1;
                } else if (p.first.getOwnNumber() == Card.cardNumber.TWO) point -= 1;

                // want the 10 best moves
                if (bestMoves.size() < 10){
                    // add the move and the corresponding point to the list
                    bestMoves.add(new Pair<>(point, new Pair<>(p.first, l)));
                    // sort the whole list by the point
                    bestMoves.sort(new Comparator<Pair<Integer, Pair<Card, Integer>>>() {
                        @Override
                        public int compare(Pair<Integer, Pair<Card, Integer>> o1, Pair<Integer, Pair<Card, Integer>> o2) {
                            if (o1.first > o2.first) return -1;
                            else if (o1.first.equals(o2.first)) return 0;
                            else return 1;
                        }
                    });

                } else {
                    // check if this move is better than the last place
                    if (!(bestMoves.get(bestMoves.size() - 1).first >= point)) {
                        // insert this move into the last moves spot
                        bestMoves.set(bestMoves.size()-1, new Pair<>(point, new Pair<>(p.first, l)));
                        // sort the whole list by the point
                        bestMoves.sort(new Comparator<Pair<Integer, Pair<Card, Integer>>>() {
                            @Override
                            public int compare(Pair<Integer, Pair<Card, Integer>> o1, Pair<Integer, Pair<Card, Integer>> o2) {
                                if (o1.first > o2.first) return -1;
                                else if (o1.first.equals(o2.first)) return 0;
                                else return 1;
                            }
                        });
                    }
                }
            }
        }

        // get the hand and check if its empty
        List<Card> hand = board.getHand();
        // if only empty spots in the hand
        if (hand.get(0).getOwnNumber() == hand.get(1).getOwnNumber() && hand.get(2).getOwnNumber() == Card.cardNumber.EMPTY && hand.get(1).getOwnNumber() == hand.get(2).getOwnNumber()){
            int point = 6;
            if (!(bestMoves.get(bestMoves.size() - 1).first >= point)) {
                // insert this move into the last moves spot
                bestMoves.set(bestMoves.size()-1, new Pair<>(point, new Pair<>(new Card(Card.cardColor.EMPTY, Card.cardNumber.HAND, Card.cardType.TURNED, 0, 0), 0)));
                // sort the whole list by the point
                bestMoves.sort(new Comparator<Pair<Integer, Pair<Card, Integer>>>() {
                    @Override
                    public int compare(Pair<Integer, Pair<Card, Integer>> o1, Pair<Integer, Pair<Card, Integer>> o2) {
                        if (o1.first > o2.first) return -1;
                        else if (o1.first.equals(o2.first)) return 0;
                        else return 1;
                    }
                });
            }
        }

        return bestMoves;
    }

    // return amount of turned cards in a column
    private static int getTurnedAmount(List<Card> column) {
        int counter = 0;
        for (Card c: column) {
            if (c.getOwnNumber() == Card.cardNumber.TURNED) counter++;
        }
        return counter;
    }

    // return true if a queen of opposite color is present on board and not in same column
    private static boolean isOtherColorQueen(List<List<Card>> board, int currentCol, Card currCard){
        for (int i = 0; i < board.size(); i++) {
            if (i != currentCol){
                for (Card c: board.get(i)) {
                    if (c.getOwnNumber() == Card.cardNumber.QUEEN && c.getOwnColor() != currCard.getOwnColor()){
                        return true;
                    }
                }
            }
        }
        return false;
    }

    // return true if a ace card of same type has been found and placed in goal column
    private static boolean aceIsFound(List<List<Card>> goalColumns, Card currCard){
        for (List<Card> cards: goalColumns) {
            if (cards.get(0).getOwnNumber() == Card.cardNumber.ACE && cards.get(0).getOwnType() == currCard.getOwnType()) return true;
        }
        return false;
    }


}
