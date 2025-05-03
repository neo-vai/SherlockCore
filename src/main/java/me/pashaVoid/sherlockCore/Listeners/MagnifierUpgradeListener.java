package me.pashaVoid.sherlockCore.Listeners;

import me.pashaVoid.sherlockCore.config.MainConfig;
import me.pashaVoid.sherlockCore.effects.CustomEffects;
import me.pashaVoid.sherlockCore.utils.MagnifierItemUtils;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.inventory.PrepareAnvilEvent;
import org.bukkit.inventory.AnvilInventory;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class MagnifierUpgradeListener implements Listener {

    @EventHandler
    public void onAnvil(PrepareAnvilEvent e) {
        AnvilInventory inventory = e.getInventory();
        ItemStack first_slot = inventory.getItem(0);
        ItemStack second_slot = inventory.getItem(1);

        if (!MainConfig.enable_magnifier_upgrade) return;
        if (!MagnifierItemUtils.checkMagnifier(first_slot)) return;
        if (MagnifierItemUtils.checkUpgrade(first_slot)) return;
        if (second_slot == null) return;
        if (!(second_slot.getType() == Material.CLOCK)) return;

        ItemStack upgrade_magnifier = MagnifierItemUtils.upgradeTimeMagnifier(first_slot);
        e.setResult(upgrade_magnifier);
    }

    @EventHandler
    public void onClick(InventoryClickEvent e) {
        Inventory inventory = e.getInventory();
        if (e.getClickedInventory() == null || e.getClickedInventory().getType() != InventoryType.ANVIL || e.getSlot() != 2) return;
        ItemStack magnifier = inventory.getItem(2);
        if (!MagnifierItemUtils.checkMagnifier(magnifier)) return;

        ItemStack second_slot = inventory.getItem(1);
        if (second_slot == null || second_slot.getType() != Material.CLOCK) return;

        Player player = (Player) e.getWhoClicked();
        if (player.getItemOnCursor().getType() != Material.AIR) return;

        player.setItemOnCursor(magnifier);

        inventory.setItem(0, new ItemStack(Material.AIR));
        second_slot.setAmount(second_slot.getAmount() - 1);
        inventory.setItem(2, new ItemStack(Material.AIR));

        CustomEffects.upgradeMagnifier(player);
    }

}
