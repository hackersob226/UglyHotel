import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.sql.*;
import javax.sql.*;

public class HotelApp {
    public static final int WIDTH = 200, HEIGHT = 400;
    public static Connection conn;

    static JPanel layout;
    static String currentState = "Login";
    
    public static void main(String[] args) {
        JFrame frame = new JFrame("Fancy Hotels");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);

        conn = getConnection();
        
        layout = new JPanel(new CardLayout());
        layout.setPreferredSize(new Dimension(WIDTH, HEIGHT));
        layout.add(new LoginPanel(), "Login");
        layout.add(new NewUserPanel(), "NewUser");
        layout.add(new CustomerPanel(), "CustomerView");
        layout.add(new ManagerPanel(), "ManagerView");
        layout.add(new SearchRoomsPanel(), "SearchRooms");
        
        checkState();
        
        frame.getContentPane().add(layout);
        frame.pack();
        frame.setVisible(true);
    }
    
    public static void checkState() {
        CardLayout c = (CardLayout)(layout.getLayout());
        c.show(layout, currentState);
    }
    
    public static Connection getConnection() throws SQLException {
        Connection aConn = null;
        //Properties connectionProps = new Properties();
        //connectionProps.put("user", "cs4400_Group_65");
        //connectionProps.put("password", "Nj7gOvKI");

        //conn = DriverManager.getConnection(
        //            "jdbc:mysql://" +
        //            "PLACEHOLDER" +
        //            ":" + "PLACEHOLDER" + "/",
        //            connectionProps);
        
        System.out.println("Connected to database");
        return conn;
    }
}
