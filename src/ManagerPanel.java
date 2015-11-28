import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;

public class ManagerPanel extends JPanel {
    private JButton viewReserve, viewPopRoom, viewRevenue;

    public ManagerPanel() {
        add(new JLabel("Welcome... "));

        viewReserve = new JButton("View Reservation Report");
        add(viewReserve);

        viewPopRoom = new JButton("View Pop. Room Cat. Report ");
        add(viewPopRoom);

        viewRevenue = new JButton("View Revenue Report");
        add(viewRevenue);

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