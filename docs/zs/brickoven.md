
### Class

```java
import mods.pyrotech.BrickOven;
```

#### Methods

```java
static void addRecipe(
  string name,       // unique recipe name
  IItemStack output,
  IIngredient input 
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
import mods.pyrotech.BrickOven;

// cook an apple into a baked apple
BrickOven.addRecipe("baked_apple_from_apple", <pyrotech:apple_baked>, <minecraft:apple>);
```
