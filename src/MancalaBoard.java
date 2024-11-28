import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * A concrete MancalaBoard class to initialize and manage the main game window, user controls, and game setup.
 * @author Ahsan Ali, Tuan-Anh Ho
 */
public class MancalaBoard extends JFrame {
    public final int WIDTH = 870;
    public final int HEIGHT = 550;
    private final MancalaView boardView;
    private final MancalaModel mancalaModel;
    private String selectedFormat;
    private String selectedStones;
    private JPanel selectionPanel;

    /**
     * Constructs a new board
     */
    public MancalaBoard() {
        super();
        this.setSize(WIDTH, HEIGHT);
        this.setResizable(false);
        mancalaModel = new MancalaModel(new OakBoardFormat());
        boardView = new MancalaView(mancalaModel);

        // Set up the lower panel with Undo and Restart buttons
        JPanel controlPanel = createControlPanel();

        // Set up the selection panel for choosing board format and marbles
        selectionPanel = createSelectionPanel(controlPanel);

        // Add panels to the main frame
        add(selectionPanel, BorderLayout.SOUTH);
        add(boardView, BorderLayout.CENTER);
        add(controlPanel, BorderLayout.NORTH);

        // Initially hide the control panel
        togglePanelVisibility(controlPanel, false);
    }

    /**
     * Creates the lower control panel with Undo and Restart buttons.
     */
    private JPanel createControlPanel() {
        JPanel controlPanel = new JPanel();
        JButton undoButton = new JButton("Undo");
        undoButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                mancalaModel.undoMove();
            }
        });

        JButton restartButton = new JButton("Restart");
        restartButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (mancalaModel.getState() == GameState.COMPLETE) {
                    setupGame(selectedFormat, Integer.parseInt(selectedStones), controlPanel, selectionPanel);
                }
            }
        });

        controlPanel.add(undoButton);
        controlPanel.add(restartButton);
        return controlPanel;
    }

    /**
     * Creates the selection panel for choosing board format and marbles.
     */
    private JPanel createSelectionPanel(JPanel controlPanel) {
        JPanel selectionPanel = new JPanel();
        JLabel selectionHeader = new JLabel("Select board style and number of stones.");

        selectionPanel.add(selectionHeader);

        // Dropdown menus for board format and stone selection
        JComboBox<String> formatSelector = new JComboBox<>(new String[]{"Select Format", "Flowery Board", "Oak Board"});
        JComboBox<String> stoneSelector = new JComboBox<>(new String[]{"Select Stones", "3", "4"});

        selectionPanel.add(new JLabel("Board Format:"));
        selectionPanel.add(formatSelector);
        selectionPanel.add(new JLabel("Stones Count:"));
        selectionPanel.add(stoneSelector);

        JButton applyButton = new JButton("Start Game");
        selectionPanel.add(applyButton);

        // Action listener for the "Start Game" button
        applyButton.addActionListener(e -> {
            selectedFormat = (String) formatSelector.getSelectedItem();
            selectedStones = (String) stoneSelector.getSelectedItem();

            if (isValidSelection(selectedFormat, selectedStones)) {
                setupGame(selectedFormat, Integer.parseInt(selectedStones), controlPanel, selectionPanel);
            } else {
                JOptionPane.showMessageDialog(this, "Please make valid selections for both options.");
            }
        });

        return selectionPanel;
    }

    /**
     * Validates the user's selections for board format and marble count.
     */
    private boolean isValidSelection(String format, String stones) {
        return (format != null
                && stones != null
                && !format.equals("Select Format")
                && !stones.equals("Select Stones"));
    }

    /**
     * Configures the game based on the user's selections and updates the UI.
     */
    private void setupGame(String boardFormat, int stoneCount, JPanel controlPanel, JPanel selectionPanel) {
        mancalaModel.setStones(stoneCount);
        mancalaModel.setState(GameState.BEGIN);

        if (boardFormat.equals("Flowery Board")) {
            mancalaModel.setFormat(new FloweryFormat());
        }
        else if (boardFormat.equals("Oak Board")) {
            mancalaModel.setFormat(new OakBoardFormat());
        }

        togglePanelVisibility(selectionPanel, false);
        togglePanelVisibility(controlPanel, true);
        boardView.startGame();
    }

    /**
     * Toggles the visibility of a given panel.
     */
    private void togglePanelVisibility(JPanel panel, boolean isVisible) {
        panel.setVisible(isVisible);
    }

    /**
     * Returns the height of this board.
     * @return the height value
     */
    @Override
    public int getWidth() {
        return WIDTH;
    }

    /**
     * Return the width of this board.
     * @return the width value
     */
    @Override
    public int getHeight() {
        return HEIGHT;
    }
}
