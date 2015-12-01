import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;
import java.sql.*;
import javax.sql.*;
import javax.swing.table.DefaultTableModel;
import java.text.SimpleDateFormat;

public class AvailableRoomsPanel extends JPanel {
    JTable table;
    DefaultListSelectionModel selectionModel;
    JLabel totalCost;
    JButton submit, calculate;
    double price; //Storing updated price here.
    Object[][] data;
    Calendar today;
    int reserveId;
    java.sql.Date sqlStartDate, sqlEndDate, sqlToday;

    public AvailableRoomsPanel(int id) {
        add(new JLabel("Rooms are available. Please confirm details below before submitting."));
        String[] col = {"Room Number", "Room Category", "Person Capacity",
                        "Cost per Day", "Cost of Extra Bed per Day", "Extra Bed?"};
        totalCost = new JLabel(" ");
        today = Calendar.getInstance();
        reserveId = id;

        sqlStartDate = new java.sql.Date(HotelApp.startSearchReserveDate.getTime().getTime());
        sqlEndDate = new java.sql.Date(HotelApp.endSearchReserveDate.getTime().getTime());
        sqlToday = new java.sql.Date(today.getTime().getTime());

        try {
            getData(HotelApp.con, HotelApp.dbname, id, sqlStartDate, sqlEndDate);
        } catch (SQLException ex) {
            System.out.println("Input Error");
        }

        table = new JTable(data, col);
        table.setPreferredScrollableViewportSize(new Dimension(HotelApp.WIDTH - 50, HotelApp.HEIGHT - 150));
        JScrollPane scrollPane = new JScrollPane(table);

        add(scrollPane);

        add(new JLabel("Total Cost Updated: "));
        add(totalCost);
        price = calculateTotal();

        submit = new JButton("Submit");
        submit.addActionListener(new ButtonListener("CustomerView"));
        add(submit);
    }

    public double calculateTotal() {
        double diff = 0;
        SimpleDateFormat format1 = new SimpleDateFormat("MM-dd-yyyy");
        String date1 = format1.format(HotelApp.startSearchReserveDate.getTime());
        //add(new JLabel("Start: " + date1));
        String date2 = format1.format(HotelApp.endSearchReserveDate.getTime());
        //add(new JLabel("End: " + date2));

        try {
            java.util.Date dateStart = format1.parse(date1);
            java.util.Date dateEnd = format1.parse(date2);
            diff = Math.round((dateEnd.getTime() - dateStart.getTime()) / (double) 86400000);
            //add(new JLabel("Number of days: " + diff));
        } catch (Exception e) {
            //hehe
        }

        double total = 0;
        for (int i = 0; i < data.length; i++) {
            if (data[i][5] == "Yes") {
                total += diff * ((Double)data[i][4]).doubleValue();
            }
            total += diff * ((Double)data[i][3]).doubleValue();
        }

        totalCost.setText("$"+total+"");
        return total;
    }

    public class ButtonListener implements ActionListener {
        private String state;

        public ButtonListener(String currState) {
            state = currState;
        }

        public void actionPerformed(ActionEvent e) {
            try {
                checkUpdate(HotelApp.con, HotelApp.dbname, reserveId, sqlStartDate, sqlEndDate, price, sqlToday);
            } catch (SQLException ex) {
                System.out.println("Input Error");
            }
            JOptionPane confirm = new JOptionPane();
            confirm.showMessageDialog(null, "Reservation Updated.");
            HotelApp.currentState = state;
            HotelApp.checkState();
        }
    }

    public void checkUpdate(Connection con, String dbName, int id, java.sql.Date startDay, java.sql.Date endDay, double price, java.sql.Date today) throws SQLException {
        PreparedStatement stmt = null;
        String query = "UPDATE RESERVATION SET StartDate = \"" + startDay+ "\", EndDate =\"" + endDay+ "\", TotalCost = \"" + price+ "\" WHERE ReservationID = \"" + id+ "\" AND \"" + today+ "\" <= DATEADD(day,-3,Reservation.StartDate)";
        try {
            con.setAutoCommit(false);
            stmt = con.prepareStatement(query);
            stmt.executeUpdate();
            con.commit();
        } catch (SQLException e ) {
            System.out.println("Execution Error");
        }
    }

    public void getData(Connection con, String dbName, int id, java.sql.Date startDay, java.sql.Date endDay) throws SQLException {
        Statement stmt = null;
        String query = "SELECT ROOM.RoomNum, ROOM.NumPersons, ROOM.RoomCategory, ROOM.CostPerDay, ROOM.CostofExtraBedPerDay, RESERVATIONHASROOM.IncludeExtraBed FROM RESERVATIONHASROOM INNER JOIN ROOM ON (ROOM.RoomNum = RESERVATIONHASROOM.RoomNum AND ROOM.Location = RESERVATIONHASROOM.Location) WHERE RESERVATIONHASROOM.ReservationID = \"" + id+ "\" AND NOT EXISTS(SELECT RESERVATION.ReservationID AND ReservationID = RESERVATION.ReservationID FROM RESERVATION WHERE RESERVATION.StartDate <= \"" + startDay + "\" AND RESERVATION.EndDate >= \"" + endDay + "\")";
        try {
            stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(query);

            ArrayList<Integer> roomNumList = new ArrayList<Integer>();
            ArrayList<Integer> numPersonList = new ArrayList<Integer>();
            ArrayList<String> roomCategoryList = new ArrayList<String>();
            ArrayList<Double> costPerDayList = new ArrayList<Double>();
            ArrayList<Double> costBedList = new ArrayList<Double>();
            ArrayList<Integer> extraBedList = new ArrayList<Integer>();

            while(rs.next())
            {
                roomNumList.add(rs.getInt("RoomNum"));
                numPersonList.add(rs.getInt("NumPersons"));
                roomCategoryList.add(rs.getString("RoomCategory"));
                costPerDayList.add(rs.getDouble("CostPerDay"));
                costBedList.add(rs.getDouble("CostofExtraBedPerDay"));
                extraBedList.add(rs.getInt("IncludeExtraBed"));
            }

            Object[][] temp = new Object[roomNumList.size()][6];
            for (int i = 0; i < roomNumList.size(); i++) {
                temp[i][0] = roomNumList.get(i);
                temp[i][1] = numPersonList.get(i);
                temp[i][2] = roomCategoryList.get(i);
                temp[i][3] = costPerDayList.get(i);
                temp[i][4] = costBedList.get(i);
                if (extraBedList.get(i) == 1) {
                    temp[i][5] = "Yes";
                } else {
                    temp[i][5] = "No";
                }
            }
            data = temp;
        } catch (SQLException e ) {
            System.out.println("Execution Error");
        }

        if (stmt != null) { 
            stmt.close();
        }
    }
}