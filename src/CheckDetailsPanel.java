import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;
import javax.swing.table.DefaultTableModel;
import java.text.SimpleDateFormat;
import java.sql.*;
import javax.sql.*;

public class CheckDetailsPanel extends JPanel {
    JTable table;
    DefaultListSelectionModel selectionModel;
    JButton submit, addCard, calculate;
    DataModel model;
    Object[][] data;
    Calendar startDate, endDate;
    float price = -1, diff = 0;
    JLabel totalCost;
    String creditCards[];
    JComboBox dropDown;

    public CheckDetailsPanel(Object[][] selected, Calendar start, Calendar end) {
        String[] col = {"Room Number", "Room Category", "Person Capacity",
                        "Cost per Day", "Cost of Extra Bed per Day", "Extra Bed?"};
        data = selected;
        startDate = start;
        endDate = end;
        model = new DataModel(data, col);
        table = new JTable(model);

        //I can't get deselection to work. So selection is a one way thing here.
        add(new JLabel("After selecting Extra Bed Option, hit Calculate to find Total Cost. Please hit Calculate before Submitting."));
        table.setPreferredScrollableViewportSize(new Dimension(HotelApp.WIDTH - 50, HotelApp.HEIGHT - 150));
        JScrollPane scrollPane = new JScrollPane(table);

        add(scrollPane);
        selectionModel = (DefaultListSelectionModel) table.getSelectionModel();

        SimpleDateFormat format1 = new SimpleDateFormat("MM-dd-yyyy");
        String date1 = format1.format(startDate.getTime());
        add(new JLabel("Start: " + date1));
        String date2 = format1.format(endDate.getTime());
        add(new JLabel("End: " + date2));

        try {
            java.util.Date dateStart = format1.parse(date1);
            java.util.Date dateEnd = format1.parse(date2);
            diff = Math.round((dateEnd.getTime() - dateStart.getTime()) / (double) 86400000);
            //add(new JLabel("Number of days: " + diff));
        } catch (Exception e) {
            //hehe
        }

        add(new JLabel("Total Cost: "));
        totalCost = new JLabel(" ");
        add(totalCost);

        calculate = new JButton("Calculate Price");
        calculate.addActionListener(new ButtonListener("calculate"));
        add(calculate);

        //Credit Card numbers go here
        //String creditCards[] = {"1234", "2345"};

        try {
            populateCards(HotelApp.con, HotelApp.dbname, LoginPanel.sessionUserName);
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
        dropDown = new JComboBox(creditCards);
        dropDown.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String selectedCard = (String)dropDown.getSelectedItem();
                System.out.println(selectedCard); //GET RID OF THIS LATER
            }
        });
        add(dropDown);

        addCard = new JButton("Add Card");
        addCard.addActionListener(new ButtonListener("PaymentInfo"));
        add(addCard);

        submit = new JButton("Submit");
        submit.addActionListener(new ButtonListener("Confirmation"));
        add(submit);
    }

    private class DataModel extends DefaultTableModel {

        public DataModel(Object[][] data, Object[] columnNames) {
            super(data, columnNames);
        }

        public Class<?> getColumnClass(int columnIndex) {
            return columnIndex == 5 ? Boolean.class : getValueAt(0, columnIndex).getClass();
        }

        public boolean isCellEditable(int row, int column) {
            return column == 5;
        }
    }

    public class ButtonListener implements ActionListener {
        private String state;

        public ButtonListener(String currState) {
            state = currState;
        }

        public void actionPerformed(ActionEvent e) {
            if (state == "calculate") {
                price = calculateTotal(diff);
            } else if (state == "PaymentInfo") {
                HotelApp.goToPaymentInfo();
                HotelApp.currentState = state;
                HotelApp.checkState();
            } else if (state == "Confirmation") {
                //TODO
                //Add a reservation to the database
                if (price == -1) {
                    JOptionPane error = new JOptionPane();
                    error.showMessageDialog(null, "Please Calculate Price first before Submitting.");
                } else if ((String)dropDown.getSelectedItem() == null) {
                    JOptionPane error = new JOptionPane();
                    error.showMessageDialog(null, "Please Select a card before Submitting. If you don't have a card, add one.");
                } else {
                    java.sql.Date sqlStartDate = new java.sql.Date(startDate.getTime().getTime());
                    java.sql.Date sqlEndDate = new java.sql.Date(endDate.getTime().getTime());

                    try {
                        addReservation(HotelApp.con, HotelApp.dbname, sqlStartDate, sqlEndDate, price, 0, LoginPanel.sessionUserName, Integer.parseInt((String)dropDown.getSelectedItem()));
                    } catch (SQLException ex) {
                        System.out.println(ex.getMessage());
                    }
                    HotelApp.goToConfirmation();
                    HotelApp.currentState = state;
                    HotelApp.checkState();
                }
            }
        }
    }

    public float calculateTotal(double numDays) {
        float total = 0;
        for (int i = 0; i < model.getRowCount(); i++){
            if (selectionModel.isSelectedIndex(i)) {
                model.setValueAt(new Boolean(true), i, 5);
                System.out.println(selectionModel.isSelectedIndex(i));
            }
        }

        for (int i = 0; i < model.getRowCount(); i++) {
            if (model.getValueAt(i, 5).equals(new Boolean(true))) { //Checks for ExtraBed
                total += numDays * (float)model.getValueAt(i, 4);
            }
            total += numDays * (float)model.getValueAt(i, 3);
        }
        String result = String.format("%.2f", total);
        totalCost.setText("$" + result + "");
        return total;
    }

    public int populateCards(Connection con, String dbName, String userName) throws SQLException {
        Statement stmt = null;
        String query = "SELECT CardNum FROM PAYMENTINFORMATION WHERE PAYMENTINFORMATION.Username = \"" + userName + "\"";
        try {
            stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            ArrayList<String> tempList = new ArrayList<String>();
            creditCards = new String[1];

            while(rs.next())
            {
                tempList.add(rs.getString("CardNum"));
            }

            if(tempList != null)
            {
                creditCards = tempList.toArray(creditCards);
            }

            return 1;
        } catch (SQLException e ) {
            System.out.println(e.getMessage());
        }

        if (stmt != null) {
            stmt.close();
        }

        return 0;
    }

    public int addReservation(Connection con, String dbName, java.sql.Date begin, java.sql.Date ending, float cost, int isCancelled, String loggedInUser, int cardNum) throws SQLException {
        PreparedStatement stmt = null;
        String query = "INSERT INTO RESERVATION (StartDate, EndDate, CardNum, IsCancelled, Username, TotalCost) VALUES (\"" + begin + "\", \"" + ending + "\", \"" + cardNum + "\", \"" + isCancelled + "\", \"" + loggedInUser + "\", \"" + cost + "\")";

        try {
            con.setAutoCommit(false);
            stmt = con.prepareStatement(query);
            stmt.executeUpdate();
            con.commit();
        } catch (SQLException e ) {
            System.out.println(e.getMessage());
        }

        if (stmt != null) {
            stmt.close();
        }
        System.out.println("Added Reservation");
        return 0;
    }
}