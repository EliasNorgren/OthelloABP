import java.util.*;
import java.lang.*;

/**
 * This class is used to represent game positions. It uses a 2-dimensional char
 * array for the board and a Boolean to keep track of which player has the move.
 * 
 * @author Henrik Bj&ouml;rklund
 */

public class OthelloPosition {

    /** For a normal Othello game, BOARD_SIZE is 8. */
    protected static final int BOARD_SIZE = 8;

    /** True if the first player (white) has the move. */
    protected boolean maxPlayer;

    public int value;

    public int depth;

    /**
     * The representation of the board. For convenience, the array actually has two
     * columns and two rows more that the actual game board. The 'middle' is used
     * for the board. The first index is for rows, and the second for columns. This
     * means that for a standard 8x8 game board, <code>board[1][1]</code> represents
     * the upper left corner, <code>board[1][8]</code> the upper right corner,
     * <code>board[8][1]</code> the lower left corner, and <code>board[8][8]</code>
     * the lower left corner. In the array, the charachters 'E', 'W', and 'B' are
     * used to represent empty, white, and black board squares, respectively.
     */
    protected char[][] board;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OthelloPosition that = (OthelloPosition) o;
        return maxPlayer == that.maxPlayer && depth == that.depth && Arrays.deepEquals(board, that.board);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(maxPlayer, depth);
        result = 31 * result + Arrays.deepHashCode(board);
        return result;
    }

    /** Creates a new position and sets all squares to empty. */
    public OthelloPosition() {
        board = new char[BOARD_SIZE + 2][BOARD_SIZE + 2];
        for (int i = 0; i < BOARD_SIZE + 2; i++)
            for (int j = 0; j < BOARD_SIZE + 2; j++)
                board[i][j] = 'E';

    }

    public OthelloPosition(String s) {
        if (s.length() != 65) {
            board = new char[BOARD_SIZE + 2][BOARD_SIZE + 2];
            for (int i = 0; i < BOARD_SIZE + 2; i++)
                for (int j = 0; j < BOARD_SIZE + 2; j++)
                    board[i][j] = 'E';
        } else {
            board = new char[BOARD_SIZE + 2][BOARD_SIZE + 2];
            if (s.charAt(0) == 'W') {
                maxPlayer = true;
            } else {
                maxPlayer = false;
            }
            for (int i = 1; i <= 64; i++) {
                char c;
                if (s.charAt(i) == 'E') {
                    c = 'E';
                } else if (s.charAt(i) == 'O') {
                    c = 'W';
                } else {
                    c = 'B';
                }
                int column = ((i - 1) % 8) + 1;
                int row = (i - 1) / 8 + 1;
                board[row][column] = c;
            }
        }

    }

    /**
     * Initializes the position by placing four markers in the middle of the board.
     */
    public void initialize() {
        board[BOARD_SIZE / 2][BOARD_SIZE / 2] = board[BOARD_SIZE / 2 + 1][BOARD_SIZE / 2 + 1] = 'W';
        board[BOARD_SIZE / 2][BOARD_SIZE / 2 + 1] = board[BOARD_SIZE / 2 + 1][BOARD_SIZE / 2] = 'B';
        maxPlayer = true;
    }

    /* getMoves and helper functions */

    /**
     * Returns a linked list of <code>OthelloAction</code> representing all possible
     * moves in the position. If the list is empty, there are no legal moves for the
     * player who has the move.
     */
    public LinkedList<OthelloAction> getMoves() {
        boolean[][] candidates = new boolean[BOARD_SIZE][BOARD_SIZE];
        LinkedList<OthelloAction> moves = new LinkedList<OthelloAction>();
        for (int i = 0; i < BOARD_SIZE; i++)
            for (int j = 0; j < BOARD_SIZE; j++)
                candidates[i][j] = isCandidate(i + 1, j + 1);
        for (int i = 0; i < BOARD_SIZE; i++)
            for (int j = 0; j < BOARD_SIZE; j++)
                if (candidates[i][j])
                    if (isMove(i + 1, j + 1))
                        moves.add(new OthelloAction(i + 1, j + 1));
        return moves;
    }

    /**
     * Check if it is possible to do a move from this position
     */
    private boolean isMove(int row, int column) {
        if (checkNorth(row, column))
            return true;
        if (checkNorthEast(row, column))
            return true;
        if (checkEast(row, column))
            return true;
        if (checkSouthEast(row, column))
            return true;
        if (checkSouth(row, column))
            return true;
        if (checkSouthWest(row, column))
            return true;
        if (checkWest(row, column))
            return true;
        if (checkNorthWest(row, column))
            return true;

        return false;
    }

    /**
     * Check if it is possible to do a move to the north from this position
     */
    private boolean checkNorth(int row, int column) {
        if (!isOpponentSquare(row - 1, column))
            return false;
        for (int i = row - 2; i > 0; i--) {
            if (isFree(i, column))
                return false;
            if (isOwnSquare(i, column))
                return true;
        }
        return false;
    }

    /**
     * Check if it is possible to do a move to the east from this position
     */
    private boolean checkEast(int row, int column) {
        if (!isOpponentSquare(row, column + 1))
            return false;
        for (int i = column + 2; i <= BOARD_SIZE; i++) {
            if (isFree(row, i))
                return false;
            if (isOwnSquare(row, i))
                return true;
        }
        return false;
    }

    /**
     * Check if it is possible to do a move to the south from this position
     */
    private boolean checkSouth(int row, int column) {
        if (!isOpponentSquare(row + 1, column))
            return false;
        for (int i = row + 2; i <= BOARD_SIZE; i++) {
            if (isFree(i, column))
                return false;
            if (isOwnSquare(i, column))
                return true;
        }
        return false;
    }

    /**
     * Check if it is possible to do a move to the west from this position
     */
    private boolean checkWest(int row, int column) {
        if (!isOpponentSquare(row, column - 1))
            return false;
        for (int i = column - 2; i > 0; i--) {
            if (isFree(row, i))
                return false;
            if (isOwnSquare(row, i))
                return true;
        }
        return false;
    }

    /**
     * Check if it is possible to do a move to the northest from this position
     */
    private boolean checkNorthEast(int row, int column) {
        if (!isOpponentSquare(row - 1, column + 1))
            return false;
        for (int i = 2; row - i > 0 && column + i <= BOARD_SIZE; i++) {
            if (isFree(row - i, column + i))
                return false;
            if (isOwnSquare(row - i, column + i))
                return true;
        }
        return false;
    }

    /**
     * Check if it is possible to do a move to the southeast from this position
     */
    private boolean checkSouthEast(int row, int column) {
        if (!isOpponentSquare(row + 1, column + 1))
            return false;
        for (int i = 2; row + i <= BOARD_SIZE && column + i <= BOARD_SIZE; i++) {
            if (isFree(row + i, column + i))
                return false;
            if (isOwnSquare(row + i, column + i))
                return true;
        }
        return false;
    }

    /**
     * Check if it is possible to do a move to the soutwest from this position
     */
    private boolean checkSouthWest(int row, int column) {
        if (!isOpponentSquare(row + 1, column - 1))
            return false;
        for (int i = 2; row + i <= BOARD_SIZE && column - i > 0; i++) {
            if (isFree(row + i, column - i))
                return false;
            if (isOwnSquare(row + i, column - i))
                return true;
        }
        return false;
    }

    /**
     * Check if it is possible to do a move to the northwest from this position
     */
    private boolean checkNorthWest(int row, int column) {
        if (!isOpponentSquare(row - 1, column - 1))
            return false;
        for (int i = 2; row - i > 0 && column - i > 0; i++) {
            if (isFree(row - i, column - i))
                return false;
            if (isOwnSquare(row - i, column - i))
                return true;
        }
        return false;
    }

    /**
     * Check if the position is occupied by the opponent
     */
    private boolean isOpponentSquare(int row, int column) {
        if (maxPlayer && (board[row][column] == 'B'))
            return true;
        if (!maxPlayer && (board[row][column] == 'W'))
            return true;
        return false;
    }

    /**
     * Check if the position is occupied by the player
     */
    private boolean isOwnSquare(int row, int column) {
        if (!maxPlayer && (board[row][column] == 'B'))
            return true;
        if (maxPlayer && (board[row][column] == 'W'))
            return true;
        return false;
    }

    /**
     * Check if the position is a candidate for a move (not empty and has a
     * neighbour)
     * 
     * @return true if it is a candidate
     */
    private boolean isCandidate(int row, int column) {
        if (!isFree(row, column))
            return false;
        if (hasNeighbor(row, column))
            return true;
        return false;
    }

    /**
     * Check if the position has any non-empty squares
     * 
     * @return true if is has any neighbours
     */
    private boolean hasNeighbor(int row, int column) {
        if (!isFree(row - 1, column))
            return true;
        if (!isFree(row - 1, column + 1))
            return true;
        if (!isFree(row, column + 1))
            return true;
        if (!isFree(row + 1, column + 1))
            return true;
        if (!isFree(row + 1, column))
            return true;
        if (!isFree(row + 1, column - 1))
            return true;
        if (!isFree(row, column - 1))
            return true;
        if (!isFree(row - 1, column - 1))
            return true;
        return false;
    }

    /**
     * Check if the position is free/empty
     */
    private boolean isFree(int row, int column) {
        return board[row][column] == 'E';
    }

    /* toMove */

    /** Returns true if the first player (white) has the move, otherwise false. */
    public boolean toMove() {
        return maxPlayer;
    }

    /* makeMove and helper functions */

    /**
     * Returns the position resulting from making the move <code>action</code> in
     * the current position. Observe that this also changes the player to move next.
     */
    public OthelloPosition makeMove(OthelloAction action) throws IllegalMoveException {

        /*
         * TODO: write the code for this method and whatever helper functions it needs.
         */




        if(action.isPassMove()){
            this.maxPlayer = !this.maxPlayer;
            return clone();
        }
        OthelloPosition pos = this.clone();
        char marker = this.maxPlayer ? 'W' : 'B';
        pos.board[action.row][action.column] = marker;

        LinkedList<OthelloAction> actions = this.getMoves();
        if(!actions.contains(action)){
            System.out.println("Not in make move " + action);
            throw new IllegalMoveException(action);
        }

        if(!pos.hasNeighbor(action.row, action.column)){
            throw new IllegalMoveException(action);
        }

        boolean eligibleSomeDir = false;

        if(eligibleSouth(pos, action)) {
            int row = action.row + 1;
            while (row <= 8 && this.isOpponentSquare(row, action.column)) {
                pos.board[row][action.column] = marker;
                row++;
            }
            eligibleSomeDir = true;
        }

        if(eligibleSouthWest(pos, action)){
            int row = action.row + 1;
            int col = action.column - 1;
            while(row <= 8 && col >= 1 && pos.isOpponentSquare(row, col)){
                pos.board[row][col] = marker;
                row ++;
                col --;
            }
            eligibleSomeDir = true;
        }

        if(eligibleSouthEast(pos, action)){
            int row = action.row + 1;
            int col = action.column + 1;
            while(row <= 8 && col <= 8 && pos.isOpponentSquare(row, col)){
                pos.board[row][col] = marker;
                row ++;
                col ++;
            }
            eligibleSomeDir = true;
        }

        if(eligibleEast(pos, action)){
            int col = action.column + 1;
            while(col <= 8 && pos.isOpponentSquare(action.row, col)){
                pos.board[action.row][col] = marker;
                col ++;
            }
            eligibleSomeDir = true;
        }

        if(eligibleNorthEast(pos, action)){
            int row = action.row - 1;
            int col = action.column + 1;
            while(row >= 1 && col <= 8 && pos.isOpponentSquare(row, col)){
                pos.board[row][col] = marker;
                row --;
                col ++;
            }
            eligibleSomeDir = true;
        }

        if(eligibleWest(pos, action)){
            int col = action.column - 1;
            while(col >= 1 && pos.isOpponentSquare(action.row, col)){
                pos.board[action.row][col] = marker;
                col --;
            }
            eligibleSomeDir = true;
        }

        if(eligibleNorthWest(pos, action)){
            int row = action.row - 1;
            int col = action.column - 1;
            while(row >= 1 && col >= 1 && pos.isOpponentSquare(row, col)){
                pos.board[row][col] = marker;
                row --;
                col --;
            }
            eligibleSomeDir = true;
        }

        if(eligibleNorth(pos, action)){
            int row = action.row - 1;
            while(row >= 1 && pos.isOpponentSquare(row, action.column)){
                pos.board[row][action.column] = marker;
                row --;
            }
            eligibleSomeDir = true;
        }
        if (!eligibleSomeDir){
            throw new IllegalMoveException(action);
        }
        pos.maxPlayer = !pos.maxPlayer;
        return pos;
    }

    private boolean eligibleNorth(OthelloPosition pos, OthelloAction action) {
        int row = action.row - 1;
        if(row >= 1 && pos.isOwnSquare(row, action.column) && !pos.isFree(row, action.column)){
            return false;
        }
        while(row >= 1){
            if(pos.isOwnSquare(row, action.column)){
                return true;
            }
            row --;
        }
        return false;
    }

    private boolean eligibleNorthWest(OthelloPosition pos, OthelloAction action) {
        int row = action.row - 1;
        int col = action.column - 1;
        if(row >= 1 && col >= 1 && pos.isOwnSquare(row, col) && !pos.isFree(row, col)){
            return false;
        }
        while(row >= 1 && col >= 1){
            if(pos.isOwnSquare(row, col)){
                return true;
            }
            row --;
            col --;
        }
        return false;
    }

    private boolean eligibleWest(OthelloPosition pos, OthelloAction action) {
        int col = action.column - 1;
        if(col >= 1 && pos.isOwnSquare(action.row, col) && !pos.isFree(action.row, col)){
            return false;
        }
        for(int c = col; c >= 1; c --){
            if(pos.isOwnSquare(action.row, c)){
                return true;
            }
        }
        return false;
    }

    private boolean eligibleNorthEast(OthelloPosition pos, OthelloAction action) {
        int row = action.row - 1;
        int col = action.column + 1;
        if(row >= 1 && col <= 8 && pos.isOwnSquare(row, col)  && !pos.isFree(row, col)){
            return false;
        }
        while(row >= 1 && col <= 8){
            if(pos.isOwnSquare(row, col)){
                return true;
            }
            row --;
            col ++;
        }
        return false;
    }

    private boolean eligibleEast(OthelloPosition pos, OthelloAction action) {
        int col = action.column + 1;
        if(col <= 8 && pos.isOwnSquare(action.row, col) && !pos.isFree(action.row, col)){
            return false;
        }
        for(int c = col; c <= 8; c++){
            if(pos.isOwnSquare(action.row, c)){
                return true;
            }
        }
        return false;
    }

    private boolean eligibleSouthEast(OthelloPosition pos, OthelloAction action) {
        int row = action.row + 1;
        int col = action.column + 1;

        if(row <= 8 && col <= 8 && pos.isOwnSquare(row, col) && !pos.isFree(row, col)){
            return false;
        }

        while(row <= 8 && col <= 8){
            if(pos.isOwnSquare(row, col)){
                return true;
            }
            row ++;
            col ++;
        }
        return false;
    }

    private boolean eligibleSouthWest(OthelloPosition pos, OthelloAction action) {
        int row = action.row + 1;
        int col = action.column - 1;
        if(row <= 8 && col >= 1 && pos.isOwnSquare(row, col) && !pos.isFree(row, col)){
            return false;
        }
        while(row <= 8 && col >= 1){
            if(pos.isOwnSquare(row, col)){
                return true;
            }
            row ++;
            col --;
        }
        return false;
    }

    private boolean eligibleSouth(OthelloPosition pos, OthelloAction action) {
        int row = action.row + 1;
        if(row <= 8 && pos.isOwnSquare(row, action.column) && !pos.isFree(row, action.column)){
            return false;
        }
        for(int r = row; r <= 8; r++){
            if(isOwnSquare(r, action.column)){
                return true;
            }
        }
        return false;
    }

    /**
     * Returns a new <code>OthelloPosition</code>, identical to the current one.
     */
    protected OthelloPosition clone() {
        OthelloPosition newPosition = new OthelloPosition();
        newPosition.maxPlayer = maxPlayer;
        for (int i = 0; i < BOARD_SIZE + 2; i++)
            for (int j = 0; j < BOARD_SIZE + 2; j++)
                newPosition.board[i][j] = board[i][j];
        return newPosition;
    }

    /* illustrate and other output functions */

    /**
     * Draws an ASCII representation of the position. White squares are marked by
     * '0' while black squares are marked by 'X'.
     */
    public void illustrate() {
        System.out.print("   ");
        for (int i = 1; i <= BOARD_SIZE; i++)
            System.out.print("| " + i + " ");
        System.out.println("|");
        printHorizontalBorder();
        for (int i = 1; i <= BOARD_SIZE; i++) {
            System.out.print(" " + i + " ");
            for (int j = 1; j <= BOARD_SIZE; j++) {
                if (board[i][j] == 'W') {
                    System.out.print("| 0 ");
                } else if (board[i][j] == 'B') {
                    System.out.print("| X ");
                } else {
                    System.out.print("|   ");
                }
            }
            System.out.println("| " + i + " ");
            printHorizontalBorder();
        }
        System.out.print("   ");
        for (int i = 1; i <= BOARD_SIZE; i++)
            System.out.print("| " + i + " ");
        System.out.println("|\n");
    }

    private void printHorizontalBorder() {
        System.out.print("---");
        for (int i = 1; i <= BOARD_SIZE; i++) {
            System.out.print("|---");
        }
        System.out.println("|---");
    }

    public String toString() {
        String s = "";
        char c, d;
        if (maxPlayer) {
            s += "W";
        } else {
            s += "B";
        }
        for (int i = 1; i <= 8; i++) {
            for (int j = 1; j <= 8; j++) {
                d = board[i][j];
                if (d == 'W') {
                    c = 'O';
                } else if (d == 'B') {
                    c = 'X';
                } else {
                    c = 'E';
                }
                s += c;
            }
        }
        return s;
    }

    public boolean isFinished() {

        int black = 0;
        int white = 0;
        for(int i = 1; i <= 8; i++){
            for(int j = 1; j <= 8; j++){
                if(this.board[i][j] == 'W'){
                    white ++;
                }
                if(this.board[i][j] == 'B'){
                    black ++;
                }
            }
        }

        if(black == 0 || white == 0 || white + black == 64){
            return true;
        }

        for(int i = 1; i <= 8; i++){
            for(int j = 1; j <= 8; j++){
                if(this.board[i][j] == 'W'){
                    return false;
                }
            }
        }
        return true;
    }
}
