package Utils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class CustomButton extends JLabel {
    private final Color[] buttonColors;
    private final String buttonText;

    public CustomButton(String text, Color backgroundColor, Color textColor) {
        this.buttonText = text;
        this.buttonColors = new Color[]{backgroundColor, textColor};

        setBackground(UIUtils.COLOR_BACKGROUND);
        setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        setPreferredSize(new Dimension(250, 44));

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                buttonColors[0] = UIUtils.COLOR_INTERACTIVE_DARKER;
                buttonColors[1] = UIUtils.OFFWHITE;
                repaint();
            }

            @Override
            public void mouseExited(MouseEvent e) {
                buttonColors[0] = UIUtils.COLOR_INTERACTIVE;
                buttonColors[1] = Color.white;
                repaint();
            }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = UIUtils.get2dGraphics(g);
        super.paintComponent(g2);

        Insets insets = getInsets();
        int w = getWidth() - insets.left - insets.right;
        int h = getHeight() - insets.top - insets.bottom;
        g2.setColor(buttonColors[0]);
        g2.fillRoundRect(insets.left, insets.top, w, h, UIUtils.ROUNDNESS, UIUtils.ROUNDNESS);

        FontMetrics metrics = g2.getFontMetrics(UIUtils.FONT_GENERAL_UI);
        int x2 = (getWidth() - metrics.stringWidth(buttonText)) / 2;
        int y2 = ((getHeight() - metrics.getHeight()) / 2) + metrics.getAscent();
        g2.setFont(UIUtils.FONT_GENERAL_UI);
        g2.setColor(buttonColors[1]);
        g2.drawString(buttonText, x2, y2);
    }
}