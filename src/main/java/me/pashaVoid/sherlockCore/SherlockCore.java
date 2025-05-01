package me.pashaVoid.sherlockCore;

import me.pashaVoid.sherlockCore.Commands.GiveMagnifierCMD;
import me.pashaVoid.sherlockCore.Listeners.MagnifierListener;
import me.pashaVoid.sherlockCore.utils.ConfigUtils;
import net.coreprotect.CoreProtectAPI;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import static me.pashaVoid.sherlockCore.CoreProtectLookups.getCoreProtectAPI;

public final class SherlockCore extends JavaPlugin {

    private static SherlockCore instance;
    public static CoreProtectAPI coreProtect;
    public static SherlockCore getInstance() {return instance;}

    public ConfigUtils configUtils;

    @Override
    public void onEnable() {
        instance = this;

        CoreProtectAPI cp = getCoreProtectAPI();
        configUtils = new ConfigUtils(this);

        saveDefaultConfig();

        configUtils.createOrLoadConfig("config");
        configUtils.createOrLoadConfig("messages");
        configUtils.loadItemsConfig(configUtils.createOrLoadConfig("items"));

        if (cp == null) {
            getLogger().severe("CoreProtect не найден! Плагин отключен.");
            Bukkit.getPluginManager().disablePlugin(this);
            return;
        }
        coreProtect = cp;
        getLogger().info("CoreProtect API подключен успешно! Версия: " + cp.APIVersion());

        getCommand("givemagnifier").setExecutor(new GiveMagnifierCMD());
        getServer().getPluginManager().registerEvents(new MagnifierListener(), this);



    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
