package Utils;

import java.awt.*;
import java.util.HashMap;

public class UIUtils {
    public static final Font FONT_GENERAL_UI = new Font("Segoe UI", Font.PLAIN, 20);
    public static final Font FONT_FORGOT_PASSWORD = new Font("Segoe UI", Font.PLAIN, 12);

    public static final Color COLOR_OUTLINE = new Color(182, 159, 159);
    public static final Color COLOR_BACKGROUND = new Color(182, 213, 214);

    
 
    public static final Color COLOR_INTERACTIVE = new Color(108, 216, 158);
    public static final Color COLOR_INTERACTIVE_DARKER = new Color(87, 171, 127);
    public static final Color OFFWHITE = new Color(229, 229, 229);

    
    public static final String BUTTON_TEXT_LOGIN = "Login";
    public static final String BUTTON_TEXT_FORGOT_PASS = "Forgot your password?";
    public static final String BUTTON_TEXT_REGISTER = "Register";
    public static final String BUTTON_TEXT_UPDATE_EXPENCES="Add exp";
    protected static final String BUTTON_TEXT_LOGOUT = "Logout";
    

    public static final String PLACEHOLDER_TEXT_USERNAME = "Username";
    public static final String PLACEHOLDER_TEXT_CATEGORY = "Category";
    public static final String PLACEHOLDER_TEXT_AMOUNT = "Amount";

    public static final int ROUNDNESS = 8;
    public static final int COLOR_WHITE = 0;
    public static final Font FONT_GENERAL_LARGE = new Font("Arial", Font.BOLD, 18);

    public static Graphics2D get2dGraphics(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        g2.addRenderingHints(new HashMap<RenderingHints.Key, Object>() {{
            put(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            put(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_LCD_HRGB);
        }});
        return g2;
    }
}