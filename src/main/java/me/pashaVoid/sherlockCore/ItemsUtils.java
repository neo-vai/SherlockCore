package me.pashaVoid.sherlockCore;

import io.papermc.paper.datacomponent.DataComponentTypes;
import io.papermc.paper.datacomponent.item.CustomModelData;
import me.pashaVoid.sherlockCore.utils.ItemBuilder;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;

import java.util.Arrays;

public class ItemsUtils {

    public static ItemStack createMagnifier(String name, int nicks, int durability, boolean show_time, int add_chances, boolean show_thief) {
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
        return item;
    }

}