!!! note "Pyrotech in 1.14.4 and beyond!"
    Hi, I'm Codetaylor, the creator of Pyrotech and I would love to work on moving the mod to more recent versions of Minecraft. If you'd like to see Pyrotech move beyond 1.12.2, [click here to read more about my campaign to make that happen](https://bit.ly/2KaxA3Hd)!

### Class

```java
import mods.pyrotech.IroncladAnvil;
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
import mods.pyrotech.IroncladAnvil;

IroncladAnvil.addRecipe("cobblestone_from_stone", <minecraft:cobblestone>, <minecraft:stone>, 8, "hammer");
```
