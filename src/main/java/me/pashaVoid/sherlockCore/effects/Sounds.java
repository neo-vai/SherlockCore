package me.pashaVoid.sherlockCore.effects;

import org.bukkit.Sound;
import org.bukkit.entity.Player;

public class Sounds {

    public void breakItem(Player player) {
        player.playSound(
                player.getLocation(),
                Sound.ENTITY_ITEM_BREAK,
                1.0f,
                1.0f
        );
    }

    public void successfulMagnifierLeft(Player player) {
        player.playSound(
                player.getLocation(),
                Sound.BLOCK_AMETHYST_BLOCK_CHIME,
                1.0f,
                1.0f
        );
    }

    public void successfulMagnifierRight(Player player) {
        player.playSound(
                player.getLocation(),
                Sound.BLOCK_AMETHYST_BLOCK_CHIME,
                1.0f,
                0.9f
        );
    }

    public void upgradeMagnifier(Player player) {
        player.playSound(
                player.getLocation(),
                Sound.BLOCK_ANVIL_USE,
                1.0f,
                1.0f
        );
        player.playSound(player.getLocation(), Sound.ITEM_TRIDENT_THUNDER, 0.7f, 0.8f);
    }


}
