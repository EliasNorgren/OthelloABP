public class Algorithm implements OthelloAlgorithm{

    private OthelloEvaluator evaluator;
    private int searchDepth;

    @Override
    public void setEvaluator(OthelloEvaluator evaluator) {
        this.evaluator = evaluator;
    }

    @Override
    public OthelloAction evaluate(OthelloPosition position) throws IllegalMoveException {

        int max = Integer.MIN_VALUE;
        OthelloAction maxAction = null;
        for(OthelloAction ac : position.getMoves()){
            OthelloPosition p = position.makeMove(ac);
            int val = miniMax(this.searchDepth, p);
            if(val > max){
                max = val;
                maxAction = ac;
            }
        }
        if(maxAction == null){
            throw new NullPointerException("MaxAction not found");
        }
        return maxAction;
    }

    private int miniMax(int depth, OthelloPosition position) throws IllegalMoveException {

        if(depth == 0){
            return this.evaluator.evaluate(position);
        }

        if(position.maxPlayer){
            int max = Integer.MIN_VALUE;
            for(OthelloAction ac : position.getMoves()){
                OthelloPosition p = position.makeMove(ac);
                int val = miniMax(depth-1, p);
                max = Math.max(max, val);
            }
            return max;
        }

        // Not max player
        int min = Integer.MAX_VALUE;

        for(OthelloAction ac : position.getMoves()){
            OthelloPosition p = position.makeMove(ac);
            int val = miniMax(depth-1, p);
            min = Math.min(min, val);
        }
        return min;
    }

    @Override
    public void setSearchDepth(int depth) {
        this.searchDepth = depth;
    }
}
