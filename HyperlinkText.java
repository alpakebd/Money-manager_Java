package Utils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

import static Utils.UIUtils.*;
import static java.awt.Cursor.*;


    public class HyperlinkText extends JLabel {
    private final List<Runnable> clickListeners = new ArrayList<>();

    public HyperlinkText(String text) {
        this(text, COLOR_OUTLINE, COLOR_WHITE);
    }

    public HyperlinkText(String text, Color normalColor, int colorWhite) {
        super(text);
        setForeground(normalColor);
        setFont(FONT_FORGOT_PASSWORD);
        setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                clickListeners.forEach(Runnable::run);
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                setForeground(colorWhite);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                setForeground(normalColor);
            }
        });
    }

   

    protected void setForeground(int colorWhite) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'setForeground'");
    }

    public void addClickListener(Runnable listener) {
        clickListeners.add(listener);
    }

    public HyperlinkText(String hyperlinkText, int xPos, int yPos, Runnable hyperlinkAction) {
        super(hyperlinkText);
        setForeground(COLOR_OUTLINE);
        setFont(FONT_FORGOT_PASSWORD);
        setCursor(getPredefinedCursor(HAND_CURSOR));

        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                hyperlinkAction.run();
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                setForeground(COLOR_OUTLINE.darker());
            }

            @Override
            public void mouseExited(MouseEvent e) {
                setForeground(COLOR_OUTLINE);
            }
        });

        Dimension prefSize = getPreferredSize();
        setBounds(xPos, yPos, prefSize.width, prefSize.height);
    }
}
