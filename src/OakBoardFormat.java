import java.awt.*;
import java.awt.geom.*;
import java.net.URL;
/**
 * A concrete strategy class to format the board.
 * An OakBoardFormat implements the BoardFormat interface to define
 * the board's pit shapes and background image.
 *
 * @author Anthony Warsah Liu
 * @version 1.0 11/18/2024
 */
public class OakBoardFormat implements FormatStrategy {
    /**
     * Sets the rectangle shape for a stone pit.
     *
     * @param s a pit shape object containing the dimensions and position of the pit
     */
    @Override
    public void formatShape(StonePit s) {
        s.setShape(new Rectangle2D.Double(s.getX(), s.getY(), s.getWidth(), s.getHeight()));
    }
    /**
     * Returns the dark blue color.
     * @return a white color
     */
    @Override
    public Color getColor() {
        return Color.WHITE;
    }
    /**
     * Returns the Arial text font.
     * @return a Arial text font, Bold, with size 24
     */
    @Override
    public Font getFont() {
        return new Font("Arial", Font.BOLD, 24);
    }
    /**
     * Returns the oak-themed background image to draw.
     *
     * @return an image representing the oak board background
     */
    @Override
    public Image backgroundImg() {
        URL imgURL = this.getClass().getResource("/board2.jpg");
        return Toolkit.getDefaultToolkit().getImage(imgURL);
    }
}