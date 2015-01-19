package io.github.hepengfei.choujiang;

import android.content.SharedPreferences;

import java.util.Random;

public class ChouJiangRandomRound implements ChouJiangInterface {
    private String list[];
    private int count;
    private Random random;

    private int currentChoosen;
    private int currentChoosenGot;
    private int numberOfGot;

    private static ChouJiangInterface sInstance;
    public static ChouJiangInterface getInstance() {
        if (sInstance == null) {
            synchronized (ChouJiangRandomRound.class) {
                if (sInstance == null) {
                    sInstance = new ChouJiangRandomRound();
                    sInstance.init();
                }
            }
        }
        return sInstance;
    }

    @Override
    public void init() {
        restore();
    }

    @Override
    public int countTotal() {
        return list.length;
    }

    @Override
    public int countLeft() {
        return count;
    }

    @Override
    public int countGot() {
        return numberOfGot;
    }

    @Override
    public void next() {
        if (list.length == 0) {
            return;
        }

        // 自动循环抽
        if (count == 0) {
            count = list.length;
        }

        randomize(random, list, 0, count);

        random.setSeed(System.currentTimeMillis());
        currentChoosen = Math.abs(random.nextInt()) % count;
        currentChoosenGot = -1;
    }

    @Override
    public String chosenForDisplay() {
        if (list.length == 0) {
            return "";
        }
        return list[Math.abs(random.nextInt()) % list.length];
    }

    @Override
    public String chosen() {
        if (currentChoosen != -1) {
            return list[currentChoosen];
        }
        if (currentChoosenGot != -1) {
            return list[count];
        }
        return null;
    }

    @Override
    public void gotIt() {
        if (currentChoosen == -1 || count == 0) {
            return;
        }

        count = count - 1;

        String tmp = list[currentChoosen];
        list[currentChoosen] = list[count];
        list[count] = tmp;

        currentChoosenGot = currentChoosen;
        currentChoosen = -1;

        numberOfGot ++;

        save();
    }

    @Override
    public void giveUp() {
        if (currentChoosenGot == -1) {
            return;
        }

        String tmp = list[count];
        list[count] = list[currentChoosenGot];
        list[currentChoosenGot] = tmp;

        count = count + 1;

        currentChoosen = currentChoosenGot;
        currentChoosenGot = -1;

        numberOfGot --;

        save();
    }

    @Override
    public int add(String name) {
        for (int i = 0; i<list.length; ++i) {
            if (list[i] == name) {
                return 0;
            }
        }

        String newList[] = new String[list.length + 1];
        System.arraycopy(list, 0, newList, 1, list.length);;
        newList[0] = name;

        list = newList;
        count ++;
        if (currentChoosen != -1) {
            currentChoosen ++;
        }
        if (currentChoosenGot != -1) {
            currentChoosenGot ++;
        }

        save();

        return 1;
    }

    @Override
    public int remove(String name) {
        int found = -1;

        for (int i = 0; i<list.length; ++i) {
            if (list[i] == name) {
                found = i;
            }
        }
        if (found == -1) {
            return 0;
        }

        list[found] = list[list.length - 1];

        String newList[] = new String[list.length - 1];
        System.arraycopy(list, 0, newList, 0, list.length - 1);
        list = newList;

        if (found < count) {
            count --;
            if (currentChoosen != -1) {
                currentChoosen --;
            }
            if (currentChoosenGot != -1) {
                currentChoosenGot --;
            }
        }

        save();

        return 1;
    }

    @Override
    public String getName(int position) {
        return list[position];
    }

    @Override
    public String getAllNames() {
        String result = "";
        for (int i = 0; i < countTotal(); ++i) {
            result = result + getName(i) +
                    (((i+1) < countTotal())? "，" : "");
        }
        return result;
    }

    @Override
    public void clear() {
        list = new String[0];
        reset();
    }

    @Override
    public void reset() {
        count = 0;
        currentChoosen = -1;
        currentChoosenGot = -1;
        numberOfGot = 0;
        save();
    }

    private void save() {
        SharedPreferences sharedPreferences = ApplicationChou.getInstance().getSharedPreferences("chou");
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("list", getAllNames());
        editor.putInt("count", count);
        editor.putInt("currentChoosen", currentChoosen);
        editor.putInt("currentChoosenGot", currentChoosenGot);
        editor.putInt("numberOfGot", numberOfGot);
        editor.commit();
    }

    private void restore() {
        SharedPreferences sharedPreferences = ApplicationChou.getInstance().getSharedPreferences("chou");
        String data = sharedPreferences.getString("list", "").trim();
        if (data.isEmpty()) {
            list = new String[0];
        } else {
            list = data.split("[,，]");
        }
        count = sharedPreferences.getInt("count", 0);
        currentChoosen = sharedPreferences.getInt("currentChoosen", -1);
        currentChoosenGot = sharedPreferences.getInt("currentChoosenGot", -1);
        numberOfGot = sharedPreferences.getInt("numberOfGot", 0);
        random = new Random();
        random.setSeed(System.currentTimeMillis());
    }

    private static void randomize(Random random, String stringArray[], int begin, int count) {
        random.setSeed(System.currentTimeMillis());

        String tmp;
        for (int i=0; i<count; ++i) {
            final int rand = Math.abs(random.nextInt());

            final int n = count - i;
            final int index = rand % n;

            tmp = stringArray[begin + index];
            stringArray[begin + index] = stringArray[begin + i];
            stringArray[begin + i] = tmp;
        }
    }
}
