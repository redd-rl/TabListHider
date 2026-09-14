package redd_rl.tabListHider;

import com.moandjiezana.toml.Toml;
import com.moandjiezana.toml.TomlWriter;
import org.bstats.bukkit.Metrics;
import org.bstats.charts.SingleLineChart;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.Map;

public final class TabListHider extends JavaPlugin {

    // TabListHider.HIDDEN_PLAYERS
    public static List<String> HIDDEN_PLAYERS = new ArrayList<>();
    private PlayerJoinListener listener;

    @Override
    public void onEnable() {
        // METRICS!!
        int pluginId = 34068;
        Metrics metrics = new Metrics(this, pluginId);

        // load the list
        loadHiddenPlayers();

        metrics.addCustomChart(new SingleLineChart("total_hidden_players", () -> {
            return (int) HIDDEN_PLAYERS.stream()
                    .filter(name -> !name.equalsIgnoreCase("Herobrine")) // default values we can go without!
                    .filter(name -> !name.equalsIgnoreCase("Notch"))
                    .count();
        }));
        this.listener = new PlayerJoinListener(this);

        // hook join events
        getServer().getPluginManager().registerEvents(this.listener, this);

        // create the commands necessary.
        TabListHiderCommand cmd = new TabListHiderCommand(this);
        getCommand("tablisthider").setExecutor(cmd);
        getCommand("tablisthider").setTabCompleter(cmd);
    }

    public PlayerJoinListener getListener() {
        return listener;
    }

    public void saveHiddenPlayers() {
        if (!getDataFolder().exists()) {
            getDataFolder().mkdirs();
        }

        File configFile = new File(getDataFolder(), "config.toml");

        // Construct the TOML data structure
        Map<String, Object> data = new HashMap<>();
        data.put("hidden_players", HIDDEN_PLAYERS != null ? HIDDEN_PLAYERS : new ArrayList<>());

        TomlWriter writer = new TomlWriter();
        try {
            writer.write(data, configFile);
            getLogger().info("Successfully saved " + (HIDDEN_PLAYERS != null ? HIDDEN_PLAYERS.size() : 0) + " hidden players to config.toml.");
        } catch (IOException e) {
            getLogger().severe("Failed to write hidden_players to config.toml!");
            e.printStackTrace();
        }
    }

    public void loadHiddenPlayers() {
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
    public void onDisable() {
        saveHiddenPlayers();
    }
}
