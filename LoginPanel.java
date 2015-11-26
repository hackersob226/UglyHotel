/*import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.JLabel;
import java.awt.Dimension;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;*/
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;

public class LoginPanel extends JPanel {
    public static final int WIDTH = 600, HEIGHT = 300;
    
    private JButton login, newUser;
    private JTextField userName, pass;
    
    public LoginPanel() {
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        userName = new JTextField(15);
        pass = new JPasswordField(15);
        
        add(new JLabel("Username: "));
        add(userName);
        
        add(new JLabel("Password: "));
        add(pass);
        
        login = new JButton("Login");
        add(login);
        newUser = new JButton("New User?");
        add(newUser);
    }
    
}