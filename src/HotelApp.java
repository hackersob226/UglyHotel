import javax.swing.JFrame;
import java.awt.BorderLayout;

public class HotelApp {
    public static void main(String[] args) {
        JFrame frame = new JFrame("Hotel Application");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(new LoginPanel());
        frame.pack();
        frame.setVisible(true);
    }
}
