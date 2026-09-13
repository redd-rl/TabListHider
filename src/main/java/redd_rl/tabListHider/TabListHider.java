package redd_rl.tabListHider;

import com.moandjiezana.toml.Toml;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public final class TabListHider extends JavaPlugin {

    // TabListHider.HIDDEN_PLAYERS
    public static List<String> HIDDEN_PLAYERS = new ArrayList<>();

    @Override
    public void onEnable() {
        loadHiddenPlayers();

        getServer().getPluginManager().registerEvents(new PlayerJoinListener(this), this);
    }

    private void loadHiddenPlayers() {
        if (!getDataFolder().exists()) {
            getDataFolder().mkdirs();
        }

        File configFile = new File(getDataFolder(), "config.toml");

        if (!configFile.exists()) {
            if (getResource("config.toml") != null) {
                saveResource("config.toml", false);
            } else {
                try {
                    configFile.createNewFile();
                } catch (IOException e) {
                    getLogger().severe("Failed to create config.toml!");
                    e.printStackTrace();
                }
            }
        }

        Toml toml = new Toml().read(configFile);
        List<String> players = toml.getList("hidden_players");

        if (players != null) {
            HIDDEN_PLAYERS = new ArrayList<>(players);
            getLogger().info("Loaded " + HIDDEN_PLAYERS.size() + " hidden players from TOML config.");
        } else {
            HIDDEN_PLAYERS = new ArrayList<>();
            getLogger().warning("Key 'hidden_players' not found in config.toml.");
        }
    }

    @Override
    public void onDisable() {}
}
