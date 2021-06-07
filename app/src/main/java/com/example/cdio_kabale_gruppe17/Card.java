package com.example.cdio_kabale_gruppe17;

public class Card {

    // we create some enums for the color and number of the cards to easier instantiate the board, iterate and make logic based moves
    public enum cardColor{
        RED(0),
        BLACK(1),
        EMPTY(2);

        private int colorNumber;

        public int getColorNumber(){
            return this.colorNumber;
        }

        private cardColor(int colorNumber){
            this.colorNumber = colorNumber;
        }
    }

    public enum cardNumber{
        ACE(0),
        ONE(1),
        TWO(2),
        THREE(3),
        FOUR(4),
        FIVE(5),
        SIX(6),
        SEVEN(7),
        EIGHT(8),
        NINE(9),
        TEN(10),
        JACK(11),
        QUEEN(12),
        KING(13),
        EMPTY(14);

        private int number;

        public int getNumber(){
            return this.number;
        }

        private cardNumber(int number){
            this.number = number;
        }
    }

    // make private variables for the color, number and position
    private cardColor ownColor;
    private cardNumber ownNumber;
    private int yCoord, xCoord;

    public Card(cardColor color, cardNumber number, int yCoord, int xCoord){
        this.ownColor = color;
        this.ownNumber = number;
        this.yCoord = yCoord;
        this.xCoord = xCoord;
    }

    public cardColor getOwnColor() {
        return ownColor;
    }

    public void setOwnColor(cardColor ownColor) {
        this.ownColor = ownColor;
    }

    public cardNumber getOwnNumber() {
        return ownNumber;
    }

    public void setOwnNumber(cardNumber ownNumber) {
        this.ownNumber = ownNumber;
    }

    public int getyCoord() {
        return yCoord;
    }

    public void setyCoord(int yCoord) {
        this.yCoord = yCoord;
    }

    public int getxCoord() {
        return xCoord;
    }

    public void setxCoord(int xCoord) {
        this.xCoord = xCoord;
    }
}
