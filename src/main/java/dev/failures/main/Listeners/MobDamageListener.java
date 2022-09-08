package dev.failures.main.Listeners;

import dev.failures.main.BossMessage;
import dev.failures.main.Util.PDUtil;
import io.lumine.xikage.mythicmobs.MythicMobs;
import io.lumine.xikage.mythicmobs.mobs.ActiveMob;
import io.lumine.xikage.mythicmobs.mobs.MythicMob;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class MobDamageListener implements Listener {
    private BossMessage main;

    public MobDamageListener(BossMessage main) {
        this.main = main;
    }

    @EventHandler
    private void mythicMobDamage(EntityDamageByEntityEvent e) {
        if(e.getEntity().getCustomName() == null) return;
        ActiveMob mMob = MythicMobs.inst().getMobManager().getMythicMobInstance(e.getEntity());
        PDUtil mobID = new PDUtil(new NamespacedKey(main, mMob.getUniqueId().toString()));

        if(!(e.getDamager() instanceof  Player)) return;
        Player p = (Player) e.getDamager();

        if(mobID.playerDataContainsDoubleKey(p)) {
            mobID.addPlayerDataDouble(p,e.getDamage());
            return;
        }
        mobID.setPlayerDataDouble(p,e.getDamage());
    }
}
