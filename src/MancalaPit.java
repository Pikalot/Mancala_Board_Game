import javax.swing.*;
import java.awt.*;
import java.awt.geom.Ellipse2D;

//TEST
public class MancalaPit extends JComponent {
    private int stoneCount;
    private String pitLabel;

    public MancalaPit(String pitLabel, int initialStoneCount) {
        this.pitLabel = pitLabel;
        this.stoneCount = initialStoneCount;
        setPreferredSize(new Dimension(120, 120));  // Adjust size as needed
    }

//    public void setStoneCount(int stoneCount) {
//        this.stoneCount = stoneCount;
//        repaint();  // Redraw the pit to update the stone display
//    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        //Draw pit background
        g2d.setColor(new Color(205, 133, 63));  //Wooden color Brown shade
        g2d.fill(new Ellipse2D.Double(10, 10, getWidth() - 20, getHeight() - 20));

        //Draw pit border
        g2d.setColor(Color.BLACK);
        g2d.draw(new Ellipse2D.Double(10, 10, getWidth() - 20, getHeight() - 20));

        //Draw pit label at the top
        g2d.setColor(Color.BLACK);
        g2d.setFont(new Font("Arial", Font.BOLD, 12));
        FontMetrics fm = g2d.getFontMetrics();
        int labelX = (getWidth() - fm.stringWidth(pitLabel)) / 2;
        g2d.drawString(pitLabel, labelX, 20);

        //Draw stones as small circles inside the pit
        drawStones(g2d);
    }

    private void drawStones(Graphics2D g2d) {
        g2d.setColor(Color.DARK_GRAY);
        int stoneSize = 10;
        int startX = 25, startY = 40;
        int stonesPerRow = 5;
        int row = 0;

        for (int i = 0; i < stoneCount; i++) {
            int x = startX + (i % stonesPerRow) * (stoneSize + 5);
            int y = startY + row * (stoneSize + 5);
            g2d.fillOval(x, y, stoneSize, stoneSize);
            if ((i + 1) % stonesPerRow == 0) {
                row++;
            }
        }
    }
}
