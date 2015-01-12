package io.github.hepengfei.choujiang;

import java.util.Random;

public class PersonManager {
    private static String initialPersonList [] = new String[] {
        "昌睿",
        "泉龙",
        "建利",
        "泰福",
        "长财",
        "红良",
        "昌逊",
        "卓臻",
        "昌哲",
        "承哲",
        "卓逊",
        "武臻",
        "武睿",
        "昌羲",
        "昌臻",
        "彦睿",
        "思源"
    };

    private static PersonManager ourInstance = new PersonManager();

    public static PersonManager getInstance() {
        return ourInstance;
    }

    ChouJiangInterface chou;

    private PersonManager() {
        chou = new ChouJiangRandom();
        chou.init(initialPersonList);
    }

    // random choose a person and remove it
    public String choosePerson() {
        chou.next();
        chou.gotIt();

        return chou.chosen();
    }

    public String showRandomPerson() {
        return chou.chosenForDisplay();
    }

    public void recoverPerson(String name) {
        chou.giveUp();
    }

    public int getLeftPersonCount() {
        return chou.countLeft();
    }

    public int getTotalPersonCount() {
        return chou.countTotal();
    }

    public void reset() {
        chou = new ChouJiangRandom();
        chou.init(initialPersonList);
    }
}
