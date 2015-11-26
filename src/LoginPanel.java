import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;

public class LoginPanel extends JPanel {   
    private JButton login, newUser;
    private JTextField userName, pass;
    
    public LoginPanel() {
        userName = new JTextField(15);
        pass = new JPasswordField(15);
        
        add(new JLabel("Username: "));
        add(userName);
        
        add(new JLabel("Password: "));
        add(pass);
        
        login = new JButton("Login");
        login.addActionListener(new ButtonListener("CustomerView"));
        //login.addActionListener(new ButtonListener("ManagerView"));
        add(login);
        
        newUser = new JButton("New User?");
        newUser.addActionListener(new ButtonListener("NewUser"));
        add(newUser);
    }
}