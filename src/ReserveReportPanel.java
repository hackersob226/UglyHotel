import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;
import java.sql.*;
import javax.sql.*;

public class ReserveReportPanel extends JPanel {
    JButton ok;
    JTable table;
    Object[][] data;
    public ReserveReportPanel() {
        try {
            getData(HotelApp.con, HotelApp.dbname);
        } catch (SQLException ex) {
            System.out.println("Error");
        }

        String[] col = {"Month", "Location", "Total Number of Reservations"};

        table = new JTable(data, col);
        table.setPreferredScrollableViewportSize(new Dimension(HotelApp.WIDTH - 50, HotelApp.HEIGHT - 150));
        JScrollPane scrollPane = new JScrollPane(table);

        add(scrollPane);
        ok = new JButton("OK");
        ok.addActionListener(new ButtonListener("ManagerView"));
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

    public void getData(Connection con, String dbName) throws SQLException {
        String august = "August", sept = "September";
        Statement stmt = null;
        String query = "SELECT MONTHNAME(RESERVATION.StartDate), RESERVATIONHASROOM.Location, COUNT(DISTINCT RESERVATIONHASROOM.ReservationID) FROM RESERVATION INNER JOIN RESERVATIONHASROOM ON RESERVATION.ReservationID = RESERVATIONHASROOM.ReservationID WHERE RESERVATION.IsCancelled=0 AND MONTHNAME(RESERVATION.StartDate) = \"" + august+ "\" OR MONTHNAME(RESERVATION.StartDate) =\"" + sept+ "\" GROUP BY MONTHNAME(StartDate), Location";

        try {
            stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(query);

            ArrayList<String> monthList = new ArrayList<String>();
            ArrayList<String> locationList = new ArrayList<String>();
            ArrayList<Integer> numList = new ArrayList<Integer>();

            while(rs.next())
            {
                monthList.add(rs.getString("MONTHNAME(RESERVATION.StartDate)"));
                locationList.add(rs.getString("Location"));
                numList.add(rs.getInt("COUNT(DISTINCT RESERVATIONHASROOM.ReservationID)"));
            }

            Object[][] temp = new Object[numList.size()][3];
            for (int i = 0; i < numList.size(); i++) {
                temp[i][0] = monthList.get(i);
                temp[i][1] = locationList.get(i);
                temp[i][2] = numList.get(i);
            }
            data = temp;
            if (stmt != null) {
                stmt.close();
            }
        } catch (SQLException e ) {
            System.out.println("Error");
        }
    }
}
