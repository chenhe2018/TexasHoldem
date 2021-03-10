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

        texasTable.init();
        texasTable.doBlindTurn();
        texasTable.doChipTurn();
        while(texasTable.getTableCards().size()<=4){
            texasTable.doCardTurn();
        }
        texasTable.doEndTurn();

    }
}
