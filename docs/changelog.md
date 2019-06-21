1.1.0

  * Added:
    * CrT builder for the worktable recipes (#1)
    * Support for non-hammer tools in the CrT worktable recipes (#1)
    * Bone meal particles to indicate recipe progress when banging on a worktable or anvil
    * Auto generated log -> plank and plank -> slab recipes from other mods (#5)
    * Auto generated ore compat for common ores (#5)

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