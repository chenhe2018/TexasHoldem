package com.company.pojo;

import java.util.ArrayList;
import java.util.List;

public class Participant {
    public enum ROLE {
        BigBlindness,//大盲
        SmallBlindness,//小盲
        Nomal//普通
    }

    /**
     * 用户角色
     */
    private ROLE role;

    /**
     * 手中筹码数
     */
    private int chipExist;

    /**
     * 是否弃牌
     */
    private boolean isDiscard;

    /**
     * 已下筹码
     */
    private int chipOnTable=0;

    /**
     * 手牌
     */
    private List<Poker> handCards = new ArrayList<>();

    private Participant(){
        chipOnTable = 0;
    }
    /**
     * 增加手牌
     * @param poker
     */
    public void addHandCard(Poker poker){
        handCards.add(poker);
    }

    /**
     * 下筹码
     * @param chipNum
     * @return
     */
    public boolean chipMinus(int chipNum){
        if(chipNum> chipExist){
            return false;
        }
        chipExist -= chipNum;
        chipOnTable += chipNum;
        return true;
    }

    /**
     * 展示筹码数
     * @return
     */
    public String showChipExist(){
        return "["+chipExist+"]";
    }

    /**
     * 展示手牌
     * @return
     */
    public String showHandCards(){
        StringBuffer sb = new StringBuffer();
        sb.append("[");
        for(Poker poker:handCards){
            sb.append(poker.toString()+",");
        }
        sb.deleteCharAt(sb.length()-1);
        sb.append("]");
        return sb.toString();
    }

    public Participant(ROLE role, int chipExist) {
        this.role = role;
        this.chipExist = chipExist;
    }

    public int getChipExist() {
        return chipExist;
    }

    public void setChipExist(int chipExist) {
        this.chipExist = chipExist;
    }

    public List<Poker> getHandCards() {
        return handCards;
    }

    public void setHandCards(List<Poker> handCards) {
        this.handCards = handCards;
    }

    public boolean isDiscard() {
        return isDiscard;
    }

    public void setDiscard(boolean discard) {
        isDiscard = discard;
    }

    public int getChipOnTable() {
        return chipOnTable;
    }

    public void setChipOnTable(int chipOnTable) {
        this.chipOnTable = chipOnTable;
    }
}
