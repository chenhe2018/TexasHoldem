package com.company.pojo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * 扑克牌堆
 */
public class PokerGroup {

    private static PokerGroup instance = null;

    private PokerGroup(){
        initCards();
    }

    public static PokerGroup getInstance(){
        if(instance == null){
            instance = new PokerGroup();
        }
        return instance;
    }

    /**
     * 牌堆
     */
    private LinkedList prepareList = new LinkedList<Poker>();

    /**
     * 弃牌堆
     */
    private LinkedList abandonList = new LinkedList<Poker>();

    public void initCards(){
        prepareList.clear();
        abandonList.clear();
        // 初始化13*4张牌
        for(int i=1;i<=13;i++){
            prepareList.push(new Poker(Poker.COLOR.spade,i));//黑桃
            prepareList.push(new Poker(Poker.COLOR.heart,i));//红心
            prepareList.push(new Poker(Poker.COLOR.diamond,i));//方块
            prepareList.push(new Poker(Poker.COLOR.club,i));//梅花
        }
        // 随机化
        Collections.shuffle(prepareList);
    }

    /**
     * 获取牌堆剩余张数
     * @return
     */
    public int getLength(){
        return prepareList.size();
    }

    /**
     * 抽取一张poker
     * @return
     */
    public Poker getCard(){
        Poker poker = (Poker)prepareList.pop();
        abandonList.push(poker);
        return poker;
    }

    /**
     * 打印扑克牌堆
     * @return
     */
    public String printPrepareCards(){
        StringBuffer sb = new StringBuffer();
        sb.append("[");
        for(Object o : prepareList){
            Poker p = (Poker) o;
            sb.append(p.toString()+",");
        }
        sb.deleteCharAt(sb.length()-1);
        sb.append("]");
        return sb.toString();
    }

    /**
     * 打印弃牌堆
     * @return
     */
    public String printAbandonCards() {
        StringBuffer sb = new StringBuffer();
        sb.append("[");
        for (Object o : abandonList) {
            Poker p = (Poker) o;
            sb.append(p.toString() + ",");
        }
        sb.deleteCharAt(sb.length() - 1);
        sb.append("]");
        return sb.toString();
    }
}
