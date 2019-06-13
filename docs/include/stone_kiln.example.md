### Examples

```java
import mods.pyrotech.StoneKiln;

// fire a cobblestone block into a stone block in five minutes
StoneKiln.addRecipe("stone_from_cobblestone", <minecraft:stone>, <minecraft:cobblestone>, 6000);

// fire a cobblestone block into a stone block in five minutes with a 25% chance
// to fail and instead produce dirt
StoneKiln.addRecipe("stone_from_cobblestone", <minecraft:stone>, <minecraft:cobblestone>, 6000, 0.25, [<minecraft:dirt>]);
```