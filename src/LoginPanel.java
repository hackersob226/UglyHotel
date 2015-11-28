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
    private String enteredUserName, enteredPassword;

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

    public class ButtonListener implements ActionListener {
        private String state;

        public ButtonListener(String currState) {
            state = currState;
        }

        public void actionPerformed(ActionEvent e) {
            int userType = 0;

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
                //TODO
                System.out.println("Incorrect Login Credentials");
            }
            HotelApp.currentState = state;
            HotelApp.checkState();
        }
    }

    public int checkUser(Connection con, String dbName, String username, String password) throws SQLException {

    //TODO
    //Make the SQL Query do the work rather than the java logic
    //Add a check for Manager vs. User
        Statement stmt = null;
        String query = "SELECT * FROM " + dbName + ".USER";
        try {
            stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {
                String dbUsername = rs.getString("Username");
                String dbPassword = rs.getString("Password");
                System.out.println(dbUsername + " : " + dbPassword);
                if(enteredUserName.equals(dbUsername) && enteredPassword.equals(dbPassword))
                {
                    System.out.println("Logged in!");
                    return 1;
                }
            }
        } catch (SQLException e ) {
            System.out.println("Could not find user in USER.");
        }

        stmt = null;
        query = "SELECT Username FROM CUSTOMER";
        try {
            stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {
                String dbUsername = rs.getString("Username");
                System.out.println(dbUsername);
                if(enteredUserName.equals(dbUsername))
                {
                    System.out.println("Logged in!");
                    return 1;
                }
            }
        } catch (SQLException e ) {
            System.out.println("Could not find user in CUSTOMER.");
        }

        stmt = null;
        query = "SELECT Username FROM MANAGEMENT";
        try {
            stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {
                String dbUsername = rs.getString("Username");
                System.out.println(dbUsername);
                if(enteredUserName.equals(dbUsername))
                {
                    System.out.println("Logged in!");
                    return 1;
                }
            }
        } catch (SQLException e ) {
            System.out.println("Could not find user in MANAGEMENT.");
        }

        if (stmt != null) { 
            stmt.close();
        }

        return 0;
    }
}