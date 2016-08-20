/*
 * ColorUtils.java Created Nov 17, 2010 by Andrew Butler, PSL
 */
//package prisms.util;

import java.awt.*;

/**
 * A set of tools for analyzing and manipulating colors
 */
public class ColorUtils {

    /**
     * Darkens a color by a given amount
     *
     * @param color  The color to darken
     * @param amount The amount to darken the color. 0 will leave the color unchanged; 1 will make
     *               the color completely black
     * @return The stained color
     */
    public static Color stain(Color color, double amount) {
        int red = (int) ((color.getRed() * (1 - amount) / 255) * 255);
        int green = (int) ((color.getGreen() * (1 - amount) / 255) * 255);
        int blue = (int) ((color.getBlue() * (1 - amount) / 255) * 255);
        return new Color(red, green, blue);
    }
}