import javax.swing.*;
import java.awt.*;

public class UndoButtonView extends JButton{
    private boolean undoEnabled = true; // Tracks the current state
    public abstract void notifyChange(){}
    public UndoButtonView() {
        setPreferredSize(new Dimension(100, 100));
        setFocusPainted(false);
        setBorderPainted(false);
        setContentAreaFilled(false);
    }

    @Override
    public void notifyChange() {
        // Called when the model state changes
        // You can update the view based on the model's state
        undoEnabled = !undoEnabled; // Example state change (customize as needed)
        repaint(); // Repaint to reflect the state change
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;

        g2.setColor(undoEnabled ? Color.BLACK : Color.GRAY);
        g2.setStroke(new BasicStroke(3));

        // Body
        g2.drawLine(30, 50, 70, 50);
        g2.drawLine(70, 50, 70, 80);
        g2.drawLine(70, 80, 35, 80);

        // Arrowhead
        int[] arrowX = {23, 35, 35};
        int[] arrowY = {80, 73, 87};
        g2.fillPolygon(arrowX, arrowY, arrowX.length);

        // Rounded Rectangle
        g2.drawRoundRect(15, 40, 70, 50, 10, 10);
    }
}
