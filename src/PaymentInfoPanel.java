import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;
import java.sql.*;
import javax.sql.*;
import java.time.*;
import java.time.format.DateTimeFormatter;

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
        save.addActionListener(new SaveListener("Save"));
        add(save);

        add(new JLabel("___________________________________________________________________________________________________________"));
        add(new JLabel("** DELETE CARD **"));

        //Credit Cards go here
        // try {
        //     removeCard(HotelApp.con, HotelApp.dbname, LoginPanel.sessionUserName, selectedCard);
        // } catch (SQLException ex) {
        //     System.out.println(ex.getMessage());
        // }
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
        delete.addActionListener(new DeleteListener("Delete"));
        add(delete);
        add(new JLabel("___________________________________________________________________________________________________________"));
        back = new JButton("Back");
        back.addActionListener(new BackListener("CheckDetails"));
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

    public class BackListener implements ActionListener {
        private String state;

        public BackListener(String currState) {
            state = currState;
        }

        public void actionPerformed(ActionEvent e) {
            state = "CheckDetails";
            HotelApp.currentState = state;
            HotelApp.checkState();
        }
    }

    public class DeleteListener implements ActionListener {
        private String state;

        public DeleteListener(String currState) {
            state = currState;
        }

        public void actionPerformed(ActionEvent e) {
            try {
                removeCard(HotelApp.con, HotelApp.dbname, LoginPanel.sessionUserName, selectedCard);
            } catch (SQLException ex) {
                System.out.println(ex.getMessage());
            }
        }
    }

    public class SaveListener implements ActionListener {
        private String state;

        public SaveListener(String currState) {
            state = currState;
        }

        public void actionPerformed(ActionEvent e) {
            String expireDate = "01/" + expDate.getText();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy", Locale.ENGLISH);
            LocalDate date = LocalDate.parse(expireDate, formatter);
            expireDate = date.toString();
            System.out.println(expireDate);
            try {
                addCard(HotelApp.con, HotelApp.dbname, LoginPanel.sessionUserName, cardNum.getText(), name.getText(), cvv.getText(), expireDate);
            } catch (SQLException ex) {
                System.out.println(ex.getMessage());
            }
        }
    }

    public int populateCards(Connection con, String dbName, String userName) throws SQLException {
        Statement stmt = null;
        String query = "SELECT CardNum FROM PAYMENTINFORMATION WHERE PAYMENTINFORMATION.Username = \"" + userName + "\"";
        try {
            stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            ArrayList<String> tempList = new ArrayList();

            while(rs.next())
            {
                tempList.add(rs.getString("CardNum"));
            }

            creditCards = tempList.toArray(creditCards);

            return 1;
        } catch (SQLException e ) {
            System.out.println(e.getMessage());
        }

        return 0;
    }

    public int addCard(Connection con, String dbName, String username, String saveCard, String name, String addCVV, String expiryDate) throws SQLException {
        PreparedStatement stmt = null;
        String query = "INSERT INTO PAYMENTINFORMATION VALUES (\"" + saveCard + "\", \"" + name + "\", \"" + expiryDate + "\", \"" + addCVV + "\", \"" + username + "\")";
        try {
            con.setAutoCommit(false);
            stmt = con.prepareStatement(query);
            stmt.executeUpdate();
            con.commit();

            return 1;
        } catch (SQLException e ) {
            System.out.println(e.getMessage());
        }

        return 0;
    }

    public int removeCard(Connection con, String dbName, String username, String deleteCard) throws SQLException {
        PreparedStatement stmt = null;
        String query = "DELETE FROM PAYMENTINFORMATION WHERE PAYMENTINFORMATION.Username = \"" + username +"\" AND PAYMENTINFORMATION.CardNum = \"" + deleteCard + "\"";
        try {
            con.setAutoCommit(false);
            stmt = con.prepareStatement(query);
            stmt.executeUpdate();
            con.commit();

            return 1;
        } catch (SQLException e ) {
            System.out.println(e.getMessage());
        }

        return 0;
    }
}