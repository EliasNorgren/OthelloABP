//import java.util.LinkedList;
//import java.util.ListIterator;
//
//public class test {
//        protected OthelloAction evaluateMax(OthelloPosition pos, int depth, int alpha, int beta) {
//            int max = Integer.MIN_VALUE;
//            LinkedList moves = pos.getMoves();
//            ListIterator moveIter = moves.listIterator();
//            OthelloAction var8;
//            if (depth == 0) {
//                var8 = new OthelloAction(0, 0);
//                var8.value = this.evaluator.evaluate(pos);
//                return var8;
//            } else if (!moveIter.hasNext()) {
//                var8 = new OthelloAction(0, 0, true);
//                var8.value = this.evaluator.evaluate(pos);
//                return var8;
//            } else {
//                OthelloAction var9 = new OthelloAction(0, 0);
//
//                while(moveIter.hasNext()) {
//                    var8 = (OthelloAction)moveIter.next();
//                    OthelloPosition var11 = pos.makeMove(var8);
//                    OthelloAction var10 = this.evaluateMin(var11, depth - 1, alpha, beta);
//                    if (max < var10.value) {
//                        var8.value = max = var10.value;
//                        var9 = var8;
//                    }
//
//                    if (max >= beta) {
//                        var8.value = max;
//                        return var8;
//                    }
//
//                    if (max > alpha) {
//                        alpha = max;
//                    }
//                }
//
//                return var9;
//            }
//        }
