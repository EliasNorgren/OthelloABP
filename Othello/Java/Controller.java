import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.concurrent.ExecutionException;


public class Controller {

    private final GUI gui;


     OthelloPosition pos = new OthelloPosition();
     Algorithm alg = new Algorithm();

    private final OthelloEvaluator eval = new Evaluator();

    int i = 0;
    public Controller(GUI gui){
        this.gui = gui;
        this.pos.initialize();
        alg.setSearchDepth(12);
        alg.setEvaluator(new Evaluator());
        gui.setListeners(new OthelloListener());
        System.out.println(pos.maxPlayer);
       // pos.illustrate();

    }

    public class OthelloListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            GUI.OthelloButton button = (GUI.OthelloButton)actionEvent.getSource();
            System.out.println(button.getCoordinate());

            try {
                System.out.println("YOU : " + pos.maxPlayer);
                pos = pos.makeMove(new OthelloAction(button.getCoordinate().getRow(), button.getCoordinate().getCol()));
                gui.draw(pos.board.clone());
               // pos.illustrate();
            } catch (IllegalMoveException e) {
                System.out.println("Illegal move");
//                pos.illustrate();
                return;
            }

            Worker w = new Worker();
            w.execute();
            i++;
        }
    }
    class Worker extends SwingWorker<OthelloAction, Integer> {

        //            private final Algorithm alg;
//            private OthelloPosition pos;
//           // private OthelloAction ac;
//
//            public Worker(Algorithm alg, OthelloPosition pos){
//                this.alg = alg;
//                this.pos = pos;
//            }
        @Override
        protected OthelloAction doInBackground() throws Exception {
//            System.out.println("ALG : " + pos.maxPlayer);
            return alg.evaluate(pos);
        }

        @Override
        protected void done() {
            try {
                OthelloAction ac = get();
                pos = pos.makeMove(ac);
                gui.draw(pos.board.clone());


//                pos.illustrate();
//                    System.out.println(pos.maxPlayer);
                System.out.println("AB Prunes : " + alg.ABPrunes);
                System.out.println("Doing move (" + ac.row + " , " + ac.column + ")\nIteration: " + i);
                System.out.println("Board value: " + eval.evaluate(pos));
                System.out.println("Tabulations: " + alg.tabulations);

            } catch (IllegalMoveException | ExecutionException | InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
