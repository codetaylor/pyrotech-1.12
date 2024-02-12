
### Class

```java
import mods.pyrotech.ObsidianAnvil;
```

#### Methods

```java
static void addRecipe(
  string name,       // unique recipe name
  IItemStack output, // recipe output
  IIngredient input, // recipe input
  int hits,          // base number of hammer hits required
  string type        // hammer | pickaxe
);
```


---


```java
static void removeRecipes(
  IIngredient output // recipe output to match
);
```


---


```java
static void removeAllRecipes();
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
import mods.pyrotech.ObsidianAnvil;

ObsidianAnvil.addRecipe("cobblestone_from_stone", <minecraft:cobblestone>, <minecraft:stone>, 8, "hammer");
```
