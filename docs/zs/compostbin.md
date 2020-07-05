!!! note "Pyrotech in 1.14.4 and beyond!"
    Hi, I'm Codetaylor, the creator of Pyrotech and I would love to work on moving the mod to more recent versions of Minecraft. If you'd like to see Pyrotech move beyond 1.12.2, [click here to read more about my campaign to make that happen](https://bit.ly/2KaxA3H)!

### Class

```java
import mods.pyrotech.CompostBin;
```

#### Methods

```java
static void removeRecipeByInput(
  IIngredient input // input ingredient to match
);
```


---


```java
static void removeRecipesByOutput(
  IIngredient output // output ingredient to match
);
```


---


```java
static void addRecipe(
  IIngredient output, // recipe output
  IItemStack input    // recipe input
);
```


---


```java
static void addRecipe(
  IIngredient output, // recipe output
  IItemStack input,   // recipe input
  int compostValue    // range [1,16]
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
import mods.pyrotech.CompostBin;

CompostBin.addRecipe(<pyrotech:mulch> * 4, <minecraft:string>);
CompostBin.addRecipe(<pyrotech:mulch> * 4, <minecraft:diamond>, 4);
```
