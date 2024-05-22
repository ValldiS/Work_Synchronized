package org.example;

import java.util.*;

public class Freqlog {

    public static final Map<Integer, Integer> sizeToFreq = new HashMap<>();

    public static void main(String[] args) {

        String[] arrCommand = new String[1000];

        for (int i = 0; i < arrCommand.length; i++) {
            arrCommand[i] = generateRoute("RLRFR", 100);
        }


        for (String text : arrCommand) {
            new Thread(() -> {
                int count = 0;
                for (int i = 0; i < text.length(); i++) {
                    if (String.valueOf(text.charAt(i)).equals("R")) {
                        count++;
                    }
                }
                synchronized (sizeToFreq) {
                    int amount = 1;
                    if (!sizeToFreq.containsKey(count)) {
                        sizeToFreq.put(count, amount);
                    } else {
                        sizeToFreq.replace(count, sizeToFreq.get(count) + 1);
                    }
                    sizeToFreq.notify();
                }
            }).start();
        }

        new Thread(() -> {
            synchronized (sizeToFreq) {
                try {
                    sizeToFreq.wait();
                    int max = 0;
                    int keyMax = 0;
                    for (Integer key : sizeToFreq.keySet()) {
                        if (sizeToFreq.get(key) > max) {
                            max = sizeToFreq.get(key);
                            keyMax = key;
                        }
                    }
                    System.out.printf("Самое частое количество повторений %d (встретилось %d раз)\n", keyMax, max);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }).start();

        Thread.interrupted();


    }

    public static String generateRoute(String letters, int length) {
        Random random = new Random();
        StringBuilder route = new StringBuilder();
        for (int i = 0; i < length; i++) {
            route.append(letters.charAt(random.nextInt(letters.length())));
        }
        return route.toString();
    }
}
