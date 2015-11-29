import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;
import java.sql.*;
import javax.sql.*;

public class LoginPanel extends JPanel {
    private JButton login, newUser;
    private JTextField userName, pass;
    public static String sessionUserName;

    public LoginPanel() {
        userName = new JTextField(15);
        pass = new JPasswordField(15);

        add(new JLabel("Username: "));
        add(userName);

        add(new JLabel("Password: "));
        add(pass);

        login = new JButton("Login");
        login.addActionListener(new ButtonListener("CustomerView"));
        add(login);

        newUser = new JButton("New User?");
        newUser.addActionListener(new NewUserListener("NewUser"));
        add(newUser);
    }

    public class ButtonListener implements ActionListener {
        private String state;

        public ButtonListener(String currState) {
            state = currState;
        }

        public void actionPerformed(ActionEvent e) {
            int userType = 0;
            sessionUserName = userName.getText();
            try {
                userType = checkUser(HotelApp.con, HotelApp.dbname, userName.getText(), pass.getText());
            } catch (SQLException ex) {
                System.out.println("Error");
            }
            
            if(userType == 1)
            {
                HotelApp.currentState = "CustomerView";
                HotelApp.checkState();
            }else if(userType == 2)
            {
                HotelApp.currentState = "ManagerView";
                HotelApp.checkState();
            }else
            {
                System.out.println("Incorrect Login Credentials");
            }
        }
    }

    public class NewUserListener implements ActionListener {
        private String state;

        public NewUserListener(String currState) {
            state = currState;
        }

        public void actionPerformed(ActionEvent e) {
            HotelApp.currentState = state;
            HotelApp.checkState();
        }
    }

    public int checkUser(Connection con, String dbName, String username, String password) throws SQLException {
        Statement stmt = null;
        String query = "SELECT CUSTOMER.Username, USER.Password FROM CUSTOMER, USER WHERE CUSTOMER.Username = \"" + username + "\" AND USER.Password = \"" + password + "\" AND CUSTOMER.Username = USER.Username";
        try {
            stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(query);

            if(!rs.isBeforeFirst())
            {
                System.out.println("Could not find user in CUSTOMER.");
            }else
            {
                System.out.println("Logged in as a customer.");
                return 1;
            }
        } catch (SQLException e ) {
            System.out.println("Error");
        }

        stmt = null;
        query = "SELECT MANAGEMENT.Username, USER.Password FROM MANAGEMENT, USER WHERE MANAGEMENT.Username = \"" + username + "\" AND USER.Password = \"" + password + "\" AND MANAGEMENT.Username = USER.Username";
        try {
            stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(query);

            if(!rs.isBeforeFirst())
            {
                System.out.println("Could not find user in MANAGEMENT.");
            }else
            {
                System.out.println("Logged in as a manager.");
                return 2;
            }
        } catch (SQLException e ) {
            System.out.println("Error");
        }

        if (stmt != null) { 
            stmt.close();
        }

        return 0;
    }
}