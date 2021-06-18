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

    public enum cardType{
        HEART(0),
        DIAMOND(1),
        SPADE(2),
        CLUB(3),
        EMPTY(4),
        TURNED(5);

        private int typeNumber;

        public int getTypeNumber(){return this.typeNumber;}

        private cardType(int typeNumber){this.typeNumber = typeNumber;}
    }


    public enum cardNumber{
        ACE(0),
        TWO(1),
        THREE(2),
        FOUR(3),
        FIVE(4),
        SIX(5),
        SEVEN(6),
        EIGHT(7),
        NINE(8),
        TEN(9),
        JACK(10),
        QUEEN(11),
        KING(12),
        EMPTY(13),
        TURNED(14),
        HAND(15);

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
    private cardType ownType;
    private int yCoord, xCoord, column, picPos;

    public Card(cardColor color, cardNumber number, cardType type, int column, int picPos, int yCoord, int xCoord){
        this.ownColor = color;
        this.ownNumber = number;
        this.ownType = type;
        this.column = column;
        this.yCoord = yCoord;
        this.xCoord = xCoord;
        this.picPos = picPos;
    }

    // constructor for no x,y coords
    public Card(cardColor color, cardNumber number, cardType type, int column, int picPos){
        this.ownColor = color;
        this.ownNumber = number;
        this.ownType = type;
        this.column = column;
        this.picPos = picPos;
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

    public cardType getOwnType() {
        return ownType;
    }

    public void setOwnType(cardType ownType) {
        this.ownType = ownType;
    }

    public int getColumn() {
        return column;
    }

    public void setColumn(int column) {
        this.column = column;
    }

    public int getPicPos() {
        return picPos;
    }

    public void setPicPos(int picPos) {
        this.picPos = picPos;
    }

    @Override
    public String toString() {
        return "Card{" +
                "ownColor=" + ownColor +
                ", ownNumber=" + ownNumber +
                ", ownType=" + ownType +
                ", yCoord=" + yCoord +
                ", xCoord=" + xCoord +
                '}';
    }
}
