import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;

public class ConfirmationPanel extends JPanel {
    JButton ok;
    public ConfirmationPanel() {
        add(new JLabel("Your Reservation ID: "));
        add(new JLabel("INSERT ID HERE AMBROSE"));
        ok = new JButton("OK");
        ok.addActionListener(new ButtonListener("CustomerView"));
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
}