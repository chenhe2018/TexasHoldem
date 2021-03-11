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
    private int skipCardNum = 14;

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
    private int bankerPosition = 0;

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

    public TexasTable() {
        init();
    }

    /**
     * 游戏轮次构造函数
     * @param peopleNumber 参与人数
     * @param chipUnit 最小筹码
     * @param chipSum 基础财富
     * @param skipCardNum 跳牌数字
     * @param bankerPosition 庄家位置
     */
    public TexasTable(int peopleNumber, int chipUnit, int chipSum, int skipCardNum, int bankerPosition) {
        this.peopleNumber = peopleNumber;
        this.chipUnit = chipUnit;
        this.chipSum = chipSum;
        this.skipCardNum = skipCardNum;
        this.bankerPosition = bankerPosition;
        init();
    }

    /**
     * 游戏轮次构造函数
     */
    public void nextGame() {
        //调整庄家位置
        this.bankerPosition = ++this.bankerPosition % this.peopleNumber;
        //输出游戏基础内容
        System.out.println("庄家位置:"+ getBankerPosition());
        System.out.println("本人位置:"+me);
        for (int i = 0; i < this.peopleNumber; i++) {
            System.out.println("玩家["+i+"]筹码数 "+this.participants.get(i).getChipExist());
        }
        //牌池重置
        this.tableCards.clear();
        //筹码基线重置
        theChipsBaseline=2;
        //洗牌
        pokerGroup.initCards();
        //跳牌
        for(int i=0;i<skipCardNum;i++){
            pokerGroup.getCard();
        }
        System.out.println("==>洗牌并跳牌："+getSkipCardNum());
        //初始化玩家
        for(int i=0;i<peopleNumber;i++){
            participants.get(i).init();
            if(i == bankerPosition){
                //庄家位
                participants.get(i).setRole(Participant.ROLE.BigBlindness);
            }else if(i==(bankerPosition -1+peopleNumber)%peopleNumber){
                //小庄位
                participants.get(i).setRole(Participant.ROLE.SmallBlindness);
            }else {
                //普通位
                participants.get(i).setRole(Participant.ROLE.Nomal);
            }
        }
        //发牌
        for(int i=0;i<peopleNumber*2;i++){
            Poker card = pokerGroup.getCard();
            participants.get(i % peopleNumber).addHandCard(card);
        }
        //庄家下筹码
        participants.get(bankerPosition).chipMinus(2);
        participants.get((bankerPosition -1+peopleNumber)%peopleNumber).chipMinus(1);
        System.out.println("发牌完成；第"+ bankerPosition +"号玩家下大筹2；第"+(bankerPosition -1+peopleNumber)+"号玩家下小筹1。");
    }

    /**
     * 初始化函数
     */
    public void init() {
        //调整庄家位置
        this.bankerPosition = -1;
        //筹码基线重置
        theChipsBaseline=2;
        //初始化玩家
        participants = new ArrayList<>();
        for(int i=0;i<peopleNumber;i++){
            if(i== bankerPosition){
                //庄家位
                participants.add(new Participant(Participant.ROLE.BigBlindness,chipSum));
            }else if(i==(bankerPosition -1+peopleNumber)%peopleNumber){
                //小庄位
                participants.add(new Participant(Participant.ROLE.SmallBlindness,chipSum));
            }else{
                //普通位
                participants.add(new Participant(Participant.ROLE.Nomal,chipSum));
            }
        }
        //输出游戏基础内容
        System.out.println("======德州扑克======");
        System.out.println("参与人数："+getPeopleNumber());
        System.out.println("最小筹码："+getChipUnit());
        System.out.println("初始金钱："+getChipSum());
        System.out.println("本人位置:"+me);
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
        //决策//从庄家下家开始
        int firstChickenPos = (bankerPosition+1)%peopleNumber;
        int lastChickenPos = (bankerPosition-1+peopleNumber)%peopleNumber;
        //用于标识是否加注
        for(int i=0;i<this.peopleNumber;i++){
            //定位具体位置
            int thisPos = (firstChickenPos + i)%peopleNumber;
            if(!participants.get(thisPos).isDiscard()){
                int realChip = theChipsBaseline;
                if(thisPos == me){
                    //本人操作
                    while (true){
                        System.out.println("选择：1.查看手牌；2.查看筹码；3.check；4.弃牌；5.下基本筹；6.下翻倍筹；7.showhand；");
                        int choice = scanner.nextInt();
                        if(choice == 1){
                            System.out.println(this.showMyCards());
                        }else if(choice == 2){
                            System.out.println(this.showMyChips());
                        }else if(choice == 3){
                            System.out.println("玩家[" + thisPos + "]CHECK。");
                            break;
                        }else if(choice == 4){
                            participants.get(thisPos).setDiscard(true);
                            System.out.println("玩家[" + thisPos + "]弃牌。");
                            break;
                        }else if(choice == 5){
                            realChip = theChipsBaseline;
                            participants.get(thisPos).chipMinus(realChip);
                            System.out.println("玩家[" + thisPos + "]跟注：" + realChip);
                            break;
                        }else if(choice == 6){
                            realChip = theChipsBaseline * 2;
                            participants.get(thisPos).chipMinus(realChip);
                            System.out.println("玩家[" + thisPos + "]跟注：" + realChip);
                            break;
                        }else if(choice == 7){
                            realChip = participants.get(thisPos).getChipExist();
                            participants.get(thisPos).chipMinus(realChip);
                            System.out.println("玩家[" + thisPos + "]跟注：" + realChip);
                            break;
                        }else {
                            System.out.println("输入错误，重新输入。");
                        }
                    }
                }else{
                    //AI操作与选择
                    PokerStrategy.STRATEGY strategy = PokerStrategy.doCal(tableCards, participants.get(thisPos).getHandCards(),theChipsBaseline);
                    switch (strategy) {
                        case DISCARD:
                            participants.get(thisPos).setDiscard(true);
                            System.out.println("玩家[" + thisPos + "]弃牌。");
                            break;
                        case FOLLOW:
                            realChip = theChipsBaseline;
                            if((bankerPosition -1+peopleNumber)%peopleNumber==thisPos){
                                //小盲
                                realChip -=1;
                            }
                            participants.get(thisPos).chipMinus(realChip);
                            System.out.println("玩家[" + thisPos + "]跟注：" + realChip);
                            break;
                        case DOUBLE:
                            theChipsBaseline *= 2;
                            realChip = theChipsBaseline;
                            if((bankerPosition -1+peopleNumber)%peopleNumber==thisPos){
                                //小盲
                                realChip -=1;
                            }
                            participants.get(thisPos).chipMinus(realChip);
                            System.out.println("玩家[" + thisPos + "]加注：" + theChipsBaseline);
                            break;
                        case SHOWHAND:
                            theChipsBaseline = participants.get(i).getChipExist();
                            realChip = theChipsBaseline;
                            if((bankerPosition -1+peopleNumber)%peopleNumber==thisPos){
                                //小盲
                                realChip -=1;
                            }
                            participants.get(thisPos).chipMinus(theChipsBaseline);
                            System.out.println("玩家[" + thisPos + "]全下：" + theChipsBaseline);
                            break;
                        default:
                            break;
                    }
                }
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
        int firstChickenPos = (bankerPosition+1)%peopleNumber;
        for(int i=0;i<this.peopleNumber;i++){
            //定位具体位置
            int thisPos = (firstChickenPos + i)%peopleNumber;
            //非弃牌用户继续参与
            if(!participants.get(thisPos).isDiscard()){
                if(thisPos==me){
                    //用户操作与选择
                    while (true){
                        System.out.println("选择：1.查看手牌；2.查看筹码；3.弃牌；4.下基本筹；5.下翻倍筹；6.showhand；");
                        int choice = scanner.nextInt();
                        if(choice == 1){
                            System.out.println(this.showMyCards());
                        }else if(choice == 2){
                            System.out.println(this.showMyChips());
                        }else if(choice == 3){
                            participants.get(thisPos).setDiscard(true);
                            System.out.println("弃牌");
                            break;
                        }else if(choice == 4){
                            participants.get(thisPos).chipMinus(theChipsBaseline);
                            System.out.println("我下筹码"+theChipsBaseline);
                            break;
                        }else if(choice == 5){
                            theChipsBaseline *=2;
                            participants.get(thisPos).chipMinus(theChipsBaseline);
                            System.out.println("我下筹码"+theChipsBaseline);
                            break;
                        }else if(choice == 6){
                            theChipsBaseline = participants.get(thisPos).getChipExist();
                            participants.get(thisPos).chipMinus(theChipsBaseline);
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
                        PokerStrategy.STRATEGY strategy = PokerStrategy.doCal(tableCards, participants.get(thisPos).getHandCards(),theChipsBaseline);
                        switch (strategy) {
                            case DISCARD:
                                participants.get(thisPos).setDiscard(true);
                                System.out.println("玩家[" + thisPos + "]弃牌。");
                                break;
                            case FOLLOW:
                                participants.get(thisPos).chipMinus(theChipsBaseline);
                                System.out.println("玩家[" + thisPos + "]跟注：" + theChipsBaseline);
                                break;
                            case DOUBLE:
                                theChipsBaseline *= 2;
                                participants.get(thisPos).chipMinus(theChipsBaseline);
                                System.out.println("玩家[" + thisPos + "]加注：" + theChipsBaseline);
                                break;
                            case SHOWHAND:
                                theChipsBaseline = participants.get(thisPos).getChipExist();
                                participants.get(thisPos).chipMinus(theChipsBaseline);
                                System.out.println("玩家[" + thisPos + "]全下：" + theChipsBaseline);
                                break;
                            default:
                                break;
                        }
                    }while (theChipsBaselineAtLastTurn<theChipsBaselineAtLastTurn);
                }
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
        int firstChickenPos = (bankerPosition+1)%peopleNumber;
        for(int i=0;i<this.peopleNumber;i++){
            //定位具体位置
            int thisPos = (firstChickenPos + i)%peopleNumber;
            //非弃牌用户继续参与
            if(!participants.get(thisPos).isDiscard()){
                if(thisPos==me){
                    //用户操作与选择
                    while (true){
                        System.out.println("选择：1.查看手牌；2.查看筹码；3.弃牌；4.check；5.下基本筹；6.下翻倍筹；7.showhand；");
                        int choice = scanner.nextInt();
                        if(choice == 1){
                            System.out.println(this.showMyCards());
                        }else if(choice == 2){
                            System.out.println(this.showMyChips());
                        }else if(choice == 3){
                            participants.get(thisPos).setDiscard(true);
                            System.out.println("弃牌");
                            break;
                        }else if(choice == 4){
                            System.out.println("CHECK");
                            break;
                        }else if(choice == 5){
                            participants.get(thisPos).chipMinus(theChipsBaseline);
                            System.out.println("我下筹码"+theChipsBaseline);
                            break;
                        }else if(choice == 6){
                            theChipsBaseline *=2;
                            participants.get(thisPos).chipMinus(theChipsBaseline);
                            System.out.println("我下筹码"+theChipsBaseline);
                            break;
                        }else if(choice == 7){
                            theChipsBaseline = participants.get(thisPos).getChipExist();
                            participants.get(thisPos).chipMinus(theChipsBaseline);
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
                        PokerStrategy.STRATEGY strategy = PokerStrategy.doCal(tableCards, participants.get(thisPos).getHandCards(),theChipsBaseline);
                        switch (strategy) {
                            case DISCARD:
                                participants.get(thisPos).setDiscard(true);
                                System.out.println("玩家[" + thisPos + "]弃牌。");
                                break;
                            case FOLLOW:
                                participants.get(thisPos).chipMinus(theChipsBaseline);
                                System.out.println("玩家[" + thisPos + "]跟注：" + theChipsBaseline);
                                break;
                            case DOUBLE:
                                theChipsBaseline *= 2;
                                participants.get(thisPos).chipMinus(theChipsBaseline);
                                System.out.println("玩家[" + thisPos + "]加注：" + theChipsBaseline);
                                break;
                            case SHOWHAND:
                                theChipsBaseline = participants.get(thisPos).getChipExist();
                                participants.get(thisPos).chipMinus(theChipsBaseline);
                                System.out.println("玩家[" + thisPos + "]全下：" + theChipsBaseline);
                                break;
                            default:
                                break;
                        }
                    }while (theChipsBaselineAtLastTurn<theChipsBaselineAtLastTurn);
                }
            }
        }
    }

    /**
     * 结束轮次并展示结果
     */
    public void doEndTurn(){
        //结果展示
        System.out.println(Arrays.toString(new List[]{tableCards}));
        for(int i=0;i<this.peopleNumber;i++){
            System.out.println("玩家"+i+":"+this.participants.get(i).getHandCards()
                    +"\t已下筹码："+this.participants.get(i).getChipOnTable()
                    +"\t是否弃牌："+this.participants.get(i).isDiscard()
            );
        }
        //筹码结算
        System.out.println("输入赢家编号");
        int winner = scanner.nextInt();
        int sumChips = 0;
        for (Participant p:participants) {
            sumChips+=p.getChipOnTable();
        }
        participants.get(winner).chipPlus(sumChips);
        System.out.println("用户["+winner+"]赢得"+sumChips);
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

    public int getBankerPosition() {
        return bankerPosition;
    }

    public void setBankerPosition(int bankerPosition) {
        this.bankerPosition = bankerPosition;
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
