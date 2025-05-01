package me.pashaVoid.sherlockCore.utils;

import me.pashaVoid.sherlockCore.SherlockCore;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nullable;
import java.util.List;

public class InventoryUtils {

    public static void addOneItemInOffHand(ItemStack itemStack, Player player, @Nullable List<Component> lore) {
        ItemStack oneItemInOffHand = itemStack.clone();;
        oneItemInOffHand.setAmount(1);
        if (!(lore == null)) {
            oneItemInOffHand.lore(lore);
        }
        player.getInventory().setItemInOffHand(oneItemInOffHand);
        itemStack.setAmount(itemStack.getAmount() - 1);
        if (player.getInventory().firstEmpty() == -1) {
            Bukkit.getScheduler().runTask(SherlockCore.getInstance(), () -> {
                player.getWorld().dropItem(player.getLocation(), itemStack);
            });
        } else {
            player.getInventory().addItem(itemStack);
        }
    }

}
