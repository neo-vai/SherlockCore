package me.pashaVoid.sherlockCore.Commands;

import io.papermc.paper.datacomponent.DataComponentTypes;
import io.papermc.paper.datacomponent.item.CustomModelData;
import me.pashaVoid.sherlockCore.Magnifier;
import me.pashaVoid.sherlockCore.utils.ItemBuilder;
import net.kyori.adventure.text.Component;
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

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        if (args.length < 3) {
            sender.sendMessage("§cNot enough arguments!");
            return true;
        }


        boolean pattern;
        if (args[0].equals("pattern")) {
            pattern = true;
        } else if (args[0].equals("custom")) {
            pattern = false;
        } else {
            sender.sendMessage("§cChoose \"custom\" or \"pattern\"");
            return true;
        }

        Player getterPlayer = (sender instanceof Player) ? (Player) sender : null;

        if (pattern && args.length >= 4) {
            getterPlayer = Bukkit.getPlayer(args[3]);
            if ((!args[2].equalsIgnoreCase("on") && !args[2].equalsIgnoreCase("off")) && getterPlayer == null) {
                sender.sendMessage("§cError in the command with the Pattern");
                return true;
            }
        } else if (!pattern && args.length >= 8) {
            getterPlayer = Bukkit.getPlayer(args[6]);
        }
        if (getterPlayer == null) {
            sender.sendMessage("§cThere is no one to give the item to :(");
            return true;
        }

        int nicks;
        int durability;
        boolean show_time;
        int add_chances;
        boolean show_thief;
        String name = "Magnifier";

        if (pattern) {

            String patternName = args[1].toUpperCase();
            if (!Magnifier.keys.contains(patternName)) {
                sender.sendMessage("§cThe pattern was not found! :(");
                return true;
            }
            Magnifier magnifier = Magnifier.patterns.get(patternName);
            nicks = magnifier.getNicks();
            durability = magnifier.getDurability();
            show_time = args[2].equalsIgnoreCase("on");
            add_chances = magnifier.getAdd_chances();
            show_thief = magnifier.isShow_thief();
            name = magnifier.getName();

        } else {
            try {
                nicks = Integer.parseInt(args[1]);
                durability = Integer.parseInt(args[2]);
                show_time = args[3].equalsIgnoreCase("on");
                add_chances = Integer.parseInt(args[4]);
                show_thief = args[5].equalsIgnoreCase("on");
                name = "Лупа Шерлока";

                if (nicks < 1 || durability < 1) {
                    sender.sendMessage("§cIncorrect argument!");
                    return true;
                }

            } catch (Exception e) {
                sender.sendMessage("§cError in arguments!");
                return true;
            }
        }

        ItemStack item = new ItemBuilder(Material.BRUSH, 1)
                .setName(name)
                .setLore(Arrays.asList(
                        Component.text("§aЛКМ - увидеть историю блока"),
                        Component.text("§aПКМ - увидеть историю содержимого блока"),
                        Component.text("§aИспользуй SHIFT чтобы увидеть историю блока выше")))
                .addPersistent("magnifier", PersistentDataType.BOOLEAN, true)
                .addPersistent("magnifier_nicks", PersistentDataType.INTEGER, nicks)
                .addPersistent("magnifier_durability", PersistentDataType.INTEGER, durability)
                .addPersistent("magnifier_show_time", PersistentDataType.BOOLEAN, show_time)
                .addPersistent("magnifier_add_chances", PersistentDataType.INTEGER, add_chances)
                .addPersistent("magnifier_show_thief", PersistentDataType.BOOLEAN, show_thief)
                .build();

        item.setData(DataComponentTypes.CUSTOM_MODEL_DATA, CustomModelData.customModelData()
                    .addString((show_time) ? "magnifier_time" : "magnifier").build());

        item.setData(DataComponentTypes.MAX_DAMAGE, durability);

        getterPlayer.getInventory().addItem(item);
        sender.sendMessage("Успешно!");

        return true;
    }

    // /giveMagnifier pattern <pattern_name> <show_time>(enable or disable) <player(optional)>
    // /giveMagnifier custom  <nicks>        <durability>                   <show_time>(enable or disable) <add_chances> <show_thief>(enable or disable) <player(optional)>

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        if (args.length == 1) {
            return List.of("pattern", "custom");
        }
        int args_length = args.length;
        boolean is_pattern = args[0].equalsIgnoreCase("pattern");

        if (args[0].equalsIgnoreCase("pattern")) {
            if (args_length == 2) {}
        } else  if (args[0].equalsIgnoreCase("custom")) {
            if (args_length == 2) {}
        }

        if (args_length == 2) {
            return (is_pattern) ? Magnifier.keys : List.of("<nicks_count>");
        } else if (args_length == 3) {
            return (is_pattern) ? List.of("<show_time>", "on", "off") : List.of("<durability_count>");
        } else if (args_length == 4) {
            return (is_pattern) ? null : List.of("<show_time>", "on", "off");
        } else if (args_length == 5) {
            return (is_pattern) ? List.of() : List.of("<add_chances>");
        } else if (args_length == 6) {
            return (is_pattern) ? List.of() : List.of("<show_thief>", "on", "off");
        } else if (args_length == 7) {
            return (is_pattern) ? List.of() : null;
        }
        return List.of();
    }
}
