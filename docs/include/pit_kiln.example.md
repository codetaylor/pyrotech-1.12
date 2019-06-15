### Examples

```java
import mods.pyrotech.PitKiln;

// stone in 5 minutes, 33% chance of failure
PitKiln.addRecipe("stone_from_cobblestone", <minecraft:stone>, <minecraft:cobblestone>, 5 * 60 * 20, 0.33, [
    <pyrotech:rock>, // randomly chosen failure items
    <pyrotech:rock> * 2,
    <pyrotech:rock> * 3,
    <pyrotech:rock> * 4,
    <pyrotech:rock> * 5,
    <pyrotech:rock> * 6
]);
```