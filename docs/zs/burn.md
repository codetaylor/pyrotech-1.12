!!! note "Pyrotech in 1.14.4 and beyond!"
    Hi, I'm Codetaylor, the creator of Pyrotech and I would love to work on moving the mod to more recent versions of Minecraft. If you'd like to see Pyrotech move beyond 1.12.2, [click here to read more about my campaign to make that happen](https://bit.ly/2KaxA3H)!

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
  ILiquidStack fluidProduced
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
  IItemStack failureItem
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


### Examples

```java
import mods.pyrotech.Burn;

Burn.createBuilder("charcoal_from_log_pile", <minecraft:coal:1>, "pyrotech:log_pile:*")
    .setBurnStages(10)
    .setTotalBurnTimeTicks(8 * 60 * 20)
    .setFluidProduced(<liquid:wood_tar> * 50)
    .setFailureChance(0.33)
    .addFailureItem(<pyrotech:material:0>) // ash
    .addFailureItem(<pyrotech:material:0> * 2) // ash
    .addFailureItem(<pyrotech:material:0> * 4) // ash
    .addFailureItem(<pyrotech:material:15> * 4) // charcoal flakes
    .addFailureItem(<pyrotech:material:15> * 6) // charcoal flakes
    .addFailureItem(<pyrotech:material:15> * 8) // charcoal flakes
    .setRequiresRefractoryBlocks(false)
    .setFluidLevelAffectsFailureChance(true)
    .register();
```
