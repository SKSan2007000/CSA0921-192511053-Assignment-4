import java.awt.*;
import java.awt.event.*;

public class DrawPadBad extends Frame {
    int count = 0;
    Label status = new Label("Clicks: 0");

    public DrawPadBad() {
        add(status, BorderLayout.SOUTH);
        addMouseListener(new MouseAdapter() {
            // Deliberately incorrect for the assignment's failing demonstration.
            public void mouseclicked(MouseEvent e) {
                count++;
                Graphics g = getGraphics();
                if (g != null) g.fillOval(e.getX(), e.getY(), 6, 6);
            }
        });
        setSize(400, 300);
    }

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> new DrawPadBad().setVisible(true));
    }
}
