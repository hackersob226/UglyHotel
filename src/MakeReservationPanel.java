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

    public MakeReservationPanel() {
        String[] col = {"Room Number", "Room Category", "Person Capacity",
                        "Cost per Day", "Cost of Extra Bed per Day", "Select Room"};
        Object[][] data = { {new Integer(11), "Filler", new Integer(1), new Integer(111), new Integer(10), new Boolean(false)},
            {new Integer(11), "Filler", new Integer(1), new Integer(111), new Integer(10), new Boolean(false)}
        };
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
        
        HotelApp.tempArrayForDetails = someArray;
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
}