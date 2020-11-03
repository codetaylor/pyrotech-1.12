
### Class

```java
import mods.pyrotech.GraniteAnvil;
```

#### Methods

```java
static void addRecipe(
  string name,                // unique recipe name
  IItemStack output,          // recipe output
  IIngredient input,          // recipe input
  int hits,                   // base number of hammer hits required
  string type,                // hammer | pickaxe
  @Optional boolean inherited // true if the recipe should be inherited
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
import mods.pyrotech.GraniteAnvil;

GraniteAnvil.addRecipe("cobblestone_from_stone", <minecraft:cobblestone>, <minecraft:stone>, 8, "hammer");
```
