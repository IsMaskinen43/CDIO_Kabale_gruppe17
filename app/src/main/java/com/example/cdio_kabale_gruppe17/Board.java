package com.example.cdio_kabale_gruppe17;

import java.util.ArrayList;
import java.util.Arrays;
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


    // instantier boardet med 7 linkedlists baseret ud fra x og y koordinater fundet i carddetector
    public void instantiate(){
        // instantier de syv kolonner
        board.clear();
        goalPoints.clear();
        for (int i = 0; i < 7; i++) {
            board.add(new LinkedList<>());
        }

        // instantier de fire goal points
        for (int i = 0; i < 4; i++) {
            goalPoints.add(new LinkedList<>());
        }

        // find max punkterne i x og y
        int maxX=Integer.MIN_VALUE, maxY=Integer.MIN_VALUE, minX=Integer.MAX_VALUE, minY=Integer.MAX_VALUE;
        for (int i = 0; i < CardDetector.xCoords.size(); i++) {
            maxX = Math.max(maxX, CardDetector.xCoords.get(i));
            maxY = Math.max(maxY, CardDetector.yCoords.get(i));
            minX = Math.min(minX, CardDetector.xCoords.get(i));
            minY = Math.min(minY, CardDetector.yCoords.get(i));
        }

        // find cirka afstand mellem hver kolonne
        int dx = (maxX - minX) / 7;
        System.out.println("DX ER " + dx);

        // iterer over kortene
        for (int i = 0; i < CardDetector.xCoords.size(); i++) {
            int kolonne = Math.min(6,(CardDetector.xCoords.get(i)-minX)/dx);
            System.out.println("KOLONNE ER " + kolonne);
            // få kort fra ML af
            Card currentCard = new Card(Card.cardColor.BLACK, Card.cardNumber.ACE, CardDetector.yCoords.get(i), CardDetector.xCoords.get(i));

            // gem kort i en liste hvis der skal rykkes på nogle
            List<Card> tempList = new ArrayList<>();
            // tjek y koordinaterne og se hvor i kolonnen kortet skal ligge
            for (int j = board.get(kolonne).size(); j > 0; j--) {
                if (board.get(kolonne).get(j-1).getyCoord() < currentCard.getyCoord()){
                    tempList.add(board.get(kolonne).get(j-1));
                    board.get(kolonne).remove(j-1);
                }
                else break;
            }
            board.get(kolonne).addLast(currentCard);
            for (int j = tempList.size()-1; j > 0; j--) {
                board.get(kolonne).addLast(tempList.get(j));
            }
        }
    }

    // ryk kort fra én linked list til en anden (ingen logik indblandet)
    public void moveCard(Card target, LinkedList<Card> originBunke, LinkedList<Card> endBunke){
        List<Card> tempList = new ArrayList<>();

        // først iterer vi fra bunden af bunken indtil at vi finder vores target kort
        // samtidig gemmer vi alle kortene i en liste så vi kan indsætte dem i end listen
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

        // indsæt templist ind i end
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
        return (from.getOwnNumber().getNumber() == to.getOwnNumber().getNumber()+1) && (from.getOwnColor() != to.getOwnColor());
    }

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
