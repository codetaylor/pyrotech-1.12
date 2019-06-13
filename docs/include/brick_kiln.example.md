### Examples

```java
import mods.pyrotech.BrickKiln;

// fire a cobblestone block into a stone block in five minutes
BrickKiln.addRecipe("stone_from_cobblestone", <minecraft:stone>, <minecraft:cobblestone>, 6000);

// fire a cobblestone block into a stone block in five minutes with a 25% chance
// to fail and instead produce dirt
BrickKiln.addRecipe("stone_from_cobblestone", <minecraft:stone>, <minecraft:cobblestone>, 6000, 0.25, [<minecraft:dirt>]);
```