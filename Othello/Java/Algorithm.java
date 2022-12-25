import java.util.HashMap;

public class Algorithm implements OthelloAlgorithm{

    public int ABPrunes;
    public int tabulations;
    private OthelloEvaluator evaluator;
    private int searchDepth;

    private final int MAX_DEPTH = 20;

    private HashMap<OthelloPosition, Integer> table = new HashMap<>();

    @Override
    public void setEvaluator(OthelloEvaluator evaluator) {
        this.evaluator = evaluator;
    }

    @Override
    public OthelloAction evaluate(OthelloPosition position) throws IllegalMoveException {

        AlphaBeta ab = new AlphaBeta();
        ABPrunes = 0;
        tabulations = 0;
        int max = Integer.MIN_VALUE;
        OthelloAction maxAction = null;

        long start = System.currentTimeMillis();
        table = new HashMap<>();
        int depth = 0;
        for(int i = 3; i < MAX_DEPTH && System.currentTimeMillis() - start <= 5000; i++){
            for(OthelloAction ac : position.getMoves()){
                OthelloPosition p = position.makeMove(ac);
                int val = miniMax(i, p, ab);
                if(val >= max){
                    max = val;
                    maxAction = ac;
                }
            }
            depth = i;
        }

        System.out.println("Depth: " + depth);
        System.out.println("Table size: " + table.size());
        if(maxAction == null){
            throw new NullPointerException("MaxAction not found");
        }
        this.ABPrunes = ab.prunes;
        this.tabulations = ab.tabulations;
        return maxAction;
    }

    private int miniMax(int depth, OthelloPosition position, AlphaBeta ab) throws IllegalMoveException {

        if(depth == 0 || position.isFinished()){
            int val = this.evaluator.evaluate(position);
            table.put(position, val);
            return val;
        }

        if(table.containsKey(position)){
            ab.tabulations ++;
            return table.get(position);
        }

        if(position.maxPlayer){
            int max = Integer.MIN_VALUE;
            for(OthelloAction ac : position.getMoves()){
                OthelloPosition p = position.makeMove(ac);
                int val = miniMax(depth-1, p, ab);
                table.put(position, val);
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
            table.put(position, val);
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
        public int tabulations = 0;
    }
}
