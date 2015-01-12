package io.github.hepengfei.choujiang;

import java.util.Random;

/**
 * Created by hpf on 15/1/12.
 */
public class ChouJiangRandom implements ChouJiangInterface {
    private String list[];
    private int count;
    private Random random;

    private int currentChoosen;
    private boolean gotIt;

    @Override
    public void init(String[] list) {
        this.list = new String[list.length];
        System.arraycopy(list, 0, this.list, 0, list.length);
        count = list.length;

        random = new Random();
        random.setSeed(System.currentTimeMillis());

        currentChoosen = -1;
        gotIt = false;
    }

    @Override
    public int countTotal() {
        return list.length;
    }

    @Override
    public int countLeft() {
        return count - ((gotIt && currentChoosen != -1) ? 1 : 0);
    }

    @Override
    public int countGot() {
        return countTotal() - countLeft();
    }

    @Override
    public void next() {
        if (gotIt && currentChoosen != -1) {
            count = count - 1;

            String tmp = list[currentChoosen];
            list[currentChoosen] = list[count];
            list[count] = tmp;

            gotIt = false;
            currentChoosen = -1;
        }

        if (count == 0) {
            return;
        }

        randomize(random, list, 0, count);

        random.setSeed(System.currentTimeMillis());
        currentChoosen = Math.abs(random.nextInt()) % count;
    }

    @Override
    public String chosenForDisplay() {
        if (count == 0) {
            return "";
        }
        return list[Math.abs(random.nextInt()) % list.length];
    }

    @Override
    public String chosen() {
        if (currentChoosen == -1) {
            return "";
        }
        return list[currentChoosen];
    }

    @Override
    public void gotIt() {
        gotIt = true;
    }

    @Override
    public void giveUp() {
        gotIt = false;
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
