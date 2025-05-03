package me.pashaVoid.sherlockCore.Listeners;

import me.pashaVoid.sherlockCore.CooldownManager;
import me.pashaVoid.sherlockCore.SherlockCore;
import me.pashaVoid.sherlockCore.config.MainConfig;
import me.pashaVoid.sherlockCore.config.MessagesConfig;
import me.pashaVoid.sherlockCore.effects.CustomEffects;
import me.pashaVoid.sherlockCore.utils.DurabilityUtils;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.List;

import static me.pashaVoid.sherlockCore.CoreProtectLookups.*;
import static me.pashaVoid.sherlockCore.Encrypt.calculatePercentagesList;
import static me.pashaVoid.sherlockCore.Encrypt.encryptNickname;
import static me.pashaVoid.sherlockCore.utils.InventoryUtils.addOneItemInOffHand;

public class MagnifierListener implements Listener {

    @EventHandler
    public void onItemClick(PlayerInteractEvent e){
        ItemStack item = e.getItem();
        if (item == null) {
            return;
        }
        if (item.getItemMeta() == null) {
            return;
        }
        if (!item.getItemMeta().getPersistentDataContainer().has(NamespacedKey.minecraft("magnifier"))) {
            return;
        }

        Action action = e.getAction();
        if (!(e.getAction() == Action.LEFT_CLICK_BLOCK || e.getAction() == Action.RIGHT_CLICK_BLOCK)) {
            return;
        }
        if (e.getClickedBlock() == null) {
            return;
        }

        Player player = e.getPlayer();

        CooldownManager cooldownManager = SherlockCore.getCooldownManager();
        if (!MainConfig.enable_magnifier) {
            e.setCancelled(true);
            return;
        }
        if (!cooldownManager.isReady(player.getUniqueId())) {
            e.setCancelled(true);
            return;
        } else {
            cooldownManager.applyCooldown(player.getUniqueId());
        }

        ItemStack itemInOffHand = e.getPlayer().getInventory().getItemInOffHand();

        boolean writePaper;
        ItemMeta metaOffHand = itemInOffHand.getItemMeta();

        if (MainConfig.enable_paper_write && itemInOffHand.getType() == Material.PAPER && metaOffHand!= null) {
            if (metaOffHand.getPersistentDataContainer().isEmpty() && !metaOffHand.hasLore()) {
                writePaper = true;
            } else {
                writePaper = false;
            }
        } else {
            writePaper = false;
        }

        e.setCancelled(true);
        PersistentDataContainer container = e.getItem().getItemMeta().getPersistentDataContainer();
        int nicks = container.get(NamespacedKey.minecraft("magnifier_nicks"), PersistentDataType.INTEGER);
        int durability = container.get(NamespacedKey.minecraft("magnifier_durability"), PersistentDataType.INTEGER);
        boolean show_time = container.get(NamespacedKey.minecraft("magnifier_show_time"), PersistentDataType.BOOLEAN);
        int add_chances = container.get(NamespacedKey.minecraft("magnifier_add_chances"), PersistentDataType.INTEGER);
        boolean show_thief = container.get(NamespacedKey.minecraft("magnifier_show_thief"), PersistentDataType.BOOLEAN);

        Block targetBlock = e.getClickedBlock(); // блок на который тыкнул
        if (player.isSneaking()) {
            targetBlock = targetBlock.getRelative(BlockFace.UP);
        }

        Block block = targetBlock; // выбираем финальный блок

        Location center = block.getLocation().add(0.5, 1, 0.5);

        DurabilityUtils.damageItem(item, 1, player, e.getHand());

        Bukkit.getScheduler().runTaskAsynchronously(SherlockCore.getInstance(), () -> {
            List<List<String>> history;
            if (action == Action.RIGHT_CLICK_BLOCK) {
                history = getContainerHistory(block, nicks);
                CustomEffects.successfulMagnifierRight(player, center);
            }
            else {
                history = getBlockHistory(block, nicks);
                CustomEffects.successfulMagnifierLeft(player, center);
            }


            List<Integer> chanceList = calculatePercentagesList(nicks, add_chances);

            if (history.isEmpty()) {
                player.sendMessage(MessagesConfig.no_history);
                return;
            }

            int cnt = 0;
            int time_cnt = (show_time) ? (nicks * 20) / 100: 0;
            List<Component> answer = new ArrayList<>();

            for (int i = 0; i < history.size(); i++) {
                List<String> entry = history.get(i);
                int chance = chanceList.get(i);
                String nick = encryptNickname(entry.get(0), chance, block.getX(), block.getY(), block.getZ());
                cnt += 1;

                String messageString = "§7" + cnt + "§f " + nick + " " + entry.get(2);
                if (time_cnt > 0) {
                    List<Long> date = convertStringDataToLong(entry.get(1));

                    String strDate = "";
                    if (date.size() > 1) strDate += date.getLast() + "д.";
                    strDate += date.getFirst() + "ч.";

                    messageString += " §8(" + strDate + ")";
                    time_cnt -= 1;
                }
                answer.add(Component.text(messageString));
            }
            if (writePaper) {
                answer.add(Component.text("§a©" + player.getName()));
                player.sendMessage(MessagesConfig.history_written_to_paper);
                if (itemInOffHand.getAmount() >= 1) {
                    addOneItemInOffHand(itemInOffHand, player, answer);
                } else {
                    itemInOffHand.lore(answer);
                }
            } else {
                player.sendMessage(MessagesConfig.block_history_preview);
                for (Component component : answer) {
                    player.sendMessage(component);
                }
            }
        });
    }
}
