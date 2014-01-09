/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.adamborowski.utils;

import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Mivilo
 */
public class FontUtil {
    // Prepare a static "cache" mapping font names to Font objects.

    private final static String[] names = {"Consolas"};
    private final static Map<String, Font> cache = new HashMap<>(names.length);

    static {
        for (String name : names) {
            cache.put(name, getFont(name, 0, 20));
        }
    }

    public static Font getFont(String name, int style, float size) {
        Font font;
        if (cache != null) {
            if ((font = cache.get(name)) != null) {
                return font.deriveFont(style, size);
            }
        }
        String fName = "fonts/" + name+".ttf";
        try {
            InputStream is = FontUtil.class.getResourceAsStream(fName);
            font = Font.createFont(Font.TRUETYPE_FONT, is);
        } catch (FontFormatException | IOException ex) {
            System.err.println(fName + " not loaded.  Using serif font.");
            font = new Font(Font.MONOSPACED, style, (int) size);
        }
        return font.deriveFont(style, size);
    }
    public static Rectangle2D getStringBounds(String s, Graphics2D g)
    {
        return g.getFont().getStringBounds(s, g.getFontRenderContext());
    }
}
