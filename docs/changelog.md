1.3.10

  * Fixed:
    * Snow accumulates on top of machines [(#172)](https://github.com/codetaylor/pyrotech/issues/172)
    * Duplicate line in TOP display for tanks [(#173)](https://github.com/codetaylor/pyrotech/issues/173)
    * Drains and Collectors missing TOP display

---

1.3.9

  * Fixed:
    * Startup crash caused by setting `DISABLE_YOUR_PATREON_EFFECTS` to `true`

---

1.3.8

  * Fixed:
    * Slag glass furnace recipe should be in the bloomery module and not the refractory module

---

1.3.7

  * Fixed:
    * No Cog sound for Mechanical Mulcher [(#162)](https://github.com/codetaylor/pyrotech/issues/162)
    * Worktable.blacklistAllVanillaRecipes() does not remove blacklisted recipes from JEI [(#165)](https://github.com/codetaylor/pyrotech/issues/165)

---

1.3.6

  * Fixed:
    * Wood chip pile doesn't work in a pit burn [(#161)](https://github.com/codetaylor/pyrotech/issues/161)

---

1.3.5

  * Fixed:
    * Block of iron and gold only give 1 ingot when broken on an anvil [(#158)](https://github.com/codetaylor/pyrotech/issues/158)

---

1.3.4

  * Fixed:
    * JEI plugin for Refractory module will fail to load under certain conditions [(#154)](https://github.com/codetaylor/pyrotech/issues/154)

---

1.3.3

  * Fixed:
    * Custom CrT drying recipes don't work [(#153)](https://github.com/codetaylor/pyrotech/issues/153)

---

1.3.2

  * Fixed:
    * Grass Path block should drop dirt and grass clumps [(#146)](https://github.com/codetaylor/pyrotech/issues/146)

---

1.3.1

  * Added:
    * `/ptlang` command will now copy the lang key to the clipboard

  * Fixed:
    * `/ptlang` command uses the wrong text component
    * Base duration modifier for Pit Kiln, Crude / Drying Rack, Soaking Pot, Pit / Refractory Burn not reflected in JEI [(#144)](https://github.com/codetaylor/pyrotech/issues/144)
    * Refractory Kiln uses Stone Kiln recipes instead of its own recipes [(#144)](https://github.com/codetaylor/pyrotech/issues/144)

---

1.3.0

  * Notes:
    * Refresh your `core.OreDict-Custom.json` file or copy the new entries from `core.OreDict-Generated.json`.
    * Rock bags in item form may lose their pre-existing contents and functionality: place in world and retrieve to restore function, but not pre-existing contents.
    * Recipes made with CraftTweaker are no longer automatically inherited. [(DOC)](inheritance) [(#110)](https://github.com/codetaylor/pyrotech/issues/110)

  * Docs:
    * Documentation for the alternate Refractory Machine textures [(DOC)](refractorymachines)
    * Documentation for recipe inheritance [(DOC)](inheritance)
    * Documentation for modules [(DOC)](modules)
    * Documentation for commands [(DOC)](commands)
    * Documentation for worktable QoL [(DOC)](worktables)
    * Documentation for drying rack speed modification [(DOC)](dryingracks)
    * Documentation for Dropt rules [(DOC)](droptrules)

  * Added:
    * GameStages support for worktable recipes [(#3)](https://github.com/codetaylor/pyrotech/issues/3) [(DOC)](zs/worktable) [(DOC)](zs/stages)
    * Exposed config values for both drying racks' speed calculations [(#92)](https://github.com/codetaylor/pyrotech/issues/92) [(DOC)](dryingracks)
    * CrT methods for the drying racks that allow overriding a biome's base speed [(#92)](https://github.com/codetaylor/pyrotech/issues/92) [(DOC)](dryingracks)
    * Vanilla smelting recipe for [(#82)](https://github.com/codetaylor/pyrotech/issues/82):
        * Slag -> Slag Glass (bloomery)
        * Unfired Clay Bucket -> Clay Bucket (bucket)
        * Unfired Refractory Brick -> Refractory Brick (core)
        * Crushed Limestone -> Quicklime (core)
        * Gravel -> Cobblestone (core)
        * Unfired Clay Shears -> Clay Shears (tool)
    * Client-side command `/ptlang` to display the unlocalized name of a held item [(#94)](https://github.com/codetaylor/pyrotech/issues/94) [(DOC)](commands)
    * Worktable QoL: Recipe Clear, Recipe Repeat [(#99)](https://github.com/codetaylor/pyrotech/issues/99) [(DOC)](worktables)
    * Progress particle indicators to [(#22)](https://github.com/codetaylor/pyrotech/issues/22):
        * Chopping Block
        * Drying Rack, Crude Drying Rack
        * Campfire
        * Soaking Pot
        * Combustion Machines
    * Sawmill audio feedback [(#22)](https://github.com/codetaylor/pyrotech/issues/22):
        * The sawmill will emit a whirring drone when the machine is lit and has a sawblade.
        * The sawmill will emit a distinct cutting noise when the machine completes a recipe.
        * Config: `module.tech.Machine.cfg`: `SAWMILL_SOUNDS`
    * Oredict entries [(#108)](https://github.com/codetaylor/pyrotech/issues/108):
        * Durable Twine -> `twine`
        * Coal Coke -> `fuelCoke`
        * Block of Coal Coke -> `blockFuelCoke`
        * Dense Nether Coal -> `oreNetherCoal`
    * Press shift to view contents of rock bags in tooltip when in item form [(#116)](https://github.com/codetaylor/pyrotech/issues/116)
    * Optional ZenScript method parameter for recipe inheritance [(DOC)](inheritance) [(#110)](https://github.com/codetaylor/pyrotech/issues/110)
    * Support for The One Probe [(#6)](https://github.com/codetaylor/pyrotech/issues/6)

  * Fixed:
    * Smelting recipes added by the mod were also being removed by the mod
    * Hammering the bloom sometimes destroys nearby water sources [(#113)](https://github.com/codetaylor/pyrotech/issues/113)
    * Can put Cooked Fish back into the Campfire to produce Cooked Fish [(#114)](https://github.com/codetaylor/pyrotech/issues/114)
    * Some recipes can't be removed [(#110)](https://github.com/codetaylor/pyrotech/issues/110)
    * Pit Burn remove method removing recipes added by CrT also [(#141)](https://github.com/codetaylor/pyrotech/issues/141)

  * Changed:
    * Combustion Machine smoke particles are now used to indicate that the machine is burning fuel instead of indicating that the machine is actively working on a recipe [(#22)](https://github.com/codetaylor/pyrotech/issues/22)
    * The Soaking Pot will now eject any extra input items that it does not have the fluid to process when the fluid level drops below the amount needed to process all input items
    * Mulch can now be placed on dry farmland as well as wet farmland [(#118)](https://github.com/codetaylor/pyrotech/issues/118)
        * See config `core.cfg` -> `MULCHED_FARMLAND` -> `RESTRICT_TO_MOISTURIZED_FARMLAND`
    * Rock bags now bypass the item capability system in Forge in an attempt to fix [(#117)](https://github.com/codetaylor/pyrotech/issues/117)
        * Item data is now written directly to the item's NBT

---

1.2.9

  * Fixed:
      * Crash when other mods throw an exception during a call to their block's `Block#getItem` method [(#139)](https://github.com/codetaylor/pyrotech/issues/139)

---

1.2.8

  * Notes:
      * CrT Bloomery recipes with an oredict as input may fail to deduce the lang key from the input if the oredict is empty during script execution. To solve this, simply supply a lang key with the recipe builder. This issue has been resolved in-dev as a part of the inheritance overhaul for `1.3.0`.

  * Added:
      * Chinese translation for guidebook! Thank you Snownee and Taskeren! [(#135 Snownee, Taskeren)](https://github.com/codetaylor/pyrotech/pull/135)

  * Fixed:
      * Crash on startup | ArrayIndexOutOfBoundsException [(#133)](https://github.com/codetaylor/pyrotech/issues/133)

---

1.2.7

  * Fixed:
      * Worktable whitelist / blacklist doesn't work [(#125)](https://github.com/codetaylor/pyrotech/issues/125)

---

1.2.6

  * Fixed:
      * ArrayIndexOutOfBoundsException when using a whitelisted non-axe tool with the chopping block [(#124)](https://github.com/codetaylor/pyrotech/issues/124)

---

1.2.5

  * Fixed:
      * ZenScript method to whitelist campfire fuel doesn't work [(#121)](https://github.com/codetaylor/pyrotech/issues/121)

---

1.2.4

  * Note:
      * Any Stone Igniter or Refractory Igniter currently in the world will be inert after this update. They will need to be broken and replaced in order to function.

  * Fixed:
      * Crash when using igniter blocks [(#107)](https://github.com/codetaylor/pyrotech/issues/107)

---

1.2.3

  * Fixed:
      * Unfired Clay Shears should not work as shears with 238 durability [(#100)](https://github.com/codetaylor/pyrotech/issues/100)

---

1.2.2

  * Note:
    * Remove the leading `.` from Pyrotech's config files
    * Thermal Expansion sawmill may be missing plank recipes, please read about solutions here: [https://pyrotech.readthedocs.io/en/latest/knownissues/](https://pyrotech.readthedocs.io/en/latest/knownissues/)

  * Fixed:
    * Config files prefixed with `.` are not correctly imported by the Twitch app (#96)
    * Removed vanilla crafting recipes still showing up in JEI (#97)

  * Changed:
    * All config files that were previously prefixed with `.` have had the `.` removed
    * Pyrotech once again removes recipes during post-init, pre-CrT

---

1.2.1

  * Fixed:
    * Missing config option to disable stick drops from leaves: `DROP_STICKS_FROM_LEAVES` in the `TWEAKS` section of `.core.cfg`

---

1.2.0

  * Note:
    * Bugs: A lot of code has been touched in this release - thank you for your continued bug reports!
    * The default oredict entries have changed, please refresh your `config/pyrotech/.core.OreDict-Custom.json` file
    * It is safe to delete the `B:leaves` entry from the `config/pyrotech/plugin.Dropt.cfg` file

  * API:
    * Added `IAirflowConsumerCapability`
    * Bumped version to 1

  * Added:
    * Config options to modify base recipe duration for (#70): Pit Kiln, Crude / Drying Rack, Soaking Pot, Pit / Refractory Burn
    * ZS methods to assign Bloomery and Wither Forge fuel modifiers (#56)
    * ZS methods to white / blacklist campfire fuels (#63)
    * Config option to disable using any `logWood` as campfire fuel (#63)
    * Pyrotech and Vanilla axes (sans wood) have been added to the oredict `toolAxe` with a wildcard damage value
    * ZS methods to restrict device interaction with gamestages for (#4): Worktable / Stone Worktable, Soaking Pot, Pit Kiln, Crude Drying Rack / Drying Rack, Compacting Bin, Chopping Block, Campfire, Granite Anvil / Ironclad Anvil, Bloom, Bloomery, Wither Forge, Stone / Refractory Machines, Mechanical Hopper, Mechanical Bellows, Mechanical Mulcher, Bellows, Stone / Brick Tank, Wood Rack, Simple / Durable Rock Bag, Crate / Durable Crate, Shelf / Durable Shelf, Stash / Durable Stash
    * Sticks will now attempt to drop from any block with an entry in the `treeLeaves` oredict (#69 giggity)

  * Changed:
    * The Chopping Block recipe will now accept any axe with a `toolAxe` oredict entry
    * The Chopping Block recipe will now damage the input axe instead of consuming the entire axe
    * The vanilla recipe removal has been delayed until the load complete event to allow time for other mods, such as TE, to generate recipes
    * Dropt is no longer in charge of the leaf / stick drops
    * The Stone / Refractory Crucible will now only accept a total number of input items for which it has the output capacity (#25)
    * The Soaking Pot will now only accept a total number of input items for which it has fluid to process (#25)

---

1.1.7

  * Fixed:
    * Mulched Farmland should not sound like stone (#90)
    * Templates of refractory machines showing wrong machine tooltip (#89)
    * Crash when using Tongs (#88)

  * Changed:
    * Updated zh_cn.lang (#87 Snownee)

---

1.1.6

  * Fixed:
    * Having an open rock bag suppresses pick-up noises of other items (#62)
    * Mycelium blocks should drop dirt clumps when broken (#83)
    * Custom CrT Worktable recipes improperly displayed in JEI

---

1.1.5

  * [!] Requires Athenaeum >= 1.17.2

  * Added:
    * Bare-bones worktable zen methods `addShaped` and `addShapeless`

  * Fixed:
    * Snow collects on and fences connect to drying racks (#76)
    * Farmland blocks should drop dirt clumps like dirt blocks
    * Mulched Farmland blocks should drop dirt clumps like dirt blocks

  * Changed:
    * Increased crude / drying rack base speed when raining in a high humidity biome from 0.0 to 0.25

---

1.1.4

  * Fixed:
    * Can't remove crude drying rack recipes (#73)
    * Contrary to the proclamations of popular literature, a stone bucket does not last longer than a stone bucket (#74)
    * Fluid rendering crash mod conflict
    * Light update issues when world is first loaded

---

1.1.3

  * Fixed:
    * CrT Burn method `addFailureItem` should take `IItemStack` not `ItemStack`

---

1.1.2

  * Note:
    * To fix a conflict with generated wood compat recipe names, the generated recipe names have been changed. They now include both the input and output and look something like this: `pyrotech:minecraft_planks_4_from_minecraft_log2_0`.

  * Fixed:
    * CrT Burn method `setFluidProduced` should take `ILiquidStack` not `FluidStack`
    * Wood compat generated recipe name conflicts (#67)

  * Changed:
    * Generated wood compat recipe names, see note

---

1.1.1

  * Notes:
    * The format of both `config/pyrotech/.core.compat.Wood` json files has changed, be sure to delete and refresh or manually edit those files.
    * Do you have duplicate iron and gold slag? Remove the iron and gold entries from the `module.tech.Bloomery.Slag-Custom.json` file.

  * Fixed:
    * Design oversight in the wood compat .json file (#67)
    * Mobs spinning on rocks (#68)

  * Changed:
    * Format of the `config/pyrotech/.core.compat.Wood` json files (#67)
    * All slag items and blocks generated by the `module.tech.Bloomery.Slag-Custom.json` config are now suffixed with `_custom` to avoid crash conflict with slag items and blocks generated by the ore compat system (#66)

---

1.1.0

  * Notes:
    * Load mod once to generate new ore and wood compat .json files in config folder:
      * `.core.compat.Ore-Custom.json`
      * `.core.compat.Ore-Generated.json`
      * `.core.compat.Wood-Custom.json`
      * `.core.compat.Wood-Generated.json`
    * Player will receive a message to restart if compat files are missing and subsequently generated
    * Reload mod for generated data to take effect
    * Each subsequent load will load generated data from the `Custom` .json files
    * `Generated` files will be overwritten with derived data on every load
    * `Custom` files will not be overwritten and will be generated if missing
    * The `Slag-Custom` and `Slag-Generated` are now only for explicitly generating a slag that isn't generated by the ore compat and are not used for the ore compat in any way

  * Added:
    * CrT builder for the worktable recipes (#1)
    * Support for non-hammer tools in the CrT worktable recipes (#1)
    * Bone meal particles to indicate recipe progress when banging on a worktable or anvil
    * Auto generated log -> plank and plank -> slab recipes from other mods (#5)
    * Auto generated ore compat for common ores (#5)
    * Config option to prevent buckets from placing source blocks when broken (#14)

  * Fixed:
    * Conflict with TE causing all wood recipes to output a Chopping Block (#17)
    * Refractory Sawmill recipe tab no longer shows in JEI (#60)

  * Changed:
    * Recipe for Chopping Block now requires a Crude Axe, prevents conflict with TE (#17)
    * Fluid `clay` has been renamed to `liquid_clay` to prevent conflict with TiC fluid `clay` (#57)

---

1.0.9

  * NOTE: Remember to refresh (delete and re-run the game) your `Custom` ore dict config file or add the changes manually, changes will only appear in the `Generated` file.

  * Added:
    * Information to the guidebook introduction to draw attention to Pyrotech's sneak + scroll inventory mechanics (#33)
    * `cobblestone` oredict entry for Cobbled Andesite, Cobbled Diorite, Cobbled Granite, Cobbled Limestone (#51)
    * `cobblestoneAndesite` oredict entry for Cobbled Andesite (#51)
    * `cobblestoneDiorite` oredict entry for Cobbled Diorite (#51)
    * `cobblestoneGranite` oredict entry for Cobbled Granite (#51)
    * `cobblestoneLimestone` oredict entry for Cobbled Limestone (#51)

  * Fixed:
    * Sawmill recipes that use a blade the machine is not configured to use should not be displayed in JEI
    * Advancement, Simply Slabs, does not check metadata (#58)

  * Changed:
    * Modified wording of the guidebook entry for Mulched Farmland to clearly state the effect is temporary (#32)
    * Improved list of factors affecting Drying Rack performance (#31)
    * Bloomery iron and gold ore recipes have been changed to use the oredict for input (#51)

---

1.0.8

  * Added:
    * Config option to disable milking cows with the buckets (#48)

  * Fixed:
    * Duplicate advancement title (#45)

  * Changed:
    * Update chinese translation (#47 Snownee)

---

1.0.7

  * Fixed:
    * Lapis block gives dandelion yellow in anvil (#35)
    * NPE when hammering a bloom from the creative tab (#38)
    * Chopping block should not sound like stone (#40)
    * Refractory glass should not sound like stone (#41)
    * Stash, crate, shelf should not sound like stone (#42)

  * Added:
    * zn_ch.lang (#34 Snownee)

  * Removed:
    * Bloom from creative tab, shouldn't have been in there to begin with (#38)

---

1.0.6

  * Added:
    * Limestone block to `stoneLimestone` oredict (#29) - NOTE: Remember to refresh (delete and re-run the game) your `Custom` ore dict config file or add the changes manually, changes will only appear in the `Generated` file.

  * Fixed:
    * Crash when pit burn is lit with door placed on top (door should be placed on the side anyway) (#28)

  * Changed:
    * Improved the guidebook information on excavating flint shards with a shovel

---

1.0.5

  * Fixed:
    * Recipe left in worktable doesn't work after logging out and back in
    * Duplicate crude axe recipe in guidebook, missing crude hoe
    * Worktable voids items when it breaks from running out of durability (#24)
    * Anvil recipe conflict for stone, removed stone -> cobblestone hammer recipe (#23)

  * Changed:
    * Split the guide entry for introduction into introduction and campfire entries making it easier to find the campfire instructions
    * Removed advancement restriction on guidebook entries for Flint and Tinder and Bone and Flint Shards; the player should now have all required information necessary to make the campfire and flint and tinder without having to unlock any guidebook entries via advancements

---

1.0.4

  * Fixed:
    * Removed conflicting bone meal anvil recipes (#18)
    * Hammer check should occur before pickaxe check (#19)
    * Dedicated server crash (#21)

---

1.0.3

  * Fixed:
    * Build filename missing MC version
    * Dedicated server crash (#15)
    * Advancements fail to load when Patchouli mod is not available (#9)
    * Campfire book recipe should not register when Patchouli mod is not available

  * Notes: Players will now receive the root Pyrotech advancement when they pick up any item from the mod or open the Patchouli book given at the start.

---

1.0.2

  * Fixed:
    * Error with Thermal Expansion (#8)

---

1.0.1

  * Fixed:
    * Repeated grammatical error in guidebook (#10)
    * Dedicated server crash (#11)

---

1.0.0

  * Initial beta release

---

0.0.0