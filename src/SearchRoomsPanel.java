import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;
import javax.swing.event.*;
import java.text.SimpleDateFormat;

public class SearchRoomsPanel extends JPanel {
    String city = "Atlanta";
    private JTextField startYear, startMonth, startDay;
    private JTextField endYear, endMonth, endDay;
    Calendar start, end;

    public SearchRoomsPanel() {
        start = Calendar.getInstance();
        end = Calendar.getInstance();
        
        startYear = new JTextField(4);
        startYear.getDocument().addDocumentListener(new DateListener(Calendar.YEAR, "start", startYear));
        
        startMonth = new JTextField(2);
        startMonth.getDocument().addDocumentListener(new DateListener(Calendar.MONTH, "start", startMonth));
        
        startDay = new JTextField(2);
        startDay.getDocument().addDocumentListener(new DateListener(Calendar.DAY_OF_MONTH, "start", startDay));
        
        endYear = new JTextField(4);
        endMonth = new JTextField(2);
        endDay = new JTextField(2);
        
        String locations[] = {"Atlanta", "Charlotte", "Savannah", "Orlando", "Miami"};
        JComboBox dropDown = new JComboBox(locations);
        dropDown.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                city = (String) dropDown.getSelectedItem();
                System.out.printf(city); //GET RID OF THIS LATER
            }
        });
        add(dropDown);
        
        add(new JLabel("Start Date: "));
        add(startMonth);
        add(new JLabel(" / "));
        add(startDay);
        add(new JLabel(" / "));
        add(startYear);
        
        add(new JLabel("_____________________"));
        
        add(new JLabel("End Date: "));
        add(endMonth);
        add(new JLabel(" / "));
        add(endDay);
        add(new JLabel(" / "));
        add(endYear);
    }
    
    public void setDate(int timeUnit, int date, String whichDate) {
        if (whichDate == "start") {
            start.set(timeUnit, date);
        } else {
            end.set(timeUnit, date);
        }
        //Testing the date
        SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
        System.out.println(start.getTime());
    }
    
    public class DateListener implements DocumentListener {
        private int unit, day;
        private String whichD;
        private JTextField tex;

        public DateListener(int timeUnit, String whichDate, JTextField text) {
            unit = timeUnit;
            whichD = whichDate;
            tex = text;
        }
        
        public void changedUpdate(DocumentEvent e) {
            actionPerformed();
        }
        public void insertUpdate(DocumentEvent e) {
            actionPerformed();
        }
        public void removeUpdate(DocumentEvent e) {
            actionPerformed();
        }

        public void actionPerformed() {
            try {
                day = Integer.parseInt(tex.getText());
                if (unit == Calendar.MONTH) {
                    day--;
                }
            } catch (NumberFormatException e) {
                JOptionPane error = new JOptionPane();
                error.showMessageDialog(null, "Insert number.");
            }
            setDate(unit, day, whichD);
        }
    }
}