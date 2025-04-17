package me.pashaVoid.sherlockCore;

import net.coreprotect.CoreProtectAPI;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import static me.pashaVoid.sherlockCore.utils.CoreProtectUtils.getCoreProtectAPI;

public final class SherlockCore extends JavaPlugin {

    private static SherlockCore instance;
    public static CoreProtectAPI coreProtect;
    public static SherlockCore getInstance() {return instance;}

    @Override
    public void onEnable() {
        instance = this;

        CoreProtectAPI cp = getCoreProtectAPI();
        if (cp == null) {
            getLogger().severe("CoreProtect не найден! Плагин отключен.");
            Bukkit.getPluginManager().disablePlugin(this);
            return;
        }
        coreProtect = cp;
        getLogger().info("CoreProtect API подключен успешно! Версия: " + cp.APIVersion());

        getCommand("checkhistory").setExecutor(new TestCMD());
        getCommand("givemagnifier").setExecutor(new GiveMagnifierCMD());
        getServer().getPluginManager().registerEvents(new ItemListener(), this);

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
