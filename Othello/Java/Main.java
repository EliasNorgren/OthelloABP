import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws IllegalMoveException {


        Algorithm alg1 = new Algorithm();
        alg1.setEvaluator(new Evaluator());
        alg1.setSearchDepth(6);

        Algorithm alg2 = new Algorithm();
        alg2.setSearchDepth(6);
        alg2.setEvaluator(new Evaluator());

        OthelloPosition p = new OthelloPosition();
        p.initialize();
        p.illustrate();
        Scanner scanner = new Scanner(System.in);

        int i = 0;
        OthelloAction ac;
        while(!p.isFinished()){

            if(i == 20){
                System.out.println();
            }

            if(i % 2 == 0){
                ac = alg2.evaluate(p);
            }else{
                ac = alg1.evaluate(p);
            }
            System.out.println("Doing move (" + ac.row + " , " + ac.column + ")\nIteration: " + i);
            p = p.makeMove(ac);
            p.illustrate();
            i++;
            //scanner.nextLine();
        }
    }
}
