package io.github.hepengfei.choujiang;

import java.util.Random;

public class PersonManager {
    private static PersonManager ourInstance = new PersonManager();
    private static String personList [] = new String[] {
            "贺鹏飞",
            "晏亮",
            "杨越",
            "杜明丰",
            "李瑾",
            "孙林",
            "朱秀军",
            "吕恒志",
            "苏怀强",
            "刁大鹏",
            "干文洁",
            "刘志明",
            "张尚良",
            "陈庆鹏",
            "徐进",
            "李尔卫",
            "李晓存",
            "欧慧珊",
            "杨欢",
            "韩路",
            "罗海生",
            "王学韵",
            "张嘉森",
            "廖吴超",
            "杨金鸽",
            "孙杨",
            "高飞",
            "刘建新"
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
