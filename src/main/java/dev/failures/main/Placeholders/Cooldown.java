package dev.failures.main.Placeholders;

import dev.failures.main.BossMessage;
import io.lumine.xikage.mythicmobs.MythicMobs;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Set;

public class Cooldown extends PlaceholderExpansion {
    @Override
    public boolean canRegister() {
        return true;
    }

    @NotNull
    @Override
    public String getIdentifier() {
        return "bossmessage";
    }

    @NotNull
    @Override
    public String getAuthor() {
        return "Failures";
    }

    @NotNull
    @Override
    public String getVersion() {
        return "1.0.0";
    }

    @Override
    public String onRequest(OfflinePlayer player, @NotNull String params) {
        List<String> bosses = BossMessage.getInstance().getConfig().getStringList("boss-spawners");
        for(String spawner: bosses) {
            if(params.equalsIgnoreCase(spawner)) {
                int seconds = MythicMobs.inst().getSpawnerManager().getSpawnerByName(spawner).getRemainingWarmupSeconds();
                return String.valueOf(formatSeconds(seconds));
            }
        }

        return null;
    }

    public static String formatSeconds(int timeInSeconds)
    {
        int hours = timeInSeconds / 3600;
        int secondsLeft = timeInSeconds - hours * 3600;
        int minutes = secondsLeft / 60;
        int seconds = secondsLeft - minutes * 60;

        String formattedTime = "";
        if (hours < 10)
            formattedTime += "0";
        formattedTime += hours + ":";

        if (minutes < 10)
            formattedTime += "0";
        formattedTime += minutes + ":";

        if (seconds < 10)
            formattedTime += "0";
        formattedTime += seconds ;

        return formattedTime;
    }
}
