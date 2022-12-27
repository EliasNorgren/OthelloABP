public class Othello{

    public static void main(String[] args) throws IllegalMoveException {
        Algorithm alg = new Algorithm();
        alg.setSearchDepth(1000);
        alg.setEvaluator(new Evaluator());
        alg.TIME_LIMIT = Integer.parseInt(args[2]);

        OthelloPosition pos = new OthelloPosition(args[1]);

        System.out.println(alg.evaluate(pos));
    }
}