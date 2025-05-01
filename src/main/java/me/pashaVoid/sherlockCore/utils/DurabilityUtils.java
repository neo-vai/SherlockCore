package me.pashaVoid.sherlockCore.utils;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class DurabilityUtils {

    public void damageItem(ItemStack item, int damage) {
        ItemMeta meta = item.getItemMeta();
        if (meta.isUnbreakable())
            return;

    }

}
