package me.pashaVoid.sherlockCore.Listeners;

import me.pashaVoid.sherlockCore.SherlockCore;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
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
import java.util.logging.Logger;

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

        ItemStack itemInOffHand = e.getPlayer().getInventory().getItemInOffHand();

        boolean writePaper;
        ItemMeta metaOffHand = itemInOffHand.getItemMeta();

        if (itemInOffHand.getType() == Material.PAPER && metaOffHand!= null) {
            if (metaOffHand.getPersistentDataContainer().isEmpty() && !metaOffHand.hasLore()) {
                writePaper = true;
            } else {
                writePaper = false;
            }
        } else {
            writePaper = false;
        }

        e.setCancelled(true);

        if (e.getAction() != Action.LEFT_CLICK_BLOCK && e.getAction() != Action.RIGHT_CLICK_BLOCK) {
            return;
        }
        if (e.getClickedBlock() == null) {
            return;
        }
        PersistentDataContainer container = e.getItem().getItemMeta().getPersistentDataContainer();
        int nicks = container.get(NamespacedKey.minecraft("magnifier_nicks"), PersistentDataType.INTEGER);
        int durability = container.get(NamespacedKey.minecraft("magnifier_durability"), PersistentDataType.INTEGER);
        boolean show_time = container.get(NamespacedKey.minecraft("magnifier_show_time"), PersistentDataType.BOOLEAN);
        int add_chances = container.get(NamespacedKey.minecraft("magnifier_add_chances"), PersistentDataType.INTEGER);
        boolean show_thief = container.get(NamespacedKey.minecraft("magnifier_show_thief"), PersistentDataType.BOOLEAN);

        Player player = e.getPlayer();

        Block targetBlock = e.getClickedBlock(); // блок на который тыкнул
        if (player.isSneaking()) {
            targetBlock = targetBlock.getRelative(BlockFace.UP);
        }

        Block block = targetBlock; // выбираем финальный блок

        Bukkit.getScheduler().runTaskAsynchronously(SherlockCore.getInstance(), () -> {
            List<List<String>> history = new ArrayList<>();
            if (e.getAction() == Action.RIGHT_CLICK_BLOCK) history = getContainerHistory(block, nicks);
            else history = getBlockHistory(block, nicks);


            List<Integer> chanceList = calculatePercentagesList(nicks, add_chances);

            if (history.isEmpty()) {
                player.sendMessage("§aУ этого блока нет истории изменений.");
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
                answer.add(Component.text("§a------ ©" + player.getName() + " ------"));
                player.sendMessage("§6Результаты записаны на бумагу:");
                if (itemInOffHand.getAmount() >= 1) {
                    addOneItemInOffHand(itemInOffHand, player, answer);
                } else {
                    itemInOffHand.lore(answer);
                }
            } else {
                player.sendMessage("§6Вот что удалось разглядеть:");
                for (Component component : answer) {
                    player.sendMessage(component);
                }
            }
        });
    }
}
