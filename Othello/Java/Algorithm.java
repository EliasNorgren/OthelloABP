public class Algorithm implements OthelloAlgorithm{

    public int ABPrunes;
    private OthelloEvaluator evaluator;
    private int searchDepth;

    @Override
    public void setEvaluator(OthelloEvaluator evaluator) {
        this.evaluator = evaluator;
    }

    @Override
    public OthelloAction evaluate(OthelloPosition position) throws IllegalMoveException {

        AlphaBeta ab = new AlphaBeta();
        ABPrunes = 0;
        int max = Integer.MIN_VALUE;
        OthelloAction maxAction = null;
        for(OthelloAction ac : position.getMoves()){
            OthelloPosition p = position.makeMove(ac);
            int val = miniMax(this.searchDepth, p, ab);
            if(val >= max){
                max = val;
                maxAction = ac;
            }
        }
        if(maxAction == null){
            throw new NullPointerException("MaxAction not found");
        }
        this.ABPrunes = ab.prunes;
        return maxAction;
    }

    private int miniMax(int depth, OthelloPosition position, AlphaBeta ab) throws IllegalMoveException {

        if(depth == 0 || position.isFinished()){
            return this.evaluator.evaluate(position);
        }

        if(position.maxPlayer){
            int max = Integer.MIN_VALUE;
            for(OthelloAction ac : position.getMoves()){
                OthelloPosition p = position.makeMove(ac);
                int val = miniMax(depth-1, p, ab);
                max = Math.max(max, val);

                if(val >= ab.beta){
                    ab.prunes ++;
                    break;
                }
                ab.alpha = Math.max(ab.alpha, val);
            }
            return max;
        }

        // Not max player
        int min = Integer.MAX_VALUE;

        for(OthelloAction ac : position.getMoves()){
            OthelloPosition p = position.makeMove(ac);
            int val = miniMax(depth-1, p, ab);
            min = Math.min(min, val);

            if(val <= ab.alpha){
                ab.prunes ++;
                break;
            }
            ab.beta = Math.min(ab.beta, val);
        }
        return min;
    }

    @Override
    public void setSearchDepth(int depth) {
        this.searchDepth = depth;
    }

    private static class AlphaBeta{
        public int alpha = Integer.MIN_VALUE;
        public int beta = Integer.MAX_VALUE;
        public int prunes;
    }
}
