package com.company;

import com.company.pojo.Poker;
import com.company.pojo.PokerGroup;
import com.company.pojo.TexasTable;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
	// write your code here

//        PokerGroup pg = PokerGroup.getInstance();
//        System.out.println(pg.printPrepareCards());
//        System.out.println(pg.printAbandonCards());
//        Poker card = pg.getCard();
//        System.out.println(card);
//        System.out.println(pg.printPrepareCards());
//        System.out.println(pg.printAbandonCards());
        TexasTable texasTable = new TexasTable();

        for (int count = 0; count < 1000; count++) {
            System.out.println("**************** 新一轮游戏开始 ["+count+"] ****************");
            texasTable.nextGame();
            texasTable.doBlindTurn();
            texasTable.doChipTurn();
            while(texasTable.getTableCards().size()<=4){
                texasTable.doCardTurn();
            }
            texasTable.doEndTurn();
            System.out.println();
            System.out.println();
        }

    }
}
