### Examples

```java
import mods.pyrotech.StoneSawmill;

// cut oak logs into oak planks in 10 seconds using an iron or diamond sawblade
// and produce 8 wood chips
StoneSawmill.addRecipe("oak_planks_from_oak_logs", <minecraft:planks:0>, <minecraft:log:0>, 200, <pyrotech:sawmill_blade_iron>.or(<pyrotech:sawmill_blade_diamond>), 8);
```