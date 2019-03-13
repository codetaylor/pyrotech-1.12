### pack.json

  * atlas_definitions: define atlas id -> size
  * paths:output: define the output path relative to the current folder
  * paths:resource_path: define the output path relative to your mod's resource root
  * paths:input: define input paths, relative to the current folder, and which atlas the images collected from that path should use

```json
{
  "atlas_definitions": {
    "atlas": {
      "width": 256,
      "height": 256
    }
  },
  "paths": {
    "output": "src/main/resources/assets/pyrotech/textures/gui/book/atlas",
    "resource_path": "textures/gui/book/atlas",
    "input": [
      {
        "path": "assets/book/atlas",
        "atlas": "atlas"
      }
    ]
  }
}
```

To pass meta information about an image to the packer, make a `.json` file:

```
image.png
image.png.json
```

  * id: will override the default id made from image path
  * sub_images: splits this image into sub images using path#sub_image for image id

```json
{
  "id": "illustrations/icons",
  "sub_images": {
    "pickaxe": {"x": 0, "y": 0, "width": 6, "height": 6},
    "hammer": {"x": 6, "y": 0, "width": 6, "height": 6}
  }
}
```