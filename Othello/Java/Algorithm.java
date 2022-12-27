import java.util.HashMap;

public class Algorithm implements OthelloAlgorithm{

    public int ABPrunes;
    public int tabulations;
    private OthelloEvaluator evaluator;

    private int MAX_DEPTH;
    public int TIME_LIMIT = 10000;



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

        long start = System.currentTimeMillis();
        HashMap<OthelloPosition, Integer> table = new HashMap<>();
        int depth = 5;
        OthelloAction bestAction = null;

        if(position.maxPlayer){
            //   maxAction = alphaMax(depth, position.clone(), ab, maxTable, minTable);
            int max = Integer.MIN_VALUE;
            for(int i = 5; i < MAX_DEPTH && (System.currentTimeMillis() - start < 10000); i++){
                for (OthelloAction ac : position.getMoves()) {
                    OthelloPosition pos = position.makeMove(ac);
                    int val = alphaMax(i, pos, ab, table);
                    max = Math.max(max, val);
                    if (val >= max) {
                        bestAction = ac;
                    }
                }
                depth = i;
            }
        }else{
            int min = Integer.MAX_VALUE;
            for(int i = 0; i < MAX_DEPTH && (System.currentTimeMillis() - start < 10000); i++){
                for (OthelloAction ac : position.getMoves()) {
                    OthelloPosition pos = position.makeMove(ac);
                    int val = alphaMin(i, pos, ab, table);
                    min = Math.min(min, val);
                    if (val <= min) {
                        bestAction = ac;
                    }
                }
                depth = i;
            }
        }


        System.out.println("Depth: " + depth);
        System.out.println("Table size: " + table.size());

        if(bestAction == null){
            throw new NullPointerException("MaxAction not found");
        }
        this.ABPrunes = ab.prunes;
        this.tabulations = ab.tabulations;
        return bestAction;
    }

    private int alphaMin(int depth, OthelloPosition position, AlphaBeta ab, HashMap<OthelloPosition, Integer> table) throws IllegalMoveException {
        if(table.containsKey(position)){
            ab.tabulations ++;
            return table.get(position);
        }

        if(depth == 0 || position.isFinished()) {
            int val = this.evaluator.evaluate(position);
            if (Runtime.getRuntime().freeMemory() / Runtime.getRuntime().totalMemory() < 1) {
                table.put(position, val);
            }
            return val;
        }

        int min = Integer.MAX_VALUE;
        for(OthelloAction ac : position.getMoves()){
            OthelloPosition p = position.makeMove(ac);
            int val = alphaMax(depth-1, p, ab, table);

            if( Runtime.getRuntime().freeMemory() / Runtime.getRuntime().totalMemory() < 1){
                table.put(p, val);
            }
            min = Math.min(min, val);

            if(val <= ab.alpha){
                ab.prunes ++;
                break;
            }
            ab.beta = Math.min(ab.beta, val);
        }
        return min;
    }


    private int alphaMax(int depth, OthelloPosition position, AlphaBeta ab, HashMap<OthelloPosition, Integer> table) throws IllegalMoveException {

        if(table.containsKey(position)){
            ab.tabulations ++;
            return table.get(position);
        }

        if(depth == 0 || position.isFinished()){
            int val = this.evaluator.evaluate(position);
            if( Runtime.getRuntime().freeMemory() / Runtime.getRuntime().totalMemory() < 1){
                table.put(position, val);
            }
            return val;
        }

        int max = Integer.MIN_VALUE;
        for(OthelloAction ac : position.getMoves()){
            OthelloPosition p = position.makeMove(ac);
            int val = alphaMin(depth-1, p, ab, table);
            if( Runtime.getRuntime().freeMemory() / Runtime.getRuntime().totalMemory() < 1){
                table.put(p, val);
            }
            max = Math.max(max, val);

            if(val >= ab.beta){
                ab.prunes ++;
                break;
            }
            ab.alpha = Math.max(ab.alpha, val);
        }
        return max;
    }

    @Override
    public void setSearchDepth(int depth) {
        this.MAX_DEPTH = depth;
    }

    private static class AlphaBeta{
        public int alpha = Integer.MIN_VALUE;
        public int beta = Integer.MAX_VALUE;
        public int prunes;
        public int tabulations = 0;
    }
}
