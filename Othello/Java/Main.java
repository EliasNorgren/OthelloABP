import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws IllegalMoveException {
        Evaluator eval = new Evaluator();

        Algorithm alg1 = new Algorithm();
        alg1.setEvaluator(new Evaluator());
        alg1.setSearchDepth(12);

        Algorithm alg2 = new Algorithm();
        alg2.setSearchDepth(0);
        alg2.setEvaluator(new Evaluator());

        OthelloPosition p = new OthelloPosition();
        p.initialize();
        p.illustrate();

        Scanner scanner = new Scanner(System.in);
        int i = 0;
        OthelloAction ac;
        p.maxPlayer = !p.maxPlayer;
        while(!p.isFinished()){

            if( p.getMoves().size() == 0){
                i ++;
                p.maxPlayer = !p.maxPlayer;
                continue;
            }

            if(i == 49){
                System.out.println();
            }

            if(i % 2 != 0){
                ac = alg1.evaluate(p);
                System.out.println("AB Prunes : " + alg1.ABPrunes);
                System.out.println("Player 1 playing");

            }else{
//
//                ac = alg2.evaluate(p);
//                System.out.println("AB Prunes : " + alg2.ABPrunes);
//                System.out.println("Player 2 playing");
                String input = scanner.nextLine();
                int row = Integer.parseInt(input.split(" ")[0]);
                int col = Integer.parseInt(input.split(" ")[1]);
                ac = new OthelloAction(row, col);
            }
            p = p.makeMove(ac);

            System.out.println("Doing move (" + ac.row + " , " + ac.column + ")\nIteration: " + i);
            System.out.println("Board value: " + eval.evaluate(p));

            p.illustrate();
            i++;
            //scanner.nextLine();
        }
        System.out.println("Player " + (eval.evaluate(p) > 0 ? "2 won." : "1 won."));
    }
}
