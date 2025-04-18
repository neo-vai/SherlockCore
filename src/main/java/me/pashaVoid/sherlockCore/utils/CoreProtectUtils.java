package me.pashaVoid.sherlockCore.utils;
import net.coreprotect.CoreProtect;
import net.coreprotect.CoreProtectAPI;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.plugin.Plugin;
import java.util.ArrayList;
import java.util.List;

import static me.pashaVoid.sherlockCore.SherlockCore.coreProtect;

public class CoreProtectUtils {

    public static CoreProtectAPI getCoreProtectAPI() {
        Plugin plugin = Bukkit.getServer().getPluginManager().getPlugin("CoreProtect");
        return (plugin instanceof CoreProtect) ? ((CoreProtect) plugin).getAPI() : null;
    }

    public static List<List<String>> getBlockHistory(Block block, int limit) { // [[player, hours:day, action][...][...]]
        List<List<String>> history = new ArrayList<>();
        if (coreProtect == null || !coreProtect.isEnabled()) return history;

        try {
            List<String[]> rawData = coreProtect.blockLookup(block, 0);
            int maxEntries = Math.min(limit, rawData.size());
            List<String[]> lastEntries = rawData.subList(0, maxEntries);

            for (String[] entry : lastEntries) {
                CoreProtectAPI.ParseResult result = coreProtect.parseResult(entry);
                List<String> entryData = new ArrayList<>(4); // [ник, часы, действие, блок]
                // Ник игрока
                entryData.add(result.getPlayer());

                // Расчет времени
                long eventTimeSeconds = result.getTimestamp(); // Время события в секундах
                long currentTimeSeconds = System.currentTimeMillis() / 1000; // Текущее время в секундах
                long diffSeconds = currentTimeSeconds - eventTimeSeconds;

                // Форматирование времени
                String timeAgo = formatTimeAgo(diffSeconds);
                entryData.add(timeAgo);

                // Действие и тип блока
                String action = switch (result.getActionId()) {
                    case 0 -> "§c-";
                    case 1 -> "§a+";
                    case 2 -> "§d*";
                    default -> "§8";
                };

                Material blockType = result.getType(); // Получаем материал блока
                String blockName = formatBlockName(blockType); // Форматируем название
                entryData.add(action + " " + blockName);

                history.add(entryData);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        return history;
    }

    private static String formatTimeAgo(long diffSeconds) {
        if (diffSeconds < 0) diffSeconds = 0; // Защита от отрицательного времени

        long hours = diffSeconds / 3600;
        if (hours < 24) {
            return String.valueOf(hours);
        }

        long days = hours / 24;
        hours = hours % 24;
        return String.valueOf(hours) + ":" + String.valueOf(days);
    }

    private static String formatBlockName(Material material) {
        if (material == null) return "неизвестный блок";

        String name = material.toString()
                .toLowerCase()
                .replace("_", " ");
        return name.substring(0, 1).toUpperCase() + name.substring(1);
    }

    public static List<Long> convertStringDataToLong(String input) {
        List<Long> result = new ArrayList<>();
        String[] parts = input.split(":"); // Разделяем строку по ":"

        for (String part : parts) {
            result.add(Long.parseLong(part)); // Парсим каждую часть в long
        }

        return result;
    }
}
