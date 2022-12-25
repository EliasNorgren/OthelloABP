import javax.swing.*;

public class GUIGame {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            OthelloPosition pos = new OthelloPosition();
            pos.initialize();
            GUI gui = new GUI("Game", pos.board);
            Controller controller = new Controller(gui);
        });

    }

}
