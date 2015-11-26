import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;

public class SearchRoomsPanel extends JPanel {
    String city = "Atlanta";

    public SearchRoomsPanel() {
        String locations[] = {"Atlanta", "Charlotte", "Savannah", "Orlando", "Miami"};
        JComboBox dropDown = new JComboBox(locations);
        dropDown.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                city = (String) dropDown.getSelectedItem();
                System.out.printf(city);
            }
        });
        add(dropDown);

    }
}