package redd_rl.tabListHider;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class TabListHiderCommand implements CommandExecutor, TabCompleter {

    private final TabListHider plugin;

    public TabListHiderCommand(TabListHider plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.hasPermission("tablisthider.admin")) {
            sender.sendMessage("§cYou do not have permission.");
            return true;
        }

        if (args.length == 0) {
            sender.sendMessage("§cUsage: /tablisthider <add|remove|list> [player]");
            return true;
        }

        String action = args[0].toLowerCase();

        if (action.equals("list")) {
            if (TabListHider.HIDDEN_PLAYERS.isEmpty()) {
                sender.sendMessage("§eNo players are currently hidden.");
            } else {
                sender.sendMessage("§aHidden Players: §f" + String.join(", ", TabListHider.HIDDEN_PLAYERS));
            }
            return true;
        }

        if (action.equals("reload")) {
            plugin.loadHiddenPlayers();
            return true;
        }
        if (action.equals("save")) {
            plugin.saveHiddenPlayers();
            return true;
        }

        if (args.length < 2) {
            sender.sendMessage("§cSpecify a player name: /tablisthider " + action + " <player>");
            return true;
        }

        String targetName = args[1];

        if (action.equals("add")) {
            if (TabListHider.HIDDEN_PLAYERS.contains(targetName)) {
                sender.sendMessage("§e" + targetName + " is already hidden.");
            } else {
                TabListHider.HIDDEN_PLAYERS.add(targetName);
                plugin.saveHiddenPlayers();
                plugin.getListener().updatePlayerVisibility(targetName, true);
                sender.sendMessage("§aAdded " + targetName + " to the hidden list.");
            }
            return true;
        }

        if (action.equals("remove")) {
            if (TabListHider.HIDDEN_PLAYERS.remove(targetName)) {
                plugin.saveHiddenPlayers();
                plugin.getListener().updatePlayerVisibility(targetName, false);
                sender.sendMessage("§aRemoved " + targetName + " from the hidden list.");
            } else {
                sender.sendMessage("§c" + targetName + " was not in the hidden list.");
            }
            return true;
        }

        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> completions = new ArrayList<>();
        if (args.length == 1) {
            completions.add("add");
            completions.add("remove");
            completions.add("list");
            completions.add("save");
            completions.add("reload");
        } else if (args.length == 2) {
            if (args[0].equalsIgnoreCase("add")) {
                for (Player p : Bukkit.getOnlinePlayers()) {
                    completions.add(p.getName());
                }
            } else if (args[0].equalsIgnoreCase("remove")) {
                completions.addAll(TabListHider.HIDDEN_PLAYERS);
            }
        }
        return completions;
    }
}