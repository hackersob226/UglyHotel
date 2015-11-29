import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;

public class PaymentInfoPanel extends JPanel {
    JTextField name, cardNum, expDate, cvv;
    JButton save, delete, back;
    public PaymentInfoPanel() {
        name = new JTextField(10);
        cardNum = new JTextField(16);
        expDate = new JTextField(7);
        cvv = new JTextField(3);

        add(new JLabel("** ADD CARD **"));
        add(new JLabel("Name on Card: "));
        add(name);

        add(new JLabel("Card Number: "));
        add(cardNum);

        add(new JLabel("Expiration Date (mm/yyyy): "));
        add(expDate);

        add(new JLabel("CVV: "));
        add(cvv);

        save = new JButton("Save");
        add(save);

        add(new JLabel("___________________________________________________________________________________________________________"));
        add(new JLabel("** DELETE CARD **"));

        //Credit Cards go here
        Integer creditCards[] = {new Integer(1234), new Integer(2345)};
        JComboBox dropDown = new JComboBox(creditCards);
        dropDown.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int selectedCard = (int)dropDown.getSelectedItem();
                System.out.println(selectedCard); //GET RID OF THIS LATER
            }
        });
        add(dropDown);

        delete = new JButton("Delete");
        add(delete);
        add(new JLabel("___________________________________________________________________________________________________________"));
        back = new JButton("Back");
        back.addActionListener(new ButtonListener("CheckDetails"));
        add(back);
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