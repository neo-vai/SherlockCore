package me.pashaVoid.sherlockCore;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

public class Encrypt {

    public static String encryptNickname(String nickname, int encryptionPercent, int x, int y, int z) {
        if (nickname.isEmpty() || encryptionPercent <= 0) return nickname;
        if (encryptionPercent >= 100) return "*".repeat(nickname.length());

        // Генерация уникального хеша из ника и координат
        String hash = generateHash(nickname + x + y + z);

        // Преобразование хеша в числовое значение
        long seed = hashToSeed(hash);

        // Расчёт количества заменяемых символов
        int totalChars = nickname.length();
        int replaceCount = (int) Math.ceil(totalChars * encryptionPercent / 100.0);

        // Массив символов для модификации
        char[] chars = nickname.toCharArray();

        // Замена символов на '*' на основе хеша
        for (int i = 0; i < replaceCount; i++) {
            int pos = (int) (seed % totalChars);
            seed /= totalChars; // Обновление "зерна" для следующей позиции
            if (pos < 0) pos = Math.abs(pos);
            chars[pos] = '*';
        }

        return new String(chars);
    }


    private static String generateHash(String input) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = md.digest(input.getBytes());
            StringBuilder hex = new StringBuilder();
            for (byte b : hashBytes) {
                String hexByte = String.format("%02x", b);
                hex.append(hexByte);
            }
            return hex.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("SHA-256 not available", e);
        }
    }


    private static long hashToSeed(String hash) {
        String hexPart = hash.substring(0, 16); // Берём первые 16 символов хеша
        return Long.parseUnsignedLong(hexPart, 16);
    }

    public static List<Integer> calculatePercentagesList(int size, int addChance) {
        List<Integer> percentages = new ArrayList<>();
        double start = 5;
        double end = 65;

        if (size == 0) return percentages;

        percentages.add((int) (start - addChance));
        if (size == 1) return percentages;

        double step = (end - start) / (size - 1);

        for (int i = 1; i < size - 1; i++) {
            int percent = (int) Math.round(15 + i * step);
            percentages.add(percent);
        }

        percentages.add((int) (end - addChance));

        return percentages;
    }

}
