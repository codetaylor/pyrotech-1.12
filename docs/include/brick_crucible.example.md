### Examples

```java
import mods.pyrotech.BrickCrucible;

// melt ice block into a bucket of water
BrickCrucible.addRecipe("water_from_ice", <liquid:water> * 1000, <minecraft:ice>);

// melt cobblestone block into a bucket of lava
BrickCrucible.addRecipe("lava_from_cobblestone", <liquid:lava> * 1000, <minecraft:cobblestone>);
```