package com.theredberrys.com.theredberrys.jamaicafier;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class Jamaicafier {

    public static final File originalPath = new File("C:\\Users\\sperob\\Pictures\\flags\\red white blue");

    public static void doTheThing() {
        try {
            Jamaicafier jam = new Jamaicafier();
            jam.processImagesAll(getOverallMap());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static Map<String, Map<RedWhiteBlue, int[]>> getOverallMap() {
        Map<String, Map<RedWhiteBlue, int[]>> map = new HashMap<String, Map<RedWhiteBlue, int[]>>();
        map.put("yellow green black", getMap(jamaicaYellow, jamaicaGreen, jamaicaBlack));
        map.put("yellow black green", getMap(jamaicaYellow, jamaicaBlack, jamaicaGreen));
        map.put("green yellow black", getMap(jamaicaGreen, jamaicaYellow, jamaicaBlack));
        map.put("green black yellow", getMap(jamaicaGreen, jamaicaBlack, jamaicaYellow));
        map.put("black yellow green", getMap(jamaicaBlack, jamaicaYellow, jamaicaGreen));
        map.put("black green yellow", getMap(jamaicaBlack, jamaicaGreen, jamaicaYellow));

        return map;
    }

    private static Map<RedWhiteBlue, int[]> getMap(Color... replacements) {
        Map<RedWhiteBlue, int[]> map = new HashMap<>();
        map.put(RedWhiteBlue.RED, getColorArray(replacements[0]));
        map.put(RedWhiteBlue.WHITE, getColorArray(replacements[1]));
        map.put(RedWhiteBlue.BLUE, getColorArray(replacements[2]));

        return map;
    }

    private static int[] getColorArray(Color color) {
        return new int[] { color.getRed(), color.getGreen(), color.getBlue() };
    }

    private void processImagesAll(Map<String, Map<RedWhiteBlue, int[]>> overallMap) throws IOException {
        for (String key : overallMap.keySet()) {
            File output = new File(originalPath, key);
            if (!output.exists()) {
                output.mkdir();
            }
            processImages(output, overallMap.get(key));
        }
    }

    private void processImages(File output, Map<RedWhiteBlue, int[]> map) throws IOException {
        for (File file : originalPath.listFiles()) {
            if (!file.isFile()) {
                continue;
            }

            BufferedImage original = ImageIO.read(file);
            BufferedImage replaced = getReplacementImage(original, map);
            File outputFile = new File(output, file.getName());
            ImageIO.write(replaced, "png", outputFile);
        }
    }

    private BufferedImage getReplacementImage(BufferedImage original, Map<RedWhiteBlue, int[]> replacements) {
        WritableRaster raster = original.getRaster();

        for (int xx = 0; xx < original.getWidth(); xx++) {
            for (int yy = 0; yy < original.getHeight(); yy++) {
                int[] pixel = raster.getPixel(xx, yy, (int[]) null);
                RedWhiteBlue colorType = getColorType(pixel);
                if (colorType == RedWhiteBlue.NONE) {
                    continue;
                }

                int[] newPixel = replacements.get(colorType);
                raster.setPixel(xx, yy, newPixel);
            }
        }
        return original;
    }

    private static final Color jamaicaYellow = new Color(251, 252, 0);
    private static final Color jamaicaGreen = new Color(36, 164, 95);
    private static final Color jamaicaBlack = new Color(0,0, 0);

    private enum RedWhiteBlue {
        RED,
        WHITE,
        BLUE,
        NONE
    }

    private RedWhiteBlue getColorType(int[] pixel) {
        return getColorType(pixel[0], pixel[1], pixel[2]);
    }

    private RedWhiteBlue getColorType(int r, int g, int b){
        if (r > g + b) {
            return RedWhiteBlue.RED;
        }

        if (r > 128 && g > 128 && b > 128) {
            return RedWhiteBlue.WHITE;
        }

        if (b > g + r) {
            return RedWhiteBlue.BLUE;
        }

        return RedWhiteBlue.NONE;
    }
}
