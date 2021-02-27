### Examples

```java
import mods.pyrotech.TanningRack;

// diamond to emerald in 10 minutes with an iron ingot failure item
TanningRack.addRecipe(
  "emerald_from_diamond",
  <minecraft:emerald>,
  <minecraft:diamond>,
  <minecraft:iron_ingot>,
  10 * 60 * 20
);
```