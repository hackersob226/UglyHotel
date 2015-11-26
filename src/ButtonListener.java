import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;

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