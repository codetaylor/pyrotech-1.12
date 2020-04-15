!!! note "Pyrotech in 1.14.4 and beyond!"
    Hi, I'm Codetaylor, the creator of Pyrotech and I would love to work on moving the mod to more recent versions of Minecraft. If you'd like to see Pyrotech move beyond 1.12.2, [click here to read more about my campaign to make that happen](https://bit.ly/2KaxA3Hd)!

### Class

```java
import mods.pyrotech.Bloomery;
```

#### Methods

```java
static void removeAllBloomeryRecipes();
```


---


```java
static void removeAllWitherForgeRecipes();
```


---


```java
static void removeBloomeryRecipes(
  IIngredient output // the output ingredients to match
);
```

Recipes that have an output that matches any of the given ingredients will be removed.

---


```java
static void removeWitherForgeRecipes(
  IIngredient output // the output ingredients to match
);
```

Recipes that have an output that matches any of the given ingredients will be removed.

---


```java
static Bloomery createBloomeryBuilder(
  string name,                // the name of the recipe - should be unique
  IItemStack output,          // the output item received from the bloom
  IIngredient input,          // the recipe input
  @Optional boolean inherited // true if the recipe should be inherited
);
```

Creates and returns a new bloomery recipe builder.

---


```java
static Bloomery createWitherForgeBuilder(
  string name,       // the name of the recipe - should be unique
  IItemStack output, // the output item received from the bloom
  IIngredient input  // the recipe input
);
```

Creates and returns a new wither forge recipe builder.

---


```java
static void addBloomeryFuelModifier(
  IIngredient fuel,
  double modifier  
);
```


---


```java
static void addWitherForgeFuelModifier(
  IIngredient fuel,
  double modifier  
);
```


---


```java
static void setBloomGameStages(
  Stages stages // game stages
);
```

Sets game stage logic required to use the bloom.

---


```java
static void setBloomeryGameStages(
  Stages stages // game stages
);
```

Sets game stage logic required to use the bloomery.

---


```java
static void setWitherForgeGameStages(
  Stages stages // game stages
);
```

Sets game stage logic required to use the wither forge.

---

```java
Bloomery setBurnTimeTicks(
  int burnTimeTicks // the base time in ticks to produce a bloom
);
```

Sets the base time in ticks that this recipe takes to produce a bloom. This value is further modified by fuel level and airflow.

---


```java
Bloomery setFailureChance(
  float failureChance // the recipe's failure chance
);
```

Sets the recipe's chance to fail and produce an item from the recipe's failure items. This is applied to items received from hammering a bloom.

---


```java
Bloomery setBloomYield(
  int min, // the minimum output yield
  int max  // the maximum output yield
);
```

Sets the random range for the total number of output items produced by hammering a bloom.

---


```java
Bloomery setSlagItem(
  IItemStack slagItem, // the item to use as slag
  int slagCount        // the amount of slag produced in-world during processing
);
```

Sets the slag item and the amount of in-world slag produced during operation.

---


```java
Bloomery addFailureItem(
  IItemStack itemStack, // the failure item
  int weight            // the weight
);
```

Adds a weighted item to the list of items chosen as a failure item.

---


```java
Bloomery setLangKey(
  string langKey // the lang key
);
```

The lang key provided here will be used to construct the display name of the output bloom.
If this parameter is omitted, the recipe will use the lang key of the input item.

When more than one lang key is provided, separated by a semicolon `;`, the first lang key is resolved, then passed into the next lang key and so on.

For example, if supplied with the parameter `tile.oreIron;item.pyrotech.slag.unique`, `tile.oreIron` will first be resolved to `Iron Ore` before being passed into `item.pyrotech.slag.unique`, resulting in `Iron Ore Slag`, which is then passed into `tile.pyrotech.bloom.unique.name` and ultimately resolved to `Iron Ore Slag Bloom`.

**NOTE:** The '.name' suffix is added internally and should not be included here.

---


```java
Bloomery setAnvilTiers(
  string[] tiers // valid enums: granite, ironclad
);
```

Provide an array of `granite` and / or `ironclad`.
Anvil recipe inheritance does not apply to bloom recipes. That means recipes created for the granite anvil will not be inherited by the ironclad anvil. A bloom recipe's anvil tier must be set here.

---


```java
void register();
```

This must be called on the builder last to actually register the recipe defined in the builder.

---


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
