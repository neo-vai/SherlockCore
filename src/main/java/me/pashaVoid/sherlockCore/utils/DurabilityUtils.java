package me.pashaVoid.sherlockCore.utils;

import io.papermc.paper.datacomponent.DataComponentType;
import io.papermc.paper.datacomponent.DataComponentTypes;
import org.bukkit.*;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class DurabilityUtils {

    public static void damageItem(ItemStack item, int damage, Player player) {

        if (player.getGameMode() == GameMode.CREATIVE || player.getGameMode() == GameMode.SPECTATOR) {
            return;
        }

        ItemMeta meta = item.getItemMeta();
        if (meta.isUnbreakable()) return;

        int unbreakingLevel = item.getEnchantmentLevel(Enchantment.UNBREAKING);
        if (unbreakingLevel > 0) {
            double chance = 1.0 / (unbreakingLevel + 1.0);
            if (Math.random() < chance) {
                return;
            }
        }

        int max_damage = item.getData(DataComponentTypes.MAX_DAMAGE);

        int damageValue = item.getData(DataComponentTypes.DAMAGE);
        damageValue += damage;
        if (damageValue >= max_damage) {
            breakItem(item, player);
            return;
        }
        item.setData(DataComponentTypes.DAMAGE, damageValue);
    }

    private static void breakItem(ItemStack item, Player player) {
        player.getInventory().remove(item);
        player.playEffect(EntityEffect.BREAK_EQUIPMENT_MAIN_HAND);
        player.spawnParticle(
                Particle.ITEM, // Тип частиц
                player.getLocation().add(0, 1, 0), // Позиция
                10,
                0.3, 0.3, 0.3,
                0.1,
                item
        );
        player.playSound(
                player.getLocation(),
                Sound.ENTITY_ITEM_BREAK,
                1.0f,
                1.0f
        );
    }

}
