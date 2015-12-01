import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;
import java.sql.*;
import javax.sql.*;

public class PopRoomReportPanel extends JPanel {
    JButton ok;
    JTable table;
    Object[][] data;
    public PopRoomReportPanel() {
        try {
            getData(HotelApp.con, HotelApp.dbname);
        } catch (SQLException ex) {
            System.out.println("Error");
        }

        String[] col = {"Month", "Top Room-Category", "Location", "Total # of reserve. per Room-Cat."};

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
        String august = "August";
        Statement stmt = null;
        String query = "SELECT Month, toproomcategory,  Location,  MAX(totalNumReservations) FROM (SELECT MONTHNAME(RESERVATION.StartDate) AS Month, ROOM.RoomCategory AS toproomcategory, RESERVATIONHASROOM.Location AS Location, COUNT(RESERVATIONHASROOM.ReservationID) AS totalNumReservations FROM RESERVATION INNER JOIN RESERVATIONHASROOM ON RESERVATION.ReservationID = RESERVATIONHASROOM.ReservationID INNER JOIN ROOM ON (ROOM.RoomNum = RESERVATIONHASROOM.RoomNum AND ROOM.Location = RESERVATIONHASROOM.Location)  WHERE RESERVATION.IsCancelled=0 AND MONTHNAME(RESERVATION.StartDate) = \"" + august+ "\" GROUP BY RoomCategory, RESERVATIONHASROOM.Location) TotalNumCat GROUP BY Location";

        try {
            stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(query);

            ArrayList<String> monthList = new ArrayList<String>();
            ArrayList<String> catList = new ArrayList<String>();
            ArrayList<String> locationList = new ArrayList<String>();
            ArrayList<Integer> numList = new ArrayList<Integer>();

            while(rs.next())
            {
                monthList.add(rs.getString("Month"));
                catList.add(rs.getString("toproomcategory"));
                locationList.add(rs.getString("Location"));
                numList.add(rs.getInt("MAX(totalNumReservations)"));
            }

            Object[][] temp = new Object[numList.size()][4];
            for (int i = 0; i < numList.size(); i++) {
                temp[i][0] = monthList.get(i);
                temp[i][1] = catList.get(i);
                temp[i][2] = locationList.get(i);
                temp[i][3] = numList.get(i);
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