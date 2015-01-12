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

    private static PersonManager ourInstance = new PersonManager();

    public static PersonManager getInstance() {
        return ourInstance;
    }

    private String personList [];
    private Random random;

    private PersonManager() {
        personList = new String[initialPersonList.length];
        System.arraycopy(initialPersonList, 0, personList, 0, initialPersonList.length);

        random = new Random();
    }

    public int getPersonIndex() {
        random.setSeed(System.currentTimeMillis());
        int rand = Math.abs(random.nextInt());

        return rand % personList.length;
    }

    public String getPerson(int index) {
        Randomizer.randomize(random, personList, 0, personList.length);

        return personList[index];
    }

}
