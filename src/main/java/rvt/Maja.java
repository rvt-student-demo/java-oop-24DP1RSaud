import javax.swing.*;
import java.awt.*;

public class Maja extends JPanel {

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        // 1. Sienas (Taisnstūris)
        g.setColor(new Color(200, 150, 100)); // Brūna krāsa
        g.fillRect(150, 200, 200, 150);
        g.setColor(Color.BLACK);
        g.drawRect(150, 200, 200, 150); // Kontūra

        // 2. Jumts (Trīsstūris)
        int[] xPoints = {150, 250, 350};
        int[] yPoints = {200, 100, 200};
        g.setColor(Color.RED);
        g.fillPolygon(xPoints, yPoints, 3);
        g.setColor(Color.BLACK);
        g.drawPolygon(xPoints, yPoints, 3);

        // 3. Durvis
        g.setColor(new Color(100, 50, 0));
        g.fillRect(230, 280, 40, 70);
        g.setColor(Color.BLACK);
        g.drawRect(230, 280, 40, 70);
        
        // Durvju rokturis
        g.fillOval(260, 315, 5, 5);

        // 4. Logs
        g.setColor(Color.CYAN);
        g.fillRect(170, 230, 40, 40);
        g.setColor(Color.BLACK);
        g.drawRect(170, 230, 40, 40);
        // Loga rāmis
        g.drawLine(190, 230, 190, 270);
        g.drawLine(170, 250, 210, 250);

        // Zāle (tīri noskaņai)
        g.setColor(Color.GREEN);
        g.fillRect(0, 350, 500, 50);
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Mana Java Māja");
        Maja panelis = new Maja();
        
        frame.add(panelis);
        frame.setSize(500, 450);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null); // Centrē logu
        frame.setVisible(true);
    }
}