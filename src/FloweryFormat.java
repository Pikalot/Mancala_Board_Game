import java.awt.*;
import java.awt.geom.*;
import java.net.URL;

/**
 * A concrete strategy class to format the board.
 * A FloweryFormat implements the FormatStrategy interface.
 *
 * @author Anthony Warsah Liu
 * @version 1.0 12/05/2024
 */
public class FloweryFormat implements FormatStrategy {
    /**
     * Sets the ellipse shape for a stone pit.
     * @param s a stone pit shape
     */
    @Override
    public void formatShape(StonePit s) {
        s.setShape(new Ellipse2D.Double(s.getX(), s.getY(), s.getWidth(), s.getHeight()));
    }

    /**
     * Returns the dark blue color.
     * @return a dark blue color
     */
    @Override
    public Color getColor() {
        return new Color(44, 62, 80);
    }

    /**
     * Returns the Roboto text font.
     * @return a Roboto text font, Bold, with size 24
     */
    @Override
    public Font getFont() {
        return new Font("Roboto", Font.BOLD, 24);
    }

    /**
     * Returns the flowery background image to draw.
     * @return a flowery image
     */
    @Override
    public Image backgroundImg() {
        URL imgURL = this.getClass().getResource("/board1.jpg");
        Image img = Toolkit.getDefaultToolkit().getImage(imgURL);
        return img;
    }
}