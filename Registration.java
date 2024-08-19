package Utils;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.SwingConstants;

import java.awt.*;
import java.awt.event.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import Toaster.Toaster;

public class Registration extends JFrame {
    private final Toaster toaster;
    private TextFieldUsername usernameField;
    private TextFieldPassword passwordField;
    private TextFieldPassword cPasswordField;
    private static MainFrame mainFrame;

    public Registration(MainFrame mainFrame) {
        Registration.mainFrame = mainFrame;
        JPanel mainJPanel = getMainJPanel();
        addSeparator(mainJPanel);
        addUsernameTextField(mainJPanel);
        addPasswordTextField(mainJPanel);
        addConfPasswordField(mainJPanel);
        addRegistButton(mainJPanel);

        this.add(mainJPanel);
        this.pack();
        this.setVisible(true);
        this.toFront();

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setLocation(screenSize.width / 2 - getWidth() / 2, screenSize.height / 2 - getHeight() / 2);

        toaster = new Toaster(mainJPanel);
    }

    
    private void addRegistButton(JPanel panel1) {
        final Color[] loginButtonColors = {UIUtils.COLOR_INTERACTIVE, Color.white};

        JLabel regisButton = new JLabel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = UIUtils.get2dGraphics(g);
                super.paintComponent(g2);

                Insets insets = getInsets();
                int w = getWidth() - insets.left - insets.right;
                int h = getHeight() - insets.top - insets.bottom;
                g2.setColor(loginButtonColors[0]);
                g2.fillRoundRect(insets.left, insets.top, w, h, UIUtils.ROUNDNESS, UIUtils.ROUNDNESS);

                FontMetrics metrics = g2.getFontMetrics(UIUtils.FONT_GENERAL_UI);
                int x2 = (getWidth() - metrics.stringWidth(UIUtils.BUTTON_TEXT_LOGIN)) / 2;
                int y2 = ((getHeight() - metrics.getHeight()) / 2) + metrics.getAscent();
                g2.setFont(UIUtils.FONT_GENERAL_UI);
                g2.setColor(loginButtonColors[1]);
                g2.drawString(UIUtils.BUTTON_TEXT_REGISTER, x2, y2);
            }
        };

        regisButton.addMouseListener(new MouseAdapter() {

            @Override
            public void mousePressed(MouseEvent e) {
                regisEventHandler();
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                loginButtonColors[0] = UIUtils.COLOR_INTERACTIVE_DARKER;
                loginButtonColors[1] = UIUtils.OFFWHITE;
                regisButton.repaint();
            }

            @Override
            public void mouseExited(MouseEvent e) {
                loginButtonColors[0] = UIUtils.COLOR_INTERACTIVE;
                loginButtonColors[1] = Color.white;
                regisButton.repaint();
            }
        });

        regisButton.setBackground(UIUtils.COLOR_BACKGROUND);
        regisButton.setBounds(60, 277, 250, 44);
        regisButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        panel1.add(regisButton);
    }
    private void addConfPasswordField(JPanel panel1){
        cPasswordField = new TextFieldPassword();

        cPasswordField.setBounds(50, 189, 250, 44);
        cPasswordField.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                cPasswordField.setForeground(Color.white);
                cPasswordField.setBorderColor(UIUtils.COLOR_INTERACTIVE);
            }

            @Override
            public void focusLost(FocusEvent e) {
                cPasswordField.setForeground(UIUtils.COLOR_OUTLINE);
                cPasswordField.setBorderColor(UIUtils.COLOR_OUTLINE);
            }
        });

        cPasswordField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                if (e.getKeyChar() == KeyEvent.VK_ENTER)
                    regisEventHandler();
            }
        });

        panel1.add(cPasswordField);
    }

    private void addPasswordTextField(JPanel panel1) {
        passwordField = new TextFieldPassword();

        passwordField.setBounds(50, 128, 250, 44);
        passwordField.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                passwordField.setForeground(Color.white);
                passwordField.setBorderColor(UIUtils.COLOR_INTERACTIVE);
            }

            @Override
            public void focusLost(FocusEvent e) {
                passwordField.setForeground(UIUtils.COLOR_OUTLINE);
                passwordField.setBorderColor(UIUtils.COLOR_OUTLINE);
            }
        });

        passwordField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                if (e.getKeyChar() == KeyEvent.VK_ENTER)
                    regisEventHandler();
            }
        });

        panel1.add(passwordField);
    }

    private void addUsernameTextField(JPanel panel1) {
        usernameField = new TextFieldUsername();

        usernameField.setBounds(50, 69, 250, 44);
        usernameField.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                if (usernameField.getText().equals(UIUtils.PLACEHOLDER_TEXT_USERNAME)) {
                    usernameField.setText("");
                }
                usernameField.setForeground(Color.white);
                usernameField.setBorderColor(UIUtils.COLOR_INTERACTIVE);
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (usernameField.getText().isEmpty()) {
                    usernameField.setText(UIUtils.PLACEHOLDER_TEXT_USERNAME);
                }
                usernameField.setForeground(UIUtils.COLOR_OUTLINE);
                usernameField.setBorderColor(UIUtils.COLOR_OUTLINE);
            }
        });

        panel1.add(usernameField);
    }

    private void addSeparator(JPanel panel1) {
        JSeparator separator1 = new JSeparator();
        separator1.setOrientation(SwingConstants.VERTICAL);
        separator1.setForeground(UIUtils.COLOR_OUTLINE);
        panel1.add(separator1);
        separator1.setBounds(410, 80, 1, 240);
    }

    private JPanel getMainJPanel() {

        this.setUndecorated(true);

        Dimension size = new Dimension(800, 400);

        JPanel panel1 = new JPanel();
        panel1.setSize(size);
        panel1.setPreferredSize(size);
        panel1.setBackground(UIUtils.COLOR_BACKGROUND);
        panel1.setLayout(null);

        MouseAdapter ma = new MouseAdapter() {
            int lastX, lastY;

            @Override
            public void mousePressed(MouseEvent e) {
                lastX = e.getXOnScreen();
                lastY = e.getYOnScreen();
            }

            @Override
            public void mouseDragged(MouseEvent e) {
                int x = e.getXOnScreen();
                int y = e.getYOnScreen();
                setLocation(getLocationOnScreen().x + x - lastX, getLocationOnScreen().y + y - lastY);
                lastX = x;
                lastY = y;
            }
        };

        panel1.addMouseListener(ma);
        panel1.addMouseMotionListener(ma);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });

        return panel1;
    }
    private void regisEventHandler() {
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());
        String confirmPassword = new String(cPasswordField.getPassword());
    
        
        if (username.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            toaster.error("Please fill in all fields.");
            return;
        }
    
        if (!password.equals(confirmPassword)) {
            toaster.error("Passwords do not match.");
            return;
        }
    
      
        if (isUsernameTaken(username)) {
            toaster.error("Username already exists.");
            return;
        }
    
        
        try (Connection connection = getConnection()) {
            int userId = getNextAvailableId(connection);
            PreparedStatement insertStatement = connection.prepareStatement("INSERT INTO user (username, password) VALUES (?, ?)" );
            insertStatement.setInt(1, userId);
            insertStatement.setString(1, username);
            insertStatement.setString(2, password);
            insertStatement.executeUpdate();
            toaster.success("Registration successful!");
            this.setVisible(false);
            mainFrame.showLoginUI();
     
        } catch (SQLException e) {
            e.printStackTrace();
            toaster.error("Database error: " + e.getMessage());
        }}

    private int getNextAvailableId(Connection connection) throws SQLException {
        String checkTableSql = "SELECT name FROM sqlite_master WHERE type='table' AND name='sequence'";
        Statement checkStatement = connection.createStatement();
        ResultSet checkRs = checkStatement.executeQuery(checkTableSql);

        if (!checkRs.next()) {
            String createTableSql = "CREATE TABLE sequence (name TEXT PRIMARY KEY, next_val INTEGER)";
            Statement createStatement = connection.createStatement();
            createStatement.execute(createTableSql);
            String insertSql = "INSERT INTO sequence (name, next_val) VALUES ('user', 1)";
            createStatement.execute(insertSql);
        }

        PreparedStatement selectStmt = connection.prepareStatement("SELECT next_val FROM sequence WHERE name = 'user'");
        ResultSet rs = selectStmt.executeQuery();
        int nextVal = 1;
        if (rs.next()) {
            nextVal = rs.getInt("next_val");
        }
        PreparedStatement updateStmt = connection.prepareStatement("UPDATE sequence SET next_val = next_val + 1 WHERE name = 'user'");
        updateStmt.executeUpdate();

        return nextVal;
    }
    
    private boolean isUsernameTaken(String username) {
        try (Connection connection = getConnection()) {
            PreparedStatement statement = connection.prepareStatement("SELECT COUNT(*) FROM user WHERE username = ?");
            statement.setString(1, username);
            ResultSet resultSet = statement.executeQuery();
    
            if (resultSet.next()) {
                int count = resultSet.getInt(1);
                return count > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false; 
        }
        return false; 
    }

    public Connection getConnection() throws SQLException {
        String dbURL = "jdbc:sqlite:database/moneyAppFirst.db";
        return DriverManager.getConnection(dbURL);
      }
          
      public static void main(String[] args) {
        new Registration(mainFrame);
    }
    }
