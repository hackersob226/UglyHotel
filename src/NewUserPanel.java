import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;
import java.sql.*;
import javax.sql.*;

public class NewUserPanel extends JPanel {
    private JButton submit,back;
    private JTextField userName, pass, confirmPass, email;

    public NewUserPanel() {
        userName = new JTextField(15);
        pass = new JPasswordField(15);
        confirmPass = new JPasswordField(15);
        email = new JTextField(15);

        add(new JLabel("Desired Username: "));
        add(userName);

        add(new JLabel("New Password: "));
        add(pass);

        add(new JLabel("Confirm Password: "));
        add(confirmPass);

        add(new JLabel("Email: "));
        add(email);

        submit = new JButton("Submit");
        submit.addActionListener(new ButtonListener("Login"));
        add(submit);

        back = new JButton("Cancel");
        back.addActionListener(new ButtonListener("Login"));
        add(back);
    }

    public class ButtonListener implements ActionListener {
        private String state;

        public ButtonListener(String currState) {
            state = currState;
        }

        public void actionPerformed(ActionEvent e) {
            HotelApp.currentState = state;
            HotelApp.checkState();
        }
    }
}