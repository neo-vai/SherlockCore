package me.pashaVoid.sherlockCore.utils;

import io.papermc.paper.datacomponent.DataComponentTypes;
import io.papermc.paper.datacomponent.item.CustomModelData;
import me.pashaVoid.sherlockCore.config.MainConfig;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

public class MagnifierItemUtils {

    public static ItemStack createMagnifier(String name, int nicks, int durability, boolean show_time, int add_chances, boolean show_thief) {
        ItemStack item = new ItemBuilder(Material.BRUSH, 1)
                .setName(name)
                .setLore(MainConfig.lore_default_magnifier)
                .addPersistent("magnifier", PersistentDataType.BOOLEAN, true)
                .addPersistent("magnifier_nicks", PersistentDataType.INTEGER, nicks)
                .addPersistent("magnifier_durability", PersistentDataType.INTEGER, durability)
                .addPersistent("magnifier_show_time", PersistentDataType.BOOLEAN, show_time)
                .addPersistent("magnifier_add_chances", PersistentDataType.INTEGER, add_chances)
                .addPersistent("magnifier_show_thief", PersistentDataType.BOOLEAN, show_thief)
                .build();

        if (MainConfig.enable_custom_model_data) {
        item.setData(DataComponentTypes.CUSTOM_MODEL_DATA, CustomModelData.customModelData()
                .addString((show_time) ? MainConfig.custom_model_data.get(1) : MainConfig.custom_model_data.get(0)).build());
        }

        item.setData(DataComponentTypes.MAX_DAMAGE, durability);
        return item;
    }

    public static ItemStack upgradeTimeMagnifier(@NotNull ItemStack magnifier) {
        if (!MainConfig.enable_magnifier_upgrade) return magnifier;
        ItemStack upgradeMagnifier = magnifier.clone();
        ItemMeta upgradeMeta = upgradeMagnifier.getItemMeta();
        upgradeMeta.getPersistentDataContainer().set(NamespacedKey.fromString("magnifier_show_time"), PersistentDataType.BOOLEAN, true);
        upgradeMeta.setDisplayName(MainConfig.prefix_time_magnifier + upgradeMeta.getDisplayName());
        upgradeMeta.setLore(MainConfig.lore_time_magnifier);
        upgradeMagnifier.setItemMeta(upgradeMeta);

        if (MainConfig.enable_custom_model_data) {
            upgradeMagnifier.setData(DataComponentTypes.CUSTOM_MODEL_DATA, CustomModelData.customModelData().addString(MainConfig.custom_model_data.get(1)).build());
        }

        return upgradeMagnifier;
    }

    public static boolean checkUpgrade(ItemStack magnifier) {
        ItemMeta meta = magnifier.getItemMeta();
        return meta.getPersistentDataContainer().get(NamespacedKey.fromString("magnifier_show_time"), PersistentDataType.BOOLEAN);
    }

    public static boolean checkMagnifier(ItemStack magnifier) {
        if (magnifier == null) return false;

        if (magnifier.getType() != Material.BRUSH) return false;

        ItemMeta meta = magnifier.getItemMeta();
        if (meta == null) return false;

        return meta.getPersistentDataContainer().has(NamespacedKey.fromString("magnifier"));
    }

}