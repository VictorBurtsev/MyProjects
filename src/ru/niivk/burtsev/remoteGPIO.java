package ru.niivk.burtsev;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class remoteGPIO {

    private final int[][] case_0_900 = {
            {0, 0}, {100, 1}, {200, 2}, {300, 3}, {400, 4},
            {500, 5}, {600, 6}, {700, 7}, {800, 8}, {900, 9},
    };

    private final int[][] case_1000_9000 = {
            {0, 0}, {1000, 1}, {2000, 2}, {3000, 3}, {4000, 4},
            {5000, 5}, {6000, 6}, {7000, 7}, {8000, 8}, {9000, 9},
    };

    private final int[][] case_10000_20000 = {
            {0, 0}, {10000, 1}, {20000, 3},
    };

    private final int[][] case_30000_240000 = {
            {0, 0}, {30_000, 1}, {60_000, 3},
            {90_000, 7}, {120_000, 15}, {150_000, 31},
            {180_000, 63}, {210_000, 127}, {240_000, 255},
    };

    private final String[] blocks = {
            "",
            "1 (30кВт - 240кВт)",
            "2 (10кВт - 20кВт)",
            "3 (1кВт - 9кВт)",
            "4 (0Вт - 900Вт)"
    };

    private final void print(int i, int[][] myCase, int shactnoe, int cases) {
        System.out.println(
                "Установить в блоке " + blocks[i] + " мощность "
                        + myCase[shactnoe][0]
                        + "Вт"
                        + ", комбинация для установки "
                        + getBinary32bitFormat(cases));

    }

    private int setPower(int power) {

        int octatok, shactnoe, case30_240, case10_20, case1_9, case0_09;


        System.out.println("Мощность введеная оператором: " + power + "Вт\n");

        octatok = power % 30000;
        shactnoe = power / 30000;
        case30_240 = case_30000_240000[shactnoe][1];
        print(1, case_30000_240000, shactnoe, case30_240);

        shactnoe = octatok / 10000;
        octatok %= 10000;
        case10_20 = case_10000_20000[shactnoe][1];
        print(2, case_10000_20000, shactnoe, case10_20);

        shactnoe = octatok / 1000;
        octatok %= 1000;
        case1_9 = case_1000_9000[shactnoe][1];
        print(3, case_1000_9000, shactnoe, case1_9);


        shactnoe = octatok / 100;
        case0_09 = case_0_900[shactnoe][1];
        print(4, case_0_900, shactnoe, case0_09);


        System.out.println();
        return case30_240 << 10 ^ case10_20 << 8 ^ case1_9 << 4 ^ case0_09;
    }


    private static String getBinary32bitFormat(int num) {
        StringBuilder s = new StringBuilder();
        for (int i = 0; i < 32; i++) {
            s.append((num & 1) == 1 ? 1 : 0);
            if (i == 3 || i == 7 || i == 11 || i == 15 || i == 19 || i == 23 || i == 27) s.append(" ");
            num >>= 1;
        }
        return s.reverse().toString();
    }

    public static void main(String[] args) {

        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

        try {
            while (true) {
                System.out.print("Введите мощность (допустимые значения от 0 до 269900Вт, шаг 100Вт: ");
                int i = Integer.parseInt(reader.readLine());
                System.out.println();
                System.out.println("Загружаем в GPIO NI: " + getBinary32bitFormat(new remoteGPIO().setPower(i)) + "\n");
            }
        } catch (NumberFormatException | IOException e) {
            System.out.println("\nПрограмма завершена.");
        }
    }
}