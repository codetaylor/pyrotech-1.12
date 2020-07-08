!!! note "Pyrotech in 1.14.4 and beyond!"
    Hi, I'm Codetaylor, the creator of Pyrotech and I would love to work on moving the mod to more recent versions of Minecraft. If you'd like to see Pyrotech move beyond 1.12.2, [click here to read more about my campaign to make that happen](https://bit.ly/2KaxA3H)!

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
