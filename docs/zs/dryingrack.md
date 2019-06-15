
### Class

```java
import mods.pyrotech.DryingRack;
```

#### Methods

```java
static void addRecipe(
  string name,       // unique recipe name
  IItemStack output, // recipe output
  IIngredient input, // recipe input
  int dryTimeTicks   // recipe duration in ticks
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


### Examples

```java
import mods.pyrotech.DryingRack;

// dried plant fibers in 30 seconds
DryingRack.addRecipe("dried_plant_fibers_from_plant_fibers", <pyrotech:material:13>, <pyrotech:material:12>, 30 * 20);
```
