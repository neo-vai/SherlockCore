package me.pashaVoid.sherlockCore.magnifier;


import me.pashaVoid.sherlockCore.SherlockCore;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import java.util.List;

import static me.pashaVoid.sherlockCore.utils.CoreProtectUtils.*;
import static me.pashaVoid.sherlockCore.utils.SecrecyUtil.calculatePercentagesList;
import static me.pashaVoid.sherlockCore.utils.SecrecyUtil.encryptNickname;

public class ItemListener implements Listener {

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

        e.setCancelled(true);

        if (e.getAction() != Action.LEFT_CLICK_BLOCK && e.getAction() != Action.RIGHT_CLICK_BLOCK) {
            return;
        }
        if (e.getClickedBlock() == null) {
            return;
        }
        PersistentDataContainer container = e.getItem().getItemMeta().getPersistentDataContainer();
        int nicks = container.get(NamespacedKey.minecraft("magnifier_nicks"), PersistentDataType.INTEGER);
        int uses = container.get(NamespacedKey.minecraft("magnifier_uses"), PersistentDataType.INTEGER);
        int break_chances = container.get(NamespacedKey.minecraft("magnifier_break_chances"), PersistentDataType.INTEGER);
        int show_time = container.get(NamespacedKey.minecraft("magnifier_show_time"), PersistentDataType.INTEGER);
        int add_chances = container.get(NamespacedKey.minecraft("magnifier_add_chances"), PersistentDataType.INTEGER);
        int show_thief = container.get(NamespacedKey.minecraft("magnifier_show_thief"), PersistentDataType.INTEGER);

        Player player = e.getPlayer();

        Block targetBlock = e.getClickedBlock(); // блок на который тыкнул
        if (player.isSneaking()) {
            targetBlock = targetBlock.getRelative(BlockFace.UP);
        }

        Block block = targetBlock; // выбираем финальный блок

        Bukkit.getScheduler().runTaskAsynchronously(SherlockCore.getInstance(), () -> {
            List<List<String>> history;
            if (e.getAction() == Action.LEFT_CLICK_BLOCK) {
                history = getBlockHistory(block, nicks);
            } else {
                player.sendMessage("ПКМ еще не готов");
                history = getBlockHistory(block, nicks);
            }
            List<Integer> chanceList = calculatePercentagesList(history.size());

            if (history.isEmpty()) {
                player.sendMessage("§aУ этого блока нет истории изменений.");
                return;
            }

            player.sendMessage("§6Вот что удалось разглядеть:");
            int cnt = 0;
            int time_cnt = show_time;
            for (int i = 0; i < history.size(); i++) {
                List<String> entry = history.get(i);
                int chance = chanceList.get(i);
                String nick = encryptNickname(entry.get(0), chance, block.getX(), block.getY(), block.getZ());
                cnt += 1;
                String str = "§7" + cnt + "§f " + nick + " " + entry.get(2);
                if (time_cnt > 0) {
                    List<Long> date = convertStringDataToLong(entry.get(1));
                    String strDate = "";
                    if (date.size() > 1) strDate += date.getLast() + "д.";
                    strDate += date.getFirst() + "ч.";
                    str += " §8(" + strDate + ")";
                    time_cnt -= 1;
                }
                player.sendMessage(str);
            }
        });
    }

}
