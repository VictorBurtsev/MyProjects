package ru.niivk.burtsev;

public class crc16 {

    private final int polynomial = 0xA001;

    private int crcRegister = 0xFFFF;

    private int getCRC16(int[] Bytes) {

        for (int i = 0; i < Bytes.length; i++) {

            crcRegister ^= Bytes[i];

            for (int j = 0; j < 8; j++) {

                if (Integer.lowestOneBit(crcRegister) == 1) {

                    crcRegister >>= 1;
                    crcRegister ^= polynomial;

                } else crcRegister >>= 1;
            }
        }
        return crcRegister;
    }

    public static void main(String[] args) {

        final int[] Bytes = new int[]{0x01, 0x10, 0x0A, 0x01, 0x00, 0x02, 0x04, 0x40, 0x16, 0x66, 0x66}; // Enter command

        System.out.println(Integer.toHexString(new crc16().getCRC16(Bytes)));

    }
}