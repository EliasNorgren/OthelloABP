import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

/**
 * Created by Pierre Lejdbring on 9/11/17.
 */
public class GUI extends JFrame{
    final static Dimension BOARDSIZE = new Dimension(800, 1);
    final static Dimension BUTTONSIZE = new Dimension((int)BOARDSIZE.getWidth()/Utils.ROWS,
            (int)BOARDSIZE.getHeight()/Utils.COLS);

    private JLabel aiStatus;
    private JLabel nodes;
    private JLabel currentPlayer;
    String playerString = "WHITE";

    private String nodeString = "";
    private boolean isMultiplayer = false;

    private OthelloButton[][] buttonGrid;


    public GUI(String name, char[][] board){
        super(name);
//        int choice = JOptionPane.showConfirmDialog(null,
//                "Do you wish to play against a friend on the same computer",
//                "Game option",
//                JOptionPane.YES_NO_OPTION);
//        if(choice == 0) {
//            isMultiplayer = true;
//        }
        isMultiplayer = false;
        setPreferredSize(new Dimension(800,800));
        setResizable(false);
        JPanel outerPanel = new JPanel(new BorderLayout());
        outerPanel.setBackground(Color.BLACK);
        outerPanel.setPreferredSize(new Dimension(800, 800));
//        this.othello = othello;
        buttonGrid = new OthelloButton[Utils.ROWS][Utils.COLS];

        JPanel panel = new JPanel(new GridLayout(Utils.ROWS, Utils.COLS));
        panel.setBackground(new Color(0x0066cc));
        panel.setSize(BOARDSIZE);
        populateButtonPanel(panel, board);
        JPanel statusPanel = new JPanel(new GridLayout(1,2));
        aiStatus = new JLabel("AI status: ");
        nodes = new JLabel("Nodes: " + nodeString);
        aiStatus.setForeground(Color.WHITE);
        nodes.setForeground(Color.WHITE);
        if(!isMultiplayer) {
            statusPanel.add(nodes, 0, 0);
            statusPanel.add(aiStatus,0, 1);
        } else {
            currentPlayer = new JLabel();
            currentPlayer.setText("Current player: " + playerString);
            currentPlayer.setForeground(Color.WHITE);
            statusPanel.add(currentPlayer, 0, 0);
        }
        statusPanel.setBackground(Color.BLACK);
        statusPanel.setPreferredSize(new Dimension(100,100));
        outerPanel.add(panel, BorderLayout.CENTER);
        outerPanel.add(statusPanel, BorderLayout.NORTH);
        add(outerPanel);
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.pack();
        this.setVisible(true);
    }
    public void setNodes(long count) {
        nodeString = String.valueOf(count);
        SwingUtilities.invokeLater(() -> {
            nodes.setText("Nodes: " + nodeString);
        });
    }


    private void populateButtonPanel(JPanel panel, char[][] board) {
        for(int i = 1; i <= Utils.ROWS; ++i){
            for(int j = 1; j <= Utils.COLS; ++j){
                buttonGrid[i-1][j-1] = new OthelloButton(new Coordinate(i,j));
                buttonGrid[i-1][j-1].setPreferredSize(BUTTONSIZE);
                if(board[i][j] == 'B') {
                    buttonGrid[i-1][j-1].setIcon(Chip.getChipInstance('B'));
                    buttonGrid[i-1][j-1].setDisabledIcon(Chip.getChipInstance('B'));
                } else if(board[i][j] == 'W') {
                    buttonGrid[i-1][j-1].setIcon(Chip.getChipInstance('W'));
                    buttonGrid[i-1][j-1].setDisabledIcon(Chip.getChipInstance('W'));
                }
                panel.add(buttonGrid[i-1][j-1]);
            }
        }
    }
    /**
     * Draws the board based on the given state
     *
     */
    public void draw(char[][] board) {
        for(int i = 1; i <= Utils.ROWS; i++) {
            for(int j = 1; j <= Utils.COLS; j++) {
                if(board[i][j] == 'W') {
                    buttonGrid[i-1][j-1].setIcon(Chip.getChipInstance('W'));
                    buttonGrid[i-1][j-1].setDisabledIcon(Chip.getChipInstance('W'));
                    buttonGrid[i-1][j-1].setEnabled(false);
                } else if(board[i][j] == 'B') {
                    buttonGrid[i-1][j-1].setIcon(Chip.getChipInstance('B'));
                    buttonGrid[i-1][j-1].setDisabledIcon(Chip.getChipInstance('B'));
                    buttonGrid[i-1][j-1].setEnabled(false);
                }
            }
        }
    }

    public void disableButtons() {
        for(OthelloButton[] b : buttonGrid) {
            for(OthelloButton ob : b) {
                ob.setEnabled(false);
            }
        }
    }

    public void enableButtons() {
        for(OthelloButton[] b : buttonGrid) {
            for(OthelloButton ob : b) {
                ob.setEnabled(true);
            }
        }
    }

    public void setListeners(ActionListener ae){
        for(OthelloButton[] b : this.buttonGrid){
            for(OthelloButton bb : b){
                bb.addActionListener(ae);
            }
        }
    }

    /**
     * Class to represent the buttons
     */
    public class OthelloButton extends JButton {
        private Coordinate coord;
        protected OthelloButton(Coordinate coord){
            super();
            this.coord = coord;
//            this.addActionListener(ae);
            setBackground(new Color(0, 99, 33));
        }

        Coordinate getCoordinate() {
            return this.coord;
        }
    }

    void setPlayerString(String s) {
        playerString = s;
        currentPlayer.setText("Current player: " + playerString);
    }


}