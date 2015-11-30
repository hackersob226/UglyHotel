import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;
import javax.swing.table.DefaultTableModel;
import java.sql.*;
import javax.sql.*;

public class MakeReservationPanel extends JPanel {
    JTable table;
    DefaultListSelectionModel selectionModel;
    JButton submit;
    DataModel model;
    Object[][] data;
    ResultSet availabilityData;

    // public MakeReservationPanel() {
    //     String[] col = {"Room Number", "Room Category", "Person Capacity",
    //                     "Cost per Day", "Cost of Extra Bed per Day", "Select Room"};
    //     Object[SearchRoomsPanel.numRows][6] data = { {new Integer(11), "Filler", new Integer(1), new Integer(111), new Integer(10), new Boolean(false)},
    //         {new Integer(11), "Filler", new Integer(1), new Integer(111), new Integer(10), new Boolean(false)}
    //     };
    //     model = new DataModel(data, col);
    //     table = new JTable(model);

    //     table.setPreferredScrollableViewportSize(new Dimension(HotelApp.WIDTH - 50, HotelApp.HEIGHT - 100));
    //     JScrollPane scrollPane = new JScrollPane(table);

    //     add(scrollPane);
    //     selectionModel = (DefaultListSelectionModel) table.getSelectionModel();
    //     submit = new JButton("Check Details");
    //     submit.addActionListener(new ButtonListener("CheckDetails"));
    //     add(submit);
    // }

    public MakeReservationPanel(int numRows, ResultSet availableData) {
        String[] col = {"Room Number", "Room Category", "Person Capacity",
                        "Cost per Day", "Cost of Extra Bed per Day", "Select Room"};

        // try {
        // while(availableData.next())
        // {
        //         System.out.println(availableData.getInt("RoomNum") + ", " + availableData.getString("RoomCategory") + ", " + availableData.getInt("NumPersons") + ", " + availableData.getFloat("CostPerDay") + ", " + availableData.getFloat("CostOfExtraBedPerDay"));
        // }
        // } catch (SQLException ex) {
        //     System.out.println("Iteration Error");
        // }

        availabilityData = availableData;

        try {
            getData();
        } catch (SQLException ex) {
            System.out.println("Error");
        }

        model = new DataModel(data, col);
        table = new JTable(model);

        table.setPreferredScrollableViewportSize(new Dimension(HotelApp.WIDTH - 50, HotelApp.HEIGHT - 100));
        JScrollPane scrollPane = new JScrollPane(table);

        add(scrollPane);
        selectionModel = (DefaultListSelectionModel) table.getSelectionModel();
        submit = new JButton("Check Details");
        submit.addActionListener(new ButtonListener("CheckDetails"));
        add(submit);
    }

    public void getSelectedRows() {
        int rowCount = 0;
        int currRow = 0;
        for (int i = 0; i < model.getRowCount(); i++){ 
            if (selectionModel.isSelectedIndex(i)) {
                model.setValueAt(new Boolean(true), i, 5);
            }
        }
        
        //This for loop is LITERALLY just counting how many checks there are
        for (int i = 0; i < model.getRowCount(); i++) {
            if (model.getValueAt(i, 5).equals(new Boolean(true))) {
                rowCount++;
            }
        }

        Object[][] someArray = new Object[rowCount][model.getColumnCount()];
        for (int i = 0; i < model.getRowCount(); i++) {
            if (model.getValueAt(i, 5).equals(new Boolean(true))) {
                for (int j = 0; j < model.getColumnCount() - 1; j++) {
                    someArray[currRow][j] = model.getValueAt(i, j);
                }
                someArray[currRow][5] = false;
                currRow++;
            }
        }
        
        HotelApp.tempArrayForDetails = someArray; //Selected Reservations go into tempArrayForDetails
        //Print Array
        /*for (int i = 0; i < HotelApp.tempArrayForDetails.length; i++) {
            for(int j = 0; j < HotelApp.tempArrayForDetails[i].length; j++) {
                System.out.println(HotelApp.tempArrayForDetails[i][j]);
            }
        }*/
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
            getSelectedRows();
            HotelApp.transReservetoDetails();
            HotelApp.currentState = state;
            HotelApp.checkState();

        }
    }

    public void getData() throws SQLException {
        
        try {
            ResultSet tableData = availabilityData;

            ArrayList<Integer> tempList = new ArrayList<Integer>();
            ArrayList<String> tempList2 = new ArrayList<String>();
            ArrayList<Integer> tempList3 = new ArrayList<Integer>();
            ArrayList<Float> tempList4 = new ArrayList<Float>();
            ArrayList<Float> tempList5 = new ArrayList<Float>();

            while(tableData.next())
            {
                tempList.add(tableData.getInt("RoomNum"));
                tempList2.add(tableData.getString("RoomCategory"));
                tempList3.add(tableData.getInt("NumPersons"));
                tempList4.add(tableData.getFloat("CostPerDay"));
                tempList5.add(tableData.getFloat("CostOfExtraBedPerDay"));
            }

            Object[][] temp = new Object[tempList.size()][6];
            for (int i = 0; i < tempList.size(); i++) {
                temp[i][0] = tempList.get(i);
                temp[i][1] = tempList2.get(i);
                temp[i][2] = tempList3.get(i);
                temp[i][3] = tempList4.get(i);
                temp[i][4] = tempList5.get(i);
                temp[i][5] = new Boolean(false);
            }
            data = temp;
        } catch (SQLException e ) {
            System.out.println("Result Set Error");
        }
    }
}