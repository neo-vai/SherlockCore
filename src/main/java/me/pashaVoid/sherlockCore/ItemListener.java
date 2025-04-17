package me.pashaVoid.sherlockCore;


import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import java.util.List;

import static me.pashaVoid.sherlockCore.utils.CoreProtectUtils.getBlockHistory;

public class ItemListener implements Listener {

    @EventHandler
    public void onItemClick(PlayerInteractEvent e){
        ItemStack item = e.getItem();
        if (item == null) {
            return;
        }
        else if (!(e.getItem().getType() == Material.SHEARS)) {
            return;
        }
        else if (e.getAction() != Action.LEFT_CLICK_BLOCK) {
            return;
        }
        else if (e.getClickedBlock() == null) {
            return;
        }
        e.setCancelled(true);

        Player player = e.getPlayer();

        Block targetBlock = e.getClickedBlock(); // блок на который тыкнул
        // Если на шифте, то берется блок сверху
        if (player.isSneaking()) {
            targetBlock = targetBlock.getRelative(BlockFace.UP);
        }

        Block block = targetBlock; // выбираем финальный блок

        Bukkit.getScheduler().runTaskAsynchronously(SherlockCore.getInstance(), () -> {
            List<List<String>> history = getBlockHistory(block, 10);

            if (history.isEmpty()) {
                player.sendMessage("§aУ этого блока нет истории изменений.");
                return;
            }

            player.sendMessage("§6История блока §f" + block.getType().toString().toLowerCase() + ":");
            int cnt = 0;
            for (List<String> entry : history) {
                cnt += 1;
                player.sendMessage("§7" + cnt + "§f " + entry.get(0) + " §7→ " + entry.get(2)+ " §8(" + entry.get(1) + ")");
            }
        });
    }

}
