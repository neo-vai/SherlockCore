package me.pashaVoid.sherlockCore;

import me.pashaVoid.sherlockCore.commands.GiveMagnifierCMD;
import me.pashaVoid.sherlockCore.listeners.MagnifierListener;
import me.pashaVoid.sherlockCore.listeners.MagnifierUpgradeListener;
import me.pashaVoid.sherlockCore.config.ConfigUtils;
import me.pashaVoid.sherlockCore.config.MainConfig;
import me.pashaVoid.sherlockCore.config.MessagesConfig;
import net.coreprotect.CoreProtectAPI;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import static me.pashaVoid.sherlockCore.CoreProtectLookups.getCoreProtectAPI;

public final class SherlockCore extends JavaPlugin {

    private static SherlockCore instance;
    public static CoreProtectAPI coreProtect;
    private static CooldownManager cooldownManager;

    public static SherlockCore getInstance() {return instance;}
    public static CooldownManager getCooldownManager() {return cooldownManager;}

    public ConfigUtils configUtils;

    @Override
    public void onEnable() {
        instance = this;

        CoreProtectAPI cp = getCoreProtectAPI();
        configUtils = new ConfigUtils(this);

        MainConfig.loadMainConfig(configUtils.createOrLoadConfig("config"));
        MessagesConfig.loadMessagesConfig(configUtils.createOrLoadConfig("messages"));
        configUtils.loadItemsConfig(configUtils.createOrLoadConfig("items"));

        saveDefaultConfig();

        if (cp == null) {
            getLogger().severe("CoreProtect was not found! The plugin is disabled.");
            Bukkit.getPluginManager().disablePlugin(this);
            return;
        }

        CooldownManager cooldown =  new CooldownManager(MainConfig.magnifier_cooldown);
        cooldown.startCleanupTask(this);
        cooldownManager = cooldown;

        coreProtect = cp;
        getLogger().info("The CoreProtect API has been successfully enabled! Version:" + cp.APIVersion());

        getCommand("givemagnifier").setExecutor(new GiveMagnifierCMD());

        getServer().getPluginManager().registerEvents(new MagnifierListener(), this);
        getServer().getPluginManager().registerEvents(new MagnifierUpgradeListener(), this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
