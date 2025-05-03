package me.pashaVoid.sherlockCore.utils;

import org.bukkit.ChatColor;

import java.util.ArrayList;
import java.util.List;

public class ColorUtils {

    public static String colorizeDefault(String input) {
        return ChatColor.translateAlternateColorCodes('&', input);
    }

    public static List<String> colorizeDefaultLore(List<String> input) {
        List<String> output = new ArrayList<String>();
        for (String s : input) {
            output.add(colorizeDefault(s));
        }
        return output;
    }

}
