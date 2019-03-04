### Class

```java
import mods.pyrotech.Bloomery
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
  string name,       // the name of the recipe - should be unique
  IItemStack output, // the output item received from the bloom
  IIngredient input  // the recipe input
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

