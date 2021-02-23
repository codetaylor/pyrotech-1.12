
### Class

```java
import mods.pyrotech.Barrel;
```

#### Methods

```java
static void addRecipe(
  string name,              // unique recipe name
  ILiquidStack outputFluid, // output fluid
  ILiquidStack inputFluid,  // input fluid
  IIngredient[] inputItems, // input items
  int timeTicks             // recipe duration in ticks
);
```


---


```java
static void removeRecipes(
  ILiquidStack output // output ingredient to match
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
import mods.pyrotech.Barrel;

// tannin from water and 4 leaf blocks in 10 minutes
Barrel.addRecipe(
  "tannin_from_water_and_leaves", 
  <liquid:tannin>, 
  <liquid:water>, 
  [<ore:treeLeaves>, <ore:treeLeaves>, <ore:treeLeaves>, <ore:treeLeaves>],
  10 * 60 * 20
);
```
