package Utils;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
public class MainFrame extends JFrame {

    private LoginUI loginUI;
    private Registration registrationUI;
    private JPanel contentPane;
    private static MainFrame instance;
    
    public MainFrame() {
        
        contentPane = new JPanel();
        this.setContentPane(contentPane);
        loginUI = new LoginUI(this); 
        registrationUI = new Registration(this); 
        registrationUI.setVisible(false);

        pack(); 
        setLocationRelativeTo(null); 

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });
    }

    public void showLoginUI() {
        registrationUI.setVisible(false);
        loginUI.setVisible(true);
    }
    public void showRegistrationUI() {
        loginUI.setVisible(false);
        registrationUI.setVisible(true);
    }

    public static void main(String[] args) {
        new MainFrame();
    }

    public static MainFrame getInstance() {
        if (instance == null) {
            instance = new MainFrame();
        }
        return instance;
    }
}

