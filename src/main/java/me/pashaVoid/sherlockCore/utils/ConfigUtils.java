package me.pashaVoid.sherlockCore.utils;

import me.pashaVoid.sherlockCore.Magnifier;
import me.pashaVoid.sherlockCore.SherlockCore;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

public class ConfigUtils {
    private final JavaPlugin plugin;
    private final Map<String, FileConfiguration> configs = new HashMap<>();
    private final Map<String, File> configFiles = new HashMap<>();

    public ConfigUtils(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    public FileConfiguration createOrLoadConfig(String name) {
        if (configs.containsKey(name)) {
            return configs.get(name);
        }

        File configFile = new File(plugin.getDataFolder(), name + ".yml");
        boolean isNewFile = false;

        try {
            if (!configFile.exists()) {
                if (plugin.getResource(name + ".yml") != null) {
                    plugin.saveResource(name + ".yml", false);
                } else {
                    configFile.getParentFile().mkdirs();
                    if (!configFile.createNewFile()) {
                        plugin.getLogger().severe("Не удалось создать " + name + ".yml");
                        return null;
                    }
                }
                isNewFile = true;
            }
        } catch (IOException e) {
            plugin.getLogger().severe("Ошибка при создании " + name + ".yml: " + e.getMessage());
            return null;
        }

        // Загружаем конфиг с диска
        FileConfiguration config = YamlConfiguration.loadConfiguration(configFile);

        // Если есть дефолтный конфиг в ресурсах, обновляем отсутствующие ключи
        try (InputStream defaultStream = plugin.getResource(name + ".yml")) {
            if (defaultStream != null) {
                YamlConfiguration defaultConfig = YamlConfiguration.loadConfiguration(
                        new InputStreamReader(defaultStream, StandardCharsets.UTF_8)
                );

                // Добавляем отсутствующие ключи из дефолтного конфига
                boolean hasChanges = false;
                for (String key : defaultConfig.getKeys(true)) {
                    if (!config.contains(key)) {
                        config.set(key, defaultConfig.get(key));
                        hasChanges = true;
                    }
                }

                // Сохраняем, если конфиг новый или были изменения
                if (hasChanges || isNewFile) {
                    config.save(configFile);
                }
            }
        } catch (IOException e) {
            plugin.getLogger().severe("Ошибка при загрузке дефолтного конфига " + name + ": " + e.getMessage());
        }

        configs.put(name, config);
        configFiles.put(name, configFile);

        return config;
    }

    public void loadItemsConfig(FileConfiguration config) {
        Logger logger = SherlockCore.getInstance().getLogger();
        if (config == null) {
            logger.severe("ITEMS.YML is null!");
            return;
        }
        ConfigurationSection itemsSection = config.getConfigurationSection("items");
        if (itemsSection == null) {
            logger.severe("In items.yml items section is null!");
            return;
        }
        Set<String> itemKeys = itemsSection.getKeys(false);
        for (String key : itemKeys) {
            ConfigurationSection item = itemsSection.getConfigurationSection(key);
            if (item == null) {
                logger.severe("Syntax in items.yml!");
                return;
            }
            String name = item.getString("name", "Magnifier");
            int nicks = item.getInt("nicks", 0);
            int durability = item.getInt("durability", 1);
            int add_chances = item.getInt("add_chances", 0);
            boolean show_thief = item.getBoolean("show_thief", false);
            new Magnifier(key, name, nicks, durability, add_chances, show_thief);
        }
    }

    public Object getValue(String configName, String key) {
        FileConfiguration config = configs.get(configName);
        if (config == null) {
            config = createOrLoadConfig(configName);
            if (config == null) return null;
        }
        return config.get(key);
    }

    public void saveConfig(String configName) throws IOException {
        FileConfiguration config = configs.get(configName);
        File configFile = configFiles.get(configName);

        if (config == null || configFile == null) {
            throw new IllegalArgumentException("Config " + configName + " not loaded!");
        }

        config.save(configFile);
    }

    public void reloadConfig(String configName) {
        File configFile = new File(plugin.getDataFolder(), configName + ".yml");
        if (!configFile.exists()) {
            plugin.getLogger().warning(configName + ".yml not found! Creating new...");
            createOrLoadConfig(configName);
            return;
        }

        FileConfiguration config = YamlConfiguration.loadConfiguration(configFile);
        configs.put(configName, config);
        configFiles.put(configName, configFile);
    }

    public void unloadConfig(String configName, boolean saveBeforeUnload) throws IOException {
        if (saveBeforeUnload) {
            saveConfig(configName);
        }
        configs.remove(configName);
        configFiles.remove(configName);
    }
}