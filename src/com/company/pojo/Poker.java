package com.company.pojo;

public class Poker {
    public enum COLOR {
        //黑桃
        spade,
        //红桃
        heart,
        //方块
        diamond,
        //梅花
        club
    }
    public enum NUMBER {
        A,
        _2,
        _3,
        _4,
        _5,
        _6,
        _7,
        _8,
        _9,
        _10,
        J,
        Q,
        K
    }

    /**
     * 牌面花色
     */
    private COLOR color;

    /**
     * 牌面数值
     */
    private NUMBER number;

    public Poker(COLOR color, NUMBER number) {
        this.color = color;
        this.number = number;
    }

    public Poker(COLOR color, int i) {
        this.color = color;
        switch (i){
            case 1:
                this.number = NUMBER.A;
                break;
            case 2:
                this.number = NUMBER._2;
                break;
            case 3:
                this.number = NUMBER._3;
                break;
            case 4:
                this.number = NUMBER._4;
                break;
            case 5:
                this.number = NUMBER._5;
                break;
            case 6:
                this.number = NUMBER._6;
                break;
            case 7:
                this.number = NUMBER._7;
                break;
            case 8:
                this.number = NUMBER._8;
                break;
            case 9:
                this.number = NUMBER._9;
                break;
            case 10:
                this.number = NUMBER._10;
                break;
            case 11:
                this.number = NUMBER.J;
                break;
            case 12:
                this.number = NUMBER.Q;
                break;
            case 13:
                this.number = NUMBER.K;
                break;
            default:
                break;
        }
    }

    public COLOR getColor() {
        return color;
    }

    public void setColor(COLOR color) {
        this.color = color;
    }

    public NUMBER getNumber() {
        return number;
    }

    public void setNumber(NUMBER number) {
        this.number = number;
    }

    @Override
    public String toString() {
        String colorStr = "";
        switch (color){
            case spade:
                colorStr="黑桃";
                break;
            case heart:
                colorStr="红桃";
                break;
            case diamond:
                colorStr="方块";
                break;
            case club:
                colorStr="梅花";
                break;
            default:
                break;
        }
        String numberStr = "";
        switch (number){
            case A:
                numberStr = "A";
                break;
            case _2:
                numberStr = "2";
                break;
            case _3:
                numberStr = "3";
                break;
            case _4:
                numberStr = "4";
                break;
            case _5:
                numberStr = "5";
                break;
            case _6:
                numberStr = "6";
                break;
            case _7:
                numberStr = "7";
                break;
            case _8:
                numberStr = "8";
                break;
            case _9:
                numberStr = "9";
                break;
            case _10:
                numberStr = "10";
                break;
            case J:
                numberStr = "J";
                break;
            case Q:
                numberStr = "Q";
                break;
            case K:
                numberStr = "K";
                break;
            default:
                break;
        }
        return colorStr+numberStr;
    }
}
