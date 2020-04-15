!!! note "Pyrotech in 1.14.4 and beyond!"
    Hi, I'm Codetaylor, the creator of Pyrotech and I would love to work on moving the mod to more recent versions of Minecraft. If you'd like to see Pyrotech move beyond 1.12.2, [click here to read more about my campaign to make that happen](https://bit.ly/2KaxA3Hd)!

### Class

```java
import mods.pyrotech.Campfire;
```

#### Methods

```java
static void addRecipe(
  string name,        // unique recipe name
  IItemStack output,  // recipe output
  IIngredient input,  // recipe input
  @Optional int ticks // recipe duration in ticks, defaults to config value
);
```


---


```java
static void blacklistSmeltingRecipes(
  IIngredient[] output // output ingredients to blacklist
);
```


---


```java
static void blacklistAllSmeltingRecipes();
```

Blacklist all smelting recipes.

---


```java
static void whitelistSmeltingRecipes(
  IIngredient[] output // output ingredients to whitelist
);
```


---


```java
static void removeRecipes(
  IIngredient output // output ingredient to match
);
```

Removes pre-existing recipes, ie. recipes added by the mod.

---


```java
static void whitelistFuel(
  IIngredient fuel
);
```

Whitelist ingredient for use as fuel in the campfire.
Note: blacklist will take precedence over whitelist.
This means you can whitelist <ore:logWood> and blacklist
<minecraft:log:0> if you wanted.

---


```java
static void blacklistFuel(
  IIngredient fuel
);
```

Blacklist ingredient from use as fuel in the campfire.
Note: blacklist will take precedence over whitelist.
This means you can whitelist an oredict group and blacklist
a single item from it if you wanted.

---


```java
static void setGameStages(
  Stages stages // game stages
);
```

Sets game stage logic required to use the device.

---


### Examples

```java
import mods.pyrotech.Campfire;

Campfire.addRecipe("roasted_carrot_from_carrot", <pyrotech:carrot_roasted>, <minecraft:carrot>);
```
