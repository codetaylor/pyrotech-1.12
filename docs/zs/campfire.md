
### Class

```java
import mods.pyrotech.Campfire;
```

#### Methods

```java
static void addRecipe(
  string name,       // unique recipe name
  IItemStack output, // recipe output
  IIngredient input  // recipe input
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


### Examples

```java
import mods.pyrotech.Campfire;

Campfire.addRecipe("roasted_carrot_from_carrot", <pyrotech:carrot_roasted>, <minecraft:carrot>);
```
