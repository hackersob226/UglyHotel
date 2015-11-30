import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.sql.*;
import javax.sql.*;
import java.util.Properties;
import java.io.*;
import java.util.*;

public class HotelApp {

    public static final int WIDTH = 800, HEIGHT = 300;
    public static Connection con = null;
    public static String dbname = "cs4400_Group_65";

    static JPanel layout, detailPanel;
    static String currentState = "Login"; //Messing around with this

    static Calendar startSearchReserveDate, endSearchReserveDate;
    static Object[][] tempArrayForDetails;

    static int numRows;

    public static void main(String[] args) {
        JFrame frame = new JFrame("Fancy Hotels");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);

        try {
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            con = DriverManager.getConnection("jdbc:mysql://academic-mysql.cc.gatech.edu/cs4400_Group_65", "cs4400_Group_65", "Nj7gOvKI");
            if(!con.isClosed()) {
                System.out.println("Successfully connected to MySQL server...");
            }
        } catch(Exception e) {
            System.err.println("Exception: " + e.getMessage());
        }
        // finally {
        //     try {
        //         if(con != null)
        //             con.close();
        //     } catch(SQLException e) {}
        // }

        layout = new JPanel(new CardLayout());
        layout.setPreferredSize(new Dimension(WIDTH, HEIGHT));
        layout.add(new LoginPanel(), "Login");
        layout.add(new NewUserPanel(), "NewUser");
        layout.add(new CustomerPanel(), "CustomerView");
        layout.add(new ManagerPanel(), "ManagerView");
        layout.add(new SearchRoomsPanel(), "SearchRooms");
        layout.add(new ViewFeedbackPanel(), "ViewFeedback");

        checkState();

        frame.getContentPane().add(layout);
        frame.pack();
        frame.setVisible(true);
    }

    public static void checkState() {
        CardLayout c = (CardLayout)(layout.getLayout());
        c.show(layout, currentState);
    }

    //These have to be added everytime since they're instances of panels
    public static void createReservation() {
        layout.add(new MakeReservationPanel(numRows), "MakeReservation");
    }

    public static void transReservetoDetails() {
        layout.add(new CheckDetailsPanel(tempArrayForDetails, startSearchReserveDate, endSearchReserveDate), "CheckDetails");
    }

    public static void createUpdateReserve() {
        layout.add(new UpdateReservePanel(), "UpdateReserve");
    }

    public static void createAvailRooms() {
        layout.add(new AvailableRoomsPanel(), "AvailableRooms");
    }

    public static void createCancelReserve() {
        layout.add(new CancelReservePanel(), "CancelReserve");
    }

    public static void createCancelRooms(int id) {
        layout.add(new CancelRoomsPanel(id), "CancelRooms");
    }

    public static void goToPaymentInfo() {
        layout.add(new PaymentInfoPanel(), "PaymentInfo");
    }

    public static void goToConfirmation() {
        layout.add(new ConfirmationPanel(), "Confirmation");
    }
    
    public static void createFeedback(String city) {
        layout.add(new CityFeedbackPanel(city), "CityFeedback");
    }
    
    public static void createMakeFeedback() {
        layout.add(new MakeFeedbackPanel(), "MakeFeedback");
    }
}
