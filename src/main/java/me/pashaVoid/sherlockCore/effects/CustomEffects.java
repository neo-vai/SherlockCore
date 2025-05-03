package me.pashaVoid.sherlockCore.effects;

import me.pashaVoid.sherlockCore.config.MainConfig;
import org.bukkit.EntityEffect;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class CustomEffects {

    private static Particles particles = new Particles();
    private static Sounds sounds = new Sounds();

    public static void breakItem(ItemStack item, Player player) {
        if (!MainConfig.disable_sounds) {
            sounds.breakItem(player);
        }
        if (!MainConfig.disable_particles) {
            particles.breakItem(player, item);
        }
        player.playEffect(EntityEffect.BREAK_EQUIPMENT_MAIN_HAND);
    }

    public static void successfulMagnifierLeft(Player player, Location center) {
        if (!MainConfig.disable_sounds) {
            sounds.successfulMagnifierLeft(player);
        }
        if (!MainConfig.disable_particles) {
            particles.successfulMagnifier(center);
        }
    }

    public static void successfulMagnifierRight(Player player, Location center) {
        if (!MainConfig.disable_sounds) {
            sounds.successfulMagnifierRight(player);
        }
        if (!MainConfig.disable_particles) {
            particles.successfulMagnifier(center);
        }
    }

    public static void upgradeMagnifier(Player player) {
        if (!MainConfig.disable_sounds) {
            sounds.upgradeMagnifier(player);
        }
    }

}
