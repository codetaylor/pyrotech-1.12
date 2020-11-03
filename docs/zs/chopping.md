
### Class

```java
import mods.pyrotech.Chopping;
```

#### Methods

```java
static void addRecipe(
  string name,                // unique recipe name
  IItemStack output,          // recipe output
  IIngredient input,          // recipe input
  @Optional boolean inherited // true if the recipe should be inherited
);
```


---


```java
static void addRecipe(
  string name,                // unique recipe name
  IItemStack output,          // recipe output
  IIngredient input,          // recipe input
  int[] chops,                // overrides the default chops array in config
  int[] quantities,           // overrides the default quantities array in config
  @Optional boolean inherited // true if the recipe should be inherited
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
import mods.pyrotech.Chopping;

Chopping.addRecipe("oak_planks_from_oak_log", <minecraft:planks>, <minecraft:log>);
```
