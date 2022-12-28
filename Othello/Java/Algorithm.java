import java.util.HashMap;

public class Algorithm implements OthelloAlgorithm{

    public int ABPrunes;
    public int tabulations;
    private OthelloEvaluator evaluator;

    private int MAX_DEPTH;
    public int TIME_LIMIT = 10000;
    public int depth;
    public int tableSize;



    @Override
    public void setEvaluator(OthelloEvaluator evaluator) {
        this.evaluator = evaluator;
    }

    @Override
    public OthelloAction evaluate(OthelloPosition position) throws IllegalMoveException {

        System.gc();
        AlphaBeta ab = new AlphaBeta();
        ABPrunes = 0;
        tabulations = 0;
        this.depth = 0;
        this.tableSize = 0;

        long start = System.currentTimeMillis();
        HashMap<OthelloPosition, Integer> table = new HashMap<>();
        OthelloAction bestAction = null;

        //for(int i = 5; i <= MAX_DEPTH && (System.currentTimeMillis() - start < TIME_LIMIT); i++){
            bestAction = position.toMove() ? this.alphaMax(12, position, ab, table, Integer.MIN_VALUE, Integer.MAX_VALUE) : this.alphaMin(12, position, ab, table, Integer.MIN_VALUE, Integer.MAX_VALUE);
          //  this.depth = i;
            //System.out.println(depth);
       // }

        this.tableSize = table.size();

        this.ABPrunes = ab.prunes;
        this.tabulations = ab.tabulations;
        return bestAction;
    }

    private OthelloAction alphaMin(int depth, OthelloPosition position, AlphaBeta ab, HashMap<OthelloPosition, Integer> table,
                         int alpha, int beta) throws IllegalMoveException {
        position.depth = depth;

        if(depth == 0 || position.isFinished()) {
            OthelloAction ac = new OthelloAction(0,0);
            ac.value = this.evaluator.evaluate(position);
//            if ( (float)Runtime.getRuntime().freeMemory() / (float)Runtime.getRuntime().totalMemory() < 0.8) {
//               table.put(position, ac.value);
//            }
            return ac;
        }
//        if(table.containsKey(position)){
//            ab.tabulations ++;
//            OthelloAction temp = new OthelloAction(0,0);
//            temp.value = table.get(position);
//            return temp;
//        }

        int min = Integer.MAX_VALUE;
        OthelloAction bestAction = new OthelloAction(0,0);
        bestAction.value = min;
        for(OthelloAction ac : position.getMoves()){
            OthelloPosition newPos = position.makeMove(ac);

            OthelloAction evaluatedAction = alphaMax(depth-1, newPos, ab, table, alpha, beta);
            if(min > evaluatedAction.value){
                ac.value = evaluatedAction.value;
                min = evaluatedAction.value;
                bestAction = ac;
            }
            if(min <= alpha){
                ab.prunes ++;
                ac.value = min;
                return ac;
            }
            beta = Math.min(beta, min);
        }
        return bestAction;
    }


    private OthelloAction alphaMax(int depth, OthelloPosition position, AlphaBeta ab, HashMap<OthelloPosition, Integer> table,
                         int alpha, int beta) throws IllegalMoveException {

        position.depth = depth;
        if(depth == 0 || position.isFinished()){
            OthelloAction ac = new OthelloAction(0,0);
            ac.value = this.evaluator.evaluate(position);
//            if( (float)Runtime.getRuntime().freeMemory() / (float)Runtime.getRuntime().totalMemory() < 0.8){
//               table.put(position, ac.value);
//            }
            return ac;
        }
//        if(table.containsKey(position)){
//            ab.tabulations ++;
//            OthelloAction temp = new OthelloAction(0,0);
//            temp.value = table.get(position);
//            return temp;
//        }

        int max = Integer.MIN_VALUE;
        OthelloAction bestAction = new OthelloAction(0,0);
        for(OthelloAction ac : position.getMoves()){
            OthelloPosition newPos = position.makeMove(ac);
            OthelloAction evaluatedAction = alphaMin(depth-1, newPos, ab, table, alpha, beta);
            if(max < evaluatedAction.value){
                ac.value = evaluatedAction.value;
                max = evaluatedAction.value;
                bestAction = ac;
            }
            if(max >= beta){
                ab.prunes ++;
                ac.value = max;
                return ac;
            }
            if(max > alpha){
                alpha = max;
            }
        }
        return bestAction;
    }

    @Override
    public void setSearchDepth(int depth) {
        this.MAX_DEPTH = depth;
    }

    private static class AlphaBeta{
//        public int alpha = Integer.MIN_VALUE;
//        public int beta = Integer.MAX_VALUE;
        public int prunes;
        public int tabulations = 0;
    }
}
