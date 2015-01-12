package io.github.hepengfei.choujiang;

import java.util.Random;

public class Randomizer {

    public static void randomize(Random random, String stringArray[], int begin, int count) {
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
