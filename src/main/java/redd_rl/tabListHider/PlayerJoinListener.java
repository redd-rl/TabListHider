package redd_rl.tabListHider;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;

public class PlayerJoinListener implements Listener {

    private final TabListHider plugin;

    public PlayerJoinListener(TabListHider plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player joining = event.getPlayer();
        boolean joiningIsHidden = TabListHider.HIDDEN_PLAYERS.contains(joining.getName());

        if (joiningIsHidden) {
            event.joinMessage(null);
        }

        Bukkit.getScheduler().runTaskLater(plugin, () -> {
            for (Player online : Bukkit.getOnlinePlayers()) {
                if (TabListHider.HIDDEN_PLAYERS.contains(online.getName())) {
                    joining.unlistPlayer(online);
                }
                if (joiningIsHidden && !online.equals(joining)) {
                    online.unlistPlayer(joining);
                }
            }
        }, 1L);
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        if (TabListHider.HIDDEN_PLAYERS.contains(event.getPlayer().getName())) {
            event.quitMessage(null);
        }
    }

    @EventHandler
    public void onPlayerRespawn(PlayerRespawnEvent event) {
        Player player = event.getPlayer();
        if (TabListHider.HIDDEN_PLAYERS.contains(player.getName())) {
            Bukkit.getScheduler().runTaskLater(plugin, () -> {
                for (Player viewer : Bukkit.getOnlinePlayers()) {
                    viewer.unlistPlayer(player);
                }
            }, 1L);
        }
    }
}