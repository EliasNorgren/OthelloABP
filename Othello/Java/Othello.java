public class Othello{

    public static void main(String[] args) throws IllegalMoveException {
        Algorithm alg = new Algorithm();
        alg.setSearchDepth(8);
        alg.setEvaluator(new Evaluator());
        alg.TIME_LIMIT = Integer.parseInt(args[1]);

        OthelloPosition pos = new OthelloPosition(args[0]);
       // System.out.println(args[0] + " - " + args[1]);
        OthelloAction ac = alg.evaluate(pos);
        System.out.println((ac == null || (ac.row == 0 && ac.column == 0)) ? "pass" : ac);
    }
}