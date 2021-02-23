### Examples

```java
import mods.pyrotech.Barrel;

// tannin from water and 4 leaf blocks in 10 minutes
Barrel.addRecipe(
  "tannin_from_water_and_leaves", 
  <liquid:tannin>, 
  <liquid:water>, 
  [<ore:treeLeaves>, <ore:treeLeaves>, <ore:treeLeaves>, <ore:treeLeaves>],
  10 * 60 * 20
);
```