package io.github.hepengfei.choujiang;


public interface ChouJiangInterface {

    // 初始化内容
    public void init(String list[]);

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

    // 当前抽中的
    public String chosen();

    // 领奖
    public void gotIt();

    // 放弃本次领奖
    public void giveUp();

}
