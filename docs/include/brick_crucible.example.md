### Examples

```java
import mods.pyrotech.BrickCrucible;

// melt ice block into a bucket of water in one minute
BrickCrucible.addRecipe("water_from_ice", <liquid:water> * 1000, <minecraft:ice>, 1 * 60 * 20);

// melt cobblestone block into a bucket of lava in five minutes
BrickCrucible.addRecipe("lava_from_cobblestone", <liquid:lava> * 1000, <minecraft:cobblestone>, 5 * 60 * 20);
```