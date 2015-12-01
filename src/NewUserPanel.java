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
        submit.addActionListener(new SubmitListener("Login"));
        add(submit);

        back = new JButton("Cancel");
        back.addActionListener(new CancelListener("Login"));
        add(back);
    }

    public class SubmitListener implements ActionListener {
        private String state;

        public SubmitListener(String currState) {
            state = currState;
        }

        public void actionPerformed(ActionEvent e) {
            if (userName.getText().length() == 0 || pass.getText().length() == 0 || confirmPass.getText().length() == 0 || email.getText().length() == 0) {
                JOptionPane error1 = new JOptionPane();
                error1.showMessageDialog(null, "Please fill out all text fields.");
            } else {
                int error = 0;
                try {
                    error = checkSubmit(HotelApp.con, HotelApp.dbname, userName.getText(), pass.getText(), confirmPass.getText(), email.getText());
                } catch (SQLException ex) {
                    System.out.println("Error");
                    error = 2;
                }
                if(error == 0)
                {
                    HotelApp.currentState = state;
                    HotelApp.checkState();
                }else if(error == 1)
                {
                    JOptionPane error2 = new JOptionPane();
                    error2.showMessageDialog(null, "Entered passwords don't match.");
                    System.out.println("Entered passwords don't match");
                }else
                {
                    JOptionPane error3 = new JOptionPane();
                    error3.showMessageDialog(null, "User already exists");
                    System.out.println("User already exists");
                }
            }
        }
    }

    public class CancelListener implements ActionListener {
        private String state;

        public CancelListener(String currState) {
            state = currState;
        }

        public void actionPerformed(ActionEvent e) {
            HotelApp.currentState = state;
            HotelApp.checkState();
        }
    }

    public int checkSubmit(Connection con, String dbName, String username, String password, String confirmPassword, String email) throws SQLException {
        PreparedStatement stmt = null;
        String query1 = "INSERT INTO USER VALUES (\"" + username + "\", \"" + password + "\")";
        String query2 = "INSERT INTO CUSTOMER VALUES (\"" + username + "\", \"" + email + "\")";
        
        if(!password.equals(confirmPassword))
        {
            return 1;
        }

        try {
            con.setAutoCommit(false);
            stmt = con.prepareStatement(query1);
            stmt.executeUpdate();
            con.commit();
        } catch (SQLException e ) {
            System.out.println(e.getMessage());
            return 2;
        }

        stmt = null;
        try {
            con.setAutoCommit(false);
            stmt = con.prepareStatement(query2);
            stmt.executeUpdate();
            con.commit();
        } catch (SQLException e ) {
            System.out.println(e.getMessage());
            return 2;
        }
        if (stmt != null) { 
            stmt.close();
        }
        JOptionPane confirm = new JOptionPane();
        confirm.showMessageDialog(null, "Added new user.");
        System.out.println("Added new user");
        return 0;
    }
}