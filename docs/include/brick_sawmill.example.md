### Examples

It is important to use `:*` for the meta value of the sawblades to ensure that the blade will continue to be valid after taking damaged.

```java
import mods.pyrotech.BrickSawmill;

// cut oak logs into oak planks in 10 seconds using an iron or diamond sawblade
// and produce 8 wood chips
BrickSawmill.addRecipe("oak_planks_from_oak_logs", <minecraft:planks:0>, <minecraft:log:0>, 200, <pyrotech:sawmill_blade_iron:*>.or(<pyrotech:sawmill_blade_diamond:*>), 8);
```