package ru.niivk.burtsev;

/**
 * 1
 * 08.07.2016
 */
public class hexIEE754Converter {

    private static String GetHexIEE754(float value) {

        String inputString = Integer.toHexString(Float.floatToIntBits(value));
        String outputString = "";

        for (int i = 0; i < inputString.length(); i++) {
            if (i == 0) {
                outputString += "0x";
            } else if ((i == 2) || (i == 4) || (i == 6) || (i == 8)) {
                outputString += " 0x";
            }
            outputString += inputString.charAt(i);
        }
        return outputString;
    }

    public static void main(String[] args) {

        System.out.println(GetHexIEE754((float) 2.35));

    }
}
