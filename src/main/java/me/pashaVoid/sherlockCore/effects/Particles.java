package me.pashaVoid.sherlockCore.effects;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class Particles {

    public void breakItem(Player player, ItemStack item) {
        player.spawnParticle(
                Particle.ITEM, // Тип частиц
                player.getLocation().add(0, 1, 0), // Позиция
                10,
                0.3, 0.3, 0.3,
                0.1,
                item
        );
    }

    public void successfulMagnifier(Location center) {
        center.getWorld().spawnParticle(
                Particle.CRIMSON_SPORE,
                center,
                20,
                0.3,
                0.3,
                0.3,
                -0.5
        );
    }

}
