import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;
import javax.swing.table.DefaultTableModel;

public class CheckDetailsPanel extends JPanel {
    JTable table;
    DefaultListSelectionModel selectionModel;
    JButton submit, addCard;
    DataModel model;

    public CheckDetailsPanel() {
        String[] col = {"Room Number", "Room Category", "Person Capacity",
                        "Cost per Day", "Cost of Extra Bed per Day", "Extra Bed?"};
        Object[][] data = { {new Integer(11), "6FIller", new Integer(1), new Integer(111), new Integer(10), new Boolean(false)},
            {new Integer(11), "FIllerino", new Integer(1), new Integer(111), new Integer(10), new Boolean(false)}
        };
        model = new DataModel(data, col);
        table = new JTable(model);

        table.setPreferredScrollableViewportSize(new Dimension(HotelApp.WIDTH - 50, HotelApp.HEIGHT - 150));
        JScrollPane scrollPane = new JScrollPane(table);

        add(scrollPane);
        selectionModel = (DefaultListSelectionModel) table.getSelectionModel();
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
            HotelApp.currentState = state;
            HotelApp.checkState();

        }
    }
}