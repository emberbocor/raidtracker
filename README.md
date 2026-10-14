# Raid Tracker

Know where you stand in a raid.

Raid Tracker puts the raid's progress on its boss bar: which wave is running, how many waves there are and how many raiders are left. When only a few raiders of a wave remain, they start to glow, so the last pillager hiding behind a house is easy to find.

The mod runs on the server only. Players join with a plain vanilla client and still see everything.

Available for Forge (also runs on NeoForge 1.20.1) and Fabric.

## Features

- **Wave counter.** While a wave is running the boss bar reads `Raid - Wave 2/5 - 7 raiders left`. The total counts the bonus wave of Bad Omen II and higher, also when a second Bad Omen raises the level in the middle of a raid.
- **Between waves.** After a wave is cleared the bar reads `Raid - Wave 2/5 cleared` until the next wave arrives (after the last wave: until the victory text).
- **Live count.** The number of raiders left drops the moment a raider dies or leaves the raid, and rises when a raider joins.
- **Vanilla where it matters.** Before the first wave arrives, and once the raid is won or lost, the bar keeps the vanilla text ("Raid", "Raid - Victory", "Raid - Defeat").
- **Glowing stragglers.** When half of the current wave or less is left alive (rounded up: 3 of 5, 4 of 7), every remaining raider of that wave glows. Raiders that join during the wave, for example patrols recruited by the raid, count toward the wave. The size of the wave is saved with the raid, so the threshold stays the same after the world is saved and loaded again.
- **Every raid on its own.** Two raids in two villages each keep their own boss bar and count.
- **Configurable.** Both texts and the glowing threshold can be changed or turned off in the server config.

The number of waves is the vanilla one: 3 on Easy, 5 on Normal and 7 on Hard, plus one bonus wave with Bad Omen II or higher.

## How the glowing works

Raiders get the vanilla Glowing effect for a few seconds, without particles and without an effect icon, renewed as long as the condition holds. Raiders are never given the permanent glowing flag. So the glowing goes away on its own a few seconds after the raid is won or lost, after a raider leaves the raid and after the mod is removed.

Glowing from other sources, such as a bell or a spectral arrow, is left alone: the mod never removes or shortens another Glowing effect and only adds its own once the other one runs out.

## Requirements

| | Forge / NeoForge | Fabric |
|---|---|---|
| Minecraft | 1.20.1 | 1.20 or 1.20.1 |
| Loader | Forge 47.1.0 or newer, or NeoForge 1.20.1 (47.1.x) | Fabric Loader 0.14.21 or newer |
| Other | | Fabric API 0.83.0 or newer |
| Jar | `raidtracker-forge-1.20.1-<version>.jar` | `raidtracker-fabric-1.20-1.20.1-<version>.jar` |

On Minecraft 1.20.1, NeoForge still runs Forge mods, so the Forge jar is used for both.

The Fabric jar bundles [NightConfig](https://github.com/TheElectronWill/night-config) 3.6.4 (core and toml, LGPL-3.0) to read the TOML config. Forge and NeoForge already ship it.

### Client and server

Install the mod on the server only, on every loader. It adds no items, blocks, entities, effects, network messages or registries, so the server has nothing a client needs to know about:

- **Forge and NeoForge:** vanilla clients and Forge or NeoForge clients without the mod can join. The mod is marked as server-only, so a client without it still shows the server as compatible in the server list.
- **Fabric:** vanilla clients and Fabric clients without the mod can join. Fabric API is only needed on the server.

Players without the mod see the boss bar text and the glowing like everyone else. Installing it on a client as well does no harm.

In singleplayer, install it in your own game; the built-in server runs it.

The boss bar text comes from the server config, so every player sees it in the same language.

## Configuration

### Server: `config/raidtracker-server.toml`

The file is created on the first server (or world) start with comments that explain every option. It is read every time a server or world starts.

```toml
# Boss bar text while a wave is running.
bossBarFormat = "Raid - Wave {wave}/{waves} - {raiders} raiders left"

# Boss bar text after a wave is cleared, until the next wave arrives.
bossBarBetweenWavesFormat = "Raid - Wave {wave}/{waves} cleared"

# The raiders of the current wave glow once this percentage of the wave or less is left alive.
glowAtRemainingPercent = 50
```

- `bossBarFormat`: the text while a wave is running. Default `"Raid - Wave {wave}/{waves} - {raiders} raiders left"`.
- `bossBarBetweenWavesFormat`: the text after a wave is cleared. Default `"Raid - Wave {wave}/{waves} cleared"`.
- `glowAtRemainingPercent`: a whole number from 0 to 100. The raiders of the current wave glow once this percentage of the wave or less is left alive, rounded up. `0` turns glowing off, `100` makes the whole wave glow from the moment it arrives. Default `50`.

Placeholders for both texts:

| Placeholder | Meaning |
|---|---|
| `{wave}` | The running wave, or the wave that was just cleared |
| `{waves}` | The total number of waves, including the bonus wave |
| `{raiders}` | The raiders left alive |

An empty text (`""`) keeps the vanilla text in that situation. For example, this shows only the wave counter during waves and the vanilla text between them, with glowing turned off:

```toml
bossBarFormat = "Wave {wave} of {waves}"
bossBarBetweenWavesFormat = ""
glowAtRemainingPercent = 0
```

Mistakes do not crash the server. An invalid value (a number where text is expected, a percentage outside 0-100 or with decimals) is replaced by its default with a warning in the log; unknown options are reported and ignored. If the file cannot be read at all (for example a TOML syntax error), the defaults are used, the file is left untouched and the log says why. The file is never rewritten once it exists, so your edits and comments stay.

The config file is the same in every Minecraft version, so a file from the 1.21.1 version of the mod can be copied as it is.

## Removing the mod

The world opens normally. Raids in progress continue with the vanilla boss bar, and raiders that were glowing stop glowing within a few seconds. The wave size saved with each raid uses its own `raidtracker:` keys, which vanilla ignores and drops on the next save. The config file stays in `config/` until you delete it.

## Compatibility

- **Glowing Illagers** makes every raider glow all the time. With both installed, this mod's glowing makes no visible difference; the boss bar text still works.
- Other mods that rename the raid boss bar may overwrite this mod's text or have theirs overwritten.

## Building from source

```
./gradlew build
```

The jars are written to `forge/build/libs/` and `fabric/build/libs/`.

## License

MIT. The Fabric jar includes NightConfig, which is licensed under the LGPL-3.0.
