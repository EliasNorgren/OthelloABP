//import java.util.LinkedList;
//import java.util.ListIterator;
//
//public class test {
//        protected OthelloAction evaluateMax(OthelloPosition pos, int depth, int alpha, int beta) {
//            int max = Integer.MIN_VALUE;
//            LinkedList moves = pos.getMoves();
//            ListIterator moveIter = moves.listIterator();
//            OthelloAction ac;
//            if (depth == 0) {
//                ac = new OthelloAction(0, 0);
//                ac.value = this.evaluator.evaluate(pos);
//                return ac;
//            } else if (!moveIter.hasNext()) {
//                ac = new OthelloAction(0, 0, true);
//                ac.value = this.evaluator.evaluate(pos);
//                return ac;
//            } else {
//                OthelloAction returnAction = new OthelloAction(0, 0);
//
//                while(moveIter.hasNext()) {
//                    ac = (OthelloAction)moveIter.next();
//                    OthelloPosition newPos = pos.makeMove(ac);
//                    OthelloAction evaluatedAction = this.evaluateMin(newPos, depth - 1, alpha, beta);
//                    if (max < evaluatedAction.value) {
//                        ac.value = max = evaluatedAction.value;
//                        returnAction = ac;
//                    }
//                    if (max >= beta) {
//                        ac.value = max;
//                        return ac;
//                    }
//                    if (max > alpha) {
//                        alpha = max;
//                    }
//                }
//
//                return returnAction;
//            }
//        }
//
//    protected OthelloAction evaluateMin(OthelloPosition pos, int depth, int alpha, int beta) {
//        int min = Integer.MAX_VALUE;
//        LinkedList var6 = pos.getMoves();
//        ListIterator var7 = var6.listIterator();
//        OthelloAction ac;
//        if (depth == 0) {
//            ac = new OthelloAction(0, 0);
//            ac.value = this.evaluator.evaluate(pos);
//            return ac;
//        } else if (!var7.hasNext()) {
//            ac = new OthelloAction(0, 0, true);
//            ac.value = this.evaluator.evaluate(pos);
//            return ac;
//        } else {
//            OthelloAction var9 = new OthelloAction(0, 0);
//            var6 = pos.getMoves();
//            var7 = var6.listIterator();
//
//            while(var7.hasNext()) {
//                ac = (OthelloAction)var7.next();
//                OthelloPosition var11 = pos.makeMove(ac);
//                OthelloAction var10 = this.evaluateMax(var11, depth - 1, alpha, beta);
//                if (min > var10.value) {
//                    ac.value = min = var10.value;
//                    var9 = ac;
//                }
//
//                if (min <= alpha) {
//                    ac.value = min;
//                    return ac;
//                }
//
//                if (min < beta) {
//                    beta = min;
//                }
//            }
//
//            return var9;
//        }
//    }