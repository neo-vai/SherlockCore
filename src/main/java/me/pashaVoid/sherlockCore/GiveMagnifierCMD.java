package me.pashaVoid.sherlockCore;

import me.pashaVoid.sherlockCore.utils.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.List;

public class GiveMagnifierCMD implements TabExecutor {

    // /giveMagnifier pattern <pattern_name> <player(optional)>
    // /giveMagnifier custom <nicks> <uses> <break_chances> <show_time> <add_chances> <show_thief> <player(optional)>

    // magnifier: True
    // magnifier_nicks: int
    // magnifier_uses: int
    // magnifier_break_chances: int
    // magnifier_show_time: int
    // magnifier_add_chances: int
    // magnifier_show_thief: int
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        if (args.length < 2) {
            sender.sendMessage("Больше аргументов плиз");
            return true;
        }
        boolean pattern;
        if (args[0].equals("pattern")) {
            pattern = true;
        } else if (args[0].equals("custom")) {
            pattern = false;
        } else {
            sender.sendMessage("Выберите из 2 вариантов");
            return true;
        }
        Player getterPlayer = null;
        if (pattern && args.length >= 3) {
            getterPlayer = Bukkit.getPlayer(args[2]);
        } else if (!pattern && args.length >= 8){
            getterPlayer = Bukkit.getPlayer(args[6]);
        } else if (sender instanceof Player) {
            getterPlayer = (Player) sender;
        }
        if (getterPlayer == null) {
            sender.sendMessage("Некому получить предмет :(");
            return true;
        }
        if (pattern) {
            String patternName = args[0].toLowerCase();
            sender.sendMessage("Еще не готово :(");
            return true;

        } else {
            int nicks = Integer.parseInt(args[1]);
            int uses = Integer.parseInt(args[2]);
            int break_chances = Integer.parseInt(args[3]);
            int show_time = Integer.parseInt(args[4]);
            int add_chances = Integer.parseInt(args[5]);
            int show_thief = Integer.parseInt(args[6]);

            ItemStack item = new ItemBuilder(Material.SHEARS, 1)
                    .setName("Лупа Шерлока")
                    .setLore(Arrays.asList("ЛКМ - увидеть историю блока", "SHIFT + ЛКМ - увидеть историю блока выше", "ЛКМ - увидеть историю содержимого блока", "ЛКМ - увидеть историю содержимого блока выше"))
                    .addPersistent("magnifier", PersistentDataType.BOOLEAN, true)
                    .addPersistent("magnifier_nicks", PersistentDataType.INTEGER, nicks)
                    .addPersistent("magnifier_uses", PersistentDataType.INTEGER, uses)
                    .addPersistent("magnifier_break_chances", PersistentDataType.INTEGER, break_chances)
                    .addPersistent("magnifier_show_time", PersistentDataType.INTEGER, show_time)
                    .addPersistent("magnifier_add_chances", PersistentDataType.INTEGER, add_chances)
                    .addPersistent("magnifier_show_thief", PersistentDataType.INTEGER, show_thief)
                    .build();

            getterPlayer.getInventory().addItem(item);
            sender.sendMessage("Успешно!");
        }
        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        return List.of();
    }
}
