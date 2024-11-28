import java.awt.*;

/**
 * The strategy interface in the Strategy design pattern for formatting the Mancala board.
 * This interface defines a strategy for formatting the pit shapes, text font, shape color and board background.
 *
 * @author Tuan-Anh Ho
 * @version 1.0 12/05/2024
 */
public interface FormatStrategy {
    /**
     * Returns the background image to draw.
     * @return an image
     */
    Image backgroundImg();

    /**
     * Sets the shape for a stone pit.
     * @param s a stone pit shape
     */
    void formatShape(StonePit s);

    /**
     * Returns the color for styling.
     * @return a color
     */
    Color getColor();

    /**
     * Returns a text font for styling.
     * @return a text font
     */
    Font getFont();
}