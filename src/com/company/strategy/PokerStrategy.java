package com.company.strategy;

import com.company.pojo.Poker;

import java.util.List;

public class PokerStrategy {

    public enum STRATEGY{
        DISCARD,//弃牌
        FOLLOW,//跟注
        DOUBLE,//翻倍
        SHOWHAND,//全下
    }

    public static STRATEGY doCal(List<Poker> tableCards, List<Poker> handCards, int baseChip){


        return STRATEGY.FOLLOW;
    }
}
