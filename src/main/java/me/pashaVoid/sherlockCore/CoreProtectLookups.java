package me.pashaVoid.sherlockCore;

import net.coreprotect.CoreProtect;
import net.coreprotect.CoreProtectAPI;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.plugin.Plugin;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;

import static me.pashaVoid.sherlockCore.SherlockCore.coreProtect;

public class CoreProtectLookups {

    public static CoreProtectAPI getCoreProtectAPI() {
        Plugin plugin = Bukkit.getServer().getPluginManager().getPlugin("CoreProtect");
        return (plugin instanceof CoreProtect) ? ((CoreProtect) plugin).getAPI() : null;
    }

    public static List<List<String>> getBlockHistory(Block block, int limit) { // [[player, hours:day, action][...][...]]
        List<List<String>> history = new ArrayList<>();
        if (coreProtect == null || !coreProtect.isEnabled()) return history;

        try {
            List<String[]> rawData = coreProtect.blockLookup(block, 0);
            return getHistory(rawData, limit, false, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return history;
    }

    public static List<List<String>> getContainerHistory(Block block, int limit) {
        List<List<String>> history = new ArrayList<>();
        if (coreProtect == null || !coreProtect.isEnabled()) return history;

        try {
            List<String[]> rawData = new ArrayList<>(coreProtect.performLookup(
                    600000,
                    null,
                    null,
                    null,
                    null,
                    new ArrayList<>(List.of(4)),
                    1,
                    block.getLocation()
            ));
            return getHistory(rawData, limit, true, block);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return history;
    }

    private static List<List<String>> getHistory(List<String[]> rawData, int limit, boolean isChest, @Nullable Block block) {

        if (rawData == null || rawData.isEmpty()) return new ArrayList<>();
        if (isChest && block == null) return new ArrayList<>();

        int x = 0;
        int y = 0;
        int z = 0;
        if (block != null) {
            x = block.getLocation().getBlockX();
            y = block.getLocation().getBlockY();
            z = block.getLocation().getBlockZ();
        }

        List<List<String>> history = new ArrayList<>();
        int cnt = 0;
        for (String[] entry : rawData) {
            if (cnt == limit) {
                break;
            }
            if (entry[11] == null) continue;

            CoreProtectAPI.ParseResult result = coreProtect.parseResult(entry);

            int quantity = 1;
            if (isChest) {
                if (
                        (Integer.parseInt(entry[2]) != x) ||
                        (Integer.parseInt(entry[3]) != y) ||
                        (Integer.parseInt(entry[4]) != z)) {
                    continue;
                }
                quantity = Integer.parseInt(entry[10]);
                if (quantity == -1) {
                    continue;
                }
            }

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
            long eventTimeSeconds = result.getTimestamp(); // Время события в секундах
            long currentTimeSeconds = System.currentTimeMillis(); // Текущее время в секундах
            long diffSeconds = (currentTimeSeconds - eventTimeSeconds) / 1000;

            String timeAgo = formatTimeAgo(diffSeconds);
            entryData.add(timeAgo);

            entryData.add((isChest) ? (action + " " + quantity + " " + formatBlockName(result.getType())) : (action + formatBlockName(result.getType())));


            history.add(entryData);
        }
        return history;
    }

    private static String formatTimeAgo(long diffSeconds) {
        if (diffSeconds < 0) diffSeconds = 0;

        long hours = diffSeconds / 3600;
        if (hours < 24) {
            return String.valueOf(hours);
        }

        long days = hours / 24;
        hours = hours % 24;
        return hours + ":" + days;
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
