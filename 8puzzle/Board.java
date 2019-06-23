/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.StdOut;

public class Board {


    private char[] board;
    private final int boardSize;
    private final int dimensions;

    public Board(int[][] blocks) {           // construct a board from an n-by-n array of blocks
                                             // (where blocks[i][j] = block in row i, column j)

        dimensions = blocks.length;
        boardSize = dimensions * dimensions;
        board = new char[boardSize];
        // Convert the 2x2 input integer array into a one dimensional char array
        int boardIterator = 0;
        for (int i = 0; i < dimensions; i++) {
            for (int j = 0; j < dimensions; j++) {

                // When you cast an int as a char, be sure to include a starting value '0' otherwise it will print ASCII value
                board[boardIterator] = (char) (blocks[i][j]);
                boardIterator++;
            }
        }
    }

    // Private Constructor used for twin board
    private Board(char[] copyBoard) {
        dimensions = (int) Math.sqrt(copyBoard.length);
        boardSize = copyBoard.length;
        board = new char[boardSize];
        for (int i = 0; i < boardSize; i++) {
            board[i] = copyBoard[i];
        }
    }

    public int dimension() {               // board dimension n
        return dimensions;
    }

    public int hamming() {                 // number of blocks out of place
        int hammingCount = 0;
        for (int i = 0; i < boardSize; i++) {
            if ((int) board[i] != 0 && (int) board[i] != i + 1) { hammingCount++; }
        }
        return hammingCount;
    }

    public int manhattan() {               // sum of Manhattan distances between blocks and goal
        int manhattanCount = 0;
        int xValuePos, yValuePos, xIndexPos, yIndexPos;

        for (int i = 0; i < boardSize; i++) {
            if ((int) board[i] != 0 && (int) board[i] != i + 1) {

                // Use modulo function to find the x- and y-position of the current array value
                xValuePos = ((int) board[i] - 1) % dimensions;
                yValuePos = ((int) board[i] - 1) / dimensions;

                // Use modulo function to find the x- and y-position of the current array index
                xIndexPos = i % dimensions;
                yIndexPos = i / dimensions;

                // Difference in index will provide the number of moves to get to its correct position
                int diff1, diff2;
                if (xValuePos > xIndexPos)  { diff1 = xValuePos - xIndexPos; }
                else                        { diff1 = xIndexPos - xValuePos; }
                if (yValuePos > yIndexPos)  { diff2 = yValuePos - yIndexPos; }
                else                        { diff2 = yIndexPos - yValuePos; }
                manhattanCount += diff1 + diff2;
            }
        }
        return manhattanCount;
    }

    public boolean isGoal() {              // is this board the goal board?
        return this.hamming() == 0;
    }

    // Be careful of references when creating twin boards
    public Board twin() {                  // a board that is obtained by exchanging any pair of blocks

        if (boardSize <= 1)
            return null;

        Board twinBoard = new Board(board);
        int a, b;

        if ((int) twinBoard.board[0] != 0) { a = 0; }
        else { a = 1; }

        if ((int) twinBoard.board[2] != 0) { b = 2; }
        else { b = 3; }

        return exchange(twinBoard, a, b);
    }

    public boolean equals(Object y) {      // does this board equal y?
        if (y == this) return true;
        if (y == null) return false;
        if (y.getClass() != this.getClass()) return false;

        Board test = (Board) y;
        if (this.boardSize != test.boardSize) return false;
        for (int i = 0; i < this.boardSize; i++) {
            if (this.board[i] != test.board[i])
                return false;
        }
        return this.dimensions == ((Board) y).dimensions;
    }

    public Iterable<Board> neighbors() {   // all neighboring boards
        Queue<Board> neighborBoards = new Queue<Board>();
        Board tempBoard = new Board(board);

        for (int i = 0; i < boardSize; i++) {

            if ((int) board[i] == 0) {
                // Check if the empty space is along the edges of the game board
                if (i >= dimensions) {
                   neighborBoards.enqueue(new Board(exchange(tempBoard, i, i - dimensions).board));
                   exchange(tempBoard, i, i - dimensions);
               }

               if (i + dimensions < boardSize) {
                   neighborBoards.enqueue(new Board(exchange(tempBoard, i, i + dimensions).board));
                   exchange(tempBoard, i, i + dimensions);
               }

               if ((i+1) % dimensions != 0) {
                   neighborBoards.enqueue(new Board(exchange(tempBoard, i, i + 1).board));
                   exchange(tempBoard, i, i + 1);
               }

               if (i % dimensions != 0) {
                   neighborBoards.enqueue(new Board(exchange(tempBoard, i, i - 1).board));
                   exchange(tempBoard, i, i - 1);
               }
            }
        }

        return neighborBoards;
    }

    public String toString() {             // string representation of this board (in the output format specified below)
        StringBuilder s = new StringBuilder();
        s.append(dimensions + "\n");
        for (int i = 0; i < boardSize; i++) {
            if (i != 0 && i % dimensions == 0) { s.append("\n"); }
            s.append(String.format("%2d ", ((int) board[i])));
        }
        return s.toString();
    }

    private Board exchange(Board cb, int a, int b) {
        char temp = cb.board[a];
        cb.board[a] = cb.board[b];
        cb.board[b] = temp;
        return cb;
    }


    public static void main(String[] args) {
        // Used separate test class
        In in = new In(args[0]);
        int n = in.readInt();
        int[][] blocks = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                blocks[i][j] = in.readInt();
        Board initial = new Board(blocks);
        StdOut.println(initial.toString());

     }
}
