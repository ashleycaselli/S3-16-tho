package utils;

import java.awt.*;
import java.io.IOException;
import java.io.InputStream;

/**
 * Class containing resources information
 */
public class Resources {

    private static final String JAVAFX_VIEW_EXTENSION = ".fxml";

    public static final String GOOGLE_MAPS_VIEW = "/GMaps" + JAVAFX_VIEW_EXTENSION;

    private static final String IMAGE_EXTENSION = ".png";

    public static final String LOGO_IMAGE = "/logo" + IMAGE_EXTENSION;

    private static final String FONT_EXTENSION = ".ttf";

    private static final String FONT_ANDREW = "/Andrew" + FONT_EXTENSION;

    private static final float FONT_DIMENSION = 18f;

    public static final Font DEFAULT_FONT = getDefaultFont();

    private static Font getDefaultFont() {
        InputStream is = Resources.class.getClass().getResourceAsStream(FONT_ANDREW);
        Font font = null;
        try {
            font = Font.createFont(Font.TRUETYPE_FONT, is);
        } catch (FontFormatException | IOException e) {
            e.printStackTrace();
        }
        return font != null ? font.deriveFont(FONT_DIMENSION) : null;
    }

}
