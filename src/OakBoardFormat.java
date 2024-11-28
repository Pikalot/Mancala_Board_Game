import java.awt.*;
import java.awt.geom.*;
import java.net.URL;

public class OakBoardFormat implements FormatStrategy {

    @Override
    public void formatShape(StonePit s) {
        s.setShape(new Rectangle2D.Double(s.getX(), s.getY(), s.getWidth(), s.getHeight()));
    }

    @Override
    public Color getColor() {
        return Color.WHITE;
    }

    @Override
    public Font getFont() {
        return new Font("Arial", Font.BOLD, 24);
    }

    @Override
    public Image backgroundImg() {
        URL imgURL = this.getClass().getResource("/board2.jpg");
        Image img = Toolkit.getDefaultToolkit().getImage(imgURL);
        return img;
    }
}