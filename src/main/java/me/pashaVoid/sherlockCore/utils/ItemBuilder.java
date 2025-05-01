package me.pashaVoid.sherlockCore.utils;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.List;
import java.util.Map;

public class ItemBuilder {

    public static ItemBuilder open(ItemStack item) {
        ItemBuilder itemBuilder = new ItemBuilder(item.getType(), item.getAmount());

        itemBuilder.itemStack = item;
        itemBuilder.itemMeta = item.getItemMeta();

        return itemBuilder;
    }

    private ItemStack itemStack;
    private ItemMeta itemMeta;

    public ItemBuilder(Material material, int amount) {
        itemStack = new ItemStack(material, amount);
        itemMeta = itemStack.getItemMeta();
    }

    public ItemBuilder setName(String name) {
        itemMeta.displayName(Component.text(name));

        return this;
    }

    public ItemBuilder setLore(List<Component> lore) {
        itemMeta.lore(lore);

        return this;
    }

    public ItemBuilder addEnchantment(Enchantment enchantment, int level) {
        itemMeta.addEnchant(enchantment, level, true);
        return this;
    }

    public ItemBuilder addPersistent(String key, PersistentDataType dataType, Object value) {
        itemMeta.getPersistentDataContainer().set(NamespacedKey.fromString(key), dataType, value);

        return this;
    }

    public ItemStack build() {
        itemStack.setItemMeta(itemMeta);

        return itemStack;
    }
}
