package com.codetaylor.mc.pyrotech.library.blockrenderer;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

public final class Util {

  public static BufferedImage createFlipped(BufferedImage image) {

    AffineTransform at = new AffineTransform();
    /*
     * Creates a compound affine transform, instead of just one, as we need
     * to perform two transformations.
     *
     * The first one is to scale the image to 100% width, and -100% height.
     * (That's *negative* 100%.)
     */

    at.concatenate(AffineTransform.getScaleInstance(1, -1));

    /*
     * We then need to translate the image back up by it's height, as flipping
     * it over moves it off the bottom of the canvas.
     */

    at.concatenate(AffineTransform.getTranslateInstance(0, -image.getHeight()));
    return createTransformed(image, at);
  }

  public static BufferedImage createTransformed(BufferedImage image, AffineTransform at) {
    // Create a blank image with the same dimensions as the old one...
    BufferedImage newImage = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_ARGB);
    // ...get it's renderer...
    Graphics2D g = newImage.createGraphics();
    /// ...and draw the old image on top of it with our transform.
    g.transform(at);
    g.drawImage(image, 0, 0, null);
    g.dispose();
    return newImage;
  }

  public static String sanitize(String str) {

    return str.replaceAll("[^A-Za-z0-9-_ ]", "_");
  }

  private Util() {
    //
  }
}
