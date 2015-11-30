import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;
import java.sql.*;
import javax.sql.*;

public class ConfirmationPanel extends JPanel {
    JButton ok;
    int id;
    public ConfirmationPanel() {
        add(new JLabel("Your Reservation ID: "));
        try {
            getReservationID(HotelApp.con, HotelApp.dbname, LoginPanel.sessionUserName);
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
        if(id != -1)
        {
            add(new JLabel("" + id + ""));
        }else
        {
            add(new JLabel("Error"));
        }
        ok = new JButton("OK");
        ok.addActionListener(new ButtonListener("CustomerView"));
        add(ok);
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

    public int getReservationID(Connection con, String dbName, String username) throws SQLException {
        Statement stmt = null;
        String query = "SELECT ReservationID FROM RESERVATION, USER WHERE USER.Username = \"" + username + "\" AND USER.Username = RESERVATION.Username";
        id = 0;
        try {
            id = 0;
            stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(query);

            while(rs.next())
            {
                if(id < rs.getInt("ReservationID"))
                {
                    id = rs.getInt("ReservationID");
                }
            }

            if (stmt != null) { 
                stmt.close();
            }

            return id;
        } catch (SQLException e ) {
            System.out.println("Error");
        }

        if (stmt != null) { 
            stmt.close();
        }
        id = -1;
        return id;
    }
}