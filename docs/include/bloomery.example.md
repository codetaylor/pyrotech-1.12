### Examples

The following two recipes are the same recipes that Pyrotech uses for Iron Ore Blooms and Iron Ore Slag Blooms.

```java
import mods.pyrotech.Bloomery;

// recipe for an iron bloom from an iron ore
Bloomery.createBloomeryBuilder(
        "bloom_from_iron_ore",   // recipe name
        <minecraft:iron_nugget>, // output
        <minecraft:iron_ore>     // input
    )
    .setAnvilTiers(["granite", "ironclad"])
    .setBurnTimeTicks(28800)
    .setFailureChance(0.25)
    .setBloomYield(12, 15)
    .setSlagItem(<pyrotech:generated_slag_iron>, 4)
    .addFailureItem(<pyrotech:slag>, 1)
    .addFailureItem(<pyrotech:generated_slag_iron>, 2)
    .register();

// recipe for an iron slag bloom from an iron slag pile
Bloomery.createBloomeryBuilder(
        "bloom_from_iron_slag",             // recipe name
        <minecraft:iron_nugget>,            // output
        <pyrotech:generated_pile_slag_iron> // input
    )
    .setAnvilTiers(["granite", "ironclad"])
    .setBurnTimeTicks(14400)
    .setFailureChance(0.25)
    .setBloomYield(12, 15)
    .setSlagItem(<pyrotech:generated_slag_iron>, 2)
    .addFailureItem(<pyrotech:rock:0>, 1)
    .addFailureItem(<pyrotech:slag>, 2)
    .setLangKey("tile.oreIron;item.pyrotech.slag.unique")
    .register();
```