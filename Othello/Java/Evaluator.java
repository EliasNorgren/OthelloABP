
public class Evaluator implements OthelloEvaluator{
    @Override
    public int evaluate(OthelloPosition position) {
        int black = 0, white = 0;
        for (char[] c1 : position.board){
            for(char c2 : c1){
                if(c2 == 'B'){
                    black ++;
                }else if (c2 == 'W'){
                    white ++;
                }
            }
        }
        return white - black;
    }
}
