import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws IllegalMoveException {

        Evaluator eval = new Evaluator();

        Algorithm alg1 = new Algorithm();
        alg1.setEvaluator(new Evaluator());
        alg1.setSearchDepth(7);

        Algorithm alg2 = new Algorithm();
        alg2.setSearchDepth(7);
        alg2.setEvaluator(new Evaluator());

        OthelloPosition p = new OthelloPosition();
        p.initialize();
        p.illustrate();
        Scanner scanner = new Scanner(System.in);

        int i = 0;
        OthelloAction ac;
        while(!p.isFinished()){

            if( p.getMoves().size() == 0){
                i ++;
                p.maxPlayer = !p.maxPlayer;
                continue;
            }

            if(i == 20){
              //  System.out.println();
            }

            if(i % 2 == 0){
                ac = alg1.evaluate(p);
            }else{
                //ac = alg2.evaluate(p);
                String input = scanner.nextLine();
                int row = Integer.parseInt(input.split(" ")[0]);
                int col = Integer.parseInt(input.split(" ")[1]);
                ac = new OthelloAction(row, col);
            }
            p = p.makeMove(ac);
            System.out.println("Player " +( i % 2 == 0 ? "0" : "X") + " laying");
            System.out.println("Doing move (" + ac.row + " , " + ac.column + ")\nIteration: " + i);
            System.out.println("Board value: " + eval.evaluate(p));
            p.illustrate();
            i++;
            //scanner.nextLine();
        }
    }
}
