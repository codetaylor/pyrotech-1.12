
### Class

```java
import mods.pyrotech.StoneOven;
```

#### Methods

```java
static void addRecipe(
  string name,                // unique recipe name
  IItemStack output,         
  IIngredient input,         
  @Optional boolean inherited // true if the recipe should be inherited
);
```


---


```java
static void blacklistSmeltingRecipes(
  IIngredient[] output
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
  IIngredient[] output
);
```


---


```java
static void removeRecipes(
  IIngredient output // output ingredient to match
);
```


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
import mods.pyrotech.StoneOven;

// cook an apple into a baked apple
StoneOven.addRecipe("baked_apple_from_apple", <pyrotech:apple_baked>, <minecraft:apple>);
```
