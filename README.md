# Arcanum Theoria
[Arcanum Theoria](https://github.com/SoSly/ArcanumTheoria) is a mod for [Minecraft](https://www.minecraft.net/) written 
using the [Forge modloader](https://files.minecraftforge.net/net/minecraftforge/forge/).

This mod explores the lore of the base Minecraft game, allowing the player to delve into the lost arcane arts discovered 
by the same ancient race that built Ancient Cities, Strongholds, and Nether Fortresses.  In Arcanum Theoria, you will 
find ancient texts written in a forgotten language that explain the powers of those ancient beings, how they obtained 
them, what they did with them, and the consequences of their actions.

## Links
- [Arcanum Theoria](https://www.curseforge.com/minecraft/mc-mods/arcanum-theoria) on CurseForge.

## Dependencies
- [patchouli](https://www.curseforge.com/minecraft/mc-mods/patchouli)

## Planned Features
Generally, this Mod will be based on the overarching theory of everything created by MatPat's Game Theory.  Although his
theories on Minecraft's builders are the origin of this mod's lore, I reserve the right to change it wherever I see fit 
in order to make better gameplay and a more coherent mod. 

### Lore
- The guidebook(s) should be pieced together over time by discovering "scraps" of lore.
- Scraps of lore should be found in various places, including bookshelves, treasure chests, etc.
  - It should be impossible to get more than one scrap from a single bookshelf, but somehow we need to account for bookshelf spam (destroying and replacing the same shelf)
  - Player-made bookshelves should probably never contain scraps of lore
  - A scrap of lore may contain a new spell part, a new piece of story, new enchantments, new alchemy recipes, and so forth.  Knowledge of how to craft anything relevant to the mod.
  - `The Shadow#6392` suggested that Scraps be drops when breaking "Lore Shelves", which look exactly like bookshelves but are not craftable and have a chance to replace bookshelves in some structures. This would solve the problems above. 
- Each "guide book" that we can collect lore from should be written from the perspective of one of the Ancient Builders.
  - Alternatively, they could be written as the player's commentary on information they've found. E.g.: "I have discovered scraps of knowledge which suggest that the Ancient Builders ...."
  - This alternative may present a more natural means of explaining the reason why "scraps" have to be collected
  - It may also be good to keep it as the modern-player's thoughts, since then the lore the player comes up with is not necessarily canon, it's just the player's _ideas_ about the information they've recovered, and thus subject to being wrong.
- Possibly, the first Guide Book you get contains clues for translation, but it needs to be combined with a terminal that is used to decipher scraps
- Experience may be required to decipher scraps
- The deeper/further you go, the more valuable the scraps you obtain
- Players might be able to create their own "lore" by conducting research on items in the minecraft world
  - This would by necessity use up the item.
  - Each item consumed might contribute a percentage towards available research related to that item, or knowledge that there's nothing else to learn
  - If the scraps are written from the perspective of the Builders, then research done in this way should create a "Scrap" from the player's perspective

### Worldgen
#### Mobs
- New mobs should focus on things which enrich the story being told in the Lore (above).

#### Ley Lines
- Ley Lines exist as sources of power
- By default, Strongholds, Nether Fortresses, and Ancient Cities _probably_ all contain a ley line.
- Other structures (desert temples, jungle temples, monuments?)  may also contain them.
- What do ley lines do?
- Can you construct a leyline anywhere or do you have to find it?

### Rituals
- Rituals involve etching runes into solid objects and then activating them.
- The most basic rituals probably involve etching a single rune into an object
- The basis for this is the Creeper Faces etched into desert temples; clear evidence that the builders learned etching!
- Rituals may result in effects changing in the world (such as opening a portal), the creation of new creatures (such as iron golems), or in the creation of a new item
- Activation may be automatic, or may require use of an item on the etched runes
  - Rituals may also require you to place items on the etchings.  This may get tricky. Think M&A style.
  - I would like to avoid these etchings and item placings being too different from the default interaction of minecraft
- There are several existing rituals in the game:
  - Constructing a Nether or End portal
  - Constructing an Iron Golem or Snowman
  - Beacons and Conduits

#### Spellcasting
- This section is extremely undefined right now and needs a lot of work
- Crafting a Spell:
  - Spellcasting is a subset of rituals because you will likely have to perform a ritual to create a spell scroll/add a spell to your spellbook.
  - Ideally, the player will find scraps of lore that point them to various spell effects ("fire damage", "touch spell",  "weakness effect", etc)
  - The player will then construct their spell in some kind of terminal, and then craft the spell possibly using a ritual
  - Once it's crafted, the player can use the spell as much as they like
- Spells should be very flexible; you should be able to construct a spell that works **how you want** too achieve your goals
- Spells should not be more powerful than the base minecraft game. A damaging spell will never be stronger than a fully charged Bow, Crossbow, or Netherite Sword, for example. Digging will never be faster than a Netherite Pick.
- Since spells cannot be enchanted, they should synergize.  Instead of having a Smite V Netherite Swword, you might cast a spell that weakens your enemies, causing them to take additional damage from your next spell.  Then, your next spell might do the damage of a Netherite Sword, except because of the other spell it is boosted to do an additional amount of damage similar to the Smite calculation.
- How do we balance out item durability? 
  - Mana seems out of place. 
  - Should a spell have "durability" of some kind?
  - Do we use spell components? Ender Pearls for Teleportation?
    - Spell components may serve as a good answer. After all, the catalyst for End Portals and Nether Portals require spell components!
  - Base minecraft game seems to imply the souls of the dead are the magical power source, but then why do Nether and End Portals exist
    - Possibly souls can stand in for spell components.  
    - This would make souls the "easy" way to do magic: after all, if you have soul energy you don't need to carry components!
    - But then there are probably **consequences** for using souls in magic
    - This would fit the lore rather well

### Enchantments
- Creating Enchanting books will be a part of this system
- Enchanting books are still applied to weapons/armor/tools the same way as normal
- Enchanting books may contain enchantments which can result in complex effects, similar to spellcasting
  - Their affects only apply to targets hit (including targets hit by Sweeping Edge)

### Brewing
- Brewing will be a part of this system
- Brewing potions can still be done the same way as normal
- However, making an improved cauldron for brewing should allow for better/easier/more powerful brewing
- Any spellcasting effect can be contained in a one-use potion effect!  
  - Drinking applies to the drinker
  - Splash potions apply to all targets hit

## Licensing
Arcanum Theoria

Copyright (C) 2022 Kevin Kragenbrink <kevin@writh.net>

This program is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program.  If not, see <https://www.gnu.org/licenses/>.