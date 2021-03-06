package io.github.hepengfei.choujiang;


public interface ChouJiangInterface {

    // 初始化内容
    public void init();

    // 待抽奖的总数
    public int countTotal();

    // 剩余的数量
    public int countLeft();

    // 已中奖的数量
    public int countGot();

    // 抽下一个
    public void next();

    // 供显示用的
    public String chosenForDisplay();

    // 当前抽中的，未开始抽奖返回null
    public String chosen();

    // 领奖
    public void gotIt();

    // 放弃本次领奖
    public void giveUp();

    public boolean isChosenGot();
    public boolean isChosenGiveUp();


    public int add(String name);
    public int remove(String name);
    public void clear();
    public void reset();
    public String getName(int position);
    public String getAllNames();
}
