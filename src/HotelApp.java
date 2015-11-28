import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.sql.*;
import javax.sql.*;
import java.util.Properties;

public class HotelApp {
<<<<<<< HEAD
    public static final int WIDTH = 200, HEIGHT = 400;
    public static Connection con = null;
    public static String dbname = "cs4400_Group_65";
=======
    public static final int WIDTH = 800, HEIGHT = 300;
    public static Connection conn;
>>>>>>> origin/master

    static JPanel layout;
    static String currentState = "Login";
    
    public static void main(String[] args) {
        JFrame frame = new JFrame("Fancy Hotels");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);

        try { 
            Class.forName("com.mysql.jdbc.Driver").newInstance(); 
            con = DriverManager.getConnection("jdbc:mysql://academic-mysql.cc.gatech.edu/cs4400_Group_65", "cs4400_Group_65", "Nj7gOvKI"); 
            if(!con.isClosed()) 
                System.out.println("Successfully connected to MySQL server..."); 
        } catch(Exception e) { 
            System.err.println("Exception: " + e.getMessage()); 
        } finally { 
            try { 
                if(con != null) 
                    con.close(); 
            } catch(SQLException e) {} 
        }
        
        layout = new JPanel(new CardLayout());
        layout.setPreferredSize(new Dimension(WIDTH, HEIGHT));
        layout.add(new LoginPanel(), "Login");
        layout.add(new NewUserPanel(), "NewUser");
        layout.add(new CustomerPanel(), "CustomerView");
        layout.add(new ManagerPanel(), "ManagerView");
        layout.add(new SearchRoomsPanel(), "SearchRooms");
        layout.add(new MakeReservationPanel(), "MakeReservation");
        layout.add(new CheckDetailsPanel(), "CheckDetails");
        
        checkState();
        
        frame.getContentPane().add(layout);
        frame.pack();
        frame.setVisible(true);
    }
    
    public static void checkState() {
        CardLayout c = (CardLayout)(layout.getLayout());
        c.show(layout, currentState);
    }
}
