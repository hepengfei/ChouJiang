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
    private int length;
    private Random random;

    private PersonManager() {
        personList = new String[initialPersonList.length];
        System.arraycopy(initialPersonList, 0, personList, 0, initialPersonList.length);
        length = personList.length;

        random = new Random();
    }

    // random choose a person and remove it
    public String choosePerson() {
        if (length == 0) {
            return "";
        }

        Randomizer.randomize(random, personList, 0, length);

        random.setSeed(System.currentTimeMillis());
        int randomIndex = Math.abs(random.nextInt()) % length;

        length = length - 1;

        String chosen = personList[randomIndex];
        personList[randomIndex] = personList[length];
        personList[length] = chosen;

        return chosen;
    }

    public void recoverPerson(String name) {
        if (length + 1 > personList.length) {
            throw new ArrayIndexOutOfBoundsException();
        }

        if (! personList[length].equals(name)) {
            throw new RuntimeException("Only last chosen person can be recovered");
        }

        length = length + 1;
    }

}
