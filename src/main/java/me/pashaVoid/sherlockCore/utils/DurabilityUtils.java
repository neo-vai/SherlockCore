package me.pashaVoid.sherlockCore.utils;

import io.papermc.paper.datacomponent.DataComponentTypes;
import me.pashaVoid.sherlockCore.effects.CustomEffects;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class DurabilityUtils {

    public static void damageItem(ItemStack item, int damage, Player player, EquipmentSlot slot) {

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
            breakItem(item, player, slot);
            return;
        }
        item.setData(DataComponentTypes.DAMAGE, damageValue);
    }

    private static void breakItem(ItemStack item, Player player, EquipmentSlot slot) {
        player.getInventory().setItem(slot, new ItemStack(Material.AIR));
        CustomEffects.breakItem(item, player);
    }

}
