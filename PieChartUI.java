package Utils;

import java.awt.*;
import java.awt.event.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.Timer;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.editor.ChartEditorManager;
import org.jfree.chart.event.ChartChangeEvent;
import org.jfree.chart.event.ChartChangeListener;
import org.jfree.chart.plot.PiePlot;
import org.jfree.data.general.DefaultPieDataset;

import java.text.SimpleDateFormat;
import java.util.Date;
import org.jfree.chart.event.ChartChangeEvent;
import org.jfree.chart.event.ChartChangeListener;

import Toaster.Toaster;

public class PieChartUI extends JFrame{

    private static MainFrame mainFrame;
    private final Toaster toaster;
    private int userId;
    private JPanel chartPanelContainer;
    public PieChartUI(MainFrame mainFrame, int userId){

        this.userId=userId;
        PieChartUI.mainFrame = mainFrame;
        JPanel mainJPanel = getMainJPanel();
        this.add(mainJPanel);

        this.pack();
        this.setVisible(true);
        this.toFront();

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setLocation(screenSize.width / 2 - getWidth() / 2, screenSize.height / 2 - getHeight() / 2);

        toaster = new Toaster(mainJPanel);
    }
    private JPanel getMainJPanel() {
        this.setUndecorated(true);
    
        Dimension size = new Dimension(800, 400);
    
        JPanel panel1 = new JPanel();
        panel1.setSize(size);
        panel1.setPreferredSize(size);
        panel1.setBackground(UIUtils.COLOR_BACKGROUND);
        panel1.setLayout(new BorderLayout());
    
        JPanel titleBar = createCustomTitleBar();
        panel1.add(titleBar, BorderLayout.NORTH);
    
        JPanel buttonPanel = addLogoutButton();
        panel1.add(buttonPanel, BorderLayout.EAST);
    
        JPanel buttonIncPanel = addExpencesPanel();
        panel1.add(buttonIncPanel, BorderLayout.WEST);
    
        chartPanelContainer = new JPanel();
        chartPanelContainer.setLayout(new GridBagLayout());
        chartPanelContainer.setBackground(UIUtils.COLOR_BACKGROUND);
        panel1.add(chartPanelContainer, BorderLayout.CENTER);
    
        DefaultPieDataset<String> dataset;
        try {
            dataset = generatePieChartData(userId);
        } catch (SQLException e) {
            e.printStackTrace();
            toaster.error("Database error: " + e.getMessage());
            dataset = new DefaultPieDataset<>();
        }
    
        JFreeChart chart = createChart(dataset);
        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new Dimension(700, 350));
    
        Timer timer = new Timer(100, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                chartPanelContainer.repaint();
            }
        });
        timer.start();
    
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.fill = GridBagConstraints.BOTH;
        chartPanelContainer.add(chartPanel, gbc);
    
        // Add the custom income circle panel on top of the pie chart
        JPanel incomeCirclePanel = createIncomeCirclePanel();
        incomeCirclePanel.setOpaque(false); // Ensure the panel is not opaque
        incomeCirclePanel.setPreferredSize(new Dimension(150, 150)); // Increase preferred size
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0; 
        gbc.anchor = GridBagConstraints.CENTER;
        chartPanelContainer.add(incomeCirclePanel, gbc);
    
        chartPanelContainer.revalidate();
        chartPanelContainer.repaint();
    
        return panel1;
    }

    private JPanel createCustomTitleBar() {
        JPanel titleBar = new JPanel();
        titleBar.setBackground(UIUtils.COLOR_BACKGROUND);
        titleBar.setLayout(new GridBagLayout()); 
    
        JLabel titleLabel = new JLabel("Expense Distribution");
        titleLabel.setForeground(Color.WHITE);

        Font currentFont = titleLabel.getFont();
        Font newFont = currentFont.deriveFont(currentFont.getStyle(), 24f); 
        titleLabel.setFont(newFont);
    
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.CENTER; 
    
        titleBar.add(titleLabel, gbc);
    
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
    
        titleBar.addMouseListener(ma);
        titleBar.addMouseMotionListener(ma);
    
        return titleBar;
    }

    private DefaultPieDataset<String> generatePieChartData(int userId) throws SQLException {
        DefaultPieDataset<String> dataset = new DefaultPieDataset<>();

        String query = "SELECT date, category, amount " +
        "FROM expenses " +
        "WHERE Id = ? " +  
        "GROUP BY category";

        try (Connection connection = getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery(); 

            while (rs.next()) {
                String category = rs.getString("category");
                double amount = rs.getDouble("amount");
                dataset.setValue(category, amount);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            toaster.error("Database error: " + e.getMessage());
            dataset = new DefaultPieDataset<>();
        }

        return dataset;
    }

    private JFreeChart createChart(DefaultPieDataset<String> dataset) {
        JFreeChart chart = ChartFactory.createPieChart(null, dataset, false, true, false);
        PiePlot plot = (PiePlot) chart.getPlot();
        plot.setBackgroundPaint(UIUtils.COLOR_BACKGROUND);
        plot.setOutlinePaint(null);
        plot.setShadowPaint(null); 
        return chart;
    }
    private JPanel addLogoutButton() {
        JPanel buttonPanel = new JPanel(new GridBagLayout());
        buttonPanel.setBackground(UIUtils.COLOR_BACKGROUND);
        buttonPanel.setPreferredSize(new Dimension(130, 50));
    
        CustomButton logoutButton = new CustomButton(UIUtils.BUTTON_TEXT_LOGOUT, UIUtils.COLOR_INTERACTIVE, Color.white);
        logoutButton.setPreferredSize(new Dimension(80, 40)); 

        logoutButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                toaster.info("Logging out...");
                dispose();
                if (MainFrame.getInstance() != null) {
                    MainFrame.getInstance().showLoginUI();
                }
            }
        });
    
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.anchor = GridBagConstraints.CENTER;
    
        buttonPanel.add(logoutButton, gbc);
    
        return buttonPanel;
    }
   
    private JPanel addExpencesPanel() {
        final Color[] expencesButtonColors = {UIUtils.COLOR_INTERACTIVE, Color.white};
    
        JPanel expencesPanel = new JPanel(new GridBagLayout());
        expencesPanel.setBackground(UIUtils.COLOR_BACKGROUND);
    
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
    
        JLabel categoryLabel = new JLabel();
        categoryLabel.setForeground(Color.WHITE);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.EAST;
        expencesPanel.add(categoryLabel, gbc);
    
        TextFieldUsername categoryField = new TextFieldUsername();
        categoryField.setText(UIUtils.PLACEHOLDER_TEXT_CATEGORY);
        categoryField.setPreferredSize(new Dimension(100, 40)); 
        categoryField.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                if (categoryField.getText().equals(UIUtils.PLACEHOLDER_TEXT_CATEGORY)) {
                    categoryField.setText("");
                }
                categoryField.setForeground(Color.white);
                categoryField.setBorderColor(UIUtils.COLOR_INTERACTIVE);
            }
    
            @Override
            public void focusLost(FocusEvent e) {
                if (categoryField.getText().isEmpty()) {
                    categoryField.setText(UIUtils.PLACEHOLDER_TEXT_CATEGORY);
                }
                categoryField.setForeground(UIUtils.COLOR_OUTLINE);
                categoryField.setBorderColor(UIUtils.COLOR_OUTLINE);
            }
        });
        gbc.gridx = 1;
        expencesPanel.add(categoryField, gbc);
    
        JLabel amountLabel = new JLabel();
        amountLabel.setForeground(Color.WHITE);
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.EAST;
        expencesPanel.add(amountLabel, gbc);
    
        TextFieldUsername amountField = new TextFieldUsername();
        amountField.setText(UIUtils.PLACEHOLDER_TEXT_AMOUNT);
        amountField.setPreferredSize(new Dimension(100, 40)); 
        amountField.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                if (amountField.getText().equals(UIUtils.PLACEHOLDER_TEXT_AMOUNT)) {
                    amountField.setText("");
                }
                amountField.setForeground(Color.white);
                amountField.setBorderColor(UIUtils.COLOR_INTERACTIVE);
            }
    
            @Override
            public void focusLost(FocusEvent e) {
                if (amountField.getText().isEmpty()) {
                    amountField.setText(UIUtils.PLACEHOLDER_TEXT_AMOUNT);
                }
                amountField.setForeground(UIUtils.COLOR_OUTLINE);
                amountField.setBorderColor(UIUtils.COLOR_OUTLINE);
            }
        });
        gbc.gridx = 1;
        gbc.gridy = 1;
        expencesPanel.add(amountField, gbc);
    
        CustomButton expencesButton = new CustomButton(UIUtils.BUTTON_TEXT_UPDATE_EXPENCES, UIUtils.COLOR_INTERACTIVE, Color.white);
        expencesButton.setPreferredSize(new Dimension(80, 40)); 
    
        expencesButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                String category = categoryField.getText();
                double amount;
                try {
                    amount = Double.parseDouble(amountField.getText());
                } catch (NumberFormatException ex) {
                    toaster.error("Invalid amount. Please enter a numeric value.");
                    return;
                }
    
                try {
                    saveExpencesToDatabase(category, amount);
                    updatePieChart();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                    toaster.error("Error saving expense: " + ex.getMessage());
                }
            }
        });
    
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        expencesPanel.add(expencesButton, gbc);
    
        expencesPanel.setPreferredSize(new Dimension(130, 50));
        expencesPanel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
    
        return expencesPanel;
    }

    private void updatePieChart() {
        DefaultPieDataset<String> dataset;
        try {
            dataset = generatePieChartData(userId);
        } catch (SQLException e) {
            e.printStackTrace();
            toaster.error("Database error: " + e.getMessage());
            dataset = new DefaultPieDataset<>();
        }

        JFreeChart chart = createChart(dataset);
        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new Dimension(700, 350));
        
        if (chartPanelContainer != null) {
            chartPanelContainer.removeAll();
    
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.gridx = 0;
            gbc.gridy = 0;
            gbc.weightx = 1.0;
            gbc.weighty = 1.0;
            gbc.anchor = GridBagConstraints.CENTER;
            gbc.fill = GridBagConstraints.BOTH;
    
            chartPanelContainer.add(chartPanel, gbc);
            chartPanelContainer.revalidate();
            chartPanelContainer.repaint();
        } else {
            toaster.error("Chart panel container is not initialized.");
        }
    }

    private void saveExpencesToDatabase(String category, double amount) throws SQLException {
        String query = "INSERT INTO expenses (Id, date, category, amount) VALUES (?, ?, ?, ?)";
        try (Connection connection = getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {
            
            stmt.setInt(1, userId);
            SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
            String formattedDate = sdf.format(new Date());
            stmt.setString(2, formattedDate);
            stmt.setString(3, category);
            stmt.setDouble(4, amount);
            
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            toaster.error("Database error: " + e.getMessage());
        }
    }

    private String getUserIncome() {
        String income = "";
        try (Connection connection = getConnection()) {
            String query = "SELECT Income FROM user WHERE Id = ?";
            PreparedStatement stmt = connection.prepareStatement(query);
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                income = rs.getString("Income");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return income;
    }

    public Connection getConnection() throws SQLException {
        String dbURL = "jdbc:sqlite:database/moneyAppFirst.db";
        return DriverManager.getConnection(dbURL);
    }

    private JPanel createIncomeCirclePanel() {
        return new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                String income = getUserIncome();
                int diameter = 150; // Increase diameter
                int radius = diameter / 2;
                int x = (getWidth() - diameter) / 2;
                int y = (getHeight() - diameter) / 2;
    
                g.setColor(Color.BLACK);
                g.fillOval(x, y, diameter, diameter);
                g.setColor(Color.WHITE);
                FontMetrics fm = g.getFontMetrics();
                int textWidth = fm.stringWidth("Income: " + income);
                int textX = (getWidth() - textWidth) / 2;
                int textY = y + radius + fm.getAscent() / 2;
                g.drawString("Income: " + income, textX, textY);
            }
        };
    }

    public static void main(String[] args, int userId) {
        new PieChartUI(mainFrame, userId);
    }
}