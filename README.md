# TabListHider
A neat little mod that facilitates hiding players easily on a scoreboard using a TOML `config` file or commands as of TabListHider 1.1.

## Where is the `config.toml`?!
The `config.toml` is adjacent to the plugin, in a folder named `TabListHider`. So ./`TabListHider`/config.toml

## What format is the config in?
A TOML config file with a list of usernames to hide.
```toml
hidden_players = [
  "hidden_player_username1",
  "hidden_player_username2",
  "hidden_player_username3"
]
```
## I'M NOT A NERD! HELP!

As of `TabListHider` version 1.1, you can use a command to add or remove players to hide from the scoreboard:
- `/tablisthider add username` - adds a player to hide
- `/tablisthider remove username` - removes a player to hide
- `/tablisthider list` - lists all hidden players
- `/tablisthider reload` - reloads from the `config.toml`
- `/tablisthider save` - saves your players to the `config.toml`
