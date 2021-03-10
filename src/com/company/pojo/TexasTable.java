package com.company.pojo;

import com.company.strategy.PokerStrategy;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class TexasTable {

    /**
     * 参与人数
     */
    private int peopleNumber=6;

    /**
     * 基本筹码单元
     */
    private int chipUnit=1;

    /**
     * 个人基础筹码数
     */
    private int chipSum=100;

    /**
     * 跳过卡牌数
     */
    private int skipCardNum = 4;

    /**
     * 所用扑克
     */
    private PokerGroup pokerGroup = PokerGroup.getInstance();

    /**
     * 参与者
     */
    private List<Participant> participants;

    /**
     * 庄家位置
     */
    private int position = 0;

    /**
     * 本人位置
     */
    private int me = 0;

    /**
     * 每轮初始的筹码基线，随着对局进行增加
     */
    private int theChipsBaseline = 2;

    /**
     * 桌面牌池
     */
    private List<Poker> tableCards = new ArrayList<>();


    /**
     * 消息读入对象
     */
    private Scanner scanner = new Scanner(System.in);

    /**
     * 初始化函数
     */
    public void init() {
        System.out.println("======德州扑克======");
        System.out.println("参与人数："+getPeopleNumber());
        System.out.println("最小筹码："+getChipUnit());
        System.out.println("初始金钱："+getChipSum());
        System.out.println("洗牌并跳牌："+getSkipCardNum());
        System.out.println("庄家位置:"+getPosition());
        System.out.println("本人位置:"+me);
        //筹码基线重置
        theChipsBaseline=2;
        //洗牌
        pokerGroup.initCards();
        //跳牌
        for(int i=0;i<skipCardNum;i++){
            pokerGroup.getCard();
        }
        //初始化玩家
        participants = new ArrayList<>();
        for(int i=0;i<peopleNumber;i++){
            if(i==position){
                //庄家位
                participants.add(new Participant(Participant.ROLE.BigBlindness,chipSum));
            }else if(i==(position-1+peopleNumber)%peopleNumber){
                //小庄位
                participants.add(new Participant(Participant.ROLE.SmallBlindness,chipSum));
            }else{
                //普通位
                participants.add(new Participant(Participant.ROLE.Nomal,chipSum));
            }
        }
        //发牌
        for(int i=0;i<peopleNumber*2;i++){
            Poker card = pokerGroup.getCard();
            participants.get(i % peopleNumber).addHandCard(card);
        }
        //庄家下筹码
        participants.get(position).chipMinus(2);
        participants.get((position-1+peopleNumber)%peopleNumber).chipMinus(1);
        System.out.println("发牌完成；第"+position+"号玩家下大筹2；第"+(position-1+peopleNumber)+"号玩家下小筹1。");
    }


    public TexasTable() {}

    public TexasTable(int peopleNumber, int chipUnit, int chipSum, int skipCardNum) {
        this.peopleNumber = peopleNumber;
        this.chipUnit = chipUnit;
        this.chipSum = chipSum;
        this.skipCardNum = skipCardNum;
        init();
    }

    /**
     * 盲注轮次：大筹，小筹
     */
    public void doBlindTurn(){
        System.out.println("==>进入盲注轮次：大筹，小筹");
//        //发牌
//        for(int i=0;i<this.peopleNumber*2;i++){
//            this.participants.get(i%this.peopleNumber).addHandCard(pokerGroup.getCard());
//        }
        //决策
        for(int i=0;i<this.peopleNumber;i++){
            if(i==this.position){
                //用户操作与选择
                while (true){
                    System.out.println("选择：1.查看手牌；2.查看筹码；3.进入下轮；");
                    int choice = scanner.nextInt();
                    if(choice == 3){
                        break;
                    }else if(choice == 1){
                        System.out.println(this.showMyCards());
                    }else if(choice == 2){
                        System.out.println(this.showMyChips());
                    }else {
                        System.out.println("输入错误，重新输入。");
                    }
                }
            }else{
                //AI操作与选择
                int theChipsBaselineAtLastTurn = theChipsBaseline;
                do {
                    theChipsBaselineAtLastTurn = theChipsBaseline;
                    PokerStrategy.STRATEGY strategy = PokerStrategy.doCal(tableCards, participants.get(i).getHandCards(),theChipsBaseline);
                    switch (strategy) {
                        case DISCARD:
                            System.out.println("第" + i + "位用户弃牌。");
                            break;
                        case FOLLOW:
                            System.out.println("第" + i + "位用户跟注：" + theChipsBaseline);
                            break;
                        case DOUBLE:
                            theChipsBaseline *= 2;
                            System.out.println("第" + i + "位用户加注：" + theChipsBaseline);
                            break;
                        case SHOWHAND:
                            theChipsBaseline = participants.get(i).getChipExist();
                            System.out.println("第" + i + "位用户全下：" + theChipsBaseline);
                            break;
                        default:
                            break;
                    }
                }while (theChipsBaselineAtLastTurn<theChipsBaselineAtLastTurn);
            }
        }

    }

    /**
     * 首次轮次：翻三张
     */
    public void doChipTurn(){
        System.out.println("==>进入首次轮次：翻三张");
        //发牌
        this.tableCards.add(pokerGroup.getCard());
        this.tableCards.add(pokerGroup.getCard());
        this.tableCards.add(pokerGroup.getCard());
        //牌面
        System.out.println("牌桌："+ Arrays.toString(new List[]{this.tableCards}));
        //决策
        for(int i=0;i<this.peopleNumber;i++){
            if(i==this.position){
                //用户操作与选择
                while (true){
                    System.out.println("选择：1.查看手牌；2.查看筹码；3.下基本筹；4.下翻倍筹；5.showhand；");
                    int choice = scanner.nextInt();
                    if(choice == 1){
                        System.out.println(this.showMyCards());
                    }else if(choice == 2){
                        System.out.println(this.showMyChips());
                    }else if(choice == 3){
                        participants.get(me).chipMinus(theChipsBaseline);
                        System.out.println("我下筹码"+theChipsBaseline);
                        break;
                    }else if(choice == 4){
                        theChipsBaseline *=2;
                        participants.get(me).chipMinus(theChipsBaseline);
                        System.out.println("我下筹码"+theChipsBaseline);
                        break;
                    }else if(choice == 5){
                        theChipsBaseline = participants.get(me).getChipExist();
                        participants.get(me).chipMinus(theChipsBaseline);
                        System.out.println("我下筹码"+theChipsBaseline);
                        break;
                    }else {
                        System.out.println("输入错误，重新输入。");
                    }
                }
            }else{
                //AI操作与选择
                int theChipsBaselineAtLastTurn = theChipsBaseline;
                do {
                    theChipsBaselineAtLastTurn = theChipsBaseline;
                    PokerStrategy.STRATEGY strategy = PokerStrategy.doCal(tableCards, participants.get(i).getHandCards(),theChipsBaseline);
                    switch (strategy) {
                        case DISCARD:
                            System.out.println("第" + i + "位用户弃牌。");
                            break;
                        case FOLLOW:
                            System.out.println("第" + i + "位用户跟注：" + theChipsBaseline);
                            break;
                        case DOUBLE:
                            theChipsBaseline *= 2;
                            System.out.println("第" + i + "位用户加注：" + theChipsBaseline);
                            break;
                        case SHOWHAND:
                            theChipsBaseline = participants.get(i).getChipExist();
                            System.out.println("第" + i + "位用户全下：" + theChipsBaseline);
                            break;
                        default:
                            break;
                    }
                }while (theChipsBaselineAtLastTurn<theChipsBaselineAtLastTurn);
            }
        }
    }

    /**
     * 平常轮次：翻一张
     */
    public void doCardTurn(){
        //翻拍
        this.tableCards.add(pokerGroup.getCard());
        //牌面
        System.out.println("牌桌："+ Arrays.toString(new List[]{this.tableCards}));
        //决策
        for(int i=0;i<this.peopleNumber;i++){
            if(i==this.position){
                //用户操作与选择
                while (true){
                    System.out.println("选择：1.查看手牌；2.查看筹码；3.下基本筹；4.下翻倍筹；5.showhand；");
                    int choice = scanner.nextInt();
                    if(choice == 1){
                        System.out.println(this.showMyCards());
                    }else if(choice == 2){
                        System.out.println(this.showMyChips());
                    }else if(choice == 3){
                        participants.get(me).chipMinus(theChipsBaseline);
                        System.out.println("我下筹码"+theChipsBaseline);
                        break;
                    }else if(choice == 4){
                        theChipsBaseline *=2;
                        participants.get(me).chipMinus(theChipsBaseline);
                        System.out.println("我下筹码"+theChipsBaseline);
                        break;
                    }else if(choice == 5){
                        theChipsBaseline = participants.get(me).getChipExist();
                        participants.get(me).chipMinus(theChipsBaseline);
                        System.out.println("我下筹码"+theChipsBaseline);
                        break;
                    }else {
                        System.out.println("输入错误，重新输入。");
                    }
                }
            }else{
                //AI操作与选择
                int theChipsBaselineAtLastTurn = theChipsBaseline;
                do {
                    theChipsBaselineAtLastTurn = theChipsBaseline;
                    PokerStrategy.STRATEGY strategy = PokerStrategy.doCal(tableCards, participants.get(i).getHandCards(),theChipsBaseline);
                    switch (strategy) {
                        case DISCARD:
                            System.out.println("第" + i + "位用户弃牌。");
                            break;
                        case FOLLOW:
                            System.out.println("第" + i + "位用户跟注：" + theChipsBaseline);
                            break;
                        case DOUBLE:
                            theChipsBaseline *= 2;
                            System.out.println("第" + i + "位用户加注：" + theChipsBaseline);
                            break;
                        case SHOWHAND:
                            theChipsBaseline = participants.get(i).getChipExist();
                            System.out.println("第" + i + "位用户全下：" + theChipsBaseline);
                            break;
                        default:
                            break;
                    }
                }while (theChipsBaselineAtLastTurn<theChipsBaselineAtLastTurn);
            }
        }
    }

    /**
     * 结束轮次并展示结果
     */
    public void doEndTurn(){
        System.out.println(Arrays.toString(new List[]{tableCards}));
        for(int i=0;i<this.peopleNumber;i++){
            System.out.println("玩家"+i+":"+this.participants.get(i).getHandCards()+"\t已下筹码："+this.participants.get(i).getChipOnTable());
        }
    }

    /**
     * 查看本人手牌
     * @return
     */
    public String showMyCards(){
        return participants.get(me).showHandCards();
    }

    /**
     * 查看本人筹码
     * @return
     */
    public String showMyChips(){
        return participants.get(me).showChipExist();
    }

    public List<Participant> getParticipants() {
        return participants;
    }

    public void setParticipants(List<Participant> participants) {
        this.participants = participants;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public int getSkipCardNum() {
        return skipCardNum;
    }

    public void setSkipCardNum(int skipCardNum) {
        this.skipCardNum = skipCardNum;
    }

    public PokerGroup getPokerGroup() {
        return pokerGroup;
    }

    public void setPokerGroup(PokerGroup pokerGroup) {
        this.pokerGroup = pokerGroup;
    }

    public int getPeopleNumber() {
        return peopleNumber;
    }

    public void setPeopleNumber(int peopleNumber) {
        this.peopleNumber = peopleNumber;
    }

    public int getChipUnit() {
        return chipUnit;
    }

    public void setChipUnit(int chipUnit) {
        this.chipUnit = chipUnit;
    }

    public int getChipSum() {
        return chipSum;
    }

    public void setChipSum(int chipSum) {
        this.chipSum = chipSum;
    }

    public List<Poker> getTableCards() {
        return tableCards;
    }

    public void setTableCards(List<Poker> tableCards) {
        this.tableCards = tableCards;
    }
}
