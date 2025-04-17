package me.pashaVoid.sherlockCore;

import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import java.util.List;

import static me.pashaVoid.sherlockCore.utils.coreProtectUtils.getBlockHistory;

public class testCMD implements CommandExecutor {


    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("§cКоманда только для игроков!");
            return true;
        }

        Block targetBlock = player.getTargetBlockExact(5);

        if (targetBlock == null) {
            player.sendMessage("§cВы не смотрите на блок!");
            return true;
        }

        int limit = 10;

        // Запускаем асинхронно, чтобы не лагал серверу
        Bukkit.getScheduler().runTaskAsynchronously(SherlockCore.getInstance(), () -> {
            List<List<String>> history = getBlockHistory(targetBlock, limit);

            if (history.isEmpty()) {
                player.sendMessage("§aУ этого блока нет истории изменений.");
                return;
            }

            player.sendMessage("§6История блока §f" + targetBlock.getType().toString().toLowerCase() + ":");
            for (List<String> entry : history) {
                player.sendMessage("§7- §f" + entry.get(0)
                        + " §8(" + entry.get(1) + " ч. назад) §7→ "
                        + entry.get(2));
            }
        });

        return true;
    }
}
