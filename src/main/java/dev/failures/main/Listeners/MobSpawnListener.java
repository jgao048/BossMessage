package dev.failures.main.Listeners;

import dev.failures.main.BossMessage;
import dev.failures.main.Util.ColorUtil;
import io.lumine.xikage.mythicmobs.adapters.AbstractLocation;
import io.lumine.xikage.mythicmobs.api.bukkit.events.MythicMobSpawnEvent;
import io.lumine.xikage.mythicmobs.mobs.ActiveMob;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class MobSpawnListener implements Listener {
    private BossMessage main;

    public MobSpawnListener(BossMessage main) {
        this.main = main;
    }

    @EventHandler
    private void mythicSpawn(MythicMobSpawnEvent e) {
        List<String> bossNames = ColorUtil.colorize(main.getConfig().getStringList("boss-names"));
        if(!bossNames.contains(e.getMob().getDisplayName())) return;

        ActiveMob mMob = e.getMob();
        AbstractLocation mobLoc = mMob.getLocation();
        DecimalFormat df = new DecimalFormat("#,###");

        String x = String.valueOf(df.format(mobLoc.getX()));
        String y = String.valueOf(df.format(mobLoc.getY()));
        String z = String.valueOf(df.format(mobLoc.getZ()));
        String world = mobLoc.getWorld().toString();

        for(Player p : Bukkit.getOnlinePlayers()) {
            List<String> bossMessage = colorize(main.getConfig().getStringList("spawn-message"),x,y,z,world,mMob.getDisplayName());
            for(String msg : bossMessage) {
                p.sendMessage(msg);
            }
            p.playSound(p.getLocation(), Sound.valueOf(main.getConfig().getString("spawn-sound").toUpperCase()),5.0F,1.0F);
        }
    }

    public List<String> colorize(List<String> lore, String x, String y, String z, String world, String mobName) {
        List<String> newLore = new ArrayList<>();
        for(String l : lore) {
            newLore.add(ColorUtil.colorize(l
                    .replace("%x%",x).replace("%y%",y).replace("%z%",z)
                    .replace("%world%",world)
                    .replace("%name%",mobName)
            ));
        }
        return newLore;
    }
}
