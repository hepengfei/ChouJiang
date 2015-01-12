package io.github.hepengfei.choujiang;

import java.util.Random;

public class PersonManager {
    private static PersonManager ourInstance = new PersonManager();
    private static String personList [] = new String[] {
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

    public static PersonManager getInstance() {
        return ourInstance;
    }

    private PersonManager() {
    }

    public String getPersonName() {
        Random random = new Random();
        random.setSeed(System.currentTimeMillis());
        int rand = Math.abs(random.nextInt());

        return personList[rand % personList.length];
    }
}
