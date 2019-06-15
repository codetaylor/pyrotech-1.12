### Examples

```java
import mods.pyrotech.Burn;

Burn.createBuilder("charcoal_from_log_pile", <minecraft:coal:1>, "pyrotech:log_pile:*")
    .setBurnStages(10)
    .setTotalBurnTimeTicks(8 * 60 * 20)
    .setFluidProduced(<liquid:wood_tar> * 50)
    .setFailureChance(0.33)
    .addFailureItem(<pyrotech:material:0>) // ash
    .addFailureItem(<pyrotech:material:0> * 2) // ash
    .addFailureItem(<pyrotech:material:0> * 4) // ash
    .addFailureItem(<pyrotech:material:15> * 4) // charcoal flakes
    .addFailureItem(<pyrotech:material:15> * 6) // charcoal flakes
    .addFailureItem(<pyrotech:material:15> * 8) // charcoal flakes
    .setRequiresRefractoryBlocks(false)
    .setFluidLevelAffectsFailureChance(true)
    .register();
```