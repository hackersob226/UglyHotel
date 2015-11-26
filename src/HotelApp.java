import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class HotelApp {
    public static final int WIDTH = 200, HEIGHT = 400;

    static JPanel layout;
    static String currentState = "Login";
    
    public static void main(String[] args) {
        JFrame frame = new JFrame("Hotel Application");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
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
}
