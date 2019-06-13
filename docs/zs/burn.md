
### Class

```java
import mods.pyrotech.Burn;
```

#### Methods

```java
static void removeRecipes(
  IIngredient output // output ingredient to match
);
```

Remove all recipes with the given recipe output.

---


```java
static void removeAllRecipes();
```


---


```java
static Burn createBuilder(
  string name,       // unique recipe name
  IItemStack output, // recipe output
  string blockString // block string to match
);
```


---

```java
Burn setBurnStages(
  int burnStages
);
```


---


```java
Burn setTotalBurnTimeTicks(
  int totalBurnTimeTicks
);
```


---


```java
Burn setFluidProduced(
  FluidStack fluidProduced
);
```


---


```java
Burn setFailureChance(
  float failureChance
);
```


---


```java
Burn addFailureItem(
  ItemStack failureItem
);
```


---


```java
Burn setRequiresRefractoryBlocks(
  boolean requiresRefractoryBlocks
);
```


---


```java
Burn setFluidLevelAffectsFailureChance(
  boolean fluidLevelAffectsFailureChance
);
```


---


```java
void register();
```


---

