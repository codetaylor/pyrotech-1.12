
### Class

```java
import mods.pyrotech.SoakingPot;
```

#### Methods

```java
static void addRecipe(
  string name,             // unique recipe name
  IItemStack output,       // recipe output
  ILiquidStack inputFluid, // input fluid
  IIngredient inputItem,   // input item
  int timeTicks            // recipe duration in ticks
);
```


---


```java
static void addRecipe(
  string name,              // unique recipe name
  IItemStack output,        // recipe output
  ILiquidStack inputFluid,  // input fluid
  IIngredient inputItem,    // input item
  boolean requiresCampfire, // needs to be above a campfire
  int timeTicks             // recipe duration in ticks
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
import mods.pyrotech.SoakingPot;

// tarred planks in 8 minutes
SoakingPot.addRecipe("tarred_planks_from_planks", <pyrotech:planks_tarred>, <liquid:wood_tar>, <ore:plankWood>, 8 * 60 * 20);
```
