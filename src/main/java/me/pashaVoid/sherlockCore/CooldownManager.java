package me.pashaVoid.sherlockCore;

import org.bukkit.plugin.java.JavaPlugin;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class CooldownManager {
    private final ConcurrentHashMap<UUID, Long> cooldownMap = new ConcurrentHashMap<>();
    private final int cooldownMillis;

    public CooldownManager(int cooldownMillis) {
        this.cooldownMillis = cooldownMillis;
    }

    public boolean isReady(UUID uuid) {
        Long lastUsed = cooldownMap.get(uuid);
        return lastUsed == null || (System.currentTimeMillis() - lastUsed >= cooldownMillis);
    }

    public void applyCooldown(UUID uuid) {
        cooldownMap.put(uuid, System.currentTimeMillis());
    }

    public void startCleanupTask(JavaPlugin plugin) {
        // Очистка каждые 5 минут (6000L = 5 * 60 * 20 тиков)
        plugin.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, () -> {
            long currentTime = System.currentTimeMillis();
            cooldownMap.entrySet().removeIf(entry ->
                    currentTime - entry.getValue() > cooldownMillis
            );
        }, 0L, 6000L);
    }
}