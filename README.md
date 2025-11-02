
# Mythica
[![CurseForge Downloads](https://img.shields.io/curseforge/dt/1354448?style=flat&logo=CurseForge&logoColor=F16436&label=CurseForge&color=F16436)](https://www.curseforge.com/minecraft/mc-mods/mythica-a-second-overworld-dimension)
[![ModRinth Downloads](https://img.shields.io/modrinth/dt/mythica?style=flat&logo=modrinth&logoColor=%2300AF5C&label=Modrinth&color=%2300AF5C)](https://modrinth.com/mod/mythica)
[![Minecraft Versions](https://img.shields.io/badge/MC-1.21.1-green?style=flat&logo=minecraft&logoColor=white)](https://www.minecraft.net/ja-jp/about-minecraft)
[![Server Side](https://img.shields.io/badge/Side-Server%26Client-orange?style=flat)](#)
[![License](https://img.shields.io/github/license/medi-torimorta/mythica?style=flat&color=purple)](https://github.com/medi-torimorta/mythica/?tab=LGPL-3.0-1-ov-file)


### Now Compatible with Terrablender Biomes!  
Biomes added by Terrablender are copied to Mythica by default, but can be set to be ignored, or removed from the overworld in favor of mythica.

## Overview
This mod adds a new dimension, "Mythica" to the game.  
The Mythica Dimension is a copy of the Overworld with a different seed(configurable), and is intended for use in ModPacks.  
This Mod adds 1 item, the Mythic powder.  
By default, right-clicking inside a portal frame made of reinforced deepslate, like those generated in Ancient Cities will spawn a portal to the Mythica Dimension.  
The Mythic powder has no built-in recipe, please use the command or add a recipe yourself.  

## Configuration
It is recommended to add a recipe or a loot table to obtain the Mythic powder using Datapacks.  
The tag `#mythica:portal_frame_blocks` defines the blocks usable for portal frames,  
and `#mythica:portal_replaceable` the blocks that can be replaced when a portal is generated inside the frame.  
Check the common config to set the blocks used when an exit portal is generated.  
Terrablender-added overworld biomes can be set to copied to Mythica, ignored, or added to mythica and removed from the overworld.  
Copied biomes can be blacklisted in a per-mod basis.  
The world seed used in mythica dimension can be configured in the server config file.  

## Integration
### Create:
The Mythica Portal works with Create trains, allowing the rails to connect through the portal and the train to pass through.  
### TerraBlender:
Allows for copying/transferring TerraBlender-added biomes from the Overworld to Mythica.  

## Bonus
The mod includes a datapack which generates mythica-namespaced biomes in place of vanilla ones.
Use the command `/datapack enable "mod/mythica:use_mythica_biomes"` and re-start the world to apply.  
Doing so will give you more control over what content are added to mythica/overworld separately.  
The datapack also adds the mythica biomes to some vanilla biome tags, but not to those enabling ruined portal and stronghold generation.  
(this was done considering nether/end return portals not taking you back to mythica, but rather the overworld. Feel free to add the tags yourself).  

Alternatively, you can cherry-pick some biomes to replace the overworld variants using your own datapack.  
see `data/mythica/worldgen/biome` for available biomes.

## Credits
Portal code adapted from [quek04/undergarden](https://github.com/quek04/undergarden/)  
Terrablender compat code adapted form [RazorDevs/Aeroblender](https://github.com/RazorDevs/Aeroblender/),  
Sound effects by [OtoLogic](https://otologic.jp/) and [soundeffect-lab](https://soundeffect-lab.info/)
