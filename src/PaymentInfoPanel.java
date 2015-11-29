import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;

public class PaymentInfoPanel extends JPanel {
    JTextField name, cardNum, expDate, cvv;
    JButton save, delete, back;
    JComboBox dropDown;
    String[] creditCards;
    //Card information saved here
    String cardName, cardNumber;
    Calendar expirationDate;
    int savedCVV;
    //Need to set selectedCard to be deleted to the first card in the drop Down
    String selectedCard = "1234";
    
    public PaymentInfoPanel() {
        expirationDate = Calendar.getInstance();
        
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
        save.addActionListener(new ButtonListener("Save"));
        add(save);

        add(new JLabel("___________________________________________________________________________________________________________"));
        add(new JLabel("** DELETE CARD **"));

        //Credit Cards go here
        String[] tempCreditCards = {"1234", "2345"};
        creditCards = tempCreditCards;
        dropDown = new JComboBox(creditCards);
        dropDown.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                selectedCard = (String)dropDown.getSelectedItem();
                System.out.println(selectedCard); //GET RID OF THIS LATER
            }
        });
        add(dropDown);

        delete = new JButton("Delete");
        delete.addActionListener(new ButtonListener("Delete"));
        add(delete);
        add(new JLabel("___________________________________________________________________________________________________________"));
        back = new JButton("Back");
        back.addActionListener(new ButtonListener("CheckDetails"));
        add(back);
    }
    
    public void save() {
        boolean flag1 = false, flag2 = false, flag3 = false;
        cardName = name.getText();
        try {
            long test = Long.parseLong(cardNum.getText());
            cardNumber = cardNum.getText();
            flag1 = true;
        } catch (Exception e) {
            JOptionPane error = new JOptionPane();
            error.showMessageDialog(null, "Please check Card Number.");
        }
        try {
            savedCVV = Integer.parseInt(cvv.getText());
            flag2 = true;
        } catch (Exception e) {
            JOptionPane error = new JOptionPane();
            error.showMessageDialog(null, "Please check CVV.");
        }
        int index, month, year;
        String exDate = expDate.getText();
        try {
            index = exDate.indexOf("/");
            month = Integer.parseInt(exDate.substring(0, index));
            year = Integer.parseInt(exDate.substring(index + 1, exDate.length()));
            expirationDate.set(year, month - 1, 1);
            flag3 = true;
        } catch (Exception e) {
            JOptionPane error = new JOptionPane();
            error.showMessageDialog(null, "Please check Expiration Date.");
        }
        if (flag1 && flag2 && flag3) {
            JOptionPane confirm = new JOptionPane();
            confirm.showMessageDialog(null, "Saved.");
        }
    }
    
    public void delete() {  
        boolean flag = false;
        for (int i = 0; i < creditCards.length; i++) {
            if (selectedCard == creditCards[i] && creditCards[i] != "Deleted") {
                creditCards[i] = "Deleted";
                flag = true;
                //Delete Logic here
            }
        }
        if (flag) {
            JOptionPane confirm = new JOptionPane();
            confirm.showMessageDialog(null, "Deleted.");
        } else {
            JOptionPane confirm = new JOptionPane();
            confirm.showMessageDialog(null, "Item is already deleted.");
        }
    }

    public class ButtonListener implements ActionListener {
        private String state;

        public ButtonListener(String currState) {
            state = currState;
        }

        public void actionPerformed(ActionEvent e) {
            if (state == "CheckDetails") {
                HotelApp.currentState = state;
                HotelApp.checkState();
            } else if (state == "Delete") {
                delete();
            } else if (state == "Save") {
                save();
            }
        }
    }
}