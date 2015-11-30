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
    DataModel model;
    JLabel totalCost;
    JButton submit, calculate;
    double price; //Storing updated price here.

    public AvailableRoomsPanel(int id) {
        add(new JLabel("After selecting Extra Bed Option, hit Calculate to find Total Cost Updated. Please hit Calculate before Submitting."));
        String[] col = {"Room Number", "Room Category", "Person Capacity",
                        "Cost per Day", "Cost of Extra Bed per Day", "Extra Bed?"};
        //Place available rooms in Object[][] data
        Object[][] data = { {new Integer(11), "Filler", new Integer(1), new Integer(111), new Integer(10), new Boolean(false)},
            {new Integer(11), "Filler", new Integer(1), new Integer(111), new Integer(10), new Boolean(false)}
        };
        model = new DataModel(data, col);
        table = new JTable(model);
        table.setPreferredScrollableViewportSize(new Dimension(HotelApp.WIDTH - 50, HotelApp.HEIGHT - 150));
        JScrollPane scrollPane = new JScrollPane(table);

        add(scrollPane);
        selectionModel = (DefaultListSelectionModel) table.getSelectionModel();

        add(new JLabel("Total Cost Updated: "));
        totalCost = new JLabel(" ");
        add(totalCost);

        calculate = new JButton("Calculate");
        calculate.addActionListener(new ButtonListener("calculate"));
        add(calculate);
        submit = new JButton("Submit");
        submit.addActionListener(new ButtonListener("CustomerView"));
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
        for (int i = 0; i < model.getRowCount(); i++){
            if (selectionModel.isSelectedIndex(i)) {
                model.setValueAt(new Boolean(true), i, 5);
                System.out.println(selectionModel.isSelectedIndex(i));
            }
        }

        for (int i = 0; i < model.getRowCount(); i++) {
            if (model.getValueAt(i, 5).equals(new Boolean(true))) { //Checks for ExtraBed, so gather any ExtraBed Logic here
                total += diff * (int)model.getValueAt(i, 4);
            }
            total += diff * (int)model.getValueAt(i, 3);
        }
        totalCost.setText(""+total+"");
        return total;
    }

    public class ButtonListener implements ActionListener {
        private String state;

        public ButtonListener(String currState) {
            state = currState;
        }

        public void actionPerformed(ActionEvent e) {
            if (state == "calculate") {
                price = calculateTotal();
            } else {
                JOptionPane confirm = new JOptionPane();
                confirm.showMessageDialog(null, "Reservation Updated.");
                HotelApp.currentState = state;
                HotelApp.checkState();
            }
        }
    }
}