package me.pashaVoid.sherlockCore.config;

import me.pashaVoid.sherlockCore.utils.ColorUtils;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.List;

public class MainConfig {

    public static boolean enable_magnifier = true;

    public static boolean enable_magnifier_upgrade = true;

    public static boolean enable_paper_write = true;

    public static boolean enable_custom_model_data = true;
    public static List<String> custom_model_data = List.of("magnifier", "magnifier_time");

    public static boolean enable_thief = true;

    public static int magnifier_cooldown = 1500;

    public static String prefix_time_magnifier = "";

    public static List<String> lore_default_magnifier = ColorUtils.colorizeDefaultLore(List.of(
            "&aЛКМ - увидеть историю блока",
            "aПКМ - увидеть историю содержимого блока",
            "&aИспользуй SHIFT чтобы увидеть историю блока выше"
    ));

    public static List<String> lore_time_magnifier = ColorUtils.colorizeDefaultLore(List.of(
            "&aЛКМ - увидеть историю блока",
            "aПКМ - увидеть историю содержимого блока",
            "&aИспользуй SHIFT чтобы увидеть историю блока выше",
            "&eClock version"
    ));

    public static String name_custom_magnifier = "Custom magnifier";

    public static boolean disable_sounds = false;
    public static boolean disable_particles = false;

    public static void loadMainConfig(FileConfiguration config) {
        enable_magnifier = config.getBoolean("enable_magnifier", true);

        enable_magnifier_upgrade = config.getBoolean("enable_magnifier_upgrade", true);

        enable_paper_write = config.getBoolean("enable_paper_write", true);

        enable_custom_model_data = config.getBoolean("enable_custom_model_data", true);
        custom_model_data = config.getStringList("custom_model_data");

        enable_thief = config.getBoolean("enable_thief", true);

        magnifier_cooldown = config.getInt("magnifier_cooldown", 1500);

        prefix_time_magnifier = ColorUtils.colorizeDefault(config.getString("prefix_time_magnifier", ""));

        lore_default_magnifier = ColorUtils.colorizeDefaultLore(config.getStringList("lore_default_magnifier"));
        lore_time_magnifier = ColorUtils.colorizeDefaultLore(config.getStringList("lore_time_magnifier"));

        name_custom_magnifier = ColorUtils.colorizeDefault(config.getString("name_custom_magnifier", "Custom magnifier"));

        disable_sounds = config.getBoolean("disable_sounds", false);
        disable_particles = config.getBoolean("disable_particles", false);
    }



}
