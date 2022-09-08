package dev.failures.main.Listeners;

import dev.failures.main.BossMessage;
import dev.failures.main.Util.ColorUtil;
import dev.failures.main.Util.MapUtil;
import dev.failures.main.Util.PDUtil;
import io.lumine.xikage.mythicmobs.api.bukkit.events.MythicMobDeathEvent;
import io.lumine.xikage.mythicmobs.mobs.ActiveMob;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.lang.reflect.Array;
import java.text.DecimalFormat;
import java.util.*;

public class MobDeathListener implements Listener {
    private BossMessage main;
    List<Player> name = new ArrayList<>();
    List<Double> damage = new ArrayList<>();

    public MobDeathListener(BossMessage main) {
        this.main = main;
    }

    @EventHandler
    private void mythicDeath(MythicMobDeathEvent e) {
        List<String> bossNames = ColorUtil.colorize(main.getConfig().getStringList("boss-names"));
        if(!bossNames.contains(e.getMob().getDisplayName())) return;

        HashMap<Player, Double> playerDamages = new HashMap<>();
        ActiveMob mMob = e.getMob();
        PDUtil mobID = new PDUtil(new NamespacedKey(main, mMob.getUniqueId().toString()));

        for(Player p : Bukkit.getOnlinePlayers()) {
            if(!p.isOnline()) continue;
            if(!mobID.playerDataContainsDoubleKey(p)) continue;
            playerDamages.put(p,mobID.getPlayerDataDouble(p));
        }

        HashMap<Player, Double> sortedDamages = MapUtil.sortByValue(playerDamages);
        name = new ArrayList<>();
        damage = new ArrayList<>();

        for(Map.Entry<Player, Double> sorted : sortedDamages.entrySet()) {
            name.add(sorted.getKey());
            damage.add(sorted.getValue());
        }

        for(Player p : Bukkit.getOnlinePlayers()) {

            if(!mobID.playerDataContainsDoubleKey(p)) continue;
            List<String> deathMessage = main.getConfig().getStringList("death-message");
            List<String> msg = getPlacementList(deathMessage,mMob.getDisplayName());
            for(String m : msg) {
                p.sendMessage(m);
                mobID.removeKeyPlayer(p);
            }
        }

        Set<String> bosses = main.getConfig().getConfigurationSection("rewards").getKeys(false);
        for(String boss:bosses) {
            if(!bossNames.contains(ColorUtil.colorize(boss))) continue;
            Set<String> rewards = main.getConfig().getConfigurationSection("rewards." + boss).getKeys(false);
            for(String reward : rewards) {
                List<Integer> places = main.getConfig().getIntegerList("rewards." + boss + "." + reward + ".placement");
                int chance = main.getConfig().getInt("rewards." +boss+ "." + reward + ".chance");
                if(!getChance(chance)) continue;
                for(int place : places) {
                    if(place-1 >= name.size()) break;
                    List<String> message = ColorUtil.colorize(main.getConfig().getStringList("rewards." + boss+ "." + reward + ".messages"));
                    for(String msg : message) {
                        name.get(place-1).sendMessage(msg);
                    }
                    name.get(place-1).getName();
                    List<String> commands = main.getConfig().getStringList("rewards." +boss+ "." + reward + ".commands");
                    for(String command: commands) {
                        Bukkit.dispatchCommand(Bukkit.getConsoleSender(),command.replace("%player%",name.get(place-1).getName()));
                    }
                }
            }
        }
    }

    public List<String> getPlacementList(List<String> message, String mobName) {
        List<String> newMessage = new ArrayList<>();
        for(String m : message) {
            if(m.equals("%ranking%")) {
                String format = main.getConfig().getString("ranking-format");
                for(int i = 0 ; i < name.size() ; i++) {
                    if(i == 3) break;
                    DecimalFormat df = new DecimalFormat("#,###");

                    newMessage.add(ColorUtil.colorize(format
                            .replace("%placement%",String.valueOf(i+1))
                            .replace("%name%",name.get(i).getName())
                            .replace("%damage%", df.format(damage.get(i)))
                    ));
                }
            } else {
                newMessage.add(ColorUtil.colorize(m
                        .replace("%name%",mobName)
                ));
            }
        }
        return newMessage;
    }



    private Boolean getChance(int chance) {
        Random rnd = new Random();
        int num = rnd.nextInt(100) +1;
        if(num <= chance) {
            return true;
        }
        return false;
    }
}
