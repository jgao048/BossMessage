package dev.failures.main.Util;

import org.bukkit.ChatColor;

import java.util.ArrayList;
import java.util.List;

public class ColorUtil {
    public static String colorize(String msg) {
        return ChatColor.translateAlternateColorCodes('&', msg);
    }

    public static List<String> colorize(List<String> lore) {
        List<String> newLore = new ArrayList<>();
        for(String l : lore) {
            newLore.add(colorize(l));
        }
        return newLore;
    }
}
