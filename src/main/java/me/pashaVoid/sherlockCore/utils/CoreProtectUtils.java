package me.pashaVoid.sherlockCore.utils;
import net.coreprotect.CoreProtect;
import net.coreprotect.CoreProtectAPI;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.plugin.Plugin;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;


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

            int cnt = 0;
            for (String[] entry : rawData) {
                if (cnt == limit) {
                    break;
                }

                CoreProtectAPI.ParseResult result = coreProtect.parseResult(entry);

                String action;
                if (result.getActionId() == 2) {
                    continue;
                } else {
                    action = switch (result.getActionId()) {
                        case 0 -> "§c-";
                        case 1 -> "§a+";
                        case 2 -> "§d*";
                        default -> "§8";
                    };
                    cnt += 1;
                }

                List<String> entryData = new ArrayList<>(4);
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

                Material blockType = result.getType(); // Получаем материал блока
                String blockName = formatBlockName(blockType); // Форматируем название
                entryData.add(action + " " + blockName);

                history.add(entryData);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return history;
    }

    public static List<List<String>> getContainerHistory(Block block, int limit) {
        List<List<String>> history = new ArrayList<>();
        if (coreProtect == null || !coreProtect.isEnabled()) return history;

        try {
            // Решение 1: Создаем изменяемую копию данных
            List<String[]> rawData = new ArrayList<>(coreProtect.performLookup(
                    600000,
                    null,
                    null,
                    null,
                    null,
                    new ArrayList<>(List.of(4)),
                    5,
                    block.getLocation()
            ));
//            for (String[] entry : rawData) {
//                System.out.println("Raw entry: " + Arrays.toString(entry));
//            }

            // Обработка данных
            int cnt = 0;
            for (String[] entry : rawData) {
                if (cnt == limit) {
                    break;
                }
                int quantity = Integer.parseInt(entry[10]);
                if (quantity == -1) {
                    continue;
                }
                CoreProtectAPI.ParseResult result = coreProtect.parseResult(entry);

                int actionId = result.getActionId();
                String action;
                if (actionId == 2) {
                    continue;
                } else {
                    action = switch (actionId) {
                        case 0 -> "§c-";
                        case 1 -> "§a+";
                        case 2 -> "§d*";
                        default -> "§8";
                    };
                    cnt += 1;
                }

                List<String> entryData = new ArrayList<>(4);

                // Ник игрока
                entryData.add(result.getPlayer());

                // Время события
                long eventTimeSeconds = result.getTimestamp();
                long currentTimeSeconds = System.currentTimeMillis() / 1000;
                long diffSeconds = currentTimeSeconds - eventTimeSeconds;
                entryData.add(formatTimeAgo(diffSeconds));

                entryData.add(action + " " + quantity +" " + formatBlockName(result.getType()));

                history.add(entryData);
            }
        } catch (Exception e) {
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
